package it.kdm.docer.digitalstamp.provider.intesi;


import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.digitalstamp.provider.intesi.stub.HandlerQRBExceptionException;
import it.kdm.docer.digitalstamp.provider.intesi.stub.HandlerStub;
import it.kdm.docer.digitalstamp.sdk.IProvider;
import it.kdm.docer.digitalstamp.sdk.ProviderException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.io.IOUtils;
import org.jaxen.JaxenException;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.axis2.transport.http.HTTPConstants.REUSE_HTTP_CLIENT;
import static org.apache.axis2.transport.http.HTTPConstants.CACHED_HTTP_CLIENT;

/**
 * Created by antsic on 10/08/17.
 */
public class ProviderIntesi implements IProvider {

    private Properties properties = new Properties();

    public ProviderIntesi() {

        try {
            properties = ConfigurationUtils.loadProperties("provider-intesi.properties");
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void setConfiguration(Properties conf) throws ProviderException {

    }

    @Override
    public Properties getConfiguration() throws ProviderException {
        return null;
    }

    @Override
    public DataHandler AddStampAndSign(DataHandler documento, String dataProvider) throws ProviderException {

            String destinationFolder=new SimpleDateFormat("yyyy.MM.dd").format(new Timestamp(new Date().getTime()))+"_"+ java.util.UUID.randomUUID();
            String templateName="";
            DigitalStampDTO digitalStampDTO=createdigitalStampDTO(documento,dataProvider,destinationFolder,templateName,null);

            HandlerStub.AddStampAndSignResponse response=addStamp(digitalStampDTO);

            DataHandler dataHandler=response.getAttachment();

            String destinationUrl=properties.getProperty("save.folder") + "/" + destinationFolder;
            String titledigitalStamp = digitalStampDTO.getTitle().replaceAll("[\\\\\\\\/:*?!'%&=£\\]\\^\\\"<>|]", "").replaceAll(" ","_") + ".pdf";
            saveDocument(dataHandler,destinationUrl,titledigitalStamp);

            return dataHandler;

    }

    @Override
    public DataHandler AddStampAndSignbyDocnum(DataHandler documento, String dataProvider, HashMap<String,String> otherMetadata) throws ProviderException {

        String destinationFolder=new SimpleDateFormat("yyyy.MM.dd").format(new Timestamp(new Date().getTime()))+"_"+ java.util.UUID.randomUUID();
        String templateName=properties.getProperty("digitalStamp.templateByDocnum");

        DigitalStampDTO digitalStampDTO=createdigitalStampDTO(documento,dataProvider,destinationFolder,templateName,otherMetadata);

        HandlerStub.AddStampAndSignResponse response=addStamp(digitalStampDTO);

        DataHandler dataHandler=response.getAttachment();

        String destinationUrl=properties.getProperty("save.folder") + "/" + destinationFolder;
        String titledigitalStamp = digitalStampDTO.getTitle().replaceAll("[\\\\\\\\/:*?!'%&=£\\]\\^\\\"<>|]", "").replaceAll(" ","_") + ".pdf";
        saveDocument(dataHandler,destinationUrl,titledigitalStamp);

        return dataHandler;

    }

    private String xpath(OMElement xml, String xpath) throws JaxenException {
        AXIOMXPath xpathExpr = new AXIOMXPath(xpath);
        return xpathExpr.stringValueOf(xml);
    }

    public List<OMElement> getNodes(OMElement xml, String xpath) throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(xpath);
        return (List<OMElement>)path.selectNodes(xml);
    }

    public OMElement getNode(OMElement xml, String xpath) throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(xpath);
        return (OMElement)path.selectSingleNode(xml);
    }

    private HandlerStub getWSClient(String epr) throws AxisFault {
        HandlerStub client = new HandlerStub();
        client._getServiceClient().setTargetEPR(new EndpointReference(epr));
        client._getServiceClient().getServiceContext().getConfigurationContext().setProperty(REUSE_HTTP_CLIENT, "true");

        Options options = client._getServiceClient().getOptions();
        options.setTimeOutInMilliSeconds(10000);

        MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
        manager.getParams().setDefaultMaxConnectionsPerHost(20);
        HttpClient HTTPclient = new HttpClient(manager);
        client._getServiceClient().getServiceContext().getConfigurationContext().setProperty(CACHED_HTTP_CLIENT, HTTPclient);

        return client;
    }

