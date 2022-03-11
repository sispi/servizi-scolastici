package it.kdm.docer.services.agid.utils;

import it.gov.digitpa.www.protocollo.Segnatura;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import javax.activation.DataHandler;
import java.io.*;
import java.util.Date;

/**
 * Created by pamput on 3/13/14.
 */
public class MessageUtils {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(MessageUtils.class);

    public static DataHandler getDataHandler(String contentId) {
        MessageContext message = MessageContext.getCurrentMessageContext();
        return message.getAttachment(contentId);
    }

    public static String[] getAllContentIds() {
        MessageContext message = MessageContext.getCurrentMessageContext();
        return message.getAttachmentMap().getAllContentIDs();
    }

    public static void saveDataHandler(DataHandler dh, File file) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = dh.getInputStream();
            outputStream = new FileOutputStream(file);

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveAttachmentsForTest(String parent) {
        String[] ids = MessageUtils.getAllContentIds();

        SaveContext saveContext = new SaveContext().init();

        if (saveContext.doSave()) {
            for (String id : ids) {

                File file = saveContext.getFile(parent, id);

                DataHandler dh = MessageUtils.getDataHandler(id);
                MessageUtils.saveDataHandler(dh, file);
            }
        }
    }

    public static void saveBodyForTest(Segnatura se, String parent) throws IOException {
        SaveContext saveContext = new SaveContext().init();

        String temp = "body_%s";
        String fileName = String.format(temp, new Date().toString());

        File file = saveContext.getFile(parent, fileName);

        FileUtils.writeStringToFile(file, se.toString());
    }

    private static class SaveContext {

        private String homePath;
        private boolean doSave;

        public String getHomePath() {
            return homePath;
        }

        public boolean doSave() {
            return doSave;
        }

        public SaveContext init() {
            homePath = "/tmp";
            doSave = false;

            try {
                PropertiesConfiguration conf = ConfigurationUtils.getConf();

                homePath = conf.getString("test.saveattachments.dir");
                doSave = conf.getBoolean("test.saveattachments");

                if(homePath.startsWith("~")) {
                    homePath = homePath.replaceAll("~", System.getProperty("user.home"));
                }

            } catch (Exception e) {
                log.error("Non è stato possibile trovare config.properties");
            }

            return this;
        }

        public File getFile(String fileName) {
            File file1 = new File(homePath);
            return new File(file1, fileName);
        }

        public File getFile(String parent, String fileName) {
            File file1 = new File(homePath);
            File file2 = new File(file1, parent);

            if(!file2.exists()) {
                file2.mkdirs();
            }

            return new File(file2, fileName);
        }
    }
}
