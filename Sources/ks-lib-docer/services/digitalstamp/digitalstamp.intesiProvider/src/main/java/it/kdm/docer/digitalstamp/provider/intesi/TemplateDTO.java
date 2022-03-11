package it.kdm.docer.digitalstamp.provider.intesi;

import java.io.InputStream;

/**
 * Created by antsic on 10/08/17.
 */
public class TemplateDTO {
    private String name;
    private String mimeType;
    private InputStream template;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public InputStream getTemplate() {
        return template;
    }

    public void setTemplate(InputStream template) {
        this.template = template;
    }
}
