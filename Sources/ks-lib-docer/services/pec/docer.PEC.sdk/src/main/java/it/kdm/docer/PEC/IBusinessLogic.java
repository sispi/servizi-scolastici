/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.PEC;

/**
 *
 * @author lorenzo
 */
public interface IBusinessLogic {
    public String login(String userId, String password, String codiceEnte)
            throws PECException;
    public String loginSSO(String saml, String codiceEnte)
            throws PECException;
    public void logout(String token)
            throws PECException;

    public String invioPEC(String token, long documentoId, String datiPec) throws PECException;
    
    public boolean writeConfig(String token, String xml) throws PECException;
    
    public String readConfig(String token) throws PECException;
    
}
