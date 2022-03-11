/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.service;

import it.filippetti.ks.api.portal.dto.InstanceDTO;
import it.filippetti.ks.api.portal.dto.PageDTO;
import it.filippetti.ks.api.portal.exception.ApplicationException;
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
public class NotificationService {
    
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private InstanceService instanceService;
    
    @Value( "${application.bpmBaseUrl}" )
    private String BASE_URL;
    
    private static final String TO_COMPILE_BY_USER = "instances?rootOnly=true&searchableOnly=true&status=1&taskOwner=true&pageNumber=1&pageSize=1";
    
    public Map<String, Long> countNotification(AuthenticationContext context) throws ApplicationException{

        log.info("Request to count notifications");
        Long portalCount = countPortalInstances(context);
        Long bpmCount = Long.valueOf(countBpmInstances(context));
        Map<String, Long> map = new HashMap<>();
        map.put("portal", portalCount);
        map.put("bpm", bpmCount);
        return map;
    }
    
    private Long countPortalInstances(AuthenticationContext context)throws ApplicationException{
        
        log.info("Request to count notifications (instances not submitted by the user)");
        PageDTO<InstanceDTO> pagedInstance = instanceService.findAllPaged(context, 1, 1, null, false);
        return pagedInstance.getCount();
    }
    
    public Integer countBpmInstances(AuthenticationContext context) throws ApplicationException{
        
        log.info("Request to count notifications (instances to be completed by the user)");
        int count = 0;
        Object pagedInstances = MicroserviceUtilities.callToMicroservice(context, BASE_URL, TO_COMPILE_BY_USER, HttpMethod.GET);
        Map<String, Object> map = new HashMap<>();
        if(pagedInstances instanceof Map){
            map.putAll((Map)pagedInstances);
            if(map.get("count") instanceof Integer){
                count = (Integer)map.get("count");
            }
        }
        return count;
    }
}
