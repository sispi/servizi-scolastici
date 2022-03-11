/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.endpoint;

import com.zaxxer.hikari.HikariDataSource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.portal.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.portal.dto.LegacyInstanceDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.exception.NotFoundException;
import java.io.IOException;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author marco.mazzocchetti
 */
@ConditionalOnProperty(prefix = "legacy.datasource", name = "url")
@Api(tags = {"/legacy-instances"}) 
@RestController
@RequestMapping("/legacy-instances")
@Transactional(readOnly = true)
public class LegacyInstancesEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(LegacyInstancesEndpoint.class);

    private static final String SQL_LIST = ""
        + "SELECT "
            + "pi.id as id, "
            + "s.name as service, "
            + "ap.title as proceedings, "
            + "pi.proceedings_bpm_code as processId, "
            + "pi.start_date as startTs, "
            + "pi.end_date as endTs, "
            + "2 as status "
        + "FROM PROCEEDINGSINSTANCE pi "
        + "INNER JOIN JHI_USER u ON u.id = pi.user_id "
        + "LEFT JOIN AGENCYPROCEEDINGS ap ON ap.id = pi.agencyproceedings_id "
        + "LEFT JOIN AGENCYSERVICE s ON s.id = ap.agencyservice_id "
        + "WHERE (UPPER(u.login) = :credentials OR UPPER(u.fiscal_code) = :credentials) "
        + "AND pi.status_id = 6 "
        + "ORDER BY pi.id DESC";
    
    private static final String SQL_DETAIL = ""
        + "SELECT "
            + "pi.id as id, "
            + "s.name as service, "
            + "ap.title as proceedings, "
            + "pi.proceedings_bpm_code as processId, "
            + "pi.start_date as startTs, "
            + "pi.end_date as endTs, "
            + "2 as status, "
            + "ap.accountable_staff as accountableStaff "
        + "FROM PROCEEDINGSINSTANCE pi "
        + "INNER JOIN JHI_USER u ON u.id = pi.user_id "
        + "LEFT JOIN AGENCYPROCEEDINGS ap ON ap.id = pi.agencyproceedings_id "
        + "LEFT JOIN AGENCYSERVICE s ON s.id = ap.agencyservice_id "
        + "WHERE (UPPER(u.login) = :credentials OR UPPER(u.fiscal_code) = :credentials) "
        + "AND pi.id = :id "
        + "AND pi.status_id = 6";

    private static final String SQL_DETAIL_ACTIVITIES = ""
        + "SELECT "
            + "SUBSTRING_INDEX(a.name, '-', -1) as label, "
            + "a.start_date as timestamp, "
            + "IF(a.status_id IN (1, 3, 4, 5), 'HT-cittadino', 'HT-ente') as type, "
            + "1 as state "
        + "FROM ACTIVITY a "
        + "WHERE a.proceedingsinstance_id = :id "
        + "AND a.status_id IN (1, 2, 3, 4, 5, 6, 8) "
        + "ORDER BY a.id ASC";
    
    private static final String SQL_DETAIL_DOCUMENTS = ""
        + "SELECT "
            + "d.id as id, "
            + "d.document_file as name, "
            + "UPPER(d.direction) as direction, "
            + "module_name as type, "
            + "COALESCE(d.upload_date, d.received_date) as createTs, "
            + "d.acquisition_date as protocolTs, "
            + "CONCAT(d.protocol_year, '/', d.protocol_number) as protocolNumber "
        + "FROM MYDOCUMENT d "
        + "WHERE d.proceedingsinstance_id = :id "
        + "ORDER BY d.id DESC";
    
    private static final String SQL_DETAIL_PAYMENTS = ""
        + "SELECT "
            + "p.id as id, "
            + "p.causale as reason, "
            + "p.importo_complessivo as amount, "
            + "p.data_elaborazione as processingTs, "
            + "'RECEIPT' as status "
        + "FROM PAYMENTINSTANCE p "
        + "WHERE p.proceedings_instance_id = :id "
        + "AND p.esito_transazione = 'OK' "
        + "ORDER BY p.id DESC";

    private static final String SQL_DOCUMENT_DOWNLOAD = ""
        + "SELECT "
            + "d.document_blob as data, "
            + "d.document_file as fileName, "
            + "d.mime_type as contentType "
        + "FROM MYDOCUMENT d "
        + "INNER JOIN PROCEEDINGSINSTANCE pi ON pi.id = d.proceedingsinstance_id "
        + "INNER JOIN JHI_USER u ON u.id = pi.user_id "
        + "WHERE (UPPER(u.login) = :credentials OR UPPER(u.fiscal_code) = :credentials) "
        + "AND d.id = :documentId "
        + "AND pi.id = :id "
        + "AND pi.status_id = 6";
    
    private static final String SQL_PAYMENT_RECEIPT_DOWNLOAD = ""
        + "SELECT "
            + "p.custom_receipt as data, "
            + "CONCAT(DATE_FORMAT(p.data_elaborazione, '%Y%m%d%H%i%s'), '.pdf') as fileName, "
            + "'application/pdf' as contentType "
        + "FROM PAYMENTINSTANCE p "
        + "INNER JOIN PROCEEDINGSINSTANCE pi ON pi.id = p.proceedings_instance_id "
        + "INNER JOIN JHI_USER u ON u.id = pi.user_id "
        + "WHERE (UPPER(u.login) = :credentials OR UPPER(u.fiscal_code) = :credentials) "
        + "AND p.id = :paymentId "
        + "AND p.esito_transazione = 'OK' "
        + "AND pi.id = :id "
        + "AND pi.status_id = 6";
    
    @Value("${legacy.datasource.url}")
    private String dataSourceUrl; 

    @Value("${legacy.datasource.username}")
    private String dataSourceUsername; 

    @Value("${legacy.datasource.password}")
    private String dataSourcePassword; 
    
    private NamedParameterJdbcTemplate jdbc;
    
    @PostConstruct
    public void init() {
        jdbc = new NamedParameterJdbcTemplate(DataSourceBuilder
            .create()
            .type(HikariDataSource.class)
            .url(dataSourceUrl)
            .username(dataSourceUsername)
            .password(dataSourcePassword)
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .build());
    }
    
    @ApiOperation(
        value = "Get legacy instances",
        notes = ""
            + "Get the list of archived legacy instances")
    @ApiResponses({
        @ApiResponse(code = 200, response = LegacyInstanceDTO.class, responseContainer = "List",
            message = "<b>Legacy instances list</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials")
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getLegacyInstances()
        throws ApplicationException{
        
        Map args = Map.of(
            "credentials", credentials());
        
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(jdbc.query(SQL_LIST, args, (r, i) -> new LegacyInstanceDTO(
                r.getLong("id"), 
                r.getString("service"),
                r.getString("proceedings"),
                r.getString("processId"),
                r.getDate("startTs"),
                r.getDate("endTs"), 
                r.getInt("status"),
                null,
                null,
                null,
                null)));
    }  
    
    @ApiOperation(
        value = "Get legacy instance",
        notes = ""
            + "Get detail of archived legacy instance")
    @ApiResponses({
        @ApiResponse(code = 200, response = LegacyInstanceDTO.class, 
            message = "<b>Legacy instance detail</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)         
    @RequestMapping(
        path = "/{id}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getLegacyInstance(
        @PathVariable("id") Long id) 
        throws ApplicationException{
        
        Map args = Map.of(
            "credentials", credentials(), 
            "id", id);
        
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(jdbc.queryForObject(SQL_DETAIL, args, (r, i) -> new LegacyInstanceDTO(
                    r.getLong("id"), 
                    r.getString("service"),
                    r.getString("proceedings"),
                    r.getString("processId"),
                    r.getDate("startTs"),
                    r.getDate("endTs"), 
                    r.getInt("status"),
                    r.getString("accountableStaff"),
                    jdbc.query(SQL_DETAIL_ACTIVITIES, args, (a, j) -> new LegacyInstanceDTO.Activity(
                        j + 1,
                        a.getString("label"),
                        a.getDate("timestamp"),
                        a.getString("type"),
                        a.getInt("state"))),
                    jdbc.query(SQL_DETAIL_DOCUMENTS, args, (a, j) -> new LegacyInstanceDTO.Document(
                        a.getLong("id"), 
                        a.getString("name"),
                        a.getString("direction"),
                        a.getString("type"),
                        a.getDate("createTs"),
                        a.getDate("protocolTs"),
                        a.getString("protocolNumber"))),
                    jdbc.query(SQL_DETAIL_PAYMENTS, args, (a, j) -> new LegacyInstanceDTO.Payment(
                        a.getLong("id"), 
                        a.getString("reason"),
                        a.getString("amount"),
                        a.getDate("processingTs"),
                        a.getString("status")
                    )))));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }        
    }    
    
    @ApiOperation(
        value = "Download legacy instance document",
        notes = ""
            + "Download a legacy instance document")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Legacy instance document content</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)                 
    @RequestMapping(
        path = "/{id}/documents/{documentId}",
        method = RequestMethod.GET, 
        produces = MediaType.ALL_VALUE
    )
    public void downloadLegacyInstanceDocument(
        @PathVariable("id") Long id, 
        @PathVariable("documentId") Long documentId, 
        HttpServletResponse response) 
        throws ApplicationException, IOException {
        
        Map args = Map.of(
            "credentials", credentials(), 
            "id", id, 
            "documentId", documentId);        
        
        try {
            jdbc.query(SQL_DOCUMENT_DOWNLOAD, args, (r) -> {
                try {
                    if (!r.next()) {
                        throw new EmptyResultDataAccessException(1);
                    }
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(r.getString("contentType"));
                    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", 
                        r.getString("filename")));                     
                    IOUtils.copy(r.getBinaryStream("data"), response.getOutputStream());
                } catch (IOException e) {
                    throw new TransientDataAccessResourceException(e.getMessage(), e);
                }
                return null;
            });
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }        
    }    
    
    @ApiOperation(
        value = "Download legacy instance payment receipt",
        notes = ""
            + "Download a legacy instance payment receipt")
    @ApiResponses({
        @ApiResponse(code = 200,
            message = "<b>Legacy instance payment receipt content</b>"),
        @ApiResponse(code = 401, 
            message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, 
            message = "Not Found: resource not found")        
    })
    @ResponseStatus(HttpStatus.OK)                 
    @RequestMapping(
        path = "/{id}/payments/{paymentId}/receipt",
        method = RequestMethod.GET, 
        produces = MediaType.ALL_VALUE
    )
    public void downloadLegacyInstancePaymentReceipt(
        @PathVariable("id") Long id, 
        @PathVariable("paymentId") Long paymentId, 
        HttpServletResponse response) 
        throws ApplicationException, IOException {

        Map args = Map.of(
            "credentials", credentials(), 
            "id", id, 
            "paymentId", paymentId);        
        
        try {
            jdbc.query(SQL_PAYMENT_RECEIPT_DOWNLOAD, args, (r) -> {
                try {
                    if (!r.next()) {
                        throw new EmptyResultDataAccessException(1);
                    }
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(r.getString("contentType"));
                    response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", 
                        r.getString("filename")));                     
                    IOUtils.copy(r.getBinaryStream("data"), response.getOutputStream());
                } catch (IOException e) {
                    throw new TransientDataAccessResourceException(e.getMessage(), e);
                }
                return null;
            });
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException();
        }        
    }      
        
    private String credentials() throws AuthenticationException {
        
        String credentials;
        
        credentials = (String) AuthenticationContextHolder
            .get()
            .getDetails()
            .otherFields()
            .get("FISCAL_CODE");
        if (StringUtils.isBlank(credentials)) {
            throw new InsufficientAuthenticationException(
                "No fiscal code information available for user");
        }
        return credentials.toUpperCase();
    }
}
