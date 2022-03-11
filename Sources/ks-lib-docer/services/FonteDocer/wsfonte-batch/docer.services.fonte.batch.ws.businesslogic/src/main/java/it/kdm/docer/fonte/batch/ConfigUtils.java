package it.kdm.docer.fonte.batch;

import it.kdm.docer.commons.Config;

import java.util.List;
import org.apache.axiom.om.OMElement;

public class ConfigUtils {

    static public OMElement readConfigElement(Config config, String xpath) throws Exception {
        OMElement omelement = config.getNode(xpath);
        if (omelement == null) {            
            throw new Exception("chiave " + xpath + " mancante in configurazione");
        }

        return omelement;
    }
    
    static public List<OMElement> readConfigNodes(Config config, String xpath) throws Exception {
        List<OMElement> list = config.getNodes(xpath);
        if (list == null) {            
            throw new Exception("collezione di nodi " + xpath + " mancante in configurazione");
        }

        return list;
    }
    
    static public String readConfigKey(Config config, String xpath) throws Exception {
        OMElement omelement = config.getNode(xpath);
        if (omelement == null) {     
            throw new Exception("chiave " + xpath + " mancante in configurazione");
        }

        return omelement.getText();
    }
}
