/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.protocollazione;

import it.kdm.docer.commons.docerservice.provider.IStandardProvider;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

/**
 *
 * @author lorenzo
 */
public interface IProvider extends IStandardProvider {
    
    public String login(String userId, String password, String codiceEnte)
            throws ProtocollazioneException;
    public void logout(String token)
            throws ProtocollazioneException;
    
    public void setCurrentUser(ILoggedUserInfo info) throws ProtocollazioneException;
    
    public String protocolla(String datiProtocollo) 
            throws ProtocollazioneException;
}
