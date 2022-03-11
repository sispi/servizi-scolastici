package keysuite.docer.client.corrispondenti;

import com.github.underscore.lodash.U;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static keysuite.docer.client.corrispondenti.ICorrispondente.valueOf;

public class Persona extends Corrispondente implements IPersona {

    String denominazione;
    String identificativo;

    @Override
    public int hashCode() {
        return super.hashCode() +
                Objects.hashCode(denominazione) +
                Objects.hashCode(identificativo);
    }

    public Persona(){
    }

    public Persona(Map<String,Object> xMap){
        super(xMap);

        String root = xMap.keySet().iterator().next();

        xMap = (Map) xMap.get(root);


        this.identificativo = valueOf(xMap.get("-id"));

        String nome = valueOf(xMap.get("Nome"));
        String cognome = valueOf(xMap.get("Cognome"));

        if (nome != null && cognome!=null)
            this.denominazione = nome + " " + cognome;
        else
            this.denominazione = valueOf(xMap.get("Denominazione"));

        setIndirizzoPostale(valueOf(U.get(xMap, "IndirizzoPostale.Denominazione")));
        setIndirizzoTelematico(valueOf(U.get(xMap, "IndirizzoTelematico")));


    }

    public Persona(String denominazione){
        this.denominazione = denominazione;
    }

    public Persona(String denominazione, String identificativo){
        this.denominazione = denominazione;
        this.identificativo = identificativo;
    }

    @Override
    public String getDenominazione() {
        return denominazione;
    }

    @Override
    public String getIdentificativo() {
        return identificativo;
    }

    @Override
    public Map<String,Object> toXmlMap(){

        return new LinkedHashMap(){{
            put( "Persona", new LinkedHashMap(){{

                put("-id", identificativo);

                put("IndirizzoTelematico",getIndirizzoTelematico());

                if (getMezzo()!=null){

                    if (getMezzo().equals(ICorrispondente.Mezzo.PEC)) {
                        put("InvioPEC", "SI");
                    }
                }

                if (getIndirizzoPostale()!=null) {
                    put("IndirizzoPostale", new LinkedHashMap() {{
                        put("Denominazione", getIndirizzoPostale());
                    }});
                }

                put("Denominazione", getDenominazione());

                if (getMezzo()!=null || getCodiceRubrica()!=null){

                    put("Metadati" , new LinkedHashMap(){{

                        put("Parametro", new ArrayList() {{
                            if (getCodiceRubrica()!=null) {
                                add(new LinkedHashMap() {{
                                    put("-nome", "codice-rubrica");
                                    put("-valore", getCodiceRubrica());
                                }});
                            }
                            if (getMezzo()!=null) {
                                add(new LinkedHashMap() {{
                                    put("-nome", "mezzo");
                                    put("-valore", getMezzo().toString());
                                }});
                            }
                            if (getRiferimentoMezzo()!=null){
                                add(new LinkedHashMap(){{
                                    put("-nome" , "riferimento-mezzo");
                                    put("-valore", getRiferimentoMezzo());
                                }});
                            }
                        }});

                    }});
                }

            }});
        }};
    }
}
