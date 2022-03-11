/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.protocollazione;

import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.protocollazione.wsclient.ProtocollazioneServiceStub;
import it.kdm.docer.registrazione.database.Registrazioni;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.commons.io.IOUtils;
import org.jaxen.JaxenException;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lorenzo
 */
public class ProviderMetro implements IProvider {

    //************************************************************
    //parametri da recuperare dalla configurazione
    //************************************************************
    private static String wsuser = "";
    private static String wspwd = "";
    private static String wsurl = "";
    private static String registro = "";
    org.slf4j.Logger logger = org.slf4j.org.slf4j.LoggerFactory.getLogger(ProviderMetro.class);

    private static Registrazioni registrazioni;
    private static Configurations CONFIGURATIONS = new Configurations();

    private Properties config = new Properties();



    private void getProperties() throws ProtocollazioneException {

        try {
            InputStream cis = this.getClass().getResourceAsStream("/config.xml");
            if (cis == null) {
                org.slf4j.LoggerFactory.getLogger(ProviderMetro.class.getName()).log(Level.SEVERE, "File di configurazione config.xml non trovato");
                throw new ProtocollazioneException("File di configurazione config.xml non trovato");
            }

            config.loadFromXML(cis);

            if (config.getProperty("wsuser") == null) {
                org.slf4j.LoggerFactory.getLogger(ProviderMetro.class.getName()).log(Level.SEVERE, "Parametro wsuser non trovato");
                throw new ProtocollazioneException("Parametro wsuser non trovato");
            } else
                wsuser = config.getProperty("wsuser");

            if (config.getProperty("wspwd") == null) {
                org.slf4j.LoggerFactory.getLogger(ProviderMetro.class.getName()).log(Level.SEVERE, "Parametro wspwd non trovato");
                throw new ProtocollazioneException("parametro wspwd non trovato");
            } else
                wspwd = config.getProperty("wspwd");

            if (config.getProperty("wsurl") == null) {
                org.slf4j.LoggerFactory.getLogger(ProviderMetro.class.getName()).log(Level.SEVERE, "Parametro wsurl non trovato");
                throw new ProtocollazioneException("parametro wsurl non trovato");
            } else
                wsurl = config.getProperty("wsurl");

            if (config.getProperty("registro") == null) {
                org.slf4j.LoggerFactory.getLogger(ProviderMetro.class.getName()).log(Level.SEVERE, "Parametro registro non trovato");
                throw new ProtocollazioneException("parametro registro non trovato");
            } else
                registro = config.getProperty("registro");
        }
        catch(Exception e) {
            throw new ProtocollazioneException("Errore lettura file di configurazione: " + e.getMessage());
        }
    }

    static {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:META-INF/database/applicationContext.xml");
        registrazioni = ctx.getBean(Registrazioni.class);
    }

    private ILoggedUserInfo currentUser;

    private ProtocollazioneServiceStub getWSClient() throws AxisFault {
        EndpointReference wsep = new EndpointReference();
        wsep.setAddress(wsurl);

        ProtocollazioneServiceStub client = new ProtocollazioneServiceStub();
        client._getServiceClient().setTargetEPR(wsep);

        return client;
    }

