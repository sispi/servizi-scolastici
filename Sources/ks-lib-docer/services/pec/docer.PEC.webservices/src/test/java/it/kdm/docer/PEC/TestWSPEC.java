package it.kdm.docer.PEC;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import it.kdm.docer.PEC.WSPEC;
import it.kdm.docer.commons.configuration.ConfigurationUtils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author stefano.vigna
 */
public class TestWSPEC {
    
    public TestWSPEC() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
      @Test
    public void test() throws Exception{
    	  
    	File response = ConfigurationUtils.loadResourceFile("xmlInput.xml");          
        String datiPec = new String(Files.readAllBytes(response.toPath()));
            
        WSPEC p = new WSPEC();
        String ticket = "";
        
        //ticket = p.login("admin", "admin", "docarea");
        
        long documentoId = 787;
        
        
        p.invioPEC(ticket, documentoId, datiPec);
    } 
}
