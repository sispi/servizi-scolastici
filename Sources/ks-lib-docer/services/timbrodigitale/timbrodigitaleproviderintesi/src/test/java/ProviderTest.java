import it.kdm.docer.timbro.provider.intesi.ProviderIntesi;
import it.kdm.docer.timbro.provider.intesi.TimbroIntesiDTO;
import it.kdm.docer.timbrodigitale.sdk.ProviderException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.activation.DataHandler;
import java.io.*;

/**
 * Created by antsic on 10/08/17.
 */
public class ProviderTest {


    @Test
    public void addStampAndSign(){

    ProviderIntesi providerTest = new ProviderIntesi();

        TimbroIntesiDTO timbro = new TimbroIntesiDTO();


        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("test.doc");

        DataHandler ds = new DataHandler(new InputStreamDataSource(is));

        timbro.setDocumento(ds);
        timbro.setAction(3);
        timbro.setTitle("Titolo documento");
        timbro.setUrl("http://qrbox-int.time4mind.com/qrbox/docs/test.pdf");
        timbro.setQrsize(125);


        try {
            DataHandler dataHandler = providerTest.applicaTimbro(timbro);
            InputStream iss = dataHandler.getInputStream();
            File tempFile = File.createTempFile("testTimbro", "doc");
            OutputStream os = new FileOutputStream(tempFile);

// This will copy the file from the two streams
            IOUtils.copy(is, os);

// This will close two streams catching exception
            IOUtils.closeQuietly(os);
            IOUtils.closeQuietly(is);
        } catch (ProviderException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
