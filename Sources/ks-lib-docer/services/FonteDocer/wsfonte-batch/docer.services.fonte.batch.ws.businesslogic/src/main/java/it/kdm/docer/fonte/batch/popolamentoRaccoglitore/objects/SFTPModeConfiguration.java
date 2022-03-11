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

public class SFTPModeConfiguration {

    private static String ID_FONTE = "id_fonte";
    private static String HOST = "host";
    private static String PORT = "port";
    private static String USERID = "userid";
    private static String PASSWORD = "password";
    private static String FOLDER_NAME = "folder_name";
    
    private static QName qnameIdFonte = new QName(ID_FONTE);
    private static QName qnameHost = new QName(HOST);
    private static QName qnamePort = new QName(PORT);
    private static QName qnameUserid = new QName(USERID);
    private static QName qnamePassword = new QName(PASSWORD);

    private OrderedList<String> orderedList = new OrderedList<String>();
    private Map<String, Map<String, String>> fontiMap = new HashMap<String, Map<String, String>>();

    public OrderedList<String> getFonti() {
        return orderedList;
    }

    public String getSFTPFolderUserid(String id_fonte) {
        Map<String, String> map = fontiMap.get(id_fonte);

        if (map == null) {
            return null;
        }

        return map.get(USERID);
    }

    public String getSFTPFolderPassword(String id_fonte) {
        Map<String, String> map = fontiMap.get(id_fonte);

        if (map == null) {
            return null;
        }

        return map.get(PASSWORD);
    }
    
    public String getSFTPFolderHost(String id_fonte) {
        Map<String, String> map = fontiMap.get(id_fonte);

        if (map == null) {
            return null;
        }

        return map.get(HOST);
    }
    
    public Integer getSFTPFolderPort(String id_fonte) {
        Map<String, String> map = fontiMap.get(id_fonte);

        if (map == null) {
            return null;
        }

        try{
            return Integer.parseInt(map.get(PORT));
        }
        catch(NumberFormatException nfe){
            return null;
        }
    }
    
    public String getSFTPFolderName(String id_fonte) {
        Map<String, String> map = fontiMap.get(id_fonte);

        if (map == null) {
            return null;
        }

        return map.get(FOLDER_NAME);
    }
    
    // <section name="sftp">
    // <fonte id_fonte="EMR1" host="93.62.155.226" port="3003" userid="raccoglitore"
    // password="rac384_hjsj78">EMR1</fonte>
    // <fonte id_fonte="ALTRA_FONTE" host="93.62.155.226" userid="asdfg"
    // password="aaaaaa">ALTRA_FOLDER</fonte>
    // </section>

    public SFTPModeConfiguration(Config config) throws Exception {

        List<OMElement> omeFonteList = ConfigUtils.readConfigNodes(config, "//group[@name='batch-popolamento-raccoglitore']/section[@name='sftp']/folder");

        if (omeFonteList == null) {
            return;
        }

        for (OMElement omeFonte : omeFonteList) {
            
            String id_fonte = omeFonte.getAttributeValue(qnameIdFonte);
            String folder_name = omeFonte.getText();
            String host = omeFonte.getAttributeValue(qnameHost);            
            String port = omeFonte.getAttributeValue(qnamePort);
            String user_id = omeFonte.getAttributeValue(qnameUserid);
            String password = omeFonte.getAttributeValue(qnamePassword);            
            
            
            if (id_fonte != null && !id_fonte.equals("")) {
                Map<String,String> props = new HashMap<String, String>();
                
                orderedList.add(id_fonte);
                fontiMap.put(id_fonte, props);
                               
                props.put(ID_FONTE, id_fonte);
                props.put(FOLDER_NAME, folder_name);
                props.put(HOST, host);
                props.put(USERID, user_id);
                props.put(PASSWORD, password);
                props.put(PORT, port);
            }

        }

    }
}
