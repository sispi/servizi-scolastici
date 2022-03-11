package it.kdm.docer.fascicolazione.batch;

import it.kdm.docer.clients.*;
import it.kdm.doctoolkit.clients.DocerServicesStub;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by SL_550 on 01/12/2015.
 */
public class WSBusinessLogic {

    public static final WSBusinessLogic INSTANCE;

    static {
            INSTANCE = new WSBusinessLogic();
        }

    public static void createFascicolo(String token, Map<String, String> info) throws RemoteException, FascicolazioneExceptionException {

        WSFascicolazioneStub.CreaFascicolo req = new WSFascicolazioneStub.CreaFascicolo();
        req.setToken(token);

        WSFascicolazioneStub wsf = ClientManager.INSTANCE.getFascicolazioneClient();

        WSFascicolazioneStub.KeyValuePair currentPair;

        for (String key : info.keySet()) {
            currentPair = new WSFascicolazioneStub.KeyValuePair();
            currentPair.setKey(key.toUpperCase());
            currentPair.setValue(info.get(key));
            req.addMetadati(currentPair);
        }

        WSFascicolazioneStub.CreaFascicoloResponse resp = wsf.creaFascicolo(req);
    }



    public static String login(String codiceEnte, String userid, String password)  {
        try {
            AuthenticationServiceStub stub = ClientManager.INSTANCE.getAuthenticationClient();
            AuthenticationServiceStub.Login login = new AuthenticationServiceStub.Login();
            login.setCodiceEnte(codiceEnte);
            login.setUsername(userid);
            login.setPassword(password);

            AuthenticationServiceStub.LoginResponse resp = stub.login(login);
            return resp.get_return();
        } catch(Exception ex) {
            return null;
        }
    }
}
