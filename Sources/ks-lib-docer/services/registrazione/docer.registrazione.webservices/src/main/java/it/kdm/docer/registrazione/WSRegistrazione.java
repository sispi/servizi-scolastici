/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.registrazione;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.docerservice.BaseService;

import it.kdm.docer.sdk.classes.KeyValuePair;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lorenzo
 */
public class WSRegistrazione extends BaseService {

    private static ApplicationContext ctx = null;

    TicketCipher cipher = new TicketCipher();

    public WSRegistrazione() {

        if (ctx == null) {
            try {
                ctx = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
            } catch (BeansException be) {
                be.printStackTrace();
            }
        }

    }

    public String login(String userId, String password, String codiceEnte)
            throws RegistrazioneException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.login(userId, password, codiceEnte));
    }

    public String loginSSO(String saml, String codiceEnte) throws RegistrazioneException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.loginSSO(saml, codiceEnte));
    }

    public boolean logout(String token)
            throws RegistrazioneException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        businessLogic.logout(cipher.decryptTicket(token));
        return true;
    }

    public boolean writeConfig(String token, String xmlConfig)
            throws RegistrazioneException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.writeConfig(cipher.decryptTicket(token), xmlConfig);
    }

    public String readConfig(String token)
            throws RegistrazioneException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.readConfig(cipher.decryptTicket(token));
    }

    public String registraById(String token, long documentoId,
                               String registroId, String datiRegistrazione) throws RegistrazioneException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.registra(cipher.decryptTicket(token), documentoId,
                registroId.toUpperCase(), datiRegistrazione);
    }

    public KeyValuePair[] getRegistri(String token, String cod_ente, String cod_aoo) throws RegistrazioneException {
        IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");
        return businessLogic.getRegistri(cipher.decryptTicket(token),cod_ente, cod_aoo);

    }




}
