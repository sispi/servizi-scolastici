/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.registrazione;

import it.kdm.docer.commons.docerservice.provider.IStandardProvider;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

/**
 *
 * @author lorenzo
 */
public interface IProvider extends IStandardProvider {
    
    public String login(String userId, String password, String codiceEnte)
            throws RegistrazioneException;
    public void logout(String token)
            throws RegistrazioneException;

    public void setCurrentUser(ILoggedUserInfo info) throws RegistrazioneException;

    public String registra(String registroId, String datiRegistrazione) throws RegistrazioneException;
    
}
