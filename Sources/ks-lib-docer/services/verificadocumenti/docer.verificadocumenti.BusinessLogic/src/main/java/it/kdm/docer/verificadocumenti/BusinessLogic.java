/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.verificadocumenti;

import com.google.common.base.Strings;
import it.eng.parer.ccd.lib.ParerLib;
import it.eng.parer.ws.versamento.dto.FileBinario;
import it.eng.parer.ws.xml.versReq.*;
import it.eng.parer.ws.xml.versReq.types.TipoConservazioneType;
import it.eng.parer.ws.xml.versReq.types.TipoSupportoType;
import it.eng.parer.ws.xml.versResp.EsitoComponente;
import it.eng.parer.ws.xml.versResp.EsitoVersamento;
import it.eng.parer.ws.xml.versResp.Firmatari;
import it.eng.parer.ws.xml.versResp.types.ECEsitoRicFormatoType;
import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerExceptionException;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.clients.DocerServicesStub.*;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.commons.docerservice.BaseService;
import it.kdm.docer.commons.docerservice.UserProfile;
import it.kdm.docer.sdk.classes.UserInfo;
import it.kdm.utils.ResourceLoader;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.jaxp.OMSource;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.jaxen.JaxenException;

import javax.xml.stream.XMLStreamException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.KeyException;
import java.util.*;

/**
 * @author lorenzo
 */
public class BusinessLogic extends BaseService implements IBusinessLogic {

    private static Configurations CONFIGURATIONS = new Configurations();

    private static List<String> initializedDirectoryPath = new ArrayList<String>();

    private ParerLib provider = null;
    // private Config config;
    private DocerServicesStub documentale;
    // private File tempDirectory = null;

    Logger logger = org.slf4j.LoggerFactory.getLogger(BusinessLogic.class);