    DigitalStampDTO createdigitalStampDTO(DataHandler documento,String dataProvider,String destinationFolder, String templateName,HashMap<String,String> otherMetadata) throws ProviderException
    {
        DigitalStampDTO digitalStampDTO = new DigitalStampDTO();

        try {
            digitalStampDTO.setDocumento(documento);

            OMElement datiReg = AXIOMUtil.stringToOM(dataProvider);

            String stampPage = xpath(datiReg, "//DigitalStampInfo/stampPage");
            if (!stampPage.equals(""))
                digitalStampDTO.setStampPage(Integer.parseInt(stampPage));

            String positionX = xpath(datiReg, "//DigitalStampInfo/positionX");
            if (!positionX.equals(""))
                digitalStampDTO.setPositionX(Integer.parseInt(positionX));

            String positionY = xpath(datiReg, "//DigitalStampInfo/positionY");
            if (!positionY.equals(""))
                digitalStampDTO.setPositionY(Integer.parseInt(positionY));

            String positionTag = xpath(datiReg, "//DigitalStampInfo/positionTag");
            if (!positionTag.equals(""))
                digitalStampDTO.setPositionTag(positionTag);

            String correctionLevel = xpath(datiReg, "//DigitalStampInfo/correctionLevel");
            if (!correctionLevel.equals(""))
                digitalStampDTO.setCorrectionLevel(correctionLevel);

            String signer = xpath(datiReg, "//DigitalStampInfo/signer");
            if (!signer.equals(""))
                digitalStampDTO.setSigner(signer);

            String pin = xpath(datiReg, "//DigitalStampInfo/pin");
            if (!pin.equals(""))
                digitalStampDTO.setPin(pin);

            String otp = xpath(datiReg, "//DigitalStampInfo/otp");
            if (!otp.equals(""))
                digitalStampDTO.setOtp(otp);

            String action = xpath(datiReg, "//DigitalStampInfo/action");
            if (!action.equals(""))
            {
                digitalStampDTO.setAction(Integer.parseInt(action));
            }
            else{digitalStampDTO.setAction(3);}

            String qrsize = xpath(datiReg, "//DigitalStampInfo/qrsize");
            if (!qrsize.equals(""))
                digitalStampDTO.setQrsize(Integer.parseInt(qrsize));

            String isShortnedURL = xpath(datiReg, "//DigitalStampInfo/isShortnedURL");
            if (!isShortnedURL.equals(""))
                digitalStampDTO.setShortnedURL(Boolean.valueOf(isShortnedURL));

            String title = xpath(datiReg, "//DigitalStampInfo/title");
            if (!title.equals("")) {
                digitalStampDTO.setTitle(title);
            } else {
                throw new ProviderException("Provider timbro digitale INTESI in errore. Il parametro TITLE è obbligatorio");
            }

            String titledigitalStamp = digitalStampDTO.getTitle().replaceAll("[\\\\\\\\/:*?!'%&=£\\]\\^\\\"<>|]", "").replaceAll(" ", "_") + ".pdf";
            String url=properties.getProperty("digitalStamp.url")+File.separator+destinationFolder+File.separator+titledigitalStamp;
            digitalStampDTO.setUrl(url);

            String typeDoc = xpath(datiReg, "//DigitalStampInfo/typeDoc");
            if(!typeDoc.equals(""))
                digitalStampDTO.setTypeDoc(typeDoc);

            String templateNameTmp = xpath(datiReg, "//DigitalStampInfo/templateName");
            String template="";
            if(!templateNameTmp.equals(""))
            {
                template=templateNameTmp;
            }else if(templateName!=null && !templateName.equals("")){
                template=templateName;
            }
            digitalStampDTO.setTemplateName(template);

            String locale = xpath(datiReg, "//DigitalStampInfo/locale");
            if(!locale.equals(""))
                digitalStampDTO.setLocale(locale);

            String exDate = xpath(datiReg, "//DigitalStampInfo/exDate");
            if(!exDate.equals(""))
                digitalStampDTO.setExDate(exDate);

            OMElement meta = getNode(datiReg,"//DigitalStampInfo/metadata");
            List<OMElement> digitalStampMetaList=null;
            int metadataSize=0;
            int otherMetadataSize=0;
            if(meta!=null){
                digitalStampMetaList = getNodes(datiReg, "//DigitalStampInfo/metadata/digitalStampMeta");
                if(digitalStampMetaList!=null){
                    metadataSize=digitalStampMetaList.size();
                }
            }
            if(otherMetadata!=null){
                otherMetadataSize=otherMetadata.size();
            }
            int totalMetadataSize=metadataSize+otherMetadataSize;
            if(totalMetadataSize>0){
                HandlerStub.KeyValueData[] metadata = new HandlerStub.KeyValueData[totalMetadataSize];
                int i=0;
                if(metadataSize>0){
                    for (i=0; i<digitalStampMetaList.size(); i++) {
                        OMElement m = digitalStampMetaList.get(i);
                        HandlerStub.KeyValueData kvd = new HandlerStub.KeyValueData();
                        QName keyName = new QName("key");
                        QName valueName = new QName("value");
                        OMElement key = m.getFirstChildWithName(keyName);
                        OMElement value = m.getFirstChildWithName(valueName);
                        kvd.setKey(key.getText());
                        kvd.setValue(value.getText());
                        metadata[i] = kvd;
                    }
                }
                if(otherMetadataSize>0){
                    for (Map.Entry<String, String> entry : otherMetadata.entrySet()) {
                        HandlerStub.KeyValueData kvd = new HandlerStub.KeyValueData();
                        kvd.setKey(entry.getKey());
                        kvd.setValue(entry.getValue());
                        metadata[i] = kvd;
                        i++;
                    }
                }
                digitalStampDTO.setMetadata(metadata);
            }

        } catch (XMLStreamException e) {
        e.printStackTrace();
        throw new ProviderException(e);
        }catch (JaxenException e) {
            e.printStackTrace();
            throw new ProviderException(e);
        }
        return digitalStampDTO;
    }

