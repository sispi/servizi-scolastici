package keysuite.docer.client.corrispondenti;

import com.github.underscore.lodash.U;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static keysuite.docer.client.corrispondenti.ICorrispondente.valueOf;

public class PersonaGiuridica extends Persona implements IPersonaGiuridica {

    @Override
    public int hashCode() {
        return super.hashCode() +
                Objects.hashCode(tipoPersonaGiuridica);
    }

    public PersonaGiuridica(){
        super();
    }

    public PersonaGiuridica(String denominazione){
        super(denominazione);
    }

    public PersonaGiuridica(Map<String,Object> xMap) {
        super(xMap);
        setTipoPersonaGiuridica(valueOf(U.get(xMap, "PersonaGiuridica.-tipo"),"partita_iva"));
    }

    public PersonaGiuridica(String denominazione, String identificativo){
        super(denominazione,identificativo);
    }

    String tipoPersonaGiuridica;

    @Override
    public String getTipoPersonaGiuridica() {
        return tipoPersonaGiuridica;
    }

    public void setTipoPersonaGiuridica(String tipoPersonaGiuridica) {
        this.tipoPersonaGiuridica = tipoPersonaGiuridica;
    }

    @Override
    public Map<String,Object> toXmlMap(){

        final String tipo = valueOf(this.getTipoPersonaGiuridica(),"partita_iva");

        return new LinkedHashMap(){{
            put( "PersonaGiuridica", new LinkedHashMap(){{

                put("-id", identificativo);
                put("-tipo" ,tipo );

                put("Denominazione", getDenominazione());

                if (getIndirizzoPostale()!=null) {
                    put("IndirizzoPostale", new LinkedHashMap() {{
                        put("Denominazione", getIndirizzoPostale());
                    }});
                }


                put("IndirizzoTelematico",getIndirizzoTelematico());

                if (getMezzo()!=null || getCodiceRubrica()!=null){

                    if (getMezzo()!=null && getMezzo().equals(ICorrispondente.Mezzo.PEC)) {
                        put("InvioPEC", "SI");
                    }

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
