package it.eng.parer.ws.versamento.dto;

import java.io.File;

/**
 *
 * @author Fioravanti_F
 */
public class FileBinario implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3038080614562309178L;
    private String id;
    private byte[] dati;
    private boolean inMemoria;
    private File fileSuDisco;
    private long dimensione;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getDati() {
        return dati;
    }

    public void setDati(byte[] dati) {
        this.dati = dati;
    }

    /**
     * @return the inMemoria
     */
    public boolean isInMemoria() {
        return inMemoria;
    }

    /**
     * @param inMemoria the inMemoria to set
     */
    public void setInMemoria(boolean inMemoria) {
        this.inMemoria = inMemoria;
    }

    /**
     * @return the fileSuDisco
     */
    public File getFileSuDisco() {
        return fileSuDisco;
    }

    /**
     * @param fileSuDisco the fileSuDisco to set
     */
    public void setFileSuDisco(File fileSuDisco) {
        this.fileSuDisco = fileSuDisco;
    }

    /**
     * @return the dimensione
     */
    public long getDimensione() {
        return dimensione;
    }

    /**
     * @param dimensione the dimensione to set
     */
    public void setDimensione(long dimensione) {
        this.dimensione = dimensione;
    }
}
