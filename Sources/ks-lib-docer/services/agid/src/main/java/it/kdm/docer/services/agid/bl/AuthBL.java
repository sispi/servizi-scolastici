package it.kdm.docer.services.agid.bl;

import it.kdm.docer.clients.AuthenticationServiceExceptionException;
import it.kdm.docer.clients.AuthenticationServiceStub;
import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.ExceptionException;
import it.kdm.docer.commons.configuration.ConfigurationLoadingException;
import it.kdm.docer.commons.configuration.ConfigurationUtils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.rmi.RemoteException;

/**
 * Created by pamput on 3/12/14.
 */
public class AuthBL {

    private String user;
    private String pass;
    private String ente;
    private String appl;

    public AuthBL() {
        try {
        	File confFile = ConfigurationUtils.loadResourceFile("configuration.properties");
            PropertiesConfiguration cnf = new PropertiesConfiguration(confFile);

            user = cnf.getString("service.auth.username");
            pass = cnf.getString("service.auth.password");
            ente = cnf.getString("service.auth.ente");
            appl = cnf.getString("service.auth.applicazione");

            if (ente.equals("")) {
                ente = null;
            }

            if (appl.equals("")) {
                appl = null;
            }

        } catch (ConfigurationLoadingException|ConfigurationException e) {
            user = "admin";
            pass = "admin";
            ente = "";
            appl = null;
        }
    }

    public String getToken() throws RemoteException, ExceptionException, AuthenticationServiceExceptionException {
        return getToken(user, pass, ente, appl);
    }

    public String getToken(String ente) throws RemoteException, ExceptionException, AuthenticationServiceExceptionException {
        return getToken(user, pass, ente, appl);
    }

    public String getToken(String user, String pass, String ente, String appl) throws RemoteException, ExceptionException, AuthenticationServiceExceptionException {

        AuthenticationServiceStub.Login login = new AuthenticationServiceStub.Login();
        login.setUsername(user);
        login.setPassword(pass);
        login.setCodiceEnte(ente);
        login.setApplication(appl);

        return ClientManager.INSTANCE.getAuthenticationClient().login(login).get_return();
    }

}
