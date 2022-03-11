package it.kdm.docer.protocollazione;

import it.kdm.docer.sdk.exceptions.DocerException;
import org.slf4j.Logger;
import org.junit.Test;

/**
 * Created by microchip on 28/04/15.
 */
public class ProviderTest {

    static Logger log = org.slf4j.LoggerFactory.getLogger(ProviderTest.class.getName());

    String username = "test2";
    String pwd = "test2";
    String ente = "enteTest";

    ProviderMetro p;

    public ProviderTest() throws DocerException {
    }

    @Test
    public void login() throws ProtocollazioneException {
        p = new ProviderMetro();
        String dst = "";
        String error = "";

        try {
            dst = p.login(username, pwd, ente);
        } catch (Exception e) {
            error = e.getMessage();
        }
            log.debug("--------------------------------------------------");
            log.debug("Provider: login");
            log.debug("DST: " + dst);
            log.debug("error: " + error);
            log.debug("--------------------------------------------------");
    }


}


