/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.service;

import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.exception.AuthorizationException;
import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.utilities.MicroserviceUtilities;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

/**
 *
 * @author dino
 */
@Service
public class UserService {
    
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    
    @Value( "${application.docerBaseUrl}" )
    private String BASE_URL;
    
    @Autowired
    private IdentityService identityService;
    
    
    public Map<String, Object> getUserFromDocer(AuthenticationContext context, String userId) throws ApplicationException{

        if (context.isAnonymous()) {
            throw new AuthorizationException();
        }
        
        String url = "/utenti/" + userId;
        Object user = MicroserviceUtilities.callToMicroservice(context, BASE_URL, url, HttpMethod.GET);
        Map<String, Object> map = new HashMap<>();
        if(user instanceof Map){
            map.putAll((Map)user);
        }
        return map;
    }
    
    public Object updateUserFromDocer(AuthenticationContext context, String userId, Map<String, Object> data) throws ApplicationException{

        if (context.isAnonymous()) {
            throw new AuthorizationException();
        }
        
        String adminToken = identityService.generateAdminJwtToken(context.getTenant(), context.getOrganization());
        
        Map<String, Object> user = new HashMap<>();
//        user.put("id", data.get("id"));
//        user.put("ADDRESS_STREET", data.get("ADDRESS_STREET"));
//        user.put("ADDRESS_MUNICIPALITY", data.get("ADDRESS_MUNICIPALITY"));
//        user.put("ADDRESS_PROVINCE", data.get("ADDRESS_PROVINCE"));
//        user.put("ADDRESS_POSTALCODE", data.get("ADDRESS_POSTALCODE"));
//        user.put("EMAIL_ADDRESS", data.get("EMAIL_ADDRESS"));
//        user.put("DIGITAL_ADDRESS", data.get("DIGITAL_ADDRESS"));
//        user.put("TELEPHONE", data.get("TELEPHONE"));
//        user.put("USER_ID", data.get("USER_ID"));
//        user.put("FIRST_NAME", data.get("FIRST_NAME"));
//        user.put("LAST_NAME", data.get("LAST_NAME"));
//        user.put("FISCAL_CODE", data.get("FISCAL_CODE"));
//        user.put("BIRTH_DATE", data.get("BIRTH_DATE"));
//        user.put("BIRTH_PLACE", data.get("BIRTH_PLACE"));
//        user.put("BIRTH_COUNTY", data.get("BIRTH_COUNTY"));
//        user.put("SEX", data.get("SEX"));
//        user.put("ADDRESS_CNUMBER", data.get("ADDRESS_CNUMBER"));
        
        user.put("CONTATTO_EMAIL_ADDRESS", data.get("CONTATTO_EMAIL_ADDRESS"));
        user.put("CONTATTO_DIGITAL_ADDRESS", data.get("CONTATTO_DIGITAL_ADDRESS"));
        user.put("CONTATTO_TELEPHONE", data.get("CONTATTO_TELEPHONE"));
        user.put("CONTATTO_TELEPHONE_HOME", data.get("CONTATTO_TELEPHONE_HOME"));
        user.put("FATTURAZIONE_NOME", data.get("FATTURAZIONE_NOME"));
        user.put("FATTURAZIONE_COGNOME", data.get("FATTURAZIONE_COGNOME"));
        user.put("FATTURAZIONE_CF", data.get("FATTURAZIONE_CF"));
        user.put("FATTURAZIONE_RESIDENZA", data.get("FATTURAZIONE_RESIDENZA"));
        user.put("FATTURAZIONE_MAIL", data.get("FATTURAZIONE_MAIL"));
        
        log.info("Rest request to update User with this map {}", user);
        
        String url = "/utenti/" + userId;
        return MicroserviceUtilities.callToMicroservice(context, BASE_URL, url, "PATCH", user, adminToken);
    }
    
}
