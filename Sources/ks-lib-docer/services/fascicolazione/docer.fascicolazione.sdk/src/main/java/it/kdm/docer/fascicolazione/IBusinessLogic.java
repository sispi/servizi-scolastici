/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fascicolazione;

import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.FascicoloInfo;
import it.kdm.docer.sdk.classes.KeyValuePair;

import java.util.List;
import java.util.Map;

/**
 *
 * @author lorenzo
 */
public interface IBusinessLogic {
    public String login(String userId, String password, String codiceEnte)
            throws FascicolazioneException;

    public String loginSSO(String saml, String codiceEnte) throws FascicolazioneException;

    public void logout(String token)
            throws FascicolazioneException;

    public String fascicola(String token, long documentId, String datiFascicolo) throws FascicolazioneException;
    public boolean changeFascicoli(String token, long docNum, KeyValuePair[][] fascicoliToRemove, KeyValuePair[][] fascicoliToAdd) throws FascicolazioneException;
    public String creaFascicolo(String token, KeyValuePair[] metadati, boolean forzaNuovoFascicolo)
            throws FascicolazioneException;
    public boolean updateACLFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] acl)
            throws FascicolazioneException;

    public String updateFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] metadati)
            throws FascicolazioneException;

    public boolean writeConfig(String token, String xml)
            throws FascicolazioneException;

    public String readConfig(String token)
            throws FascicolazioneException;

    public boolean addRemoveACLFascicolo(String ticket, KeyValuePair[] fascicoloid, KeyValuePair aclToAdd[], String aclToRemove[]) throws FascicolazioneException;
}
