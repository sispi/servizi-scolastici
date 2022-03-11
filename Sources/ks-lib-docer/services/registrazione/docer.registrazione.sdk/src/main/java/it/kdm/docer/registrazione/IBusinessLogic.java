/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.registrazione;

import it.kdm.docer.sdk.classes.KeyValuePair;

/**
 * @author lorenzo
 */
public interface IBusinessLogic {
    public String login(String userId, String password, String codiceEnte)
            throws RegistrazioneException;

    public void logout(String token)
            throws RegistrazioneException;

    public String registra(String token, long documentoId, String registroId, String datiRegistrazione)
            throws RegistrazioneException;

    public boolean writeConfig(String token, String xml)
            throws RegistrazioneException;

    public String readConfig(String token)
            throws RegistrazioneException;
    public KeyValuePair [] getRegistri(String token, String ente, String aoo)
            throws RegistrazioneException;

    public String loginSSO(String saml, String codiceEnte) throws RegistrazioneException;
}
