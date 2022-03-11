/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.PEC;

import it.kdm.docer.commons.TicketCipher;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lorenzo
 */
public class WSPEC {

    Logger logger = org.slf4j.LoggerFactory.getLogger(WSPEC.class);

    private static ApplicationContext ctx;
    TicketCipher cipher = new TicketCipher();


    public WSPEC() {
        if (ctx == null) {
            try {
                ctx = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
            } catch (BeansException be) {
                logger.error(be.getMessage(),be);
            }
        }
    }

    public String login(String userId, String password, String codiceEnte)
            throws PECException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.login(userId, password, codiceEnte));
    }

    public String loginSSO(String saml, String codiceEnte) throws PECException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.loginSSO(saml, codiceEnte));
    }

    public boolean logout(String token)
            throws PECException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        businessLogic.logout(cipher.decryptTicket(token));
        return true;
    }

    public boolean writeConfig(String token, String xmlConfig)
            throws PECException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.writeConfig(cipher.decryptTicket(token), xmlConfig);
    }

    public String readConfig(String token)
            throws PECException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.readConfig(cipher.decryptTicket(token));
    }

    public String invioPEC(String token, long documentoId,
                           String datiPec) throws PECException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.invioPEC(cipher.decryptTicket(token), documentoId, datiPec);
    }
}
