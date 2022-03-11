/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.filippetti.ks.api.portal.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.portal.exception.ApplicationException;
import it.filippetti.ks.api.portal.service.IdentityService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author marco.mazzocchetti
 */
@Controller
@RequestMapping("/payment-callback")
public class PaymentCallbackController {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentCallbackController.class);

    @Value("${application.baseUrl}")    
    private String baseUrl;
    
    @Value("${application.bpmBaseUrl}")
    private String bpmBaseUrl;

    @Value("${application.paymentBaseUrl}")
    private String paymentBaseUrl;

    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private RestTemplate restTemplate;

    public PaymentCallbackController() {
        this.restTemplate = new RestTemplate();
    }
    
    @RequestMapping(
        path = "/redirect",
        method = RequestMethod.GET, 
        produces = MediaType.TEXT_HTML_VALUE
    )
    public void redirect(
        @RequestParam(name = "paymentId", required = true) String paymentId,
        HttpServletRequest request,    
        HttpServletResponse response) 
        throws IOException {
        
        JsonNode payment;
        String userToken, paymentStatus, redirectUrl = null;
        Long taskId;
        
        taskId = null;
        
        // this request must be authenticated (user cookie), get context token
        userToken = AuthenticationContextHolder.get().getJwtToken();
        
        try {
            // get payment
            payment = restTemplate.execute(
                String.format("%s/paymentInstances/%s", paymentBaseUrl, paymentId),  
                HttpMethod.GET, 
                (ClientHttpRequest r) -> {
                    r.getHeaders().setBearerAuth(userToken);
                }, 
                (ClientHttpResponse r) -> {
                    return objectMapper.readValue(r.getBody(), JsonNode.class);
                });
            
            // get payment status
            paymentStatus = payment.get("transactionResult").textValue();
            
            // get payment task id
            taskId = restTemplate.execute(
                String.format("%s/tasks?assignedAs=ActualOwner&businessKey=%s", bpmBaseUrl, paymentId),  
                HttpMethod.GET, 
                (ClientHttpRequest r) -> {
                    r.getHeaders().setBearerAuth(userToken);
                }, 
                (ClientHttpResponse r) -> {
                    JsonNode page;
                    
                    page = objectMapper.readValue(r.getBody(), JsonNode.class);
                    if (page.get("count").intValue() > 0) {
                        return page.get("data").get(0).get("id").longValue();
                    } else {
                        log.warn(String.format(
                            "Cannot redirect payment %s: no task found",
                            paymentId));
                        return null;
                    }
                });
            
            // process paymment
            switch (paymentStatus) {
                case "READY": 
                case "WAITING": 
                    redirectUrl = String.format(
                        "%s/portal/features/task?id=%d", 
                        baseUrl, taskId);
                    break;
                case "COMPLETED": 
                case "RECEIPT":
                    redirectUrl = String.format(
                        "%s/portal/features/paymentsOk?paymentId=%s&paymentStatus=%s&taskId=%d", 
                        baseUrl, paymentId, paymentStatus, taskId);
                    break;
                case "FAILED": 
                case "CANCELED":
                    redirectUrl = String.format(
                        "%s/portal/features/paymentsKo?paymentId=%s&paymentStatus=%s&taskId=%d", 
                        baseUrl, paymentId, paymentStatus, taskId);
                    break;
                default:
                    throw new RuntimeException(String.format(
                        "Unexpected payment status: %s", 
                        payment.get("status").textValue()));                    
            }
        } catch (RestClientResponseException e) {
            log.warn(String.format(
                "Cannot redirect payment %s: %s", 
                paymentId,
                e.getMostSpecificCause().getMessage()));
        }                
        
        // redirect
        if (redirectUrl != null) {
            response.sendRedirect(redirectUrl);
        }
        else {
            response.sendRedirect(String.format(
                "%s/error/error-404", 
                baseUrl, 
                taskId));
        }
    }     
    
    @RequestMapping(
        path = "/s2s/status",    
        method = RequestMethod.GET
    )
    public ResponseEntity<?> status(
        @RequestParam(name = "tenant", required = true) String tenant,
        @RequestParam(name = "organization", required = true) String organization,
        @RequestParam(name = "paymentId", required = true) String paymentId) 
        throws ApplicationException {

        String adminToken, userToken;
        JsonNode payment;
        String paymentStatus;
        final Set<Long> taskIds;
        
        // this request is not authenticated, create admin token        
        adminToken = identityService.generateAdminJwtToken(tenant, organization);

        try {
            // get payment
            payment = restTemplate.execute(
                String.format("%s/paymentInstances/%s", paymentBaseUrl, paymentId),  
                HttpMethod.GET, 
                (ClientHttpRequest r) -> {
                    r.getHeaders().setBearerAuth(adminToken);
                }, 
                (ClientHttpResponse r) -> {
                    return objectMapper.readValue(r.getBody(), JsonNode.class);
                });
            
            // get payment status
            paymentStatus = payment.get("transactionResult").textValue();

            // process paymment
            switch (paymentStatus) {
                case "READY": 
                case "WAITING": 
                case "COMPLETED": 
                case "FAILED": 
                case "CANCELED":
                    // not managed
                    break;
                case "RECEIPT":
                    // create payment reference user token        
                    userToken = identityService.generateUserJwtToken(
                        tenant, 
                        organization, 
                        payment.get("referenceUserId").textValue());

                    // get payment task id(s)
                    taskIds = new HashSet<>();
                    restTemplate.execute(
                        String.format("%s/tasks?assignedAs=ActualOwner&businessKey=%s", bpmBaseUrl, paymentId),  
                        HttpMethod.GET, 
                        (ClientHttpRequest r) -> {
                            r.getHeaders().setBearerAuth(userToken);
                        }, 
                        (ClientHttpResponse r) -> {

                            JsonNode page;

                            page = objectMapper.readValue(r.getBody(), JsonNode.class);
                            if (page.get("count").intValue() > 0) {
                                page.get("data").forEach(t -> {
                                    if (t.get("status").asText().equals("InProgress")) {
                                       taskIds.add(t.get("id").longValue());
                                    }
                                });
                                return null;
                            } else {
                                throw new RuntimeException(String.format(
                                    "Cannot process payment %s: no task found",
                                    paymentId));
                            }
                        });

                    // complete payment task(s)
                    for (Long taskId : taskIds) {
                        restTemplate.execute(
                            String.format("%s/tasks/%d/actions/complete", bpmBaseUrl, taskId),  
                            HttpMethod.POST, 
                            (ClientHttpRequest r) -> {
                                r.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                r.getHeaders().setBearerAuth(userToken);
                                objectMapper.writeValue(
                                    r.getBody(), 
                                    Map.of(
                                        "output", Map.of(
                                            "paymentId", paymentId,    
                                            "payment", payment)));
                            }, 
                            (ClientHttpResponse r) -> {
                                return null;
                            });
                    }
                    break;
                default:
                    throw new RuntimeException(String.format(
                        "Unexpected payment status: %s", 
                        payment.get("status").textValue()));
            }

        } catch (RestClientResponseException e) {
            throw new RuntimeException(String.format(
                "Cannot process payment %s: %s", 
                paymentId,
                e.getMostSpecificCause().getMessage()));
        }                
        return ResponseEntity.ok().build();
    }       
}
