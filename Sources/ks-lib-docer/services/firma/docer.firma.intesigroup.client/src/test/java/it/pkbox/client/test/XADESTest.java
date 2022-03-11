package it.pkbox.client.test;

import it.pkbox.client.PKBox;
import it.pkbox.client.XMLDataObjectFormat;
import it.pkbox.client.XMLEnvelope;
import it.pkbox.client.XMLReference;
import it.pkbox.client.XMLSignatureProductionPlace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class XADESTest {
	static final String KDMDir;
	static final String inputDir;
	static final String outputDir;
	static final String alias;
	static final String pin;
	static final String pkboxServerUrl;
	static final String securPin;
	static {
		InputStream isProps;
		
		try {
			isProps = Class.forName(XADESTest.class.getName()).getResourceAsStream("config.properties");
			Properties props = new Properties();
			props.load(isProps);
			KDMDir = props.getProperty("KDMDir");
			inputDir = props.getProperty("inputDir");
			outputDir = props.getProperty("outputDir");
			alias = props.getProperty("alias");
			pin = props.getProperty("pin");
			pkboxServerUrl = props.getProperty("pkboxServerUrl");
			securPin = props.getProperty("securPin");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Properties non loaded");
		}
	}
	
	public static void main(String[] args) {

		PKBox pbox = null;
		XMLEnvelope xmlEnve = null;
		
		try {
			pbox = new PKBox();
			pbox.addServer(pkboxServerUrl, null, null, null);
			xmlEnve = new XMLEnvelope(pbox);
			
            XMLSignatureProductionPlace prodPlace = null;
            XMLReference[] references = new XMLReference[1];
            
            references[0] = new XMLReference(null, false, new XMLDataObjectFormat(), null, "", null, false, true);
            
            File fin = new File( inputDir + File.separator + "sample.xml" );
            FileInputStream fis = new FileInputStream( fin );
            FileOutputStream fos = new FileOutputStream( outputDir + File.separator + "sample_out.xml" );
            
            String signer = alias;
            String signerPin = "202683";
            String canonicalization = null;
            int envelopedMode = XMLEnvelope.envelopedMode;

            int dataLength = (int)fin.length();
                       
            xmlEnve.xmlsign(fis, dataLength , canonicalization, references, prodPlace, signer, pin, signerPin, envelopedMode , new Date(), fos);

            System.out.println("completed");
            
            if(fos!=null){
                fos.close();
            }
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

}
