package it.kdm.docer.ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

public class InputStreamDataSource implements DataSource {
    private InputStream inputStream;

    public InputStreamDataSource(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String getContentType() {
        return "*/*";
    }

    public String getName() {
        return "InputStreamDataSource";
    }
}
