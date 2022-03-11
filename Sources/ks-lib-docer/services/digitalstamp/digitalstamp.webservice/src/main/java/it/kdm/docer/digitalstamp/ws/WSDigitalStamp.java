package it.kdm.docer.digitalstamp.ws;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.digitalstamp.bl.BusinessLogic;
import it.kdm.docer.digitalstamp.sdk.BusinessLogicException;
import it.kdm.docer.digitalstamp.sdk.DigitalStampException;
import it.kdm.docer.digitalstamp.sdk.KeyValuePair;
import it.kdm.docer.sdk.classes.StreamDescriptor;
import org.springframework.context.ApplicationContext;

import javax.activation.DataHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WSDigitalStamp {

    private BusinessLogic businessLogic;


    private static ApplicationContext ctx = null;
    TicketCipher cipher = new TicketCipher();



    public WSDigitalStamp(BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
    }

    public String login(String userId, String password, String codiceEnte) throws DigitalStampException {
       return UUID.randomUUID().toString();
    }

    public String loginSSO(String saml, String codiceEnte) throws BusinessLogicException {
        return UUID.randomUUID().toString();
    }

    public boolean logout(String token) throws BusinessLogicException {
        return true;
    }

   /* public boolean writeConfig(String token, String xml) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public String readConfig(String token) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }*/





    public boolean writeConfig(String token, String xmlConfig)
            throws DigitalStampException{
       /* IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");*/
        return businessLogic.writeConfig(token, xmlConfig);
    }

    public String readConfig(String token)
            throws DigitalStampException {
      /*  IBusinessLogic businessLogic = (IBusinessLogic)ctx.getBean("businessLogic");*/
        return businessLogic.readConfig(token);
    }

    public StreamDescriptor AddStampAndSign(String ente, String aoo,DataHandler documento,String data ) throws BusinessLogicException {

        DataHandler dh = this.businessLogic.AddStampAndSign(ente,aoo,documento,data);

        try {
            StreamDescriptor descriptor = new StreamDescriptor();
            descriptor.setHandler(dh);
            return descriptor;

        } catch (Exception e) {
            throw new BusinessLogicException(e);
        }
    }

    public String AddStampAndSignbyDocnum(String ente, String aoo, String token, String docnum,String data) throws BusinessLogicException {

        String dh = null;

        try {
            dh = this.businessLogic.AddStampAndSignbyDocnum(ente, aoo, token,docnum, data);

        } catch (Exception e) {
            throw new BusinessLogicException(e);
        }
        return dh;
    }





   /* public DataHandler AddStampAndSign(String ente, String aoo,DataHandler documento,KeyValuePair [] metadata, int stampPage, int positionX, int positionY, String positionTag, String correctionLevel,
            String signer,String pin,String otp,int action,int qrsize,boolean isShortnedURL,String url,
            String typeDoc, String templateName, String locale, String title, String exDate ) throws BusinessLogicException {

        try {


            DataHandler dh = this.businessLogic.AddStampAndSign(ente,aoo,documento, metadata, stampPage, positionX, positionY, positionTag, correctionLevel,signer,pin,otp,action,qrsize,isShortnedURL,url,
                    typeDoc, templateName, locale, title, exDate);

            return dh;
        } catch (Exception e) {
            throw new BusinessLogicException(e.getMessage());
        }
    }
*/

    private Map<String, String> toMap(KeyValuePair[] kvpArray) {

        if (kvpArray != null) {
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < kvpArray.length; i++) {

                String key = kvpArray[i].getKey();
                String value = kvpArray[i].getValue();

                if (key != null && value != null) {
                    map.put(key, value);
                }
            }
            return map;
        }

        return null;
    }

}
