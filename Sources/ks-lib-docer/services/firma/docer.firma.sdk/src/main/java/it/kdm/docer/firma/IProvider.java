package it.kdm.docer.firma;

import it.kdm.docer.commons.docerservice.provider.IStandardProvider;
import it.kdm.docer.firma.model.DocumentResultInfo;
import it.kdm.docer.firma.model.KeyValuePair;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Łukasz Kwasek on 23/06/15.
 */
public interface IProvider extends IStandardProvider {
    String login(String username, String password, String codiceEnte);

    void logout(String token) throws FirmaException;

    void setToken(String token);

    void setCurrentUser(ILoggedUserInfo info) throws FirmaException;

    List<DocumentResultInfo> firmaRemota(String ticket, String alias, String pin, String tipo, String OTP, Collection<String> documenti, Map<String, String> opzioni) throws FirmaException;

    List<DocumentResultInfo> firmaAutomatica(String ticket, String alias, String pin, String tipo, Collection<String> documenti, Map<String, String> opzioni) throws FirmaException;

    String requestOTP(String alias, String pin) throws FirmaException;
}