    public String login(String username, String password, String library) throws ProtocollazioneException{

        try {
            this.getProperties();
        }
        catch(Exception e) {
            throw new ProtocollazioneException(e.getMessage());
        }

        try {
            ProtocollazioneServiceStub.Login login = new ProtocollazioneServiceStub.Login();
            login.setUsername(username);
            login.setPassword(password);

            ProtocollazioneServiceStub.LoginE loginReq = new ProtocollazioneServiceStub.LoginE();
            loginReq.setLogin(login);

            ProtocollazioneServiceStub client = getWSClient();
            ProtocollazioneServiceStub.LoginResponseE resp = client.login(loginReq);

            if (resp.getLoginResponse().get_return().isDSTSpecified()) {
                return resp.getLoginResponse().get_return().getDST();
            }

            String erroCode = resp.getLoginResponse().get_return().getError_number() + " - " + resp.getLoginResponse().get_return().getError_description();

            return erroCode;

        } catch (Exception ex) {
            org.slf4j.LoggerFactory.getLogger(ProviderMetro.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        }
    }

    public void logout(String token) throws ProtocollazioneException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public String protocolla(String datiProtocollo) throws ProtocollazioneException {

        try {

            /*
            - xpath su Documenti per recuperare gli allegati,annessi e annotazioni
            - crea N oggetti Attach da inviare al WS
            - bridge xslt per garantire un punto di intervento per il formato di input del protocollo
            - chiama il WS protocollazione con l'xml dati Protocollo, il doc principale e tutti gl attach
            - ritorna i dati di protocollo secondo il formato previsto
             */

            /* formato risposta:
            <esito>
                <codice>0 OK|1 WARNING</codice>
                <descrizione></descrizione>
                <esito_protocollo>
                    <NUM_PG/> <!-- è il numero di protocollo assegnato al documentoo -->
                    <DATA_PG/> <!-- è la data di protocollo assegnata al documento -->
                    <ANNO_PG/> <!-- è l’anno di protocollo assegnato al documento -->
                    <OGGETTO_PG/> <!-- è l’oggetto del protocollo assegnato al documento -->
                    <REGISTRO_PG/> <!-- è il registro del protocollo assegnato al documento -->
                </esito_protocollo>
            </esito>
            */

            OMElement datiReg = AXIOMUtil.stringToOM(datiProtocollo);
            String oggettoPG = xpath(datiReg, "//Intestazione/Oggetto");

            List<ProtocollazioneServiceStub.Attach> attachments = new ArrayList<ProtocollazioneServiceStub.Attach>();

            List<OMElement> allegati = xpathOMElement(datiReg, "//Documenti/Allegati/*");
            //cicla i risultati e crea oggetti Attach
            for (OMElement elem : allegati) {
                ProtocollazioneServiceStub.Attach attach = buildAttach(elem);
                attachments.add(attach);
            }

            List<OMElement> annessi = xpathOMElement(datiReg, "//Documenti/Annessi/*");
            //cicla i risultati e crea oggetti Attach
            for (OMElement elem : annessi) {
                ProtocollazioneServiceStub.Attach attach = buildAttach(elem);
                attachments.add(attach);
            }

            List<OMElement> annotazioni = xpathOMElement(datiReg, "//Documenti/Annotazioni/*");
            //cicla i risultati e crea oggetti Attach
            for (OMElement elem : annotazioni) {
                ProtocollazioneServiceStub.Attach attach = buildAttach(elem);
                attachments.add(attach);
            }


            ProtocollazioneServiceStub client = getWSClient();
            ProtocollazioneServiceStub.ProtocollazioneDocumento prot = new ProtocollazioneServiceStub.ProtocollazioneDocumento();
            prot.setDST(this.currentUser.getTicket());

            // chiamata con allegati
            if (attachments.size()>0)
                prot.setListaAllegati(attachments.toArray(new ProtocollazioneServiceStub.Attach[]{}));
            else
                prot.setListaAllegati(null);

            // file profilo xml
            DataHandler dataXml = new DataHandler(datiProtocollo, "text/plain; charset=UTF-8");
            prot.setDataXML(dataXml);

            //recupero file doc principale da docer
            String uriDoc = xpath(datiReg, "//Documenti/Documento/@uri");
            DataHandler docPrinc = retrieveFileDoc(uriDoc);
            prot.setDocPrinc(docPrinc);

            ProtocollazioneServiceStub.ProtocollazioneDocumentoE protE = new ProtocollazioneServiceStub.ProtocollazioneDocumentoE();
            protE.setProtocollazioneDocumento(prot);

            ProtocollazioneServiceStub.ProtocollazioneDocumentoResponseE res = client.protocollazioneDocumento(protE);
            if (res.getProtocollazioneDocumentoResponse().get_return().isDataProtSpecified()) {
                //**************************************************************************************
                //                          FORMATTA LA RISPOSTA
                //**************************************************************************************
                Long numPG = res.getProtocollazioneDocumentoResponse().get_return().getNumProt();
                Long annoPG = res.getProtocollazioneDocumentoResponse().get_return().getAnnoProt();
                String dataPG = res.getProtocollazioneDocumentoResponse().get_return().getDataProt();


                Date protDate;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    protDate = sdf.parse(dataPG);
                } catch (ParseException e) {
                    throw  new ProtocollazioneException(e);
                }

                InputStream resStream = this.getClass().getResourceAsStream("/providerDataTest.xml");
                String xml = IOUtils.toString(resStream);
                OMElement doc = AXIOMUtil.stringToOM(xml);

                AXIOMXPath xpath = new AXIOMXPath("//DATA_PG");
                OMElement elem = (OMElement) xpath.selectSingleNode(doc);
                elem.setText(ISODateTimeFormat.dateTime().print(
                        protDate.getTime()));

                xpath = new AXIOMXPath("//NUM_PG");
                elem = (OMElement) xpath.selectSingleNode(doc);
                elem.setText(Long.toString(numPG));

                xpath = new AXIOMXPath("//ANNO_PG");
                elem = (OMElement) xpath.selectSingleNode(doc);
                elem.setText(Long.toString(annoPG));

                xpath = new AXIOMXPath("//REGISTRO_PG");
                elem = (OMElement) xpath.selectSingleNode(doc);
                elem.setText(registro);

                xpath = new AXIOMXPath("//OGGETTO_PG");
                elem = (OMElement) xpath.selectSingleNode(doc);
                elem.setText(oggettoPG);

                return doc.toString();
            } else {
                //Errore dal servizio di protocollo
                InputStream resStream = this.getClass().getResourceAsStream("/providerDataTest.xml");
                String xml = IOUtils.toString(resStream);
                OMElement doc = AXIOMUtil.stringToOM(xml);

                Integer errCode = res.getProtocollazioneDocumentoResponse().get_return().getError_number();
                String errDescription = res.getProtocollazioneDocumentoResponse().get_return().getError_description();

                AXIOMXPath xpath = new AXIOMXPath("//codice");
                OMElement elem = (OMElement) xpath.selectSingleNode(doc);
                elem.setText(String.valueOf(errCode));

                xpath = new AXIOMXPath("//descrizione");
                elem = (OMElement) xpath.selectSingleNode(doc);
                elem.setText(errDescription);

                return doc.toString();
            }

            //throw new UnsupportedOperationException("Not supported yet.");
        } catch (IOException ex) {
            org.slf4j.LoggerFactory.getLogger(ProviderMetro.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        } catch (JaxenException ex) {
            org.slf4j.LoggerFactory.getLogger(ProviderMetro.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        } catch (XMLStreamException ex) {
            org.slf4j.LoggerFactory.getLogger(ProviderMetro.class.getName()).error(ex.getMessage(), ex);
            throw new ProtocollazioneException(ex);
        }
    }

    private ProtocollazioneServiceStub.Attach buildAttach(OMElement elem) throws MalformedURLException, JaxenException {
        DataHandler docHandler = retrieveFileDoc(elem.getAttributeValue(new QName("uri")));
        ProtocollazioneServiceStub.Attach attach = new ProtocollazioneServiceStub.Attach();
        attach.setFileContent(docHandler);
        attach.setId(elem.getAttributeValue(new QName("id")));
        attach.setFileName(xpath(elem,"DOC_NAME"));

        return attach;
    }
    private DataHandler retrieveFileDoc(String docUrl) throws MalformedURLException {
        return new DataHandler( new URL( docUrl ) );
    }


    private String xpath(OMElement xml, String xpath) throws JaxenException {
        AXIOMXPath xpathExpr = new AXIOMXPath(xpath);
        return xpathExpr.stringValueOf(xml);
    }

    private List<OMElement> xpathOMElement(OMElement xml, String xpath) throws JaxenException {
        AXIOMXPath xpathExpr = new AXIOMXPath(xpath);
        return xpathExpr.selectNodes(xml);
    }

    private OMElement xpathSingleOMElement(OMElement xml, String xpath) throws JaxenException {
        AXIOMXPath xpathExpr = new AXIOMXPath(xpath);
        return (OMElement)xpathExpr.selectSingleNode(xml);
    }

    public void setCurrentUser(ILoggedUserInfo info) throws ProtocollazioneException {
        this.currentUser = info;
    }
    
    
}