    private HandlerStub.AddStampAndSignResponse addStamp(DigitalStampDTO digitalStampDTO) throws ProviderException {

        HandlerStub.AddStampAndSignResponse response=null;

        try {
            String epr = properties.getProperty("intesi.ws.epr");
            HandlerStub handlerStub = getWSClient(epr);
            HandlerStub.AddStampAndSign addStampAndSign = new HandlerStub.AddStampAndSign();
            HandlerStub.AddStampAndSignRequest addStampAndSignRequest = new HandlerStub.AddStampAndSignRequest();

            addStampAndSignRequest.setAction(digitalStampDTO.getAction());
            addStampAndSignRequest.setCorrectionLevel(digitalStampDTO.getCorrectionLevel());
            addStampAndSignRequest.setDocument(digitalStampDTO.getDocumento());
            addStampAndSignRequest.setExDate(digitalStampDTO.getExDate());
            addStampAndSignRequest.setIsShortnedUrl(digitalStampDTO.isShortnedURL());
            addStampAndSignRequest.setLocale(digitalStampDTO.getLocale());
            if (digitalStampDTO.getMetadata() != null) {
                addStampAndSignRequest.setMetadata(digitalStampDTO.getMetadata());
            }
            addStampAndSignRequest.setOtp(digitalStampDTO.getOtp());
            addStampAndSignRequest.setPin(digitalStampDTO.getPin());
            addStampAndSignRequest.setPositionTag(digitalStampDTO.getPositionTag());
            addStampAndSignRequest.setPositionX(digitalStampDTO.getPositionX());
            addStampAndSignRequest.setPositionY(digitalStampDTO.getPositionY());
            addStampAndSignRequest.setQrSize(digitalStampDTO.getQrsize());
            addStampAndSignRequest.setSigner(digitalStampDTO.getSigner());
            addStampAndSignRequest.setStampPage(digitalStampDTO.getStampPage());
            addStampAndSignRequest.setTemplateName(digitalStampDTO.getTemplateName());
            addStampAndSignRequest.setTitle(digitalStampDTO.getTitle());
            addStampAndSignRequest.setTypeDoc(digitalStampDTO.getTypeDoc());
            addStampAndSignRequest.setUrl(digitalStampDTO.getUrl());

            addStampAndSign.setRequest(addStampAndSignRequest);
            HandlerStub.AddStampAndSignResponseE addStampAndSignResponseE = handlerStub.addStampAndSign(addStampAndSign);
            response = addStampAndSignResponseE.get_return();

            handlerStub.cleanup();
            handlerStub._getServiceClient().cleanup();
        }catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            throw new ProviderException(axisFault);
        }catch (HandlerQRBExceptionException e) {
            e.printStackTrace();
            throw new ProviderException(e);
        }catch (RemoteException e) {
            e.printStackTrace();
            throw new ProviderException(e);
        }
        return response;
    }

    private void saveDocument(DataHandler dataHandler, String destinationUrl, String title) throws ProviderException {

        try {
            InputStream inputStream = dataHandler.getInputStream();
            File fileDoc = new File(destinationUrl, title);
            fileDoc.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(fileDoc);
            byte[] arrayb = IOUtils.toByteArray(inputStream);
            FileOutputStream fous = new FileOutputStream(fileDoc);
            fous.write(arrayb);
            fous.close();
            writer.close();
            IOUtils.closeQuietly(inputStream);
        }catch (IOException e) {
            e.printStackTrace();
            throw new ProviderException(e);
        }

    }



}
