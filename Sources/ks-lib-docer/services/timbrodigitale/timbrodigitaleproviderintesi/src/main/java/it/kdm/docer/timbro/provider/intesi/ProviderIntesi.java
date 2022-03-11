package it.kdm.docer.timbro.provider.intesi;


import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.timbro.provider.intesi.contractdocer.IProvider;
import it.kdm.docer.timbro.provider.intesi.stub.HandlerQRBExceptionException;
import it.kdm.docer.timbro.provider.intesi.stub.HandlerStub;
import it.kdm.docer.timbrodigitale.sdk.ProviderException;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;

import javax.activation.DataHandler;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;

/**
 * Created by antsic on 10/08/17.
 */
public class ProviderIntesi implements IProvider {

    private Properties properties = new Properties();

    public ProviderIntesi() {

        
        try {
            properties.putAll(ConfigurationUtils.loadProperties("provider-intesi.properties"));
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
    public DataHandler applicaTimbro(TimbroIntesiDTO timbroParam) throws ProviderException {

        //recuperare wsurl da configurazione file di properties del provider
        //TODO
        try {
            String epr=properties.getProperty("intesi.ws.epr");
            HandlerStub handlerStub = getWSClient(epr);
            HandlerStub.AddStampAndSign addStampAndSign = new HandlerStub.AddStampAndSign();
            HandlerStub.AddStampAndSignRequest addStampAndSignRequest = new HandlerStub.AddStampAndSignRequest();
            if(timbroParam.getAction()!=0) {
                addStampAndSignRequest.setAction(timbroParam.getAction());
            }
            if(timbroParam.getContentId()!=null && !timbroParam.getContentId().equals("")) {
                throw new ProviderException("method SWA not supported");
            }
            addStampAndSignRequest.setCorrectionLevel(timbroParam.getCorrectionLevel());
            addStampAndSignRequest.setDocument(timbroParam.getDocumento());
            addStampAndSignRequest.setExDate(timbroParam.getExDate());
            addStampAndSignRequest.setIsShortnedUrl(timbroParam.isShortnedURL());
            addStampAndSignRequest.setLocale(timbroParam.getLocale());
            if(timbroParam.getMetadata()!=null){
                addStampAndSignRequest.setMetadata(timbroParam.getMetadata());
            }
            addStampAndSignRequest.setOtp(timbroParam.getOtp());
            addStampAndSignRequest.setPin(timbroParam.getPin());
            addStampAndSignRequest.setPositionTag(timbroParam.getPositionTag());
            addStampAndSignRequest.setPositionX(timbroParam.getPositionX());
            addStampAndSignRequest.setPositionY(timbroParam.getPositionY());
            addStampAndSignRequest.setQrSize(timbroParam.getQrsize());
            addStampAndSignRequest.setSigner(timbroParam.getSigner());
            addStampAndSignRequest.setStampPage(timbroParam.getStampPage());
            addStampAndSignRequest.setTemplateName(timbroParam.getTemplateName());
            addStampAndSignRequest.setTitle(timbroParam.getTitle());
            addStampAndSignRequest.setTypeDoc(timbroParam.getTypeDoc());
            addStampAndSignRequest.setUrl(timbroParam.getUrl());

            addStampAndSign.setRequest(addStampAndSignRequest);
            HandlerStub.AddStampAndSignResponseE addStampAndSignResponseE = handlerStub.addStampAndSign(addStampAndSign);
            HandlerStub.AddStampAndSignResponse response = addStampAndSignResponseE.get_return();
            return response.getAttachment();
        } catch (AxisFault axisFault) {
          throw new ProviderException(axisFault);
        } catch (RemoteException e) {
            throw new ProviderException(e);
        } catch (HandlerQRBExceptionException e) {
            throw new ProviderException(e);
        }



    }

    @Override
    public TemplateDTO retrieveTemplate(TemplateDTO template) throws ProviderException {
        return null;
    }




    private HandlerStub getWSClient(String epr) throws AxisFault {
        EndpointReference wsep = new EndpointReference();


        wsep.setAddress(epr);

        HandlerStub client = new HandlerStub();
        client._getServiceClient().setTargetEPR(wsep);

        return client;
    }


}
