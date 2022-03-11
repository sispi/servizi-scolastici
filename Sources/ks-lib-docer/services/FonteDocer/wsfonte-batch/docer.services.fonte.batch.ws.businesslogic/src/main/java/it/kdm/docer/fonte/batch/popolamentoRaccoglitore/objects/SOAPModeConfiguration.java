package it.kdm.docer.fonte.batch.popolamentoRaccoglitore.objects;

import it.kdm.docer.commons.Config;
import it.kdm.docer.fonte.batch.ConfigUtils;
import it.kdm.docer.fonte.batch.popolamentoFonte.objects.OrderedList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

public class SOAPModeConfiguration {

    private static QName qnameIdFonte = new QName("id_fonte");
    private String url = "";
    private String ontology = "";
    
    private Map<String,String> fontiTokenMap = new HashMap<String,String>();
        
    private OrderedList<String> orderedList = new OrderedList<String>(); 
    public OrderedList<String> getFonti(){
        return orderedList;
    }
    public String getTokenByFonte(String idFonte){
        return fontiTokenMap.get(idFonte);
    }
    
//    <url>http://raccoglitore.cn-er.org/rac-middleware-service-1.0.1-SNAPSHOT/RerServiziPerLeFonti/RerServicePerLeFontiImpl?wsdl</url>
//        <ontology>http://www.regione.emilia-romagna.it/ontologies/DOCER.owl</ontology>
//        <fonte id_fonte="EMR1" token="EMR1" />
//        <fonte id_fonte="EMR2" token="EMR2" />
//        <fonte id_fonte="ALTRA_FONTE" token="ALTRA_FONTE" />
        
    public SOAPModeConfiguration(Config config) throws Exception{
        
        OMElement omeUrl = ConfigUtils.readConfigElement(config, "//group[@name='batch-popolamento-raccoglitore']/section[@name='soap']/url");
        if(omeUrl==null){
            throw new Exception("Parametri soap non completi per la configurazione: nodo //group[@name='batch-popolamento-raccoglitore']/section[@name='soap']/url mancante o non valorizzato");
        }
        
        url = omeUrl.getText();
        
        OMElement omeOntology = ConfigUtils.readConfigElement(config, "//group[@name='batch-popolamento-raccoglitore']/section[@name='soap']/ontology");
        if(omeOntology==null){
            throw new Exception("Parametri soap non completi per la configurazione: nodo //group[@name='batch-popolamento-raccoglitore']/section[@name='soap']/ontology mancante o non valorizzato");
        }
        
        ontology = omeOntology.getText();
        
        List<OMElement> omeFontiList = ConfigUtils.readConfigNodes(config, "//group[@name='batch-popolamento-raccoglitore']/section[@name='soap']/fonte");

        if(omeFontiList==null){
            return;
        }
        for(OMElement omeFonte : omeFontiList){
        
            //String text = omeFolder.getText();

            String id_fonte = omeFonte.getAttributeValue(qnameIdFonte);
            String token = omeFonte.getText();

            if(id_fonte!=null && !id_fonte.equals("")){
                orderedList.add(id_fonte);
                fontiTokenMap.put(id_fonte, token);
            }

        }
                
    }
    public String getUrl() {
        return url;
    }
//    public void setUrl(String url) {
//        this.url = url;
//    }
    public String getOntology() {
        return ontology;
    }
//    public void setOntology(String ontology) {
//        this.ontology = ontology;
//    }     
    

}
