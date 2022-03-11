package it.kdm.docer.fascicolazione.bean;

/**
 * Created by antsic on 15/12/14.
 */
public class Fascicolo {

    private String ente;
    private String aoo;
    private String classifica;
    private String progressivo;
    private String anno;

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(String progressivo) {
        this.progressivo = progressivo;
    }

    public String getClassifica() {
        return classifica;
    }

    public void setClassifica(String classifica) {
        this.classifica = classifica;
    }

    public String getAoo() {
        return aoo;
    }

    public void setAoo(String aoo) {
        this.aoo = aoo;
    }

    public String getEnte() {
        return ente;
    }

    public void setEnte(String ente) {
        this.ente = ente;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fascicolo fascicolo = (Fascicolo) o;
        if (anno.equals(fascicolo.anno)  && aoo.equals(fascicolo.aoo) && classifica.equals(fascicolo.classifica) && ente.equals(fascicolo.ente) &&
                progressivo.equals(fascicolo.progressivo) ) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int result = ente != null ? ente.hashCode() : 0;
        result = 31 * result + (aoo != null ? aoo.hashCode() : 0);
        result = 31 * result + (classifica != null ? classifica.hashCode() : 0);
        result = 31 * result + (progressivo != null ? progressivo.hashCode() : 0);
        result = 31 * result + (anno != null ? anno.hashCode() : 0);
        return result;
    }
}
