package it.kdm.docer.sdk.classes;

/**
 * Created by Lorenzo Lucherini on 12/17/14.
 */
public class Version {

    KeyValuePair[] metadata = new KeyValuePair[0];

    public KeyValuePair[] getMetadata() {
        return metadata;
    }

    public void setMetadata(KeyValuePair[] metadata) {
        this.metadata = metadata;
    }
}
