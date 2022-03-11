package it.kdm.docer.providers.solr;

import com.google.common.base.Strings;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.fascicolazione.FascicolazioneException;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.solr.client.SolrClient;
import org.apache.axiom.om.OMElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.slf4j.Logger;
import org.springframework.util.StreamUtils;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Calendar;

public class Fascicolazione implements it.kdm.docer.fascicolazione.IProvider {

    private static Configurations CONFIGURATIONS = new Configurations();

    static Logger log = org.slf4j.LoggerFactory.getLogger(Fascicolazione.class.getName());

    private String ROOT_TICKET = "admin";
    private ILoggedUserInfo currentUser;

    private final static String xmlResponse;

    static {
        try {

            InputStream is = Provider.class.getClassLoader().getResourceAsStream("fascicolazioneResponse.xml");
            xmlResponse = StreamUtils.copyToString(is, Charset.defaultCharset());

            //File response = ConfigurationUtils.loadResourceFile("fascicolazioneResponse.xml");
            //xmlResponse = new String(Files.readAllBytes(response.toPath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Fascicolazione() {
        /*String zkHost = System.getProperty("zkHost");
        if (Strings.isNullOrEmpty(zkHost)){
            Properties prop = null;
            try {
                prop = ConfigurationUtils.loadProperties("solr.properties");
            } catch (ConfigurationLoadingException e) {
                throw new RuntimeException(e);
            }
            zkHost = prop.getProperty(Configuration.Keys.ZK_HOST);
            System.setProperty("zkHost",zkHost);
        }*/
    }
    @Override
    public String login(String userId, String password, String codiceEnte) throws FascicolazioneException {
        /*try {
            return manager.login(userId,password,codiceEnte);
        } catch (DocerException e) {
            throw new FascicolazioneException(e);
        }*/
        return "ticketProviderTest";
    }

    @Override
    public void logout(String token) throws FascicolazioneException {
    }

    @Override
    public void setCurrentUser(ILoggedUserInfo info) throws FascicolazioneException {
        this.currentUser = info;
    }

    @Override
    public String fascicola(String datiFascicolo) throws FascicolazioneException {
        return xmlResponse;
    }

    public Integer staccaNumero(String ente, String aoo, String classifica, String anno, String progressivoParent, boolean perpetuo) throws Exception {
        SolrClient client = SolrClient.getInstance();
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("ticket",this.ROOT_TICKET);
        params.set("q","type:fascicolo");
        params.set("fq","COD_ENTE:"+ente);
        params.set("fq","COD_AOO:"+aoo);
        params.set("fq","CLASSIFICA:"+classifica);
        params.set("fl","PROGR_FASCICOLO");
        params.set("rows",1);
        params.set("sort","created_on desc");

        if (!Strings.isNullOrEmpty(progressivoParent)){
            params.add("fq","PARENT_PROGR_FASCICOLO:"+progressivoParent);
        } else {
            params.add("fq","-PARENT_PROGR_FASCICOLO:*");
        }

        if (!perpetuo){
            params.add("fq","ANNO_FASCICOLO:"+anno);
        }

        QueryResponse resp = client.query(params);
        SolrDocumentList docs = resp.getResults();

        Integer next;
        if (docs.getNumFound()>0){
            SolrDocument lastResult = docs.get(0);
            String lastProgr = (String) lastResult.getFieldValue("PROGR_FASCICOLO");
            assert lastProgr!=null;
            lastProgr = lastProgr.substring(lastProgr.lastIndexOf("/")+1);
            next = Integer.parseInt(lastProgr) + 1 ;
        } else {
            next = 1;
        }

        return next;
    }

    @Override
    public String creaFascicolo(KeyValuePair[] metadati) throws FascicolazioneException {
        try {
            String ente = null;
            String aoo = null;
            String classifica = null;
            String anno = null;
            String progressivoParent = null;
            String descrizione = null;

            for (KeyValuePair pair : metadati) {
                String key = pair.getKey();
                if (key.equals("COD_ENTE")) {
                    ente = pair.getValue();
                } else if (key.equals("COD_AOO")) {
                    aoo = pair.getValue();
                } else if (key.equals("ANNO_FASCICOLO")) {
                    anno = pair.getValue();
                } else if (key.equals("CLASSIFICA")) {
                    classifica = pair.getValue();
                } else if (key.equals("PARENT_PROGR_FASCICOLO")) {
                    progressivoParent = pair.getValue();
                } else if (key.equals("DES_FASCICOLO")) {
                    descrizione = pair.getValue();
                }
            }

            String xpathProvider = String.format("//group[@name='Fascicolazione']//provider[@ente='%s' and @aoo='%s']", ente, aoo);

            int cifre = Integer.parseInt(System.getProperty("padding.fascicoli","0"));
            try {
                String v = null;
                OMElement el = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpathProvider);
                if (el!=null)
                    v = el.getAttributeValue(new QName("padding"));
                if (v==null)
                    v = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode("//group[@name='Fascicolazione']/section").getAttributeValue(new QName("padding"));

                if (v!=null)
                    cifre = Integer.parseInt(v);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String attribute = null;

            try {
                attribute = CONFIGURATIONS.getConfiguration(ente).getConfig()
                        .getNode(xpathProvider).getAttributeValue(new QName("perpetuo"));
            } catch (Exception e) {
                //e.printStackTrace();
            }

            boolean perpetuo = Boolean.parseBoolean(attribute);

            if(Strings.isNullOrEmpty(anno)){
                anno = ""+Calendar.getInstance().get(Calendar.YEAR);
            }

            Integer next = staccaNumero(ente,aoo,classifica,anno,progressivoParent,perpetuo);

            String NUM = cifre>0 ? StringUtils.leftPad(next.toString(), cifre , '0') : next.toString();

            XMLUtil dom = new XMLUtil(xmlResponse);

            dom.setNodeValue("//esito_fascicolo/COD_ENTE", ente);
            dom.setNodeValue("//esito_fascicolo/COD_AOO", aoo);
            dom.setNodeValue("//esito_fascicolo/CLASSIFICA", classifica);
            dom.setNodeValue("//esito_fascicolo/DES_FASCICOLO", descrizione);
            dom.setNodeValue("//esito_fascicolo/ANNO_FASCICOLO", anno);

            StringBuilder progressivo = new StringBuilder();
            if (!Strings.isNullOrEmpty(progressivoParent)) {
                dom.setNodeValue("//esito_fascicolo/PARENT_PROGR_FASCICOLO", progressivoParent);
                progressivo.append(progressivoParent);
                progressivo.append("/");
                dom.setNodeValue("//esito_fascicolo/ANNO_FASCICOLO", anno);
            }
            progressivo.append(NUM);
            dom.setNodeValue("//esito_fascicolo/PROGR_FASCICOLO", progressivo.toString());

            return dom.toXML();

        } catch (Exception ex) {
            throw new FascicolazioneException(ex);
        }
    }

    @Override
    public String forzaNuovoFascicolo(KeyValuePair[] metadati) throws FascicolazioneException {
        try {
            XMLUtil dom = new XMLUtil(xmlResponse);
            for (KeyValuePair metadata : metadati) {
                String xpath = String.format("//%s", metadata.getKey().toUpperCase());
                if (dom.getNode(xpath) != null) {
                    dom.setNodeValue(xpath, metadata.getValue());
                }
            }
            return dom.toXML();
        } catch (Exception ex) {
            throw new FascicolazioneException(ex);
        }
    }

    @Override
    public boolean updateACLFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] acl) throws FascicolazioneException {
        return true;
    }

    @Override
    public String updateFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] metadati) throws FascicolazioneException {
        return null;
    }
}
