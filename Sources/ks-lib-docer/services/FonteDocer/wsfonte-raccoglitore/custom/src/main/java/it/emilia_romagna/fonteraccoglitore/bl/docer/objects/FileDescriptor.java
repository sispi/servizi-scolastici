package it.emilia_romagna.fonteraccoglitore.bl.docer.objects;

import java.io.InputStream;

public class FileDescriptor {

    private InputStream in;
    public InputStream getIn() {
        return in;
    }
    public void setIn(InputStream in) {
        this.in = in;
    }    
    
    private String fileName;
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}
