package keysuite.docer.client.corrispondenti;

import com.google.common.base.Strings;

import java.util.*;


public abstract class Corrispondente implements ICorrispondente {

    public Corrispondente(){};

    @Override
    public int hashCode() {
        return Objects.hashCode(indirizzoTelematico) +
                Objects.hashCode(mezzo) +
                Objects.hashCode(indirizzoPostale) +
                Objects.hashCode(riferimentoMezzo) +
                Objects.hashCode(codiceRubrica);
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Corrispondente) {
            Corrispondente e = (Corrispondente)o;
            return e.hashCode() == this.hashCode();
        }
        return false;
    }

    public Corrispondente(Map<String,Object> xMap){
        if (xMap==null)
            return ;

        String root = xMap.keySet().iterator().next();

        /*if (root.equals("Mittente") || root.equals("Destinatario") || root.equals("Firmatario")){
            xMap = (Map) xMap.get(root);
            root = xMap.keySet().iterator().next();
        }*/



        Object xMetadatiObj =  xMap.get("Metadati");

        xMap = (Map) xMap.get(root);

        if (xMetadatiObj==null)
            xMetadatiObj =  xMap.get("Metadati");

        List<Map> xMetadati = null;

        if(xMetadatiObj instanceof Map) {
            xMetadati = Collections.singletonList((Map)xMetadatiObj);
        } else if (xMetadatiObj instanceof List) {
            xMetadati = (List) xMetadatiObj;
        }

        Map<String,String> metadati = new LinkedHashMap<>();

        if (xMetadati!=null){
            for (Map x : xMetadati) {
                Object o = x.get("Parametro");
                List<Map> l = o instanceof List ? (List) o : Collections.singletonList( (Map) o);

                for ( Map m : l ){
                    metadati.put( (String) m.get("-nome"), (String) m.get("-valore"));
                }
            }
        }

        String codiceRubrica = metadati.get("codice-rubrica");

        if (!Strings.isNullOrEmpty(codiceRubrica))
            setCodiceRubrica(codiceRubrica);

        String mezzoStr = metadati.get("mezzo");

        Mezzo mezzo = ICorrispondente.Mezzo.Altro;
        //String indirizzoMezzo = null;

        if ("SI".equals(xMap.get("InvioPEC"))){
            mezzoStr = "PEC";
        }

        if (mezzoStr!=null) {
            String rifmezzoStr = metadati.get("riferimento-mezzo");

            try{
                mezzo = Mezzo.valueOf(mezzoStr);
            } catch (Exception e){
                mezzoStr = mezzoStr.toLowerCase();
                if (mezzoStr.matches(".*posta.*certificata.*")) {
                    mezzo = Mezzo.PEC;
                } else if (mezzoStr.matches(".*elettronica.*|.*mail.*")) {
                    mezzo = Mezzo.PEO;
                } else if (mezzoStr.matches(".*brevimano.*")) {
                    mezzo = Mezzo.Brevimano;
                } else if (mezzoStr.matches(".*raccomandata.*|.*posta.*")) {
                    mezzo = Mezzo.Posta;
                }
            }

            if (!Strings.isNullOrEmpty(rifmezzoStr))
                this.setRiferimentoMezzo(rifmezzoStr);
        }
        this.setMezzo(mezzo);
    }

    //String tipoCorrispondente;

    public String getTipoCorrispondente() { return this.getClass().getSimpleName(); };
    //public void setTipoCorrispondente(String tipoCorrispondente) { this.tipoCorrispondente = tipoCorrispondente;  };

    public Mezzo getMezzo() {
        return mezzo;
    }
    public void setMezzo(Mezzo mezzo) {
        this.mezzo = mezzo;
    }

    public String getRiferimentoMezzo() {
        return riferimentoMezzo;
    }
    public void setRiferimentoMezzo(String riferimentoMezzo) {
        this.riferimentoMezzo = riferimentoMezzo;
    }

    Mezzo mezzo;
    String riferimentoMezzo;
    String indirizzoPostale;
    String codiceRubrica;

    @Override
    public String getCodiceRubrica() {
        return codiceRubrica;
    }

    public void setCodiceRubrica(String codiceRubrica) {
        this.codiceRubrica = codiceRubrica;
    }

    @Override
    public String getIndirizzoPostale() {
        return indirizzoPostale;
    }

    public void setIndirizzoPostale(String indirizzoPostale) {
        this.indirizzoPostale = indirizzoPostale;
    }

    @Override
    public String getIndirizzoTelematico() {
        return indirizzoTelematico;
    }

    public void setIndirizzoTelematico(String indirizzoTelematico) {
        this.indirizzoTelematico = indirizzoTelematico;
    }

    String indirizzoTelematico;

    public static Corrispondente fromXmlMap(Map<String,Object> xMap){

        for( String key : xMap.keySet().toArray(new String[0]))
            if (key.startsWith("#"))
                xMap.remove(key);

        String root = xMap.keySet().iterator().next();

        if ("Amministrazione".equals(root))
            return new Amministrazione(xMap);
        else if ("Persona".equals(root))
            return new Persona(xMap);
        else if ("PersonaGiuridica".equals(root))
            return new PersonaGiuridica(xMap);
        else
            return null;
    }

    @Override
    public String toString(){
        String den = getDenominazione();
        String id = getIdentificativo();

        if (!Strings.isNullOrEmpty(den) && !Strings.isNullOrEmpty(id)){
            return den + " <"+id+">";
        } else if (!Strings.isNullOrEmpty(den)) {
            return den;
        } else if (!Strings.isNullOrEmpty(id)) {
            return id;
        } else {
            return "";
        }
    }

}
