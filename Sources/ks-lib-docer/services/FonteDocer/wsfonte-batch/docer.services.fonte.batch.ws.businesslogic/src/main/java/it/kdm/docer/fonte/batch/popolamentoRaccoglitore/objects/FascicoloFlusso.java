package it.kdm.docer.fonte.batch.popolamentoRaccoglitore.objects;

import java.io.File;

import it.kdm.docer.clients.DocerServicesStub.KeyValuePair;

public class FascicoloFlusso {
    
    String idFonte = null;
    String idFlusso = null;
    KeyValuePair[] idFascicolo = null;
    File file = null;
    public KeyValuePair[] getIdFascicolo() {
        return idFascicolo;
    }
    public void setIdFascicolo(KeyValuePair[] idFascicolo) {
        this.idFascicolo = idFascicolo;
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
