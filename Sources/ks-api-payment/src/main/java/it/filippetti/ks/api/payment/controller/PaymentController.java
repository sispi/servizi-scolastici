/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.controller;

import it.filippetti.ks.api.payment.authentication.AuthenticationContextHolder;
import it.filippetti.ks.api.payment.model.PaymentInstance;
import it.filippetti.ks.api.payment.service.PaymentInstanceService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author dino
 */
@RequestMapping("/payment")
@Controller
public class PaymentController {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Value("${application.portalBaseUrl}")
    private String portalBaseUrl;

    @Autowired
    private PaymentInstanceService paymentService;
   
    @GetMapping("/test")
    public void test(
            @RequestParam(name="name", required=false, defaultValue="World") String name,
            HttpServletResponse httpServletResponse) {
        
        log.info("Controller test");
        
        
        
        httpServletResponse.setHeader("Location", "https://www.google.it");
        httpServletResponse.setStatus(302);
    }

    @GetMapping("/ok")
    public void paymentOk(
            HttpServletResponse httpServletResponse,
            HttpServletRequest request,
            @RequestParam String idrichiesta,
            @RequestParam String timestamp,
            @RequestParam String rifprat,
            @RequestParam String importo,
            @RequestParam String divisa,
            @RequestParam String esito) {
        log.info("Controller OK with params idrichiesta {}, timestamp {}, rifprat {}, importo {}, divisa {}, esito {}",
                idrichiesta, timestamp, rifprat, importo, divisa, esito);

        // payment/ok?idrichiesta=10
        // &timestamp=1626342088109
        // &rifprat=RF15000000000002227
        // &importo=10292
        // &divisa=EUR
        // &esito=OK

        // Controller OK with params idrichiesta 226, timestamp 1631114787618, rifprat RF37000000000002413, importo 2500, divisa EUR, esito ERROR

        try {
            PaymentInstance pi = null;
            String error = "ERROR";
            if(esito.equals(error)) {
                // esito = ERROR
                pi = paymentService
                    .updateToKo(AuthenticationContextHolder.get(), idrichiesta, timestamp, rifprat);
            } else {
                // esito = OK
                pi = paymentService
                    .updateToOk(AuthenticationContextHolder.get(), idrichiesta, timestamp, rifprat);
            }
            
            String redirectUrl = String.format("%s%s?paymentId=%s",
                        portalBaseUrl, pi.getCallbackRedirect(), pi.getUuid());

            httpServletResponse.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @GetMapping("/ko")
    public void paymentKo(
            HttpServletResponse httpServletResponse,
            HttpServletRequest request,
            @RequestParam String idrichiesta,
            @RequestParam String timestamp,
            @RequestParam String rifprat,
            @RequestParam String importo,
            @RequestParam String divisa,
            @RequestParam String esito
    ) {

        // idrichiesta=13
        // &timestamp=1626961910376
        // &rifprat=RF57000000000002247
        // &importo=292
        // &divisa=EUR
        // &esito=ERROR' );

        log.info("Controller KO");
        try {
            PaymentInstance pi = paymentService
                    .updateToKo(AuthenticationContextHolder.get(), idrichiesta, timestamp, rifprat);

//            String redirectUrl = String.format("%s://%s%s?paymentId=%s",
//                    request.getProtocol(), request.getHeader("Host"), pi.getCallbackRedirect(), pi.getUuid());

            String redirectUrl = String.format("%s%s?paymentId=%s",
                    portalBaseUrl, pi.getCallbackRedirect(), pi.getUuid());

            httpServletResponse.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    @GetMapping("/cancel/{uuid}")
    public void paymentCancel(
            HttpServletResponse httpServletResponse,
            HttpServletRequest request,
            @PathVariable("uuid") String uuid) {
        log.info("Controller cancel");
        try {
            PaymentInstance pi = paymentService.updateToCancel(AuthenticationContextHolder.get(), uuid);
//            String redirectUrl = String.format("%s://%s%s?paymentId=%s",
//                    request.getProtocol(), request.getHeader("Host"), pi.getCallbackRedirect(), pi.getUuid());

            String redirectUrl = String.format("%s%s?paymentId=%s",
                    portalBaseUrl, pi.getCallbackRedirect(), pi.getUuid());

            httpServletResponse.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @GetMapping("/s2s")
    public void paymentS2s(HttpServletResponse httpServletResponse) {
        // non serve???
        log.info("Controller s2s");
        httpServletResponse.setHeader("Location", "https://www.google.it");
        httpServletResponse.setStatus(302);
    }
    
    @GetMapping("/backUrl")
    public void paymentBackUrl(HttpServletResponse httpServletResponse, HttpServletRequest request) {
        log.info("Controller backUrl");
        try {
//            String redirectUrl = String.format("%s://%s%s?paymentId=%s",
//                    request.getProtocol(), request.getHeader("Host"), pi.getCallbackRedirect(), pi.getUuid());
            //TODO
            String redirectUrl = String.format("%s%s?paymentId=%s",
                    portalBaseUrl, "", "");

            httpServletResponse.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @GetMapping("/doPayment/{paymentInstanceUuid}/{channelId}")
    public void doPayment(
            @PathVariable String paymentInstanceUuid,
            @PathVariable Long channelId,
            HttpServletRequest request, 
            HttpServletResponse httpServletResponse) throws Exception {
        
        String redirectUrl;
        
        log.info("Controller doPayment");
        
        PaymentInstance pi = paymentService.findOneByUuid(AuthenticationContextHolder.get(), paymentInstanceUuid);
        if (pi.getOutcome().isReady() || 
            pi.getOutcome().isFailed() || 
            pi.getOutcome().isCanceled()) {
            // redirect to checkout
            redirectUrl = paymentService.executePayment(
                request, AuthenticationContextHolder.get(), paymentInstanceUuid, channelId);
        } else {
            // redirect to portal
            redirectUrl = String.format("%s%s?paymentId=%s",
                portalBaseUrl, pi.getCallbackRedirect(), pi.getUuid());
        }

        httpServletResponse.sendRedirect(redirectUrl);

    }
}
