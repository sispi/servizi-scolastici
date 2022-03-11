package it.kdm.docer.assemblea_legislativa.test;

import it.pkbox.client.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XMLTest
{
	/**
	 * @param args
	 */
	public static void main(String args[])
	{
		byte[] envelope = null;

		try
		{
            PKBox pkbox = new PKBox();
			pkbox.addServer("http://localhost:8081/pkserver/servlet/defaulthandler", "", "", "");

            // Initialize Envelope Object
            XMLEnvelope  xmlenv  = new XMLEnvelope(pkbox);

            XMLVerifyInfo   xmlvi         = null;
            
            FileInputStream fileDoc = new FileInputStream("C:\\test_dir\\sample.xml");
            byte[] document = new byte[fileDoc.available()];
            fileDoc.read(document);
            fileDoc.close();
            
            XMLSignatureProductionPlace prodPlace = null;
            XMLDataObjectFormat[] dataObjForms = null;
            XMLReferenceData[] refsData = null;
            XMLReference[] references = new XMLReference[1];
            
            references[0] = new XMLReference(null, false, null, null, "", null, false, true);
            
            envelope = xmlenv.xmlsign(document, null, references, null, null, "remota01", "12345678", null, XMLEnvelope.envelopedMode, null);
            
            FileOutputStream fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Enveloped-1.xml");
            fileEnv.write(envelope);
            fileEnv.close();

            references[0] = new XMLReference(null, false, null, null, "", null, false, true);

            envelope = xmlenv.xmladdsign(envelope, null, references, null, "Intesi", "Signer 4", "12345678", null, XMLEnvelope.envelopedMode, null);

            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Enveloped-2.xml");
            fileEnv.write(envelope);
            fileEnv.close();

            envelope = xmlenv.xmlcountersign(envelope, null, null, "Intesi", "Signer 4", "12345678", null, null);

            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Enveloped-3.xml");
            fileEnv.write(envelope);
            fileEnv.close();
            
//            xmlvi = xmlenv.xmlverify(null, envelope, null, true, "Intesi");
            System.out.println(xmlvi.toString());
            
            fileEnv = new FileOutputStream("C:\\test_dir\\Temp\\CDA-Enveloped-Verified-1.1.xml");
            fileEnv.write(xmlvi.getSigner(0).getReferences()[0].geteReferenceData().getDataBuffer());
            fileEnv.close();

            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Enveloped-Verified-2.1.xml");
            fileEnv.write(xmlvi.getSigner(1).getReferences()[0].geteReferenceData().getDataBuffer());
            fileEnv.close();
            
            refsData = new XMLReferenceData[2];
            references = new XMLReference[2];
            
            refsData[0] = new XMLReferenceData(null, 0, 0, "file:///C:/Temp/debug.txt", "text", null);
            references[0] = new XMLReference(refsData[0], false, null, null, "file:///C:/Temp/debug.txt", null, false, true);

            refsData[1] = new XMLReferenceData(null, 0, 0, "file:///C:/Temp/SampleImage.jpg", "binary", null);
            references[1] = new XMLReference(refsData[1], false, null, null, "file:///C:/Temp/SampleImage.jpg", null, false, true);
            
            envelope = xmlenv.xmlsign(null, null, references, null, "Intesi", "Signer 4", "12345678", null, XMLEnvelope.detachedMode, null);
            
            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Detached(e)-1.xml");
            fileEnv.write(envelope);
            fileEnv.close();

            refsData[0] = new XMLReferenceData(null, 0, 0, "file:///C:/Temp/debug.txt", "text", null);
            references[0] = new XMLReference(refsData[0], false, null, null, "file:///C:/Temp/debug.txt", null, false, true);

            refsData[1] = new XMLReferenceData(null, 0, 0, "file:///C:/Temp/SampleImage.jpg", "binary", null);
            references[1] = new XMLReference(refsData[1], false, null, null, "file:///C:/Temp/SampleImage.jpg", null, false, true);

            envelope = xmlenv.xmladdsign(envelope, null, references, null, "Intesi", "Signer 4", "12345678", null, XMLEnvelope.detachedMode, null);

            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Detached(e)-2.xml");
            fileEnv.write(envelope);
            fileEnv.close();

            envelope = xmlenv.xmlcountersign(envelope, null, null, "Intesi", "Signer 4", "12345678", null, null);

            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Detached(e)-3.xml");
            fileEnv.write(envelope);
            fileEnv.close();
            
//            xmlvi = xmlenv.xmlverify(null, envelope, null, true, "Intesi");
            System.out.println(xmlvi.toString());

            refsData = new XMLReferenceData[2];
            references = new XMLReference[2];
            
            refsData[0] = new XMLReferenceData("C:\\test_dir\\debug.txt", "C:\\test_dir\\debug.txt", "text", null);
            references[0] = new XMLReference(refsData[0], true, null, null, "C:\\test_dir\\debug.txt", null, false, true);

            refsData[1] = new XMLReferenceData("C:\\test_dir\\SampleImage.jpg", "C:\\test_dir\\SampleImage.jpg", "binary", null);
            references[1] = new XMLReference(refsData[1], true, null, null, "C:\\test_dir\\SampleImage.jpg", null, false, true);
            
            envelope = xmlenv.xmlsign(null, null, references, null, "Intesi", "Signer 4", "12345678", null, XMLEnvelope.detachedMode, null);
            
            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Detached(i)-1.xml");
            fileEnv.write(envelope);
            fileEnv.close();

            refsData[0] = new XMLReferenceData("C:\\test_dir\\debug.txt", "C:\\test_dir\\debug.txt", "text", null);
            references[0] = new XMLReference(refsData[0], true, null, null, "C:/Temp/debug.txt", null, false, true);

            refsData[1] = new XMLReferenceData("C:\\test_dir\\SampleImage.jpg", "C:\\test_dir\\SampleImage.jpg", "binary", null);
            references[1] = new XMLReference(refsData[1], true, null, null, "C:\\test_dir\\SampleImage.jpg", null, false, true);

            envelope = xmlenv.xmladdsign(envelope, null, references, null, "Intesi", "Signer 4", "12345678", null, XMLEnvelope.detachedMode, null);

            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Detached(i)-2.xml");
            fileEnv.write(envelope);
            fileEnv.close();

            envelope = xmlenv.xmlcountersign(envelope, null, null, "Intesi", "Signer 4", "12345678", null, null);

            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Detached(i)-3.xml");
            fileEnv.write(envelope);
            fileEnv.close();
            
//            xmlvi = xmlenv.xmlverify(null, envelope, null, true, "Intesi");
            System.out.println(xmlvi.toString());

            dataObjForms = new XMLDataObjectFormat[2];
            refsData = new XMLReferenceData[2];
            references = new XMLReference[2];
            
            dataObjForms[0] = new XMLDataObjectFormat("Object Format 1", null, "text", "UTF-8");
            refsData[0] = new XMLReferenceData("C:\\test_dir\\debug.txt", "C:\\test_dir\\debug.txt", "text", "UTF-8");
            references[0] = new XMLReference(refsData[0], true, dataObjForms[0], null, "C:/Temp/debug.txt", null, false, true);

            dataObjForms[1] = new XMLDataObjectFormat("Object Format 2", new XMLObjectIdentifier("1,1", XMLObjectIdentifier.OID_AS_URN, "Object Identifier Description", null), "binary", "jpg");
            refsData[1] = new XMLReferenceData("C:\\test_dir\\SampleImage.jpg", "C:\\test_dir\\SampleImage.jpg", "binary", "jpg");
            references[1] = new XMLReference(refsData[1], true, dataObjForms[1], null, "C:/Temp/SampleImage.jpg", null, false, true);
            
            envelope = xmlenv.xmlsign(null, null, references, null, "Intesi", "Signer 4", "12345678", null, XMLEnvelope.envelopingMode, null);
            
            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Enveloping-1.xml");
            fileEnv.write(envelope);
            fileEnv.close();

            refsData[0] = new XMLReferenceData("C:\\test_dir\\debug.txt", "C:\\test_dir\\debug.txt", "text", null);
            references[0] = new XMLReference(refsData[0], true, null, null, "C:\\test_dir\\debug.txt", null, false, true);

            refsData[1] = new XMLReferenceData("C:\\test_dir\\SampleImage.jpg", "C:\\test_dir\\SampleImage.jpg", "binary", null);
            references[1] = new XMLReference(refsData[1], true, null, null, "C:\\test_dir\\SampleImage.jpg", null, false, true);

            prodPlace = new XMLSignatureProductionPlace ("Milan", "IT", "20100", "Italy");
            
            envelope = xmlenv.xmladdsign(envelope, null, references, prodPlace, "Intesi", "Signer 4", "12345678", null, XMLEnvelope.detachedMode, null);

            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Enveloping-2.xml");
            fileEnv.write(envelope);
            fileEnv.close();

            envelope = xmlenv.xmlcountersign(envelope, null, null, "Intesi", "Signer 4", "12345678", null, null);

            fileEnv = new FileOutputStream("C:\\test_dir\\CDA-Enveloping-3.xml");
            fileEnv.write(envelope);
            fileEnv.close();
            
//            xmlvi = xmlenv.xmlverify(null, envelope, null, true, "Intesi");
            System.out.println(xmlvi.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
