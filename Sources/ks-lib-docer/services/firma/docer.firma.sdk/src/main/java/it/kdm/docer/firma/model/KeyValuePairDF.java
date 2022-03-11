package it.kdm.docer.firma.model;

import javax.activation.DataHandler;

/**
 * Created by antsic on 19/07/17.
 */
public class KeyValuePairDF {

    private DataHandler stream;
    private String fileName;


    public KeyValuePairDF() {
    }

    public DataHandler getStream() {
        return stream;
    }

    public void setStream(DataHandler stream) {
        this.stream = stream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
