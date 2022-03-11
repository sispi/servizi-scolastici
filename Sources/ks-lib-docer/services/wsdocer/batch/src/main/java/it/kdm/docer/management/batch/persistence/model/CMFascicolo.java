package it.kdm.docer.management.batch.persistence.model;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 02/11/15
 * Time: 17.44
 * To change this template use File | Settings | File Templates.
 */
public class CMFascicolo extends CMBase {

    private String classifica;
    private String annoFascicolo;
    private String progrFascicolo;

    public String getClassifica() {
        return classifica;
    }
    public void setClassifica(String classifica) {
        this.classifica = classifica;
    }
    public String getAnnoFascicolo() {
        return annoFascicolo;
    }
    public void setAnnoFascicolo(String annoFascicolo) {
        this.annoFascicolo = annoFascicolo;
    }
    public String getProgrFascicolo() {
        return progrFascicolo;
    }
    public void setProgrFascicolo(String progrFascicolo) {
        this.progrFascicolo = progrFascicolo;
    }



}

