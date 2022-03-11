package it.kdm.docer.providers.solr;

import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.registrazione.RegistrazioneException;
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

public class Registrazione implements it.kdm.docer.registrazione.IProvider {

    private static Configurations CONFIGURATIONS = new Configurations();

    static Logger log = org.slf4j.LoggerFactory.getLogger(Registrazione.class.getName());

    private String ROOT_TICKET = "admin";
    private ILoggedUserInfo currentUser;

    private final static String xmlResponse;

    static {
        try {
            InputStream is = Provider.class.getClassLoader().getResourceAsStream("registrazioneResponse.xml");
            xmlResponse = StreamUtils.copyToString(is, Charset.defaultCharset());

            //File response = ConfigurationUtils.loadResourceFile("registrazioneResponse.xml");
            //xmlResponse = new String(Files.readAllBytes(response.toPath()));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Registrazione() {
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
    public String login(String userId, String password, String codiceEnte) throws RegistrazioneException {
        return "ticketProviderTest";
    }

    @Override
    public void logout(String token) throws RegistrazioneException {
    }

    @Override
    public void setCurrentUser(ILoggedUserInfo info) throws RegistrazioneException {
        this.currentUser = info;
    }

    public Integer staccaNumero(String ente, String aoo, String registro, boolean perpetuo) throws Exception{

        DateTime now = new DateTime(DateTimeZone.UTC);

        SolrClient client = SolrClient.getInstance();
        ModifiableSolrParams params = new ModifiableSolrParams();

        params.set("ticket",this.ROOT_TICKET);
        params.set("q","type:documento");
        params.set("fq","COD_ENTE:"+ente);
        params.add("fq","COD_AOO:"+aoo);
        params.add("fq","N_REGISTRAZ:*");
        params.set("fl","N_REGISTRAZ,D_REGISTRAZ");
        params.set("rows",1);
        params.set("sort","D_REGISTRAZ desc");

        if (!perpetuo) {
            params.add("fq", "A_REGISTRAZ:" + now.getYear());
        }

        QueryResponse resp = client.query(params);
        SolrDocumentList docs = resp.getResults();

        Integer next;

        if (docs.getNumFound() > 0) {
            SolrDocument lastResult = docs.get(0);
            String lastProgr = (String) lastResult.getFieldValue("N_REGISTRAZ");
            assert lastProgr != null;
            next = Integer.parseInt(lastProgr) + 1;
        } else {
            next = 1;
        }

        return next;

    }

    public String registra(String registroId, String datiRegistrazione) throws RegistrazioneException {
        try {
            OMElement datiReg = AXIOMUtil.stringToOM(datiRegistrazione);

            String oggetto = xpath(datiReg, "//Intestazione/Oggetto");
            String ente = xpath(datiReg, "//Documenti/Documento/Metadati/Parametro[@nome='COD_ENTE']/@valore");
            String aoo = xpath(datiReg, "//Documenti/Documento/Metadati/Parametro[@nome='COD_AOO']/@valore");
            String docId = xpath(datiReg, "//Documenti/Documento/@id");

            SolrClient client = SolrClient.getInstance();
            ModifiableSolrParams params = new ModifiableSolrParams();

            params.set("ticket",this.ROOT_TICKET);
            params.set("q","type:documento");
            params.set("fq","COD_ENTE:"+ente);
            params.add("fq","COD_AOO:"+aoo);
            params.add("fq","N_REGISTRAZ:*");
            params.add("fq","DOCNUM:"+docId);
            params.set("fl","N_REGISTRAZ,D_REGISTRAZ");
            params.set("rows",1);
            params.set("sort","D_REGISTRAZ desc");

            QueryResponse resp = client.query(params);
            SolrDocumentList docs = resp.getResults();

            DateTime now;
            Integer next;
            String xpathProvider = String.format("//group[@name='Registrazione']//provider[@ente='%s' and @aoo='%s' and @registro='%s']", ente, aoo, registroId);

            int cifre = Integer.parseInt(System.getProperty("padding.registri","0"));
            try {
                String v = null;
                OMElement el = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpathProvider);
                if (el!=null)
                    v = el.getAttributeValue(new QName("padding"));
                if (v==null)
                    v = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode("//group[@name='Registrazione']/section").getAttributeValue(new QName("padding"));

                if (v!=null)
                    cifre = Integer.parseInt(v);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (docs.getNumFound()>0){

                SolrDocument lastResult = docs.get(0);
                String lastProgr = (String) lastResult.getFieldValue("N_REGISTRAZ");
                assert lastProgr!=null;
                next = Integer.parseInt(lastProgr);
                now  = new DateTime(lastResult.getFieldValue("D_REGISTRAZ"),DateTimeZone.UTC);

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

                next = staccaNumero(ente,aoo,registroId,perpetuo);
            }

            String NUM = cifre>0 ? StringUtils.leftPad(next.toString(), cifre , '0') : next.toString();

            OMElement doc = AXIOMUtil.stringToOM(xmlResponse);

            AXIOMXPath xpath = new AXIOMXPath("//OggettoRegistrazione");
            OMElement elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(oggetto);

            xpath = new AXIOMXPath("//DataRegistrazione");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(now.toString());

            xpath = new AXIOMXPath("//NumeroRegistrazione");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(NUM);

            xpath = new AXIOMXPath("//IDRegistro");
            elem = (OMElement)xpath.selectSingleNode(doc);
            elem.setText(registroId);

            return doc.toString();
        } catch (Exception ex) {
            throw new RegistrazioneException(ex);
        }
    }

    private String xpath(OMElement xml, String xpath) throws JaxenException {
        AXIOMXPath xpathExpr = new AXIOMXPath(xpath);
        return xpathExpr.stringValueOf(xml);
    }
}
