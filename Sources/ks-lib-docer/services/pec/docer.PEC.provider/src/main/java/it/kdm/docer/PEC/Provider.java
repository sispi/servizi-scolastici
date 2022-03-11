/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.PEC;

import java.util.Date;

/**
 *
 * @author lorenzo
 */
public class Provider implements IProvider {
    
	private String token = null;
	private it.kdm.docer.sdk.interfaces.ILoggedUserInfo currentUser = null;
	
    public String login(String username, String password, String codiceEnte) {
        return "tokenpec";
    }

    public void logout(String token) throws PECException {
        return;
    }

    public void setToken(String token) throws PECException {
        this.token = token;
    }

    public void setCurrentUser(it.kdm.docer.sdk.interfaces.ILoggedUserInfo info) throws PECException {
        this.currentUser = info;
    }

    public String invioPEC(long documentoId, String datiPec) {
        return "<Esito><Codice>0</Codice><Descrizione/><Identificativo>MSGID:" +new Date().getTime() +"</Identificativo></Esito>";
    }
    
}
