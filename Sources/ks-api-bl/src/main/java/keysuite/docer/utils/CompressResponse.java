package keysuite.docer.utils;

import java.io.Serializable;
import java.net.URI;

public class CompressResponse implements Serializable {
    public URI getFile() {
        return file;
    }

    public void setFile(URI file) {
        this.file = file;
    }

    private URI file;
    private URI url;

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }
}
