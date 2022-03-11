/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.verificadocumenti;

import java.io.InputStream;


/**
 *
 * @author Lorenzo Lucherini
 */
public interface IBusinessLogic {
    public String login(String userId, String password, String codiceEnte)
            throws VerificaDocumentoException;
    public String loginSSO(String saml, String codiceEnte)
            throws VerificaDocumentoException;
    public void logout(String ticket)
            throws VerificaDocumentoException;
    public boolean writeConfig(String ticket, String xml)
            throws VerificaDocumentoException;
    public String readConfig(String ticket)
            throws VerificaDocumentoException;
	public String verificaDocumento(String ticket, String docId, String metadata)
			throws VerificaDocumentoException;
	public String verificaDocumento(String ticket, String nomeFile, InputStream documento,
			String metadata) throws VerificaDocumentoException;
}
