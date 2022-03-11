import it.kdm.docer.digitalstamp.provider.intesi.ProviderIntesi;
import it.kdm.docer.digitalstamp.sdk.ProviderException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;

/**
 * Created by antsic on 10/08/17.
 */
public class ProviderTest_saveDoc {


    @Test
    public void addStampAndSign() {

    ProviderIntesi providerTest = new ProviderIntesi();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("test.pdf");
        DataHandler ds = new DataHandler(new InputStreamDataSource(is));

        //timbro.setDocumento(ds);
        //timbro.setUrl("http://qrbox-int.time4mind.com/qrbox/docs/test.doc");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // root element
        Document doc = docBuilder.newDocument();
        Element DigitalStampInfo = doc.createElement("DigitalStampInfo");
        doc.appendChild(DigitalStampInfo);
        // stampPage elements
        Element stampPage = doc.createElement("stampPage");
        stampPage.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(stampPage);
        // positionX elements
        Element positionX = doc.createElement("positionX");
        positionX.appendChild(doc.createTextNode("9"));
        DigitalStampInfo.appendChild(positionX);
        // positionY elements
        Element positionY = doc.createElement("positionY");
        positionY.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(positionY);
        // positionTag elements
        Element positionTag = doc.createElement("positionTag");
        positionTag.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(positionTag);
        // correctionLevel elements
        Element correctionLevel = doc.createElement("correctionLevel");
        correctionLevel.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(correctionLevel);
        // signer elements
        Element signer = doc.createElement("signer");
        signer.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(signer);
        // pin elements
        Element pin = doc.createElement("pin");
        pin.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(pin);
        // otp elements
        Element otp = doc.createElement("otp");
        otp.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(otp);
        // action elements
        Element action = doc.createElement("action");
        action.appendChild(doc.createTextNode("3"));
        DigitalStampInfo.appendChild(action);
        // qrsize elements
        Element qrsize = doc.createElement("qrsize");
        qrsize.appendChild(doc.createTextNode("150"));
        DigitalStampInfo.appendChild(qrsize);
        // isShortnedURL elements
        Element isShortnedURL = doc.createElement("isShortnedURL");
        isShortnedURL.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(isShortnedURL);
        // typeDoc elements
        Element typeDoc = doc.createElement("typeDoc");
        typeDoc.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(typeDoc);
        // templateName elements
        Element templateName = doc.createElement("templateName");
        templateName.appendChild(doc.createTextNode("v1_addStampAndSignByDocnumTemplate"));
        DigitalStampInfo.appendChild(templateName);
        // locale elements
        Element locale = doc.createElement("locale");
        locale.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(locale);
        // title elements
        Element title = doc.createElement("title");
        String titleString="prova001";
        title.appendChild(doc.createTextNode(titleString));
        DigitalStampInfo.appendChild(title);
        // exDate elements
        Element exDate = doc.createElement("exDate");
        exDate.appendChild(doc.createTextNode(""));
        DigitalStampInfo.appendChild(exDate);

        // metadata elements
        Element metadata = doc.createElement("metadata");

        // digitalStampMeta elements
        Element digitalStampMeta1 = doc.createElement("digitalStampMeta");
        Element key1 = doc.createElement("key");
        key1.appendChild(doc.createTextNode("Descrizione"));
        Element value1 = doc.createElement("value");
        value1.appendChild(doc.createTextNode("prova"));
        digitalStampMeta1.appendChild(key1);
        digitalStampMeta1.appendChild(value1);

        metadata.appendChild(digitalStampMeta1);

        DigitalStampInfo.appendChild(metadata);

        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        HashMap<String,String> otherData=new HashMap<String,String>();
        otherData.put("Numero protocollo","3");
        otherData.put("Descrizione 2","05/09/2017");
        otherData.put("Anno protocollo","2017");

        try {

            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);

            DataHandler dataHandler = providerTest.AddStampAndSignbyDocnum(ds,writer.toString(),otherData);

        } catch (ProviderException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }

    }