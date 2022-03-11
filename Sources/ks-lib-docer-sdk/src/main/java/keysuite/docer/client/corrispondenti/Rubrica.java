package keysuite.docer.client.corrispondenti;

import com.fasterxml.jackson.annotation.JsonProperty;
import keysuite.docer.client.Anagrafica;

public class Rubrica extends Anagrafica {

    public Rubrica(){
        super();
    }

    public enum Tipo {
        Persona("PF"),
        PersonaGiuridica("PG"),
        Amministrazione("PA");

        String value;
        Tipo(String value){
            this.value = value;
        }

        public String getValue(){
            return value;
        }

        public static Tipo getTipo(String value){
            if ("PF".equals(value))
                return Persona;
            else if ("PG".equals(value))
                return PersonaGiuridica;
            else if ("PA".equals(value))
                return Amministrazione;
            else
                return valueOf(value);
        }
    }

    public Tipo getTipoRubrica() {
        return tipoRubrica;
    }
    public void setTipoRubrica(Tipo tipoRubrica) {
        this.tipoRubrica = tipoRubrica;
    }

    public String getIndirizzoPostale() {
        return indirizzoPostale;
    }
    public void setIndirizzoPostale(String indirizzoPostale) {
        this.indirizzoPostale = indirizzoPostale;
    }

    public String getIndirizzoPec() {
        return indirizzoPec;
    }
    public void setIndirizzoPec(String indirizzoPec) {
        this.indirizzoPec = indirizzoPec;
    }

    public String getInririzzoPeo() {
        return inririzziPeo;
    }
    public void setInririzzoPeo(String inririzziPeo) {
        this.inririzziPeo = inririzziPeo;
    }

    public String[] getIndirizziPECSecondari() {
        return indirizziPECSecondari;
    }

    public void setIndirizziPECSecondari(String[] indirizziPECSecondari) {
        this.indirizziPECSecondari = indirizziPECSecondari;
    }

    @JsonProperty("TIPO_RUBRICA")
    Tipo tipoRubrica;

    @JsonProperty("INDIRIZZO_POSTALE")
    String indirizzoPostale;

    @JsonProperty("INDIRIZZO_PEC")
    String indirizzoPec;

    @JsonProperty("INDIRIZZO_PEO")
    String inririzziPeo;

    @JsonProperty("INDIRIZZI_PEC_SECONDARI")
    String[] indirizziPECSecondari;

}
