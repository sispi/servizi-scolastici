/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.controller;

import freemarker.template.TemplateException;
import it.filippetti.ks.spa.form.authentication.AuthenticationContextHolder;
import it.filippetti.ks.spa.form.configuration.PreviewConfiguration;
import it.filippetti.ks.spa.form.dto.CreateFormViewDTO;
import it.filippetti.ks.spa.form.exception.ApplicationException;
import it.filippetti.ks.spa.form.service.FormService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Controller
public class PreviewController {

    @Autowired
    PreviewConfiguration previewConfiguration;
    
    @Autowired
    private FormService formService;
    
    @RequestMapping(
        path = "/preview/{id}",
        method = RequestMethod.GET, 
        produces = MediaType.TEXT_HTML_VALUE
    )
    public void preview(
        @PathVariable("id") String id,
        HttpServletResponse response) 
        throws ApplicationException, TemplateException, IOException {
        
        if (StringUtils.isBlank(previewConfiguration.getExternalUrl())) {
            formService.createFormView(
                AuthenticationContextHolder.get(), 
                id, 
                new CreateFormViewDTO(
                    previewConfiguration.getTemplate(), 
                    previewConfiguration.getOptions()), 
                true,
                response);
        } else {
            response.sendRedirect(
                previewConfiguration
                    .getExternalUrl()
                    .replace("$formId", UriUtils.encodeQuery(id, StandardCharsets.UTF_8)));
        }
    }            
}
