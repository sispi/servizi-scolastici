package it.kdm.docer.providers.solr;

import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.protocollazione.ProtocollazioneException;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.solr.client.SolrClient;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.jaxen.JaxenException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.springframework.util.StreamUtils;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.nio.charset.Charset;

public class Protocollazione implements it.kdm.docer.protocollazione.IProvider {

    private static Configurations CONFIGURATIONS = new Configurations();

    static Logger log = org.slf4j.LoggerFactory.getLogger(Protocollazione.class.getName());

    private String ROOT_TICKET = "admin";
    private ILoggedUserInfo currentUser;

    private final static String xmlResponse;

    static {
        try {
            InputStream is = Provider.class.getClassLoader().getResourceAsStream("protocollazioneResponse.xml");
            xmlResponse = StreamUtils.copyToString(is, Charset.defaultCharset());

            //File response = ConfigurationUtils.loadResourceFile("protocollazioneResponse.xml");
            //xmlResponse = new String(Files.readAllBytes(response.toPath()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Protocollazione() {
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
    public String login(String userId, String password, String codiceEnte) throws ProtocollazioneException {
        return "ticketProviderTest";
    }

    @Override
    public void logout(String token) throws ProtocollazioneException {
    }

    @Override
    public void setCurrentUser(ILoggedUserInfo info) throws ProtocollazioneException {
        this.currentUser = info;
    }

    public Integer staccaNumero(String ente, String aoo, boolean perpetuo) throws Exception {
        SolrClient client = SolrClient.getInstance();

        ModifiableSolrParams params = new ModifiableSolrParams();

        params.set("ticket",this.ROOT_TICKET);
        params.set("q","type:documento");
        params.set("fq","COD_ENTE:"+ente);
        params.add("fq","COD_AOO:"+aoo);
        params.add("fq","NUM_PG:*");
        params.set("fl","NUM_PG");
        params.set("rows",1);
        params.set("sort","DATA_PG desc");

        if (!perpetuo){
            params.add("fq","ANNO_PG:"+new DateTime(DateTimeZone.UTC).getYear());
        }

        QueryResponse resp = client.query(params);
        SolrDocumentList docs = resp.getResults();

        Integer next;

        if (docs.getNumFound()>0){
            SolrDocument lastResult = docs.get(0);
            String lastProgr = (String) lastResult.getFieldValue("NUM_PG");
            assert lastProgr!=null;
            next = Integer.parseInt(lastProgr) + 1 ;
        } else {
            next = 1;
        }

        return next;
    }

    public String protocolla(String datiProtocollo) throws ProtocollazioneException {
        try {
            OMElement datiReg = AXIOMUtil.stringToOM(datiProtocollo);

            String oggetto = xpath(datiReg, "//Intestazione/Oggetto");
            //String tipoRichiesta = xpath(datiReg, "//Intestazione/Flusso/TipoRichiesta");
            String docId = xpath(datiReg, "//Documenti/Documento/@id");
            String ente = xpath(datiReg, "//Documenti/Documento/Metadati/Parametro[@nome='COD_ENTE']/@valore");
            String aoo = xpath(datiReg, "//Documenti/Documento/Metadati/Parametro[@nome='COD_AOO']/@valore");

            SolrClient client = SolrClient.getInstance();
            ModifiableSolrParams params = new ModifiableSolrParams();

            params.set("ticket",this.ROOT_TICKET);
            params.set("q","type:documento");
            params.set("fq","COD_ENTE:"+ente);
            params.add("fq","COD_AOO:"+aoo);
            params.add("fq","DOCNUM:"+docId);
            params.add("fq","NUM_PG:*");
            params.set("fl","NUM_PG,DATA_PG");
            params.set("rows",1);
            params.set("sort","DATA_PG desc");

            QueryResponse resp = client.query(params);
            SolrDocumentList docs = resp.getResults();

            Integer next;
            DateTime now;
            String xpathProvider = String.format("//group[@name='Protocollazione']//provider[@ente='%s' and @aoo='%s']", ente, aoo);

            int cifre = Integer.parseInt(System.getProperty("padding.protocollo","7"));
            try {
                String v = null;
                OMElement el = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpathProvider);
                if (el!=null)
                    v = el.getAttributeValue(new QName("padding"));
                if (v==null)
                    v = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode("//group[@name='Protocollazione']/section").getAttributeValue(new QName("padding"));

                if (v!=null)
                    cifre = Integer.parseInt(v);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (docs.getNumFound()>0){

                SolrDocument lastResult = docs.get(0);
                String lastProgr = (String) lastResult.getFieldValue("NUM_PG");
                assert lastProgr!=null;
                next = Integer.parseInt(lastProgr);
                now  = new DateTime(lastResult.getFieldValue("DATA_PG"),DateTimeZone.UTC);

            } else {

                String attribute = null;

                try {
                    attribute = CONFIGURATIONS.getConfiguration(ente).getConfig()
                            .getNode(xpathProvider).getAttributeValue(new QName("perpetuo"));
                } catch (Exception e) {
                    //provider di default
                }

                boolean perpetuo = Boolean.parseBoolean(attribute);

                now = new DateTime(DateTimeZone.UTC);

                next = staccaNumero(ente,aoo,perpetuo);
            }

            String NUM = cifre>0 ? StringUtils.leftPad(next.toString(), cifre , '0') : next.toString();

            OMElement doc = AXIOMUtil.stringToOM(xmlResponse);

            AXIOMXPath xpath = new AXIOMXPath("//DATA_PG");
            OMElement elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(now.toString());

            xpath = new AXIOMXPath("//NUM_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(NUM);

            xpath = new AXIOMXPath("//ANNO_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(""+now.getYear());

            xpath = new AXIOMXPath("//REGISTRO_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText("PROTOCOLLO_PG");

            xpath = new AXIOMXPath("//OGGETTO_PG");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(oggetto);

            return doc.toString();
        } catch (Exception ex) {
            throw new ProtocollazioneException(ex);
        }
    }

    private String xpath(OMElement xml, String xpath) throws JaxenException {
        AXIOMXPath xpathExpr = new AXIOMXPath(xpath);
        return xpathExpr.stringValueOf(xml);
    }
}
