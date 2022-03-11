/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.protocollazione;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.commons.docerservice.BaseService;
import it.kdm.docer.commons.docerservice.BaseServiceException;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import org.jaxen.JaxenException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author lorenzo
 */
public class WSProtocollazione {
    
    private static ApplicationContext ctx = null;
    TicketCipher cipher = new TicketCipher();
    
    public WSProtocollazione() {
        if(ctx==null){
            try{
        	ctx = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
            }
            catch(BeansException be){
        	be.printStackTrace();
            }
        }
    }

    public String loginSSO(String saml, String codiceEnte) throws ProtocollazioneException {

        IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.loginSSO(saml, codiceEnte));
    }

    public String login(String userId, String password, String codiceEnte)
            throws ProtocollazioneException {
        
        IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.login(userId, password, codiceEnte));
    }
    
    public boolean logout(String token)
            throws ProtocollazioneException {
        
        IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");
        businessLogic.logout(cipher.decryptTicket(token));
        return true;
    }
    
    public boolean writeConfig(String token, String xmlConfig) 
            throws ProtocollazioneException{
        IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");
        return businessLogic.writeConfig(cipher.decryptTicket(token), xmlConfig);
    }
    
    public String readConfig(String token) 
            throws ProtocollazioneException {
        IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");
        return businessLogic.readConfig(cipher.decryptTicket(token));
    }
    
    public String protocollaById(String token, long documentoId, 
            String datiProtocollo) throws ProtocollazioneException {
        
        
        IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");
        String esito = businessLogic.protocolla(cipher.decryptTicket(token), documentoId,datiProtocollo);
        
        return esito;
        
        /*
        XMLUtil dom;
        try {
            dom = new XMLUtil(esito);
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(WSProtocollazione.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        } catch (XMLStreamException ex) {
            org.slf4j.LoggerFactory.getLogger(WSProtocollazione.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        }
        
        String code;
        try {
            code = dom.getNodeValue("//codice");
        } catch (JaxenException ex) {
            org.slf4j.LoggerFactory.getLogger(WSProtocollazione.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        }
        
        if (code.equalsIgnoreCase("0"))
            return true;
        
        return false;
        */
    }
    
    
}
