/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.PEC;

import it.kdm.docer.commons.docerservice.provider.IStandardProvider;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

/**
 *
 * @author lorenzo
 */
public interface IProvider extends IStandardProvider {
    
    public String login(String userId, String password, String codiceEnte)
            throws PECException;
    public void logout(String token)
            throws PECException;
    
    public void setCurrentUser(ILoggedUserInfo info) throws PECException;

    public String invioPEC(long documentoId, String datiPec);
}
