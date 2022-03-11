package keysuite.docer.utils;

import java.net.URI;

public class UncompressResponse {

    public URI[] getFiles() {
        return files;
    }

    public void setFiles(URI[] files) {
        this.files = files;
    }

    private URI[] files;
    private URI[] urls;

    public URI[] getUrls() {
        return urls;
    }

    public void setUrls(URI[] urls) {
        this.urls = urls;
    }
}