/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.firma;

import it.kdm.docer.firma.model.DocumentResultInfo;
import it.kdm.docer.firma.model.factory.DocumentResultInfoFactory;
import it.kdm.docer.firma.utils.ConfigUtils;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.sign.model.ResultInfo;
import it.kdm.sign.service.SignServiceImplementation;
import it.kdm.sign.service.SignServiceInterface;
import org.apache.commons.configuration.ConfigurationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Lukasz Kwasek
 */
public class Provider implements IProvider {

    private String token;
    private ILoggedUserInfo currentUser;
    private ConfigUtils config;

    public Provider() throws IOException, ConfigurationException {
        config = new ConfigUtils();
    }

    @Override
    public String login(String username, String password, String codiceEnte) {
        return "tokenpec";
    }

    @Override
    public void logout(String token) throws FirmaException {
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public void setCurrentUser(ILoggedUserInfo info) throws FirmaException {
        this.currentUser = info;
    }

    @Override
    public List<DocumentResultInfo> firmaRemota(String ticket, String alias, String pin, String tipo, String OTP, Collection<String> documenti, Map<String, String> opzioni) throws FirmaException {

        if (!isValidTipo(tipo)) {
            throw new FirmaException("Tipo firma non valido (validi: CADES, PADES");
        }

        SignServiceInterface signService = new SignServiceImplementation();
        Map<String, String> opzioniz = config.map();
        signService.setConfig(opzioniz);

        if (opzioni != null) {
            opzioniz.putAll(opzioni);
        }

        opzioniz.put("formatoFirma", tipo);
        opzioniz.put("tipoFirma", "REMOTA");
        opzioniz.put("inputDir", config.getInputDirectory().getAbsolutePath());
        opzioniz.put("outputDir", config.getOutputDirectory().getAbsolutePath());
        if("PADES".equals(tipo)){
            opzioniz.put("accessPermissions",config.getAccessPermissions());
        }
        Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, OTP, opzioniz, new ArrayList<String>(documenti));
        List<DocumentResultInfo> ret = DocumentResultInfoFactory.list(result);

        return ret;

    }

    @Override
    public List<DocumentResultInfo> firmaAutomatica(String ticket, String alias, String pin, String tipo, Collection<String> documenti, Map<String, String> opzioni) throws FirmaException {

        if (!isValidTipo(tipo)) {
            throw new FirmaException("Tipo firma non valido (validi: CADES, PADES");
        }

        SignServiceInterface signService = new SignServiceImplementation();
        Map<String, String> opzioniz = config.map();
        signService.setConfig(opzioniz);

        if (opzioni != null) {
            opzioniz.putAll(opzioni);
        }

        opzioniz.put("formatoFirma", tipo);
        opzioniz.put("tipoFirma", "AUTOMATICA");
        opzioniz.put("inputDir", config.getInputDirectory().getAbsolutePath());
        opzioniz.put("outputDir", config.getOutputDirectory().getAbsolutePath());
        if("PADES".equals(tipo)){
            opzioniz.put("accessPermissions",config.getAccessPermissions());
        }
        Map<String, ResultInfo> result = signService.firmaDocumenti(alias, pin, "", opzioniz, new ArrayList<String>(documenti));
        List<DocumentResultInfo> ret = DocumentResultInfoFactory.list(result);

        return ret;

    }

    @Override
    public String requestOTP(String alias, String pin) throws FirmaException {

        SignServiceInterface signService = new SignServiceImplementation();
        Map<String, String> opzioniz = config.map();
        signService.setConfig(opzioniz);

        ResultInfo i = signService.richiestaOTP(alias,pin);

        if (i.getStatus() != 0) {
            throw new FirmaException("Impossibile ottenere OTP: " + i.getInfo());
        }

        for(Character c : alias.toCharArray()) {

        }

        return "OTP generato";
    }

    private boolean isValidTipo(String tipo) {
        return tipo.equals("CADES") || tipo.equals("PADES") || tipo.equals("XADES");
    }

}
