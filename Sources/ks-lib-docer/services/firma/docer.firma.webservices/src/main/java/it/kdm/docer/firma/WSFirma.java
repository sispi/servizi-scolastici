/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.firma;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.firma.model.KeyValuePairDF;
import it.kdm.docer.firma.model.StreamDescriptor;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Łukasz Kwasek
 */
public class WSFirma {

    Logger logger = org.slf4j.LoggerFactory.getLogger(WSFirma.class);

    private static ApplicationContext ctx;
    TicketCipher cipher = new TicketCipher();

    public WSFirma() {
        if (ctx == null) {
            try {
                ctx = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
            } catch (BeansException be) {
                logger.error(be.getMessage(), be);
            }
        }
    }

    public String login(String userId, String password, String codiceEnte) throws FirmaException {
        IBusinessLogic businessLogic = getiBusinessLogic();
        return cipher.encryptTicket(businessLogic.login(userId, password, codiceEnte));
    }

    private IBusinessLogic getiBusinessLogic() {
        return (IBusinessLogic) ctx.getBean("businessLogic");
    }

    public String loginSSO(String saml, String codiceEnte) throws FirmaException {
        IBusinessLogic businessLogic = getiBusinessLogic();
        return cipher.encryptTicket(businessLogic.loginSSO(saml, codiceEnte));
    }

    public boolean logout(String token) throws FirmaException {

        IBusinessLogic businessLogic = getiBusinessLogic();
        businessLogic.logout(cipher.decryptTicket(token));
        return true;
    }

    public boolean writeConfig(String token, String xmlConfig) throws FirmaException {
        IBusinessLogic businessLogic = getiBusinessLogic();
        return businessLogic.writeConfig(cipher.decryptTicket(token), xmlConfig);
    }

    public String readConfig(String token) throws FirmaException {
        IBusinessLogic businessLogic = getiBusinessLogic();
        return businessLogic.readConfig(cipher.decryptTicket(token));
    }

    public StreamDescriptor[] firmaAutomaticaFile(String token, String alias, String pin, String tipo, KeyValuePairDF[] file) throws FirmaException {
        IBusinessLogic businessLogic = getiBusinessLogic();
        return businessLogic.firmaAutomatica(cipher.decryptTicket(token), alias, pin, tipo, file);
    }
    public StreamDescriptor[] firmaAutomatica(String token, String alias, String pin, String tipo, String[] documenti) throws FirmaException {
        IBusinessLogic businessLogic = getiBusinessLogic();
        return businessLogic.firmaAutomatica(cipher.decryptTicket(token), alias, pin, tipo, documenti);
    }

    public StreamDescriptor[] firmaRemotaFile(String token, String alias, String pin, String tipo, String OTP, KeyValuePairDF[] file) throws FirmaException {
        IBusinessLogic businessLogic = getiBusinessLogic();
        return businessLogic.firmaRemota(cipher.decryptTicket(token), alias, pin, tipo, OTP, file);
    }
    public StreamDescriptor[] firmaRemota(String token, String alias, String pin, String tipo, String OTP, String[] documenti) throws FirmaException {
        IBusinessLogic businessLogic = getiBusinessLogic();
        return businessLogic.firmaRemota(cipher.decryptTicket(token), alias, pin, tipo, OTP, documenti);
    }

    public String requestOTP(String token, String alias, String pin) throws FirmaException {
        IBusinessLogic businessLogic = getiBusinessLogic();
        return businessLogic.requestOTP(cipher.decryptTicket(token), alias, pin);
    }
}
