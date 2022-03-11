/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.firma;

import it.kdm.docer.firma.model.KeyValuePairDF;
import it.kdm.docer.firma.model.StreamDescriptor;

/**
 * @author lorenzo
 */
interface IBusinessLogic {
    String login(String userId, String password, String codiceEnte) throws FirmaException;

    String loginSSO(String saml, String codiceEnte) throws FirmaException;

    void logout(String token) throws FirmaException;

    StreamDescriptor[] firmaRemota(String ticket, String alias, String pin, String tipo, String OTP, String[] documenti) throws FirmaException;
    StreamDescriptor[] firmaRemota(String ticket, String alias, String pin, String tipo, String OTP, KeyValuePairDF [] file) throws FirmaException;

    StreamDescriptor[] firmaAutomatica(String ticket, String alias, String pin, String tipo, String[] documenti) throws FirmaException;
    StreamDescriptor[] firmaAutomatica(String ticket, String alias, String pin, String tipo, KeyValuePairDF[] file) throws FirmaException;

    String requestOTP(String ticket, String alias, String pin) throws FirmaException;

    boolean writeConfig(String token, String xml) throws FirmaException;

    String readConfig(String token) throws FirmaException;

}
