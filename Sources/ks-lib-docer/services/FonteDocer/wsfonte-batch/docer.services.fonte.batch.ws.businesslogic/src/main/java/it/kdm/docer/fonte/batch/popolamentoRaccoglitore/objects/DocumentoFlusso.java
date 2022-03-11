package it.kdm.docer.fonte.batch.popolamentoRaccoglitore.objects;

import java.io.File;

import it.kdm.docer.clients.DocerServicesStub.KeyValuePair;

public class DocumentoFlusso {
    
    String idFonte = null;
    String idFlusso = null;
    String idDoc = null;
    File file = null;
    public String getIdDoc() {
        return idDoc;
    }
    public void setIdDoc(String idDoc) {
        this.idDoc= idDoc;
    }
    public File getFileXml() {
        return file;
    }
    public void setFileXml(File fileXml) {
        this.file = fileXml;
    }
    public String getIdFlusso() {
        return idFlusso;
    }
    public void setIdFlusso(String idFlusso) {
        this.idFlusso = idFlusso;
    }
    public String getIdFonte() {
        return idFonte;
    }
    public void setIdFonte(String idFonte) {
        this.idFonte = idFonte;
    }
}
