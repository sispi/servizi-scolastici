package it.kdm.docer.timbrodigitale.ws;

import it.kdm.docer.timbrodigitale.bl.BusinessLogic;
import it.kdm.docer.timbrodigitale.sdk.BusinessLogicException;
import it.kdm.docer.timbrodigitale.sdk.ImageFormat;
import it.kdm.docer.timbrodigitale.sdk.KeyValuePair;
import it.kdm.docer.timbrodigitale.ws.utility.TemporaryFileSource;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.commons.io.IOUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WSTimbroDigitale {

    private BusinessLogic businessLogic;

    public WSTimbroDigitale(BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
    }

    public String login(String userId, String password, String codiceEnte) throws BusinessLogicException {
        return UUID.randomUUID().toString();
    }

    public String loginSSO(String saml, String codiceEnte) throws BusinessLogicException {
        return UUID.randomUUID().toString();
    }

    public boolean logout(String token) throws BusinessLogicException {
        return true;
    }

    public boolean writeConfig(String token, String xml) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public String readConfig(String token) throws BusinessLogicException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    public DataHandler getTimbro(DataHandler data, String imgFormat, int imgMaxH, int imgMaxW, int imgDPI, KeyValuePair[] params) throws BusinessLogicException {

        try {

            DataSource source = data.getDataSource();
            byte[] buffer = IOUtils.toByteArray(source.getInputStream());

            buffer = this.businessLogic.getTimbro(buffer, ImageFormat.getImageFormat(imgFormat), imgMaxH, imgMaxW, imgDPI, toMap(params));

            return new DataHandler(new ByteArrayDataSource(buffer));
        } catch (IOException e) {
            throw new BusinessLogicException(e.getMessage());
        }
    }

    public DataHandler applicaTimbro(DataHandler timbro, DataHandler pdf, int pagina, int x, int y) throws BusinessLogicException {
        try {
            File f = this.businessLogic.applicaTimbro(timbro.getInputStream(), pdf.getInputStream(), pagina, x, y);

            TemporaryFileSource tfs = new TemporaryFileSource(f);
            return new DataHandler(tfs);

        } catch (IOException e) {
            throw new BusinessLogicException(e.getMessage());
        }
    }

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
