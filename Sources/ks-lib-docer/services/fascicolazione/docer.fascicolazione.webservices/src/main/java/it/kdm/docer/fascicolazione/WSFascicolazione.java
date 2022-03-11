/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fascicolazione;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.sdk.classes.KeyValuePair;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author lorenzo
 */
public class WSFascicolazione {

    private static ApplicationContext ctx;
    TicketCipher cipher = new TicketCipher();

    public WSFascicolazione() {
        if (ctx == null) {
            try {
                ctx = new ClassPathXmlApplicationContext("META-INF/applicationContext.xml");
            } catch (BeansException be) {
                be.printStackTrace();
            }
        }
    }

    public String loginSSO(String saml, String codiceEnte) throws FascicolazioneException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.loginSSO(saml, codiceEnte));
    }

    public String login(String userId, String password, String codiceEnte)
            throws FascicolazioneException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return cipher.encryptTicket(businessLogic.login(userId, password, codiceEnte));
    }

    public boolean logout(String token)
            throws FascicolazioneException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        businessLogic.logout(cipher.decryptTicket(token));
        return true;
    }

    public boolean writeConfig(String token, String xmlConfig)
            throws FascicolazioneException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.writeConfig(cipher.decryptTicket(token), xmlConfig);
    }

    public String readConfig(String token)
            throws FascicolazioneException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.readConfig(cipher.decryptTicket(token));
    }

    public String fascicolaById(String token, long documentId, String datiFascicolo)
            throws FascicolazioneException {

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        String esito = businessLogic.fascicola(cipher.decryptTicket(token), documentId, datiFascicolo);

        return esito;
    }

    public Boolean changeFascicoliById(String token, long docNum, KeyValuePair[][] fascicoliToRemove, KeyValuePair[][] fascicoliToAdd) throws FascicolazioneException{
        IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");
        Boolean esito = businessLogic.changeFascicoli(cipher.decryptTicket(token), docNum, fascicoliToRemove, fascicoliToAdd);
        return esito;
    }

    //permette di aggiungere o rimuovere acl di un fascicolo
    public boolean changeACLFascicolo(String token, KeyValuePair[] fascicoloId, KeyValuePair[] aclToAdd, String aclToRemove[]) throws FascicolazioneException {
        IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");
        return businessLogic.addRemoveACLFascicolo(cipher.decryptTicket(token), fascicoloId, aclToAdd, aclToRemove);
    }

    public String creaFascicolo(String token, KeyValuePair[] metadati) throws FascicolazioneException {
        //ritorna frmato esito solo codice e descrizione

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.creaFascicolo(cipher.decryptTicket(token), metadati, false);
    }

    public String forzaNuovoFascicolo(String token, KeyValuePair[] metadati) throws FascicolazioneException {
        //ritorna frmato esito solo codice e descrizione

        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.creaFascicolo(cipher.decryptTicket(token), metadati, true);
    }

    public boolean updateACLFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] acl)
            throws FascicolazioneException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.updateACLFascicolo(cipher.decryptTicket(token), fascicoloid, acl);
    }

    public String updateFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] metadati)
            throws FascicolazioneException {
        IBusinessLogic businessLogic = (IBusinessLogic) ctx.getBean("businessLogic");
        return businessLogic.updateFascicolo(cipher.decryptTicket(token), fascicoloid, metadati);
    }
}
