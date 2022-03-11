package it.kdm.docer.fonte.batch.popolamentoFonte.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MigrationFilterCriteria {

    private boolean isFascicoloCriteria = false;
    private boolean doSearchDocumentiFascicolo = false;
    
    private String id = "";
    private String idFonte = "";    
    private Map<String,List<String>> objectCriteria = new HashMap<String,List<String>>();
    private Map<String,List<String>> childrenObjectCriteria = new HashMap<String,List<String>>();    
    
//    private String cfPersonaSimulata = "";
//    private String cfAziendaSimulata = "";
    
    private boolean forceUpdate = false;   
    
    public boolean getForceUpdate() {
        return forceUpdate;
    }
    
    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public Map<String, List<String>> getObjectCriteria() {
        return objectCriteria;
    }
    public void setObjectCriteria(Map<String, List<String>> objectCriteria) {
        this.objectCriteria = objectCriteria;
    }
    
    public Map<String, List<String>> getChildrenObjectCriteria() {
        return childrenObjectCriteria;
    }
    public void setChildrenObjectCriteria(Map<String, List<String>> childrenObjectCriteria) {
        this.childrenObjectCriteria = childrenObjectCriteria;
    }
    public String getIdFonte() {
        return idFonte;
    }
    public void setIdFonte(String idFonte) {
        this.idFonte = idFonte;
    }

    public boolean isFascicoloCriteria() {
        return isFascicoloCriteria;
    }

    public void setIsFascicoloCriteria(boolean isFascicoloCriteria) {
        this.isFascicoloCriteria = isFascicoloCriteria;
    }

    public boolean doSearchDocumentiFascicolo() {
        return doSearchDocumentiFascicolo;
    }

    public void setDoSearchDocumentiFascicolo(boolean doSearchDocumentiFascicolo) {
        this.doSearchDocumentiFascicolo = doSearchDocumentiFascicolo;
    }

//    public String getCfPersonaSimulata() {
//        return cfPersonaSimulata;
//    }
//
//    public void setCfPersonaSimulata(String cfPersonaSimulata) {
//        this.cfPersonaSimulata = cfPersonaSimulata;
//    }
//
//    public String getCfAziendaSimulata() {
//        return cfAziendaSimulata;
//    }
//
//    public void setCfAziendaSimulata(String cfAziendaSimulata) {
//        this.cfAziendaSimulata = cfAziendaSimulata;
//    }
   
}
