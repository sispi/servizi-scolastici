package it.kdm.docer.digitalstamp.bl;

import com.google.common.base.Strings;
import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.commons.configuration.Configuration;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.commons.docerservice.BaseService;
import it.kdm.docer.commons.docerservice.BaseServiceException;
import it.kdm.docer.digitalstamp.sdk.*;
import it.kdm.docer.sdk.classes.StreamDescriptor;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.doctoolkit.exception.DocerApiException;
import it.kdm.doctoolkit.model.DocerFile;
import it.kdm.doctoolkit.model.Documento;
import it.kdm.doctoolkit.model.Protocollo;
import it.kdm.doctoolkit.services.DocerService;
import it.kdm.utils.ResourceLoader;
import org.apache.axiom.om.OMElement;
import org.slf4j.Logger;
import org.dom4j.Document;
import org.jaxen.JaxenException;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.security.KeyException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class BusinessLogic extends BaseService implements IBusinessLogic{
	static private Logger logger = org.slf4j.LoggerFactory.getLogger(BusinessLogic.class.getName());
	private IProvider provider;
	public BusinessLogic() {

	}
	private String entetoken = null;
	private static Configurations CONFIGURATIONS = new Configurations();

	public String login(String userId, String password, String codiceEnte) throws DigitalStampException {

		entetoken = codiceEnte;

		try {
			return baseLogin(userId, password, codiceEnte);
		} catch (BaseServiceException e) {
			e.printStackTrace();
			logger.error("BusinessLogic login errore:{}",e);
			throw new DigitalStampException(e);
		}
	}

	public String loginSSO(String saml, String codiceEnte) throws DigitalStampException {
		entetoken = codiceEnte;

		try {
			return baseLoginSSO(saml, codiceEnte);
		} catch (BaseServiceException e) {
			e.printStackTrace();
			logger.error("BusinessLogic loginSSO errore:{}",e);
			throw new DigitalStampException(e);
		}
	}

	public void logout(String token) throws DigitalStampException {

		return ;
	}


	public boolean writeConfig(String token, String xml) throws DigitalStampException {

		try {
			entetoken = Utils.extractTokenKey(token, "ente");
		} catch (KeyException e) {
			e.printStackTrace();
			throw new DigitalStampException(e.getMessage());
		}

		try {
			CONFIGURATIONS.writeConfig(entetoken, xml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("BusinessLogic writeConfig errore:{}",e);
			throw new DigitalStampException("Configurazione digitalstamp:"+e.getMessage());
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DigitalStampException("Configurazione malformata");
		}

		return true;

	}

	public String readConfig(String token) throws DigitalStampException {
		try {
			entetoken = Utils.extractTokenKey(token, "ente");
		} catch (KeyException e) {
			e.printStackTrace();
			throw new DigitalStampException(e.getMessage());
		}

		try {
			return CONFIGURATIONS.readConfig(entetoken);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DigitalStampException(e.getMessage());
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DigitalStampException(e.getMessage());
		}

	}



	private IProvider getProvider(String ente, String aoo) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, DigitalStampException {

		String providerClassName = "";

		String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
		try {
			providerClassName = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpathProvider).getText();
		} catch (Exception e) {
			throw new DigitalStampException("Impossibile trovare il provider per ENTE: " + ente + " e AOO: " + aoo + " nel file di configurazione. " + e.getMessage());
		}

		IProvider prov = (IProvider) Class.forName(providerClassName).newInstance();
		return prov;
	}









	public DataHandler AddStampAndSign(String ente, String aoo, DataHandler documento, KeyValuePair[] metadata, int stampPage, int positionX, int positionY, String positionTag, String correctionLevel,
									   String signer, String pin, String otp, int action, int qrsize, boolean isShortnedURL, String url,
									   String typeDoc, String templateName, String locale, String title, String exDate) throws BusinessLogicException {
		

		try {


			this.provider = getProvider(ente, aoo);






		}catch (Exception e) {
			throw new BusinessLogicException(e);
		} finally{
			return null;
		}

		}





	public DataHandler AddStampAndSign(String ente, String aoo, DataHandler documento,String providerDataDigitalStamp) throws BusinessLogicException {

		try {

			File targetFile = new File("/tmp/targetFile.tmp");

			java.nio.file.Files.copy(
					documento.getInputStream(),
					targetFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING);

			try {

                if(Strings.isNullOrEmpty(ente)){
                    throw new BusinessLogicException("Parametro ente obbligatorio");
                }
                if(Strings.isNullOrEmpty(aoo)){
                    throw new BusinessLogicException("Parametro aoo obbligatorio");
                }

                if(documento.getInputStream()==null)
                    throw new BusinessLogicException("Parametro documento obbligatorio");

				XMLUtil dom =  new XMLUtil(providerDataDigitalStamp);
				// spostato il controllo XSD per switchare il file
				// validazione del formato
				String xsdFileName = getProviderXsdAttribute(ente, aoo, "xsd"); // "/input-validation.xsd"
				
				File schema = ConfigurationUtils.loadResourceFile(xsdFileName);
				dom.validate(schema.getAbsolutePath());
			} catch (Exception e) {
				logger.error("AddStampAndSign error:{}",e);
				throw new DigitalStampException(e);
			}

			this.provider = getProvider(ente, aoo);

			return provider.AddStampAndSign(documento,providerDataDigitalStamp);


		}catch (Exception e) {
			throw new BusinessLogicException("Errore generico:"+e.getMessage());
		}
	}

    private Properties getMetadataProperties() throws BusinessLogicException{
        try{
            return ConfigurationUtils.loadProperties("metadata.properties");
        }catch (Exception e) {
            throw new BusinessLogicException("Error file metadata.properties:"+e.getMessage());
        }
    }

    public String AddStampAndSignbyDocnum(String ente, String aoo, String token, String docnum,String providerDataDigitalStamp) throws BusinessLogicException {

        try {

            if(Strings.isNullOrEmpty(ente)){
                throw new BusinessLogicException("Parametro ente obbligatorio");
            }
            if(Strings.isNullOrEmpty(aoo)){
                throw new BusinessLogicException("Parametro aoo obbligatorio");
            }
            if(Strings.isNullOrEmpty(token)){
                throw new BusinessLogicException("Parametro token obbligatorio");
            }
            if(Strings.isNullOrEmpty(docnum))
                throw new BusinessLogicException("Parametro docnum obbligatorio");

            try {
                XMLUtil dom =  new XMLUtil(providerDataDigitalStamp);
                String xsdFileName = getProviderXsdAttribute(ente, aoo, "xsd");
                
                File schema = ConfigurationUtils.loadResourceFile(xsdFileName);
                dom.validate(schema.getAbsolutePath());

            } catch (Exception e) {
                logger.error("AddStampAndSign error:{}",e);
                throw new DigitalStampException(e);
            }

            this.provider = getProvider(ente, aoo);

            Documento profileDocument=DocerService.recuperaProfiloDocumento(token,docnum);

            if(profileDocument!=null){
                String num_pg = profileDocument.getProperty("NUM_PG");
                if(!Strings.isNullOrEmpty(num_pg)){
                    DocerFile downloadDocument = DocerService.downloadDocument(token, docnum);

                    HashMap<String,String> otherMetadata=new HashMap<String, String>();
                    Properties metadataProperties=getMetadataProperties();
                    Enumeration<String> enums = (Enumeration<String>) metadataProperties.propertyNames();
                    while (enums.hasMoreElements()) {
                        String key = enums.nextElement();
                        if(!Strings.isNullOrEmpty(profileDocument.getProperty(key))){
                            String value = metadataProperties.getProperty(key);
                            otherMetadata.put(value,profileDocument.getProperty(key));
                        }
                    }

                    DataHandler dataHandler = downloadDocument.getContent();
                    DataHandler newDataHandler = provider.AddStampAndSignbyDocnum(dataHandler, providerDataDigitalStamp, otherMetadata);

                    Documento tmp=new Documento();
                    tmp.setEnte(ente);
                    tmp.setAoo(aoo);
                    tmp.setDocName(profileDocument.getDocName());
                    tmp.setDocType(profileDocument.getDocType());
                    tmp.setTipoComponente("PRINCIPALE");
                    tmp.setFile(newDataHandler.getInputStream(), profileDocument.getDocName());
                    Documento documento=DocerService.creaDocumento(token,tmp,null);

                    return documento.getDocNum();

                }else {
                    throw new BusinessLogicException("Documento non protocollato");
                }
            } else {
                throw new BusinessLogicException("Documento non trovato");
            }

        }catch (Exception e) {
            throw new BusinessLogicException("Errore generico:"+e.getMessage());
        }
    }


	private String getProviderXsdAttribute(String ente, String aoo, String attributeName) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			DigitalStampException, DocerException {

		String providerAttribute = "";
		String defaultXsdFile = "";

		OMElement ome1;
		try {
			ome1 = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode("//section[@name='Providers']");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new DigitalStampException(e1.getMessage());
		} catch (XMLStreamException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new DigitalStampException(e1.getMessage());
		}
		if (ome1 == null) {
			throw new DigitalStampException("xpath //section[@name='Providers'] non trovato nel file di configurazione.");
		}

		defaultXsdFile = ome1.getAttributeValue(new QName("default-xsd"));
		if (defaultXsdFile == null) {
			throw new DigitalStampException("Impossibile trovare l'attributo 'default-xsd' del nodo 'Providers' nel file di configurazione.");
		}

		String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);

		OMElement ome2;
		try {
			ome2 = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpathProvider);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DigitalStampException(e.getMessage());
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DigitalStampException(e.getMessage());
		}
		if (ome2 == null) {
			throw new DigitalStampException("provider non definito per ente=" + ente + " e aoo=" + aoo);
		}

		providerAttribute = ome2.getAttributeValue(new QName(attributeName));
		if (providerAttribute == null) {
			return defaultXsdFile;
		}

		return providerAttribute;
	}

}
