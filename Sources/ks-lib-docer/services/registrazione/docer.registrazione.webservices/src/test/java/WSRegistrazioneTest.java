/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import it.kdm.docer.registrazione.RegistrazioneException;
import it.kdm.docer.registrazione.WSRegistrazione;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author stefano.vigna
 */
public class WSRegistrazioneTest {
    
    public WSRegistrazioneTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void test() throws IOException, RegistrazioneException{
        InputStream resStream = this.getClass().getResourceAsStream("/xmlInput.xml");
        String datiInput = IOUtils.toString(resStream);
            
        WSRegistrazione p = new WSRegistrazione();
        String ticket = p.login("admin", "admin", "docarea");
        
        long documentoId = 787;
        String registroId = "atti";
        
        
        p.registraById(ticket, documentoId, registroId, datiInput);
    }
    
   
}
