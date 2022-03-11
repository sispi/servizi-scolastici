/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fascicolazione;

import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

/**
 * @author lorenzo
 */
public interface IProvider extends it.kdm.docer.commons.docerservice.provider.IStandardProvider {

    public String login(String userId, String password, String codiceEnte)
            throws FascicolazioneException;

    public void logout(String token)
            throws FascicolazioneException;

    public void setCurrentUser(ILoggedUserInfo info) throws FascicolazioneException;

    public String fascicola(String datiFascicolo) throws FascicolazioneException;

    public String creaFascicolo(KeyValuePair[] metadati)
            throws FascicolazioneException;

    public String forzaNuovoFascicolo(KeyValuePair[] metadati)
            throws FascicolazioneException;

    public boolean updateACLFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] acl)
            throws FascicolazioneException;

    public String updateFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] metadati)
            throws FascicolazioneException;
}
