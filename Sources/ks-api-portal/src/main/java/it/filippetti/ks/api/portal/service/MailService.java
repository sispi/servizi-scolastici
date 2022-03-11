/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.service;

import it.filippetti.ks.api.portal.dto.ContactUsDTO;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author dino
 */
@Service
public class MailService {
    
    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Value( "${mail.username}" )
    private String mailFrom;
    
    @Value( "${application.mailTo}" )
    private String mailTo;
    
    @Value( "${application.docerBaseUrl}" )
    private String BASE_URL;
     
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Async
    public void sendContactUsEmail(ContactUsDTO contactUsDTO) throws Exception {
        log.info("Request to send contactUs email");
        String to = contactUsDTO.getTo() != null && !contactUsDTO.getTo().equals("") ? contactUsDTO.getTo() : mailTo;
        
        String signature = "\n\n--\nMittente: " + contactUsDTO.getName() + "\nEmail mittente: " + contactUsDTO.getFrom();
        String phone = contactUsDTO.getTelephone() != null && !contactUsDTO.getTelephone().equals("") ? "\n\nTelefono: " + contactUsDTO.getTelephone() : "";
        
        String text = contactUsDTO.getBody() + phone + signature;
        
//      log.info("Sending email...");
//	SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//	simpleMailMessage.setFrom(contactUsDTO.getFrom());
//	simpleMailMessage.setTo(to);
//	simpleMailMessage.setSubject(contactUsDTO.getSubject());
//	simpleMailMessage.setText(text);
//	javaMailSender.send(simpleMailMessage);
        
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(mailFrom);
        helper.setTo(to);
        helper.setText(text, false);
        helper.setSubject(contactUsDTO.getSubject());
        
        if(contactUsDTO.getAttachment() != null) {
            for(int i = 0; i < contactUsDTO.getAttachment().length; i++) {
                //TODO: gestire recupero allegati da docer
                // FileSystemResource file  = new FileSystemResource(new File(contactUsDTO.getAttachment()[i]));
                UrlResource file = new UrlResource("http", BASE_URL + contactUsDTO.getAttachment()[i]);
                log.info("Email attachments: {}", file.getFilename());
                String fileName = file.getFilename(); 
                helper.addAttachment(fileName, file);
            }
        }
        
        javaMailSender.send(message);
        log.info("Email sent to {}", to);
    }
    
}
