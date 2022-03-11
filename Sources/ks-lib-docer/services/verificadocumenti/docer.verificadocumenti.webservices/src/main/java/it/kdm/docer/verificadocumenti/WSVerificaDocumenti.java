/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.verificadocumenti;

import it.kdm.docer.commons.TicketCipher;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.activation.DataHandler;

/**
 * @author Lorenzo Lucherini
 */
public class WSVerificaDocumenti {

    Logger logger = org.slf4j.LoggerFactory.getLogger(WSVerificaDocumenti.class);

    private static ApplicationContext ctx = null;

    TicketCipher cipher = new TicketCipher();
    IBusinessLogic businessLogic;

    public WSVerificaDocumenti() throws VerificaDocumentoException {

        if (ctx == null) {
            try {
                ctx = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
            } catch (BeansException be) {
                logger.error(be.getMessage(), be);
            }
        }

    }

    public String login(String userId, String password, String codiceEnte) throws VerificaDocumentoException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.login(userId, password, codiceEnte));
    }

    public String loginSSO(String saml, String codiceEnte) throws VerificaDocumentoException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.loginSSO(saml, codiceEnte));
    }

    public boolean logout(String token) throws VerificaDocumentoException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        businessLogic.logout(cipher.decryptTicket(token));
        return true;
    }

    public boolean writeConfig(String token, String xmlConfig) throws VerificaDocumentoException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.writeConfig(cipher.decryptTicket(token), xmlConfig);
    }

    public String readConfig(String token) throws VerificaDocumentoException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.readConfig(cipher.decryptTicket(token));
    }

    public String verificaDocumentoDocer(String token, String docId, String verifiche) throws VerificaDocumentoException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.verificaDocumento(cipher.decryptTicket(token), docId, verifiche);
    }

    public String verificaDocumento(String token, String nomeFile, DataHandler contenutoFile, String verifiche) throws VerificaDocumentoException {
        try {
            IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
            return businessLogic.verificaDocumento(cipher.decryptTicket(token), nomeFile, contenutoFile.getInputStream(), verifiche);
        } catch (Exception e) {
            throw new VerificaDocumentoException(e);
        }
    }
}