    public BusinessLogic() throws VerificaDocumentoException {

        try {
            documentale = ClientManager.INSTANCE.getDocerServicesClient();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e);
        }

    }

    private String makePropertyXpath(String property) {
        return String.format("//group[@name='Configurazione']/section[@name='Parer']/%s", property);
    }

    private File getTempDir(String ente) throws VerificaDocumentoException {

        String tempDirPath;
        try {
            tempDirPath = CONFIGURATIONS.getConfiguration(ente).getConfig().getNode("//group[@name='Configurazione']/section[@name='Base']/tempDir").getText().trim();
        } catch (JaxenException e1) {
            logger.error(e1.getMessage(), e1);
            throw new VerificaDocumentoException(e1.getMessage());
        } catch (IOException e1) {
            logger.error(e1.getMessage(), e1);
            throw new VerificaDocumentoException(e1.getMessage());
        } catch (XMLStreamException e1) {
            logger.error(e1.getMessage(), e1);
            throw new VerificaDocumentoException(e1.getMessage());
        }


        File tempDirectory = new File(tempDirPath);

        if (!initializedDirectoryPath.contains(tempDirectory.getAbsolutePath())) {

            try {
                if (tempDirectory.exists()) {
                    FileUtils.deleteDirectory(tempDirectory);
                }

                FileUtils.forceMkdir(tempDirectory);

                initializedDirectoryPath.add(tempDirectory.getAbsolutePath());
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new VerificaDocumentoException(e.getMessage());
            }
        }

        return tempDirectory;

    }

    public void setProvider(ParerLib provider) {
        this.provider = provider;
    }

    public ParerLib getProvider(String ente) throws VerificaDocumentoException {

        try {
            this.provider = new ParerLib();
            this.provider.setSacerHost(CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(makePropertyXpath("sacerHost")).getText());
            this.provider.setSacerVersion(CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(makePropertyXpath("sacerVersion")).getText());
            this.provider.setDeleteAfterSend(Boolean.parseBoolean(CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(makePropertyXpath("deleteAfterSend")).getText()));
            this.provider.setUseHttps(Boolean.parseBoolean(CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(makePropertyXpath("useHttps")).getText()));

            // initTmpDir(ente);

            return this.provider;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e);
        }

    }

    public String login(String userId, String password, String codiceEnte) throws VerificaDocumentoException {

        DocerServicesStub.Login login = new DocerServicesStub.Login();
        login.setCodiceEnte(codiceEnte);
        login.setPassword(password);
        login.setUserId(userId);
        DocerServicesStub.LoginResponse loginResp;

        try {
            // eseguire il login sull'authenticationService per il retrieve del
            // token
            loginResp = ClientManager.INSTANCE.getDocerServicesClient().login(login);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException("Impossibile collegarsi al sistema documentale. " + e.getMessage());
        }

        String ticket = Utils.addTokenKey("", "uid", userId);
        ticket = Utils.addTokenKey(ticket, "documentale", loginResp.get_return());
        ticket = Utils.addTokenKey(ticket, "ente", codiceEnte);

        return ticket;

    }

    public String loginSSO(String saml, String codiceEnte) throws VerificaDocumentoException {

        UserProfile ui = extractUserProfile(saml);
        String userId = ui.getUserID();

        DocerServicesStub.LoginSSO login = new DocerServicesStub.LoginSSO();
        login.setCodiceEnte(codiceEnte);
        login.setSaml(saml);
        DocerServicesStub.LoginSSOResponse loginResp;

        try {
            // eseguire il login sull'authenticationService per il retrieve del
            // token
            loginResp = ClientManager.INSTANCE.getDocerServicesClient().loginSSO(login);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException("Impossibile collegarsi al sistema documentale. " + e.getMessage());
        }

        String ticketSSO = null;
        String uid = null;

        for (DocerServicesStub.KeyValuePair p : loginResp.get_return()) {
            if (p.getKey().equalsIgnoreCase("ticket")) {
                ticketSSO = p.getValue();
            }
            if (p.getKey().equalsIgnoreCase("uid")) {
                uid = p.getValue();
            }
        }

        if (!Strings.isNullOrEmpty(uid)) {
            userId = uid;
        }

        String ticket = Utils.addTokenKey("", "uid", userId);
        ticket = Utils.addTokenKey(ticket, "documentale", ticketSSO);
        ticket = Utils.addTokenKey(ticket, "ente", codiceEnte);

        return ticket;

    }

    public void logout(String ticket) throws VerificaDocumentoException {

    }

    public boolean writeConfig(String token, String xml) throws VerificaDocumentoException {

        String entetoken;
        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        }

        try {
            CONFIGURATIONS.writeConfig(entetoken, xml);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        }

        return true;

    }

    public String readConfig(String token) throws VerificaDocumentoException {

        String entetoken;
        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        }

        try {
            return CONFIGURATIONS.readConfig(entetoken);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        }

    }

    public String verificaDocumento(String ticket, String docId, String metadata) throws VerificaDocumentoException {

        File file = null;

        try {
            OMElement metadataXml = AXIOMUtil.stringToOM(metadata);
            validateMetadata(metadataXml);

            Set<Verifiche> verifiche = getVerifiche(metadataXml);
            boolean debug = getDebug(metadataXml);

            String filename = getFilename(ticket, docId);
            file = downloadDocument(ticket, docId, filename);

            return verificaDocumento(ticket, file, verifiche, debug);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e);
        } finally {
            if (file != null) {
                if (!FileUtils.deleteQuietly(file)) {
                    logger.error("Failed to delete file: " + file.getAbsolutePath());
                }
            }
        }
    }

    public String verificaDocumento(String ticket, String nomeFile, InputStream documento, String metadata) throws VerificaDocumentoException {
        File file = null;

        String entetoken;
        try {
            entetoken = Utils.extractTokenKey(ticket, "ente");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        }

        try {
            OMElement metadataXml = AXIOMUtil.stringToOM(metadata);
            validateMetadata(metadataXml);

            Set<Verifiche> verifiche = getVerifiche(metadataXml);

            boolean debug = getDebug(metadataXml);

            //String tempDirPath = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//group[@name='Configurazione']/section[@name='Base']/tempDir").getText().trim();

            File tempDir = getTempDir(entetoken);

            file = new File(tempDir, nomeFile);
            FileOutputStream fileStream = null;
            try {
                fileStream = new FileOutputStream(file);
                IOUtils.copy(documento, fileStream);
            } finally {
                if (fileStream != null) {
                    fileStream.close();
                }
            }

            return verificaDocumento(ticket, file, verifiche, debug);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e);
        } finally {
            if (file != null) {
                if (!FileUtils.deleteQuietly(file)) {
                    logger.error("Failed to delete file: " + file.getAbsolutePath());
                }
            }
        }
    }

    private Set<Verifiche> getVerifiche(OMElement metadata) throws JaxenException {
        AXIOMXPath xpath = new AXIOMXPath("//verifiche/verifica");
        List<?> nodes = xpath.selectNodes(metadata);

        assert nodes.size() > 0;

        Set<Verifiche> verifiche = new HashSet<Verifiche>();
        for (Object node : nodes) {
            OMElement verifica = (OMElement) node;
            if (!verifica.getText().equals("")) {
                verifiche.add(Verifiche.valueOf(verifica.getText()));
            }
        }

        return verifiche;
    }

    private boolean getDebug(OMElement metadata) throws JaxenException {
        AXIOMXPath xpath = new AXIOMXPath("//verifiche/debug");
        OMElement debug = (OMElement) xpath.selectSingleNode(metadata);
        if (debug != null) {
            String text = debug.getText();
            return text.equalsIgnoreCase("true");
        }

        return false;
    }

    public String verificaDocumento(String ticket, File documento, Set<Verifiche> verifiche, boolean debug) throws VerificaDocumentoException {

        String entetoken;
        try {
            entetoken = Utils.extractTokenKey(ticket, "ente");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        }

        try {
            FileBinario fileBinario = new FileBinario();
            fileBinario.setId(documento.getName());
            fileBinario.setInMemoria(false);
            fileBinario.setFileSuDisco(documento);
            fileBinario.setDimensione(documento.length());

            Vector<FileBinario> fileBinari = new Vector<FileBinario>();
            fileBinari.add(fileBinario);

            UnitaDocumentaria ud = new UnitaDocumentaria();

            UnitaDocumentariaConfigurazione configurazione = new UnitaDocumentariaConfigurazione();
            configurazione.setTipoConservazione(TipoConservazioneType.valueOf(getParerProperty(entetoken, "tipoConservazione")));
            configurazione.setForzaAccettazione(Boolean.parseBoolean(getParerProperty(entetoken, "forzaAccettazione")));
            configurazione.setForzaCollegamento(Boolean.parseBoolean(getParerProperty(entetoken, "forzaCollegamento")));
            configurazione.setForzaConservazione(Boolean.parseBoolean(getParerProperty(entetoken, "forzaConservazione")));
            configurazione.setSimulaSalvataggioDatiInDB(true);

            ud.setConfigurazione(configurazione);

            UnitaDocumentariaIntestazione intestazione = new UnitaDocumentariaIntestazione();
            intestazione.setVersione(CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//group[@name='Configurazione']/section[@name='Parer']/sacerVersion").getText());

            Versatore versatore = new Versatore();
            versatore.setAmbiente(getParerProperty(entetoken, "ambiente"));
            versatore.setEnte(getParerProperty(entetoken, "ente"));
            versatore.setStruttura(getParerProperty(entetoken, "struttura"));
            versatore.setUserID(getParerProperty(entetoken, "userid"));
            intestazione.setVersatore(versatore);

            Chiave chiave = new Chiave();
            chiave.setNumero(getParerProperty(entetoken, "numero"));
            chiave.setAnno(Long.parseLong(getParerProperty(entetoken, "anno")));
            chiave.setTipoRegistro(getParerProperty(entetoken, "tipoRegistro"));
            intestazione.setChiave(chiave);

            intestazione.setTipologiaUnitaDocumentaria(getParerProperty(entetoken, "tipologiaUnitaDocumentaria"));

            ud.setIntestazione(intestazione);

            DocumentoPrincipale documentoPrincipale = new DocumentoPrincipale();
            documentoPrincipale.setIDDocumento(documento.getName());
            documentoPrincipale.setTipoDocumento(getParerProperty(entetoken, "tipoDocumento"));

            DatiSpecifici datiSpecifici = new DatiSpecifici();
            String xpath = "//group[@name='Configurazione']/section[@name='ParerRichiesta']/datiSpecifici";
            OMElement configDatiSpecifici = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode(xpath);
            Iterator<?> datiSpecificiChildren = configDatiSpecifici.getChildElements();
            while (datiSpecificiChildren.hasNext()) {
                OMElement datoSpecifico = (OMElement) datiSpecificiChildren.next();

                AnyNode node = new AnyNode(AnyNode.ELEMENT, datoSpecifico.getLocalName(), null, null, null);
                node.addAnyNode(new AnyNode(AnyNode.TEXT, null, null, null, datoSpecifico.getText()));
                datiSpecifici.addAnyObject(node);
            }

            datiSpecifici.setVersioneDatiSpecifici(getParerProperty(entetoken, "versioneDatiSpecifici"));
            documentoPrincipale.setDatiSpecifici(datiSpecifici);

            StrutturaOriginale strutturaOriginale = new StrutturaOriginale();
            strutturaOriginale.setTipoStruttura(getParerProperty(entetoken, "tipoStruttura"));

            Componenti componenti = new Componenti();

            Componente componente = new Componente();
            componente.setID(documento.getName());
            componente.setNomeComponente(documento.getName());
            componente.setFormatoFileVersato(it.kdm.docer.conservazione.Utils.findExtension(documento.getName()));
            componente.setOrdinePresentazione(1);
            componente.setTipoComponente(getParerProperty(entetoken, "tipoComponente"));
            componente.setTipoSupportoComponente(TipoSupportoType.FILE);
            componente.setUtilizzoDataFirmaPerRifTemp(false);

            componenti.addComponente(componente);

            strutturaOriginale.setComponenti(componenti);

            documentoPrincipale.setStrutturaOriginale(strutturaOriginale);

            ud.setDocumentoPrincipale(documentoPrincipale);

            EsitoVersamento esito = provider.versamento(getParerProperty(entetoken, "userid"), getParerProperty(entetoken, "password"), ud, fileBinari);

            OMElement response = processEsito(esito, verifiche);

            if (debug) {
                response.addChild(buildDebugInformation(ud, esito));
            }

            StringWriter writer = new StringWriter();
            response.serialize(writer);
            String responseString = writer.toString();
            writer.close();
            return responseString;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e);
        }
    }

    private OMElement buildDebugInformation(UnitaDocumentaria ud, EsitoVersamento esito) throws XMLStreamException, MarshalException, ValidationException {

        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMElement debugInformation = factory.createOMElement("DebugInformation", null);

        OMElement request = factory.createOMElement("Request", null);

        StringWriter writer = new StringWriter();
        ud.marshal(writer);
        OMElement child = AXIOMUtil.stringToOM(writer.toString());
        request.addChild(child);

        debugInformation.addChild(request);

        OMElement response = factory.createOMElement("Response", null);

        writer = new StringWriter();
        esito.marshal(writer);
        child = AXIOMUtil.stringToOM(writer.toString());
        response.addChild(child);

        debugInformation.addChild(response);

        return debugInformation;
    }

    private OMElement processEsito(EsitoVersamento esito, Set<Verifiche> verifiche) throws MarshalException, ValidationException, IOException, XMLStreamException {
        OMFactory factory = OMAbstractFactory.getOMFactory();
        StringWriter writer = null;
        OMElement response = factory.createOMElement("EsitoVerifiche", null);
        for (Verifiche verifica : verifiche) {
            switch (verifica) {
                case FIRMA:
                    OMElement verificaFirma = factory.createOMElement("VerificaFirma", null);
                    Firmatari firmatari = esito.getUnitaDocumentaria().getDocumentoPrincipale().getComponenti().getComponente(0).getFirmatari();
                    if (firmatari != null) {
                        writer = new StringWriter();
                        firmatari.marshal(writer);
                        OMElement firmatariElement = AXIOMUtil.stringToOM(writer.toString());
                        writer.close();
                        verificaFirma.addChild(firmatariElement);
                    }
                    response.addChild(verificaFirma);
                    break;
                case FORMATO:

                    OMElement verificaFormato = factory.createOMElement("VerificaFormato", null);

                    EsitoComponente esitoComponente = esito.getUnitaDocumentaria().getDocumentoPrincipale().getComponenti().getComponente(0).getEsitoComponente();

                    OMElement verificaRiconoscimentoFormato = factory.createOMElement("VerificaRiconoscimentoFormato", null);
                    ECEsitoRicFormatoType enumVerifica = esitoComponente.getEsitoComponenteSequence().getVerificaRiconoscimentoFormato();
                    verificaRiconoscimentoFormato.setText(enumVerifica == null ? "" : enumVerifica.toString());
                    verificaFormato.addChild(verificaRiconoscimentoFormato);

                    OMElement messaggioRiconoscimentoFormato = factory.createOMElement("MessaggioRiconoscimentoFormato", null);
                    messaggioRiconoscimentoFormato.setText(esitoComponente.getEsitoComponenteSequence().getMessaggioRiconoscimentoFormato());
                    verificaFormato.addChild(messaggioRiconoscimentoFormato);

                    response.addChild(verificaFormato);
                    break;
                default:
                    break;
            }
        }

        return response;
    }

    private void validateMetadata(OMElement metadata) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        
        File schemaFile = ConfigurationUtils.loadResourceFile("metadata.xsd");       
        Schema schema = factory.newSchema(schemaFile);
        Validator validator = schema.newValidator();

        validator.validate(new OMSource(metadata));
    }

    private String getParerProperty(String ente, String property) throws VerificaDocumentoException {
        String xpath = String.format("//group[@name='Configurazione']/section[@name='ParerRichiesta']/%s", property);
        try {
            return CONFIGURATIONS.getConfiguration(ente).getConfig().getNode(xpath).getText();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        } catch (JaxenException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        }
    }

    private String getFilename(String ticket, String docId) throws RemoteException, DocerExceptionException, KeyException {
        String docTicket = "calltype:internal|";
        docTicket += Utils.extractTokenKey(ticket, "documentale");

        GetProfileDocument req = new GetProfileDocument();
        req.setToken(docTicket);
        req.setDocId(docId);
        GetProfileDocumentResponse resp = documentale.getProfileDocument(req);
        KeyValuePair[] documentData = resp.get_return();
        String filename = null;

        for (KeyValuePair pair : documentData) {
            if (pair.getKey().equalsIgnoreCase("DOCNAME")) {
                filename = pair.getValue();
                break;
            }
        }

        if (filename == null) {
            throw new IllegalStateException("Il documentale non contiene il nome del file");
        }
        return filename;
    }

    private File downloadDocument(String ticket, String docId, String filename) throws DocerExceptionException, IOException, KeyException, VerificaDocumentoException {

        String entetoken;
        try {
            entetoken = Utils.extractTokenKey(ticket, "ente");
        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw new VerificaDocumentoException(e.getMessage());
        }


        String docTicket = "calltype:internal|";
        docTicket += Utils.extractTokenKey(ticket, "documentale");

        DownloadDocument req = new DownloadDocument();
        req.setToken(docTicket);
        req.setDocId(docId);
        DownloadDocumentResponse resp = documentale.downloadDocument(req);
        StreamDescriptor desc = resp.get_return();

        File tempDir = getTempDir(entetoken);

        File f = new File(tempDir, filename);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            IOUtils.copy(desc.getHandler().getInputStream(), out);
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return f;
    }

}
