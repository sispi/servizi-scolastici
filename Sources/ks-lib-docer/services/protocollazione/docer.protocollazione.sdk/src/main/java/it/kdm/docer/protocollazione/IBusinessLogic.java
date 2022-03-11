/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.protocollazione;

/**
 * @author lorenzo
 */
public interface IBusinessLogic {
    public String login(String userId, String password, String codiceEnte)
            throws ProtocollazioneException;

    public String loginSSO(String saml, String codiceEnte) throws ProtocollazioneException;

    public void logout(String token)
            throws ProtocollazioneException;

    public String protocolla(String ticket, long documentoId, String datiProtocollo)
            throws ProtocollazioneException;

    public boolean writeConfig(String token, String xml)
            throws ProtocollazioneException;

    public String readConfig(String token)
            throws ProtocollazioneException;


}
