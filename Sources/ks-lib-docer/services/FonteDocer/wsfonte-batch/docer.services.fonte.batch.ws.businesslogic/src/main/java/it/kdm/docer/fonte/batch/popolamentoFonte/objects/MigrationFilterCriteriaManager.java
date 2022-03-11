package it.kdm.docer.fonte.batch.popolamentoFonte.objects;

import it.kdm.docer.commons.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.impl.llom.OMElementImpl;

public class MigrationFilterCriteriaManager {

    // private static Config c = null;
    // private static File configFile = null;
    //
    // static public String getParameter(String configFilePath, String
    // sectionName, String parameterName) throws Exception {
    //
    // if (configFile == null) {
    // configFile = new File(configFilePath);
    // if (!configFile.exists()) {
    // throw new
    // Exception("file di configurazione della business logic archivio corrente non trovato: "
    // + configFilePath);
    // }
    // }
    //
    // if(c==null){
    // c = new Config();
    // c.setConfigFile(configFile);
    // }
    //
    // OMElement element =
    // c.getNode("//configuration/group[@name='batch-popolamento-fonte']/section[@name='"
    // +sectionName +"']/" + parameterName);
    // return element.getText();
    //
    // }

    private static QName idQName = new QName("id");
    private static QName idFonteQName = new QName("id_fonte");
    private static QName forceUpdateQName = new QName("force_update");

    // private static QName documentiPrincipaliCriteriaQName = new
    // QName("documenti_principali_criteria");
    //
//     private static QName cfPersonaQName = new QName("cf_persona");
//     private static QName cfAziendaQName = new QName("cf_azienda");

    static public OrderedList<MigrationFilterCriteria> buildMigrationFilterCriteria(List<OMElement> omeMigrationFilterCriteriaList) throws Exception {

        List<String> idCollection = new ArrayList<String>();

        OrderedList<MigrationFilterCriteria> migrationCriteriaList = new OrderedList<MigrationFilterCriteria>();

        for (OMElement omeMigrationFilterCriteria : omeMigrationFilterCriteriaList) {

            if (omeMigrationFilterCriteria == null) {
                continue;
            }

            String id = omeMigrationFilterCriteria.getAttributeValue(idQName);
            if (id == null || id.equals("")) {
                continue;
            }

            String idFonte = omeMigrationFilterCriteria.getAttributeValue(idFonteQName);
            if (idFonte == null || idFonte.equals("")) {
                continue;
            }

            boolean forceUpdate = Boolean.parseBoolean(omeMigrationFilterCriteria.getAttributeValue(forceUpdateQName));

//            String cfPersonaSimulata = omeMigrationFilterCriteria.getAttributeValue(cfPersonaQName);
//            String cfAziendaSimulata = omeMigrationFilterCriteria.getAttributeValue(cfAziendaQName);
            
            if (idCollection.contains(id)) {
                continue;
            }

            idCollection.add(id);

            // ciclo criteri di ricerca dell'oggetto di I livello (fascicolo o
            // documento)

            MigrationFilterCriteria migrationFilterCriteria = new MigrationFilterCriteria();
            migrationFilterCriteria.setId(id);
            migrationFilterCriteria.setIdFonte(idFonte);
            migrationFilterCriteria.setForceUpdate(forceUpdate);
            migrationFilterCriteria.setDoSearchDocumentiFascicolo(false);
//            if(cfPersonaSimulata!=null){
//                migrationFilterCriteria.setCfPersonaSimulata(cfPersonaSimulata);
//            }            
//            if(cfAziendaSimulata!=null){
//                migrationFilterCriteria.setCfAziendaSimulata(cfAziendaSimulata);
//            }
            getObjectCriteria(omeMigrationFilterCriteria, migrationFilterCriteria);

            migrationCriteriaList.add(migrationFilterCriteria);

        }// chisura ciclo totale

        return migrationCriteriaList;

    }

    static private void getObjectCriteria(OMElement omeObjectCriteria, MigrationFilterCriteria migrationFilterCriteria) {

        if (omeObjectCriteria.getLocalName().equalsIgnoreCase("fascicolo_criteria")) {
            migrationFilterCriteria.setIsFascicoloCriteria(true);
        }
        else if (!omeObjectCriteria.getLocalName().equalsIgnoreCase("documento_criteria")) {
            return;
        }

        // Iterator<OMElement> objectCriteriaIterator =
        // omeObjectCriteria.getChildElements();

        Iterator children = omeObjectCriteria.getChildren();
        while (children.hasNext()) {

            Object obj = children.next();

            if (obj instanceof OMElementImpl) {

                OMElement propElem = (OMElement)obj;
                String propName = propElem.getLocalName().toUpperCase();
                if (propName.equals("") || propName.equalsIgnoreCase("relazioni") || propName.equalsIgnoreCase("documento_criteria") || propName.equalsIgnoreCase("fascicolo_criteria")) {
                    continue;
                }

                if (propName.equalsIgnoreCase("documenti_principali_criteria") && migrationFilterCriteria.isFascicoloCriteria()) {
                    migrationFilterCriteria.setDoSearchDocumentiFascicolo(true);
                    getDocumentiFascicoloCriteria(propElem, migrationFilterCriteria);
                    continue;
                }

                String propValue = propElem.getText();

                List<String> objectOrCriteria = migrationFilterCriteria.getObjectCriteria().get(propName);
                if (objectOrCriteria == null) {
                    objectOrCriteria = new ArrayList<String>();
                    migrationFilterCriteria.getObjectCriteria().put(propName, objectOrCriteria);
                }

                objectOrCriteria.add(propValue);

            }
        }

    }

    static private void getDocumentiFascicoloCriteria(OMElement omeDocumentiFascicoloCriteria, MigrationFilterCriteria migrationFilterCriteria) {
       
        Iterator children = omeDocumentiFascicoloCriteria.getChildren();
        while (children.hasNext()) {

            Object obj = children.next();

            if (obj instanceof OMElementImpl) {

                OMElement docElem = (OMElement)obj;

                String docPropName = docElem.getLocalName().toUpperCase();
                if (docPropName.equals("")) {
                    continue;
                }

                if (docPropName.equalsIgnoreCase("documenti_principali_criteria") || docPropName.equalsIgnoreCase("fascicolo_criteria") || docPropName.equalsIgnoreCase("documento_criteria")) {
                    continue;
                }

                String docPropValue = docElem.getText();

                List<String> docOrCriteria = migrationFilterCriteria.getChildrenObjectCriteria().get(docPropName);
                if (docOrCriteria == null) {
                    docOrCriteria = new ArrayList<String>();
                    migrationFilterCriteria.getChildrenObjectCriteria().put(docPropName, docOrCriteria);
                }

                // aggiunto con puntatore
                docOrCriteria.add(docPropValue);

            }
        }

    }
    

}
