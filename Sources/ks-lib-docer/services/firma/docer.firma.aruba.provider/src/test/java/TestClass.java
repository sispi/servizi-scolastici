import it.filippetti.docer.firma.aruba.Provider;
import it.kdm.docer.firma.FirmaException;
import it.kdm.docer.firma.model.DocumentResultInfo;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Created by microchip on 18/10/18.
 */
public class TestClass {
    @Test
    public void testFirmaPADES() throws IOException, ConfigurationException, FirmaException {
        Provider provider = new Provider();

        String ticket = "xxxxx";
        String alias =  "titolare"; //"francescotassone";
        String pin = "password22"; //"1451936673";
        String tipo = "PADES";
        String otp = "0462174766";
        List<String> documenti = new ArrayList<String>();
        String pathDoc = "/home/microchip/Desktop/TIMBRO/filePdf.pdf";
        documenti.add(pathDoc);
        pathDoc =  "/home/microchip/Desktop/TIMBRO/filePdf2.pdf";
        documenti.add(pathDoc);

        Map<String,String> opzioni = new HashMap<String,String>();
//        opzioni.put("otpFirma","dsign");

        List<DocumentResultInfo> docs =  provider.firmaRemota(ticket,alias,pin,tipo,otp,documenti,opzioni);

    }

    @Test
    public void testFirmaCADES() throws IOException, ConfigurationException, FirmaException {
        Provider provider = new Provider();

        String ticket = "xxxxx";
        String alias = "titolare"; //"XVVIRXAASO";
        String pin = "password22"; //"1451936673";
        String tipo = "CADES";
        String otp = "0462174766";
        List<String> documenti = new ArrayList<String>();
        String pathDoc = "1318743291705522_determina.pdf";
        documenti.add(pathDoc);
//        pathDoc =  "filePdf2.pdf";
//        documenti.add(pathDoc);

        Map<String,String> opzioni = new HashMap<String,String>();
//        opzioni.put("otpFirma","dsign");

        List<DocumentResultInfo> docs =  provider.firmaRemota(ticket,alias,pin,tipo,otp,documenti,opzioni);

    }

    @Test
    public void testFirmaXADES() throws IOException, ConfigurationException, FirmaException {
        Provider provider = new Provider();

        String ticket = "xxxxx";
        String alias = "titolare"; //"TSSFNC83M30H534R"; //"XVVIRXAASO";
        String pin = "password22"; //"1451936673"; //"1451936673";
        String tipo = "XADES";
        String otp = "0462174766";
        List<String> documenti = new ArrayList<String>();
        String pathDoc = "/home/microchip/Desktop/TIMBRO/buffer2.xml";
        documenti.add(pathDoc);


        Map<String,String> opzioni = new HashMap<String,String>();
//        opzioni.put("otpFirma","dsign");

        List<DocumentResultInfo> docs =  provider.firmaRemota(ticket,alias,pin,tipo,otp,documenti,opzioni);

    }
}
