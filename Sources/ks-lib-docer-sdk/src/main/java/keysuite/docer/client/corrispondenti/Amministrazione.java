package keysuite.docer.client.corrispondenti;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.underscore.lodash.U;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static keysuite.docer.client.corrispondenti.ICorrispondente.valueOf;

public class Amministrazione extends Corrispondente implements IAmministrazione {

    public Amministrazione(){};

    public Amministrazione(Map<String,Object> xMap){
        super(xMap);

        this.setCodiceAmm(valueOf(U.get(xMap,"Amministrazione.CodiceAmministrazione")));
        this.setDenominazioneAmm(valueOf(U.get(xMap,"Amministrazione.Denominazione")));
        this.setCodiceAOO(valueOf(U.get(xMap,"AOO.CodiceAOO")));
        this.setDenominazioneAOO(valueOf(U.get(xMap,"AOO.Denominazione")));
        this.setCodiceUO(valueOf(U.get(xMap,"Amministrazione.UnitaOrganizzativa.Identificativo")));
        this.setDenominazioneUO(valueOf(U.get(xMap,"Amministrazione.UnitaOrganizzativa.Denominazione")));

        if ("competente".equals(valueOf(U.get(xMap,"Amministrazione.UnitaOrganizzativa.-tipo"))))
            this.setCompetente(true);

        String idPersona = valueOf(U.get(xMap,"Amministrazione.UnitaOrganizzativa.Persona.-id"));
        String desPersona = valueOf(U.get(xMap,"Amministrazione.UnitaOrganizzativa.Persona.Denominazione"));

        if (idPersona!=null || desPersona!=null){
            this.setPersona(new Persona(desPersona,idPersona));
        }

        String IndirizzoPostale = valueOf(U.get(xMap, "IndirizzoPostale.Denominazione"));

        if (IndirizzoPostale==null)
            IndirizzoPostale = valueOf(U.get(xMap, "Amministrazione.IndirizzoPostale.Denominazione"));

        this.setIndirizzoPostale(IndirizzoPostale);

        String IndirizzoTelematico = (String) xMap.get("IndirizzoTelematico");

        if (IndirizzoTelematico==null)
            IndirizzoTelematico = valueOf(U.get(xMap,"Amministrazione.UnitaOrganizzativa.IndirizzoTelematico"));

        if (IndirizzoTelematico==null)
            IndirizzoTelematico = valueOf(U.get(xMap,"Amministrazione.IndirizzoTelematico"));

        if (IndirizzoTelematico==null)
            IndirizzoTelematico = valueOf(U.get(xMap,"AOO.IndirizzoTelematico"));

        this.setIndirizzoTelematico(IndirizzoTelematico);
    }

    @Override
    @JsonIgnore
    public String getDenominazione() {
        String denominazione = denominazioneAmm;
        if (denominazione==null)
            denominazione = denominazioneAOO;
        if (denominazione==null)
            denominazione = denominazioneUO;
        else if (denominazioneUO!=null)
            denominazione += " " + denominazioneUO;

        return denominazione;
    }

    @Override
    @JsonIgnore
    public String getIdentificativo() {
        String identificativo = codiceAmm;
        if (identificativo==null)
            identificativo = codiceAOO;
        if (identificativo==null)
            identificativo = codiceUO;
        else if (codiceUO!=null)
            identificativo += "/" + codiceUO;

        return identificativo;
    }

    public Map<String,Object> toXmlMap(){

        return new LinkedHashMap(){{

            put("Amministrazione", new LinkedHashMap(){{

                put("Denominazione" , denominazioneAmm);
                put("CodiceAmministrazione", codiceAmm);

                if (denominazioneUO !=null || codiceUO !=null) {
                    put("UnitaOrganizzativa", new LinkedHashMap() {{
                        put("Denominazione", denominazioneUO);
                        put("Identificativo", codiceUO);

                        if (isCompetente())
                            put("-tipo","competente");

                        if (getPersona()!=null)
                            put( "Persona", new LinkedHashMap(){{
                                put("-id",getPersona().getIdentificativo());
                                put("Denominazione",getPersona().getDenominazione());
                            }});
                    }});
                }

                if (getMezzo()!=null){

                    if (getMezzo().equals(ICorrispondente.Mezzo.PEC)) {
                        put("InvioPEC", "SI");
                    }
                }

            }});

            put("AOO", new LinkedHashMap(){{
                put("Denominazione" , denominazioneAOO);
                put("CodiceAOO", codiceAOO);
            }});

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

            if (valueOf(getIndirizzoTelematico())!=null) {
                put("IndirizzoTelematico", getIndirizzoTelematico());
            }

            if (valueOf(getIndirizzoPostale())!=null){
                put("IndirizzoPostale", new LinkedHashMap() {{
                    put("Denominazione", getIndirizzoPostale());
                }});
            }
        }};
    }

    String codiceAmm;

    public void setCodiceAmm(String codiceAmm) {
        this.codiceAmm = codiceAmm;
    }

    public void setDenominazioneAmm(String denominazioneAmm) {
        this.denominazioneAmm = denominazioneAmm;
    }

    public void setCodiceAOO(String codiceAOO) {
        this.codiceAOO = codiceAOO;
    }

    public void setDenominazioneAOO(String denominazioneAOO) {
        this.denominazioneAOO = denominazioneAOO;
    }

    public void setCodiceUO(String codiceUO) {
        this.codiceUO = codiceUO;
    }

    public void setDenominazioneUO(String denominazioneUO) {
        this.denominazioneUO = denominazioneUO;
    }

    public void setPersona(IPersona persona) {
        this.persona = persona;
    }

    public void setCompetente(boolean competente) {
        this.competente = competente;
    }

    String denominazioneAmm;
    String codiceAOO;
    String denominazioneAOO;
    String codiceUO;
    String denominazioneUO;
    IPersona persona;
    boolean competente;

    @Override
    public int hashCode() {
        return super.hashCode() +
                Objects.hashCode(competente) +
                Objects.hashCode(persona) +
                Objects.hashCode(denominazioneUO) +
                Objects.hashCode(codiceUO) +
                Objects.hashCode(denominazioneAOO) +
                Objects.hashCode(codiceAOO) +
                Objects.hashCode(denominazioneAmm) +
                Objects.hashCode(codiceAmm);
    }

    @Override
    public String getCodiceAmm() {
        return codiceAmm;
    }

    @Override
    public String getDenominazioneAmm() {
        return denominazioneAmm;
    }

    @Override
    public String getCodiceAOO() {
        return codiceAOO;
    }

    @Override
    public String getDenominazioneAOO() {
        return denominazioneAOO;
    }

    @Override
    public String getCodiceUO() {
        return codiceUO;
    }

    @Override
    public String getDenominazioneUO() {
        return denominazioneUO;
    }

    @Override
    public IPersona getPersona() {
        return persona;
    }

    @Override
    public boolean isCompetente() {
        return competente;
    }
}
