/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.filippetti.ks.api.payment.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.payment.dto.CreatePaymentInstanceDTO;
import it.filippetti.ks.api.payment.dto.ErrorDTO;
import it.filippetti.ks.api.payment.dto.PaymentInstanceDTO;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.exception.NotFoundException;
import it.filippetti.ks.api.payment.model.PaymentInstance;
import it.filippetti.ks.api.payment.service.PaymentInstanceService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dino
 */
@Api(tags = {"/paymentInstances"}) 
@RestController
@RequestMapping("/paymentInstances")
public class PaymentInstanceEndpoint {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentInstanceEndpoint.class);
    
    @Autowired
    private PaymentInstanceService paymentService;
    
    @ApiOperation(value = "Create payment", notes = "Create a payment.<br/>")
    @ApiResponses({
        @ApiResponse(code = 201, response = PaymentInstanceDTO.class, message = "<b>Payment successfully created</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 422, response = ErrorDTO.class, 
            message = "Operation failed<br><br>"
            + "<li><b>EXAMPLE_BUSINESS_ERROR</b>: Business error description")       
    })
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@RequestBody CreatePaymentInstanceDTO createPaymentDTO) throws ApplicationException, Exception {
        log.info("Rest request to create Payment");
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.create(AuthenticationContextHolder.get(), createPaymentDTO));
    }
    
    @ApiOperation(value = "Get payment", notes = "Get detail of payment.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = PaymentInstanceDTO.class, message = "<b>Deployment detail</b>"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 404, message = "Not Found: resource not found")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        path = "/{id}",
        method = RequestMethod.GET, 
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> findOne(@PathVariable("id") String id) throws ApplicationException{
        log.info("Rest request to get Payment by uuid");
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.findOneCompleteByUuid(AuthenticationContextHolder.get(), id));
    }
    
    
    @ApiOperation(value = "Delete payment", notes = "Delete detail of payment by set CANCELED.<br/>")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") String id) throws ApplicationException{
        log.info("Rest request to delete Payment by uuid");
        paymentService.delete(AuthenticationContextHolder.get(), id);
        return ResponseEntity.noContent().build();
    }
    
   
    
    @ApiOperation(value = "Get payments", notes = "Get a paged list of payments.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = PaymentInstanceDTO.Page.class, message = "<b>Payments list</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllPaged(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy
    ) throws ApplicationException {
        log.info("Rest request to get all Payments");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(paymentService.findAllPaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy));
    }
    
    @ApiOperation(value = "Get payments", notes = "Get a paged list of a user's Payments.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = PaymentInstanceDTO.Page.class, message = "<b>Payments list</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllByUserIdPaged(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy
    ) throws ApplicationException {
        log.info("Rest request to get a paged list of a user's Payments");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(paymentService.findAllByUserIdPaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy));
    }
    
    @ApiOperation(value = "Get payments", notes = "Request to obtain a list of a user's payment pages related to a case.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = PaymentInstanceDTO.Page.class, message = "<b>Payments list</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "{referenceInstanceId}/{referenceProcessId}/{referenceClientId}/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAllByReferencePaged(
        @RequestParam(name = "pageNumber", required = false) Integer pageNumber, 
        @RequestParam(name = "pageSize", required = false) Integer pageSize,
        @RequestParam(name = "orderBy", required = false) String orderBy,
        @PathVariable("referenceInstanceId") String referenceInstanceId,
        @PathVariable("referenceProcessId") String referenceProcessId,
        @PathVariable("referenceClientId") String referenceClientId
    ) throws ApplicationException {
        log.info("Request to obtain a list of a user's payment pages related to a case");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(paymentService.findAllByReferencePaged(AuthenticationContextHolder.get(), pageNumber, pageSize, orderBy, referenceInstanceId, referenceProcessId, referenceClientId));
    }
    
    @ApiOperation(value = "Execute Payment", notes = "Execute the payment and updates the payment status.<br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = PaymentInstanceDTO.class, message = "<b>Payment successfully executed</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
        @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
        @ApiResponse(code = 404, message = "Not Found: resource not found"),
        @ApiResponse(code = 422, response = ErrorDTO.class, message = "Operation failed<br><br>"
            + "<li><b>PAYMENT_BUSINESS_ERROR</b>: Business error")   
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
        path = "/{uuid}/execute/{channelId}",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> execute(@PathVariable("uuid") String uuid, @PathVariable("channelId") Long channelId, HttpServletRequest request) throws ApplicationException{
        log.info("Rest request to execute payment");
        
        String url = paymentService.executePayment(
                    request, AuthenticationContextHolder.get(), uuid, channelId);
        
        if( StringUtils.isEmpty(url) ) {
            throw new NotFoundException();
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(url);
    }


    //chiamata da PmPay S2S per settare esito
    @RequestMapping(value = "/s2s",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public void outcomePaymentInstance(HttpServletRequest request) throws ApplicationException {

        String idrichiesta = request.getParameter("idrichiesta");
        String rifprat = request.getParameter("rifprat");
        String esito = request.getParameter("esito");
        log.debug("Rest request for s2s : "+ idrichiesta +" - "+rifprat+" - "+esito);
        // 1: transazione andata a buon fine - OK
        // 0: transazione cancellata dall'utente - CANCELLED
        // 2: transazione NON andata a buon fine - ERROR
        // 3: transazione in corso - PENDING

        try {
            paymentService.manageS2S(idrichiesta, rifprat, esito);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @ApiOperation(value = "Download receipt", notes = "Download the payment receipt in the format you prefer (json, xml, pdf).<br/>")
    @ApiResponses({
            @ApiResponse(code = 200, response = PaymentInstanceDTO.class, message = "<b>Payment receipt successfully supplied</b>"),
            @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
            @ApiResponse(code = 403, message = "Authorization failed: user doesn't have rights to access the resource or perform operation"),
            @ApiResponse(code = 404, message = "Not Found: resource not found"),
            @ApiResponse(code = 422, response = ErrorDTO.class, message = "Operation failed<br><br>"
                    + "<li><b>PAYMENT_BUSINESS_ERROR</b>: Business error")
    })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            path = "/{uuid}/receipt",
            method = RequestMethod.GET
    )
    public ResponseEntity<?> getReceiptFile(@PathVariable String uuid, @RequestParam String format) throws ApplicationException, IOException, URISyntaxException, InterruptedException {
        log.info("REST request to get the receipt file for payment instance : {}", uuid);
        
        if(format.equals("json")){
            String json = paymentService.xmlToJson(AuthenticationContextHolder.get(), uuid);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(json);
        } else if(format.equals("xml")){
            PaymentInstance paymentInstance = paymentService.findOneByUuid(AuthenticationContextHolder.get(), uuid);
            if(paymentInstance.getReceipt() == null) {
                throw new NotFoundException(); //TODO: not found receipt?
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(paymentInstance.getReceiptContentType()))
                    .body(paymentInstance.getReceipt());
        } else if(format.equals("pdf")){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(paymentService.getDocumentUrl(AuthenticationContextHolder.get(), uuid));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @ApiOperation(value = "Get number of payments", notes = "Get the total number of payments.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = Integer.class, message = "<b>Total number of payments</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
            path = "/count",
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> count() throws ApplicationException {
        log.info("Rest request to get all Payments");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(paymentService.count(AuthenticationContextHolder.get()));
    }
    
    
    @ApiOperation(value = "Get sum of total amount of payments", notes = "Get the sum of total amount of payments.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = Double.class, message = "<b>Sum of total amount of payments</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
            path = "/total",
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sumTotalAmount() throws ApplicationException {
        log.info("Rest request to get all Payments");
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(paymentService.totalAmount(AuthenticationContextHolder.get()));
    }
    
    
    @ApiOperation(value = "Get sum of total amount of payments by month", notes = "Get the sum of total amount of payments grouped by month.<br/><br/>")
    @ApiResponses({
        @ApiResponse(code = 200, response = List.class, message = "<b>Sum of total amount of payments by month</b>"),
        @ApiResponse(code = 400, message = "Validation failed: request contains syntax errors"),
        @ApiResponse(code = 401, message = "Authentication failed: missing or invalid credentials"),
    })
    @ResponseStatus(HttpStatus.OK)    
    @RequestMapping(
            path = "/total/byMonth/{start}/{end}",
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Object[]>> sumTotalAmountByMonth(
            @PathVariable("start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date start, 
            @PathVariable("end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws ApplicationException {
        
        log.info("Rest request to get Payments amount sum by month");
        List<Object[]> totals = paymentService
                .totalAmountByMonth(AuthenticationContextHolder.get(), start, end);
        
        return ResponseEntity.status(HttpStatus.OK).body(totals);
    }
}
    