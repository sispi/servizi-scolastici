/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.fascicolazione;

import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerExceptionException;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.clients.DocerServicesStub.GetUserRights;
import it.kdm.docer.clients.DocerServicesStub.GetUserRightsAnagrafiche;
import it.kdm.docer.clients.DocerServicesStub.GetUserRightsAnagraficheResponse;
import it.kdm.docer.clients.DocerServicesStub.GetUserRightsResponse;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.commons.docerservice.BaseService;
import it.kdm.docer.commons.docerservice.BaseServiceException;
import it.kdm.docer.fascicolazione.bean.Fascicolo;
import it.kdm.docer.fascicolazione.utils.XmlUtils;
import it.kdm.docer.sdk.Constants;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.KeyValuePair;
import it.kdm.docer.sdk.classes.LoggedUserInfo;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.utils.DocerUtils;
import it.kdm.utils.ResourceLoader;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.KeyException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author lorenzo
 */
public class BusinessLogic extends BaseService implements IBusinessLogic {

    private IProvider provider;
    // private static Config config = null;
    private DocerServicesStub documentale;
    private boolean protocollato = false;

    // Comparator<KeyValuePair> keyValuePairComparator = new
    // Comparator<KeyValuePair>() {
    // public int compare(KeyValuePair o1, KeyValuePair o2) {
    // return o1.getKey().toLowerCase().compareTo(o2.getKey().toLowerCase());
    // }
    // };

    private String fascicoliSecondariFormatDefault = "classifica/anno_fascicolo/progr_fascicolo";
    private String regexpFascicoliSecondariDefault = "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
    private String fascicoliSecondariFormat = fascicoliSecondariFormatDefault;
    private String regexpFascicoliSecondari = regexpFascicoliSecondariDefault;

    static private QName format_qname = new QName("format");
    static private QName regexp_qname = new QName("regexp");
    static private QName classifica_position_qname = new QName("classifica_position");
    static private QName anno_fascicolo_position_qname = new QName("anno_fascicolo_position");
    static private QName progr_fascicolo_position_qname = new QName("progr_fascicolo_position");

    private FascicoloUtils fascicoloUtils = null;
    private String entetoken = null;

    private static Configurations CONFIGURATIONS = new Configurations();

    public BusinessLogic() throws IOException, XMLStreamException, JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        documentale = ClientManager.INSTANCE.getDocerServicesClient();
    }

    public String login(String userId, String password, String codiceEnte) throws FascicolazioneException {
        entetoken = codiceEnte;

        try {
            return baseLogin(userId, password, codiceEnte);
        } catch (BaseServiceException e) {
            throw new FascicolazioneException(e);
        }
    }

    public String loginSSO(String saml, String codiceEnte) throws FascicolazioneException {
        entetoken = codiceEnte;

        try {
            return baseLoginSSO(saml, codiceEnte);
        } catch (BaseServiceException e) {
            throw new FascicolazioneException(e);
        }
    }


    public void logout(String token) throws FascicolazioneException {

        provider.logout(token);
    }


    public boolean changeFascicoli(String token, long docNum,
                                   KeyValuePair[][] fascicoliToRemove,
                                   KeyValuePair[][] fascicoliToAdd) throws FascicolazioneException {

        DocerServicesStub.KeyValuePair[] metadatiDOC;
        String docTicket = "calltype:internal|";
        try {
            entetoken = Utils.extractTokenKey(token, "ente");
            initFascicoloUtils();
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new FascicolazioneException(e1.getMessage());
        }

        try {
            docTicket += Utils.extractTokenKey(token, "documentale");
            DocerServicesStub.GetProfileDocument doc = new DocerServicesStub.GetProfileDocument();
            doc.setDocId(String.valueOf(docNum));
            doc.setToken(docTicket);
            DocerServicesStub.GetProfileDocumentResponse resp = documentale.getProfileDocument(doc);
            metadatiDOC = resp.get_return();

            boolean ret = true;
            if (fascicoliToRemove.length > 0) {
                ret = removeFascicoli(token, docNum, fascicoliToRemove, metadatiDOC);
            }

            if (!ret) {
                return ret;
            }

            if (fascicoliToAdd.length > 0) {
                // Bisogna aggiornare lo stato del documento altrimenti la chiamata precedente viene annullata
                resp = documentale.getProfileDocument(doc);
                metadatiDOC = resp.get_return();

                 ret &= addFascicoli(token, docNum, fascicoliToAdd, metadatiDOC);
            }

            return ret;

        } catch (Exception e) {
            throw new FascicolazioneException(e);
        }
    }

    private Boolean removeFascicoli(String token, long docNum,
                                    KeyValuePair[][] fascicoli,
                                    DocerServicesStub.KeyValuePair[] metadatiDOC)
            throws FascicolazioneException {

        String ente = null;
        String aoo = null;
        String fascSecondariOnDocument = null;
        String progressivoFascicoloPrimario = null;
        String classificaPrimario = null;
        String annoPrimario = null;

        List<Fascicolo> fascicoliToAdd = new ArrayList<Fascicolo>();

        try {
            // getProfileDocument del principale
            fascicoliToAdd = creaListaFascicoliDaRimuovere(metadatiDOC, fascicoli);

            for (DocerServicesStub.KeyValuePair pair : metadatiDOC) {
                if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                    ente = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                    aoo = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("FASC_SECONDARI")) {
                    fascSecondariOnDocument = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                    progressivoFascicoloPrimario = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                    classificaPrimario = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                    annoPrimario = pair.getValue();
                    continue;
                }
            }


            Element element = null;
            if (fascicoliToAdd.size() > 0) {
                element = XmlUtils.createXml();
                if (progressivoFascicoloPrimario == null || "".equals(progressivoFascicoloPrimario)) {
                    XmlUtils.createPrimarioXml(element, fascicoliToAdd.get(0));
                    fascicoliToAdd.remove(0);
                } else {
                    Fascicolo fascicolo = new Fascicolo();
                    fascicolo.setEnte(ente);
                    fascicolo.setAnno(annoPrimario);
                    fascicolo.setProgressivo(progressivoFascicoloPrimario);
                    fascicolo.setClassifica(classificaPrimario);
                    fascicolo.setAoo(aoo);
                    if (fascicoliToAdd.contains(fascicolo)) {
                        XmlUtils.createPrimarioXml(element, fascicolo);
                        fascicoliToAdd.remove(fascicolo);
                    } else {
                        XmlUtils.createPrimarioXml(element, fascicoliToAdd.get(0));
                        fascicoliToAdd.remove(0);
                    }

                }
                //aggiungo eventuali secondari
                if (fascicoliToAdd.size() == 0) {
                    XmlUtils.createSecondarioXmlToRemoveAll(element);
                } else {
                    XmlUtils.createSecondarioXml(element, fascicoliToAdd);
                }
                String xmlOutputParsed = element.asXML().replace("<SKIP>", "").replace("</SKIP>", "");
                fascicola(token, docNum, xmlOutputParsed);
                return true;
            } else {
                element = XmlUtils.createXml();
                Fascicolo fascicolo = new Fascicolo();
                fascicolo.setEnte(ente);
                fascicolo.setClassifica(classificaPrimario);
                fascicolo.setAoo(aoo);
                fascicolo.setAnno(annoPrimario);
                XmlUtils.createPrimarioXmlSfascicola(element, fascicolo);
                String xmlOutputParsed = element.asXML().replace("<SKIP>", "").replace("</SKIP>", "");

                fascicola(token, docNum, xmlOutputParsed);
                //XmlUtils.createPrimarioXml(element,fascicolo);
                return true;
            }


        } catch (Exception e) {
            throw new FascicolazioneException(e);
        }


    }

    private Boolean addFascicoli(String token, long docNum,
                                 KeyValuePair[][] fascicoli,
                                 DocerServicesStub.KeyValuePair[] metadatiDOC)
            throws FascicolazioneException {

        String ente = null;
        String aoo = null;
        String fascSecondariOnDocument = null;
        String progressivoFascicoloPrimario = null;
        String classificaPrimario = null;
        String annoPrimario = null;
        String docTicket = "calltype:internal|";

        List<Fascicolo> fascicoliToAdd = new ArrayList<Fascicolo>();

        try {

            fascicoliToAdd = creaListaFascicoli(metadatiDOC, fascicoli);

            for (DocerServicesStub.KeyValuePair pair : metadatiDOC) {
                if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                    ente = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                    aoo = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("FASC_SECONDARI")) {
                    fascSecondariOnDocument = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                    progressivoFascicoloPrimario = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                    classificaPrimario = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                    annoPrimario = pair.getValue();
                    continue;
                }

            }

            Element element = null;
            if (fascicoliToAdd.size() > 0) {
                element = XmlUtils.createXml();
                if (progressivoFascicoloPrimario == null || "".equals(progressivoFascicoloPrimario)) {
                    XmlUtils.createPrimarioXml(element, fascicoliToAdd.get(0));
                    fascicoliToAdd.remove(0);
                } else {
                    Fascicolo fascicolo = new Fascicolo();
                    fascicolo.setEnte(ente);
                    fascicolo.setAnno(annoPrimario);
                    fascicolo.setProgressivo(progressivoFascicoloPrimario);
                    fascicolo.setClassifica(classificaPrimario);
                    fascicolo.setAoo(aoo);
                    XmlUtils.createPrimarioXml(element, fascicolo);
                    fascicoliToAdd.remove(fascicolo);
                }
                //aggiungo eventuali secondari
                XmlUtils.createSecondarioXml(element, fascicoliToAdd);
                String xmlOutputParsed = element.asXML().replace("<SKIP>", "").replace("</SKIP>", "");
                fascicola(token, docNum, xmlOutputParsed);
                return true;
            } else {
                return false;
            }


        } catch (Exception e) {
            throw new FascicolazioneException(e);
        }

    }


    private List<Fascicolo> creaListaFascicoliDaRimuovere(DocerServicesStub.KeyValuePair[] metadatiDOC, KeyValuePair[][] fascicoli) throws Exception {
        List<Fascicolo> fascicoliToAdd = new ArrayList<Fascicolo>();
        String secondari = "";
        String ente = "";
        String aoo = "";
        String progressivoFascicoloPrimario = "";
        String classificaPrimario = "";
        String annoPrimario = "";

        for (DocerServicesStub.KeyValuePair pair : metadatiDOC) {
            if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                ente = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                aoo = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("FASC_SECONDARI")) {
                secondari = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                progressivoFascicoloPrimario = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                classificaPrimario = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                annoPrimario = pair.getValue();
                continue;
            }

        }
        //String contenente tutti i fascicoli del documento
        String secFasc = "";

        if (progressivoFascicoloPrimario != null && !"".equals(progressivoFascicoloPrimario)) {
            secFasc = secondari + ";" + fascicoloUtils.toFascicoloSecondarioString(classificaPrimario, annoPrimario, progressivoFascicoloPrimario);
        }

        for (int i = 0; i < fascicoli.length; i++) {
            boolean isDuplicate = false;
            KeyValuePair[] kvpf = fascicoli[i];
            if (kvpf.length > 0) {
                String annoInput = null;
                String enteInput = null;
                String classificaInput = null;
                String aooInput = null;
                String progressivoInput = null;
                for (KeyValuePair pair : kvpf) {

                    if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                        annoInput = pair.getValue();
                    }
                    if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                        classificaInput = pair.getValue();
                    }
                    if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                        progressivoInput = pair.getValue();
                    }
                    if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                        aooInput = pair.getValue();
                    }
                    if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                        enteInput = pair.getValue();
                    }
                }

                //controllo ENTE AOO
                if (!ente.equals(enteInput) || !aoo.equals(aooInput)) {
                    throw new Exception("Ente e Aoo errati ente Documento:" + ente + " Aoo Documento:" + aoo);
                }

                //controllo consistenza dati input
                if ((annoInput == null || "".equals(annoInput)) || (classificaInput == null || "".equals(classificaInput))
                        || (progressivoInput == null || "".equals(progressivoInput))) {
                    throw new Exception("Dati input errati ");
                }

                String tempSec = ";" + fascicoloUtils.toFascicoloSecondarioString(classificaInput, annoInput, progressivoInput) + ";";

                String tempSecFas = ";" + secFasc + ";";

                if (tempSecFas.contains(tempSec)) {
                    secFasc = StringUtils.replace(tempSecFas, tempSec, ";");
                } else {
                    throw new Exception("Richiesta di rimozione di un fascicolo non presente nel documento: fascicolo " + enteInput + " " + aooInput + " " + classificaInput + " " + annoInput + " " + progressivoInput);
                }
            }
        }

        String listFasc[] = StringUtils.split(secFasc, ";");

        for (String tmp : listFasc) {
            String apc[] = fascicoloUtils.parseFascicoloString(tmp);
            Fascicolo fascicolo = new Fascicolo();
            fascicolo.setAnno(apc[1]);
            fascicolo.setAoo(aoo);
            fascicolo.setClassifica(apc[0]);
            fascicolo.setEnte(ente);
            fascicolo.setProgressivo(apc[2]);
            fascicoliToAdd.add(fascicolo);
        }


        return fascicoliToAdd;


    }


    private List<Fascicolo> creaListaFascicoli(DocerServicesStub.KeyValuePair[] metadatiDOC, KeyValuePair[][] fascicoli) throws Exception {
        List<Fascicolo> fascicoliToAdd = new ArrayList<Fascicolo>();
        String secondari = "";
        String ente = "";
        String aoo = "";
        String progressivoFascicoloPrimario = "";
        String classificaPrimario = "";
        String annoPrimario = "";

        for (DocerServicesStub.KeyValuePair pair : metadatiDOC) {
            if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                ente = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                aoo = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("FASC_SECONDARI")) {
                secondari = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                progressivoFascicoloPrimario = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                classificaPrimario = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                annoPrimario = pair.getValue();
                continue;
            }

        }
        //String contenente titti i fascicoli del documento
        String secFasc = "";

        if (progressivoFascicoloPrimario != null && !"".equals(progressivoFascicoloPrimario)) {
            secFasc = secondari + ";" + fascicoloUtils.toFascicoloSecondarioString(classificaPrimario, annoPrimario, progressivoFascicoloPrimario);
        }

        for (int i = 0; i < fascicoli.length; i++) {
            boolean isDuplicate = false;
            KeyValuePair[] kvpf = fascicoli[i];
            if (kvpf.length > 0) {
                String annoInput = null;
                String enteInput = null;
                String classificaInput = null;
                String aooInput = null;
                String progressivoInput = null;
                for (KeyValuePair pair : kvpf) {

                    if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                        annoInput = pair.getValue();
                    }
                    if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                        classificaInput = pair.getValue();
                    }
                    if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                        progressivoInput = pair.getValue();
                    }
                    if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                        aooInput = pair.getValue();
                    }
                    if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                        enteInput = pair.getValue();
                    }
                }


                //controllo ENTE AOO non ci devono essere in input

                if (!ente.equals(enteInput) || !aoo.equals(aooInput)) {
                    throw new Exception("Ente e Aoo errati ente Documento:" + ente + " Aoo Documento:" + aoo);
                }


                //controllo consistenza dati input
                if ((annoInput == null || "".equals(annoInput)) || (classificaInput == null || "".equals(classificaInput))
                        || (progressivoInput == null || "".equals(progressivoInput))) {
                    throw new Exception("Dati input errati ");
                }

                String tempSec = ";" + fascicoloUtils.toFascicoloSecondarioString(classificaInput, annoInput, progressivoInput) + ";";


                String tempSecFas = ";" + secFasc + ";";

                if (!tempSecFas.contains(tempSec)) {
                    secFasc += ";" + tempSec;
                }
            }
        }

        String listFasc[] = StringUtils.split(secFasc, ";");

        for (String tmp : listFasc) {
            String apc[] = fascicoloUtils.parseFascicoloString(tmp);
            Fascicolo fascicolo = new Fascicolo();
            fascicolo.setAnno(apc[1]);
            fascicolo.setAoo(aoo);
            fascicolo.setClassifica(apc[0]);
            fascicolo.setEnte(ente);
            fascicolo.setProgressivo(apc[2]);
            fascicoliToAdd.add(fascicolo);
        }


        return fascicoliToAdd;


    }


    public String fascicola(String ticket, long documentId, String datiFascicolo) throws FascicolazioneException {

        try {
            entetoken = Utils.extractTokenKey(ticket, "ente");
            initFascicoloUtils();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new FascicolazioneException(e1.getMessage());
        }

        String userId;
        String providerData;
        String docTicket = "calltype:internal|";
        String ente = "";
        String aoo = "";
        String classifica = "";
        String annoFascicolo = "";
        String progrFascicolo = "";
        String fascSecondari = "";

        XMLUtil dom = null;

        DocerServicesStub.KeyValuePair[] metadatiDOC;

        try {
            dom = new XMLUtil(datiFascicolo);


            // validazione del formato - Vecchia gestione
        /*
	     * File schema =
	     * ResourceLoader.openFile(this.getClass().getResource(
	     * "/input-validation.xsd"));
	     * dom.validate(schema.getAbsolutePath());
	     */

            docTicket += Utils.extractTokenKey(ticket, "documentale");

            // getProfileDocument del principale
            DocerServicesStub.GetProfileDocument doc = new DocerServicesStub.GetProfileDocument();

            doc.setDocId(String.valueOf(documentId));
            doc.setToken(docTicket);
            DocerServicesStub.GetProfileDocumentResponse resp = documentale.getProfileDocument(doc);
            metadatiDOC = resp.get_return();

            // TipoComponente -> ALLEGATO, ANNESSO, ANNOTAZIONE

            for (DocerServicesStub.KeyValuePair pair : metadatiDOC) {
                if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                    ente = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                    aoo = pair.getValue();
                    continue;
                }
                // if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                // classifica = pair.getValue();
                // continue;
                // }
                // if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                // annoFascicolo = pair.getValue();
                // continue;
                // }
                // if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                // progrFascicolo = pair.getValue();
                // continue;
                // }
                // if (pair.getKey().equalsIgnoreCase("FASC_SECONDARI")) {
                // fascSecondari = pair.getValue();
                // continue;
                // }
            }

            // Validazione del formato - Nuova gestione
            String xsdFileName = getProviderXsdAttribute(ente, aoo, "xsd"); // "/input-validation.xsd"
            String resource = String.format("/%s", xsdFileName);
            
            File schema = ConfigurationUtils.loadResourceFile(xsdFileName);
            dom.validate(schema.getAbsolutePath());

        } catch (Exception e) {
            throw new FascicolazioneException(e);
        }

        String keyProvider = "Provider_" + ente + "_" + aoo;

        ILoggedUserInfo currentUser = new LoggedUserInfo();

        try {
            userId = Utils.extractTokenKey(ticket, "uid");
            // library = Utils.extractTokenKey(ticket, "lib");
            providerData = Utils.extractTokenKey(ticket, keyProvider);
            // token = Utils.extractTokenKey(ticket, "token");
            currentUser.setCodiceEnte(ente);
            currentUser.setTicket(providerData);
            currentUser.setUserId(userId);

            this.provider = getProvider(ente, aoo);

            this.provider.setCurrentUser(currentUser);
        } catch (Exception e) {
            throw new FascicolazioneException("Ticket non valido o provider per ENTE: " + ente + " AOO: " + aoo + " non disponibile. " + e.getMessage());
        }

        String tipoRichiesta = "";
        String firma = "";

        try {

            // ********************************************************************************************
            // Controlli preventivi su parametri XML di input
            // ********************************************************************************************

	    /*
	     * NON SERVONO PIU'???????????????
	     * 
	     * tipoRichiesta = dom.getNodeValue("//Flusso/TipoRichiesta"); firma
	     * = dom.getNodeValue("//Flusso/Firma");
	     * 
	     * if (!tipoRichiesta.equalsIgnoreCase("F")){ throw new
	     * FascicolazioneException("Valore 'TipoRichiesta' non valido."); }
	     * 
	     * if (!firma.equalsIgnoreCase("FD") &&
	     * !firma.equalsIgnoreCase("FE") && !firma.equalsIgnoreCase("F") &&
	     * !firma.equalsIgnoreCase("NF")) { throw new
	     * FascicolazioneException("Valore 'Firma' non valido."); }
	     */
            // ********************************************************************************************

            // ********************************************************************************************

            // ho trovato il documento quindi ho almeno readOnly
            // ora verifico che abbia almeno normalAccess
            checkUserRights(documentId, docTicket, userId, EnumACLRights.normalAccess);

            List<String> fascicoli_delta = getFascicoliDelta(userId, docTicket, metadatiDOC, dom);

            // controllo i diritti sui fascicoli da aggiungere o da rimuovere
            // (almeno normalAccess)
            for (String f : fascicoli_delta) {

                DocerServicesStub.KeyValuePair[] kvp = fascicoloUtils.toDocerKeyValuePairArray(ente, aoo, f);

                checkUserRights(kvp, docTicket, userId, EnumACLRights.normalAccess, "FASCICOLO");
            }

            // ********************************************************************************************
            String xmlRet = "";
            boolean fascicolazioneInterna = getFascicolazioneInterna(ente, aoo);
            String fsecStr = "";

            // ***Recupero metadati e file dei documenti (principale, allegati ,
            // annessi e annotazioni)
            OMElement nodeToAdd = getExtendedNode(docTicket, String.valueOf(documentId));

            // fascicolazioneInterna=true significa che Doc/er fascicola i non
            // protocollati
            // (caso di protocolli qualificati che non supportano fascicolazione
            // dei non protocollati
            // In generale, quando il documento e' protocollato la
            // fascicolazione la fa sempre il protocollo
            // (requisito obbligatorio di qualificazione)
            if (protocollato == true || fascicolazioneInterna == false) {

                // OMElement root = dom.getNode("/Segnatura");
                // root.addChild(nodeToAdd);

                dom.addNode("/Segnatura", nodeToAdd);

                String datiFascicoloFull = dom.toXML();

                xmlRet = this.provider.fascicola(datiFascicoloFull);

                // controllare solo esito (se OK formatta output e return
                XMLUtil util = new XMLUtil(xmlRet);

                String esito = util.getNodeValue("/esito/codice");
                String errDescription = util.getNodeValue("/esito/descrizione");

                if (!esito.equalsIgnoreCase("0"))
                    throw new FascicolazioneException(errDescription);
            }

            String errorMessage = "Impossibile aggiornare il documento con i dati di fascicolo. ";
            String esito = "0";

            // aggiorno i metadati di fascicolazione solo se cambiano
            if (fascicoli_delta.size() > 0) {
                // Altrimenti e' fascicolazione interna
                DocerServicesStub.FascicolaDocumento protDoc = new DocerServicesStub.FascicolaDocumento();

                DocerServicesStub.KeyValuePair[] params = null;
                OMElement fPrimario = dom.getNode("//Intestazione/FascicoloPrimario");
                if (fPrimario != null) {

                    params = buildFascMeta(fPrimario);
                    protDoc.setMetadata(params);
                }

                List<OMElement> fSecondari = dom.getNodes("//Intestazione/FascicoliSecondari");

                if (fSecondari.size() > 0) {
                    it.kdm.docer.clients.DocerServicesStub.KeyValuePair fSecMeta = new it.kdm.docer.clients.DocerServicesStub.KeyValuePair();
                    fSecMeta.setKey(Constants.doc_fascicoli_secondari);
                    fSecMeta.setValue("");
                    protDoc.addMetadata(fSecMeta);
                }

                fSecondari = dom.getNodes("//Intestazione/FascicoliSecondari/FascicoloSecondario");

                if (fSecondari.size() > 0) {
                    fsecStr = buildFascSecondari(fSecondari);
                    it.kdm.docer.clients.DocerServicesStub.KeyValuePair fSecMeta = new it.kdm.docer.clients.DocerServicesStub.KeyValuePair();
                    fSecMeta.setKey(Constants.doc_fascicoli_secondari);
                    fSecMeta.setValue(fsecStr);
                    protDoc.addMetadata(fSecMeta);
                }

                try {
                    // Aggiornamento dei metadati di fascicolazione sul
                    // documentale
                    protDoc.setDocId(String.valueOf(documentId));
                    protDoc.setToken(docTicket);

                    documentale.fascicolaDocumento(protDoc);
                } catch (Exception e) {
                    // errorMessage+=e.getMessage();
                    // esito="1";
                    throw new FascicolazioneException("Impossibile aggiornare il documento in Doc/er con i metadati di fascicolazione. " + e.getMessage());
                }
            }

            OMFactory factory = OMAbstractFactory.getOMFactory();
            OMElement docItem = factory.createOMElement("esito", null);

            OMElement metaItem = factory.createOMElement("codice", null);
            metaItem.setText(esito);
            docItem.addChild(metaItem);

            metaItem = factory.createOMElement("descrizione", null);
            if (!esito.equalsIgnoreCase("0")) {
                metaItem.setText(errorMessage);
            }
            docItem.addChild(metaItem);

            return docItem.toString();

	    /*
	     * //carica ilformato xml di risposta //InputStream resStream =
	     * this.getClass().getResourceAsStream("/providerResponse.xml");
	     * //String xml = IOUtils.toString(resStream); dom = new
	     * XMLUtil(xmlRet);
	     * 
	     * fPrimario = dom.getNode("//FascicoloPrimario");
	     * dom.addNode("/esito/dati_fascicolo", fPrimario);
	     * 
	     * fSecondari = dom.getNodes("//FascicoloSecondario");
	     * 
	     * for(OMElement fs:fSecondari){
	     * dom.addNode("/esito/dati_fascicolo", fs); }
	     * 
	     * return dom.toXML();
	     */

        } catch (Exception e) {
            throw new FascicolazioneException(e);
        }

    }

    public String creaFascicolo(String ticket, KeyValuePair[] metadati, boolean forzaNuovoFascicolo) throws FascicolazioneException {

        try {
            entetoken = Utils.extractTokenKey(ticket, "ente");
            initFascicoloUtils();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new FascicolazioneException(e1.getMessage());
        }

        String userId;
        String providerData;
        String docTicket = "calltype:internal|";

        // Arrays.sort(metadati, keyValuePairComparator);

        String ente = null;
        String aoo = null;
        String classifica = null;
        String progr_fascicolo = null;
        String anno_fascicolo = null;
        String parent_progr_fascicolo = null;

        for (KeyValuePair pair : metadati) {

            if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                ente = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                aoo = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                classifica = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                anno_fascicolo = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                progr_fascicolo = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("PARENT_PROGR_FASCICOLO")) {
                parent_progr_fascicolo = pair.getValue();
                continue;
            }
        }

        if (ente == null || ente.equals("") || aoo == null || aoo.equals(""))
            throw new FascicolazioneException("Codice ENTE e/o codice AOO non validi.");

        String keyProvider = "Provider_" + ente + "_" + aoo;

        ILoggedUserInfo currentUser = new LoggedUserInfo();

        try {
            userId = Utils.extractTokenKey(ticket, "uid");
            // library = Utils.extractTokenKey(ticket, "lib");
            providerData = Utils.extractTokenKey(ticket, keyProvider);
            docTicket += Utils.extractTokenKey(ticket, "documentale");
            currentUser.setCodiceEnte(ente);
            currentUser.setTicket(providerData);
            currentUser.setUserId(userId);

            this.provider = getProvider(ente, aoo);
            this.provider.setCurrentUser(currentUser);
        } catch (Exception e) {
            throw new FascicolazioneException("Ticket non valido o provider per ENTE: " + ente + " AOO: " + aoo + " non disponibile. " + e.getMessage());
        }

        String retProv = "";

        // se e' un sottofascicolo controllo il padre
        if (StringUtils.isNotEmpty(parent_progr_fascicolo)) {
            if (StringUtils.isEmpty(anno_fascicolo)) {
                throw new FascicolazioneException("ANNO_FASCICOLO obbligatorio per individuare il Fascicolo padre");
            }

            KeyValuePair[] parentFascicoloId = new KeyValuePair[5];
            parentFascicoloId[0] = new KeyValuePair("COD_ENTE", ente);
            parentFascicoloId[1] = new KeyValuePair("COD_AOO", aoo);
            parentFascicoloId[2] = new KeyValuePair("CLASSIFICA", classifica);
            parentFascicoloId[3] = new KeyValuePair("ANNO_FASCICOLO", anno_fascicolo);
            parentFascicoloId[4] = new KeyValuePair("PROGR_FASCICOLO", parent_progr_fascicolo);

            // controllo diritti sul padre
            checkUserRights(parentFascicoloId, docTicket, userId, EnumACLRights.normalAccess, "FASCICOLO");
        } else {

            KeyValuePair[] classificaId = new KeyValuePair[3];
            classificaId[0] = new KeyValuePair("COD_ENTE", ente);
            classificaId[1] = new KeyValuePair("COD_AOO", aoo);
            classificaId[2] = new KeyValuePair("CLASSIFICA", classifica);

            // controllo diritti sulla voce di titolario
            checkUserRights(classificaId, docTicket, userId, EnumACLRights.normalAccess, "voce di titolario");
        }

        try {
            if (forzaNuovoFascicolo == true)
                retProv = provider.forzaNuovoFascicolo(metadati);
            else
                retProv = provider.creaFascicolo(metadati);

            XMLUtil dom;
            try {
                dom = new XMLUtil(retProv);
            } catch (IOException ex) {
                throw new FascicolazioneException(ex);
            } catch (XMLStreamException ex) {
                throw new FascicolazioneException(ex);
            }

            OMFactory omFactory = OMAbstractFactory.getOMFactory();

            for (KeyValuePair meta : metadati) {
                OMElement node = dom.getNode(String.format("//esito_fascicolo/%s", meta.getKey()));
                if (node == null) {
                    OMElement newElement = omFactory.createOMElement(new QName(null, meta.getKey()));
                    newElement.setText(meta.getValue());
                    dom.addDocumentNode("//esito_fascicolo", newElement);
                }
            }

            String errorMessage = "Errore durante l'allineamento di Doc/er: ";
            String esito = "0";

            DocerServicesStub.CreateFascicolo create = new DocerServicesStub.CreateFascicolo();

            try {

                List<OMElement> nodes = dom.getNodes("//esito_fascicolo/*");

                for (OMElement item : nodes) {
                    create.addFascicoloInfo(createPair(item.getLocalName(), item.getText()));
                }

                create.setToken(docTicket);
                documentale.createFascicolo(create);
            } catch (Exception e) {
                errorMessage += e.getMessage();
                throw new FascicolazioneException(errorMessage);
            }

            OMFactory factory = OMAbstractFactory.getOMFactory();
            OMElement docItem = factory.createOMElement("esito", null);

            OMElement metaItem = factory.createOMElement("codice", null);
            metaItem.setText(esito);
            docItem.addChild(metaItem);

            metaItem = factory.createOMElement("descrizione", null);

            if (!esito.equalsIgnoreCase("0")) {
                metaItem.setText(errorMessage);
            }

            docItem.addChild(metaItem);

            OMElement elem = dom.getNode("//esito_fascicolo");
            docItem.addChild(elem.detach());

            return docItem.toString();
        } catch (Exception e) {
            throw new FascicolazioneException("Impossibile creare il fascicolo. " + e.getMessage());
        }
    }

    public boolean writeConfig(String token, String xml) throws FascicolazioneException {

        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            e.printStackTrace();
            throw new FascicolazioneException(e.getMessage());
        }

        try {
            CONFIGURATIONS.writeConfig(entetoken, xml);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new FascicolazioneException("Configurazione malformata");
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new FascicolazioneException("Configurazione malformata");
        }

        return true;

    }

    public String readConfig(String token) throws FascicolazioneException {

        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            e.printStackTrace();
            throw new FascicolazioneException(e.getMessage());
        }

        try {
            return CONFIGURATIONS.readConfig(entetoken);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new FascicolazioneException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new FascicolazioneException(e.getMessage());
        }

    }

    public String forzaNuovoFascicolo(String token, KeyValuePair[] metadati) throws FascicolazioneException {

        return this.creaFascicolo(token, metadati, true);
    }

    public boolean updateACLFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] acl) throws FascicolazioneException {

        try {
            entetoken = Utils.extractTokenKey(token, "ente");
            initFascicoloUtils();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new FascicolazioneException(e1.getMessage());
        }

        String userId;
        String providerData;
        String docTicket = "calltype:internal|";
        String ente = "";
        String aoo = "";

        try {
            for (KeyValuePair pair : fascicoloid) {
                if (pair.getKey().equals("COD_ENTE"))
                    ente = pair.getValue();

                if (pair.getKey().equals("COD_AOO"))
                    aoo = pair.getValue();
            }

        } catch (Exception e) {
            throw new FascicolazioneException("Codice ENTE e/o codice AOO non validi.");
        }

        String keyProvider = "Provider_" + ente + "_" + aoo;

        ILoggedUserInfo currentUser = new LoggedUserInfo();

        try {
            userId = Utils.extractTokenKey(token, "uid");
            providerData = Utils.extractTokenKey(token, keyProvider);
            docTicket += Utils.extractTokenKey(token, "documentale");

            currentUser.setCodiceEnte(ente);
            currentUser.setTicket(providerData);
            currentUser.setUserId(userId);

            this.provider = getProvider(ente, aoo);

            this.provider.setCurrentUser(currentUser);
        } catch (Exception e) {
            throw new FascicolazioneException("Ticket non valido o provider per ENTE: " + ente + " AOO: " + aoo + " non disponibile. " + e.getMessage());
        }

        try {
            //if (acl.length > 0) { MODIFICA: devo anche poter settare acl vuote
            if (acl != null) {
                DocerServicesStub.SetACLFascicolo aclFasc = new DocerServicesStub.SetACLFascicolo();
                aclFasc.setToken(docTicket);
                aclFasc.setFascicoloId(convertMeta(fascicoloid));
                DocerServicesStub.KeyValuePair[] aclCall = convertMeta(acl);
                //if (aclCall.length > 0) {

                // per modificare le ACL devo avere fullAccess
                checkUserRights(fascicoloid, docTicket, userId, EnumACLRights.fullAccess, "FASCICOLO");

                try {
                    this.provider.updateACLFascicolo(token, fascicoloid, acl);
                } catch (Exception e) {
                    throw new FascicolazioneException(e);
                }

                aclFasc.setAcls(aclCall);
                documentale.setACLFascicolo(aclFasc);
                //}
            }
        } catch (Exception e) {
            throw new FascicolazioneException(e);
        }

        return true;
    }

    public String updateFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair[] metadati) throws FascicolazioneException {

        try {
            entetoken = Utils.extractTokenKey(token, "ente");
            initFascicoloUtils();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new FascicolazioneException(e1.getMessage());
        }

        String userId;
        String providerData;
        String docTicket = "calltype:internal|";
        String ente = "";
        String aoo = "";
        String progFasc = "";

        try {
            for (KeyValuePair pair : fascicoloid) {
                if (pair.getKey().equals("COD_ENTE"))
                    ente = pair.getValue();

                if (pair.getKey().equals("COD_AOO"))
                    aoo = pair.getValue();

                if (pair.getKey().equals("PROGR_FASCICOLO"))
                    progFasc = pair.getValue();
            }

        } catch (Exception e) {
            throw new FascicolazioneException("Codice ENTE e/o codice AOO non validi.");
        }

        String keyProvider = "Provider_" + ente + "_" + aoo;

        ILoggedUserInfo currentUser = new LoggedUserInfo();

        try {
            userId = Utils.extractTokenKey(token, "uid");
            providerData = Utils.extractTokenKey(token, keyProvider);
            docTicket += Utils.extractTokenKey(token, "documentale");

            currentUser.setCodiceEnte(ente);
            currentUser.setTicket(providerData);
            currentUser.setUserId(userId);

            this.provider = getProvider(ente, aoo);

            this.provider.setCurrentUser(currentUser);
        } catch (Exception e) {
            throw new FascicolazioneException("Ticket non valido o provider per ENTE: " + ente + " AOO: " + aoo + " non disponibile. " + e.getMessage());
        }

        try {

            // controllo che l'utente abbia almeno normalAccess sul Fascicolo
            // documentale per modificarlo
            checkUserRights(fascicoloid, docTicket, userId, EnumACLRights.normalAccess, "FASCICOLO");

            DocerServicesStub.UpdateFascicolo updateFasc = new DocerServicesStub.UpdateFascicolo();
            updateFasc.setToken(docTicket);
            updateFasc.setFascicoloId(convertMeta(fascicoloid));
            updateFasc.setFascicoloInfo(convertMeta(metadati));

            try {
                this.provider.updateFascicolo(token, fascicoloid, metadati);
            } catch (Exception e) {
                throw new FascicolazioneException(e);
            }

            documentale.updateFascicolo(updateFasc);

        } catch (Exception e) {
            throw new FascicolazioneException(e);
        }

        return progFasc;
    }

    // Fascicolazione xsd custom: NUOVA con errori parlanti
    private String getProviderXsdAttribute(String ente, String aoo, String attributeName) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            FascicolazioneException, DocerException {

        String providerAttribute = "";
        String defaultXsdFile = "";

        OMElement ome1;
        try {
            ome1 = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//section[@name='Providers']");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new FascicolazioneException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new FascicolazioneException(e.getMessage());
        }
        if (ome1 == null) {
            throw new FascicolazioneException("xpath //section[@name='Providers'] non trovato nel file di configurazione.");
        }

        defaultXsdFile = ome1.getAttributeValue(new QName("default-xsd"));
        if (defaultXsdFile == null) {
            throw new FascicolazioneException("Impossibile trovare l'attributo 'default-xsd' del nodo 'Providers' nel file di configurazione.");
        }

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);

        OMElement ome2;
        try {
            ome2 = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode(xpathProvider);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new FascicolazioneException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new FascicolazioneException(e.getMessage());
        }
        if (ome2 == null) {
            throw new FascicolazioneException("provider non definito per ente=" + ente + " e aoo=" + aoo);
        }

        providerAttribute = ome2.getAttributeValue(new QName(attributeName));
        if (providerAttribute == null) {
            return defaultXsdFile;
        }

        return providerAttribute;
    }

    private String getValueFromKey(DocerServicesStub.KeyValuePair[] profile, String key) {
        for (DocerServicesStub.KeyValuePair kvp : profile) {
            if (kvp.getKey().equalsIgnoreCase(key)) {
                return kvp.getValue();
            }
        }

        return null;
    }

    // private String getValue(KeyValuePair[] metadati, String key) {
    // KeyValuePair searchKey = new KeyValuePair();
    // searchKey.setKey(key);
    // int index = Arrays.binarySearch(metadati, searchKey,
    // keyValuePairComparator);
    // if (index >= 0) {
    // return metadati[index].getValue();
    // }
    //
    // return null;
    // }

    private it.kdm.docer.clients.DocerServicesStub.KeyValuePair createPair(String key, String value) {
        it.kdm.docer.clients.DocerServicesStub.KeyValuePair pair = new it.kdm.docer.clients.DocerServicesStub.KeyValuePair();
        pair.setKey(key);
        pair.setValue(value);
        return pair;
    }

    private boolean getFascicolazioneInterna(String ente, String aoo) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, FascicolazioneException {

        String fascicolazioneInterna = "";

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
        try {
            OMElement providerNode = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode(xpathProvider);

            fascicolazioneInterna = providerNode.getAttributeValue(new QName("fascicolazioneInterna"));
            return Boolean.parseBoolean(fascicolazioneInterna);
        } catch (Exception e) {
            throw new FascicolazioneException("Flag 'fascicolazioneInterna' non valido sul provider per ENTE: " + ente + " e AOO: " + aoo + " nel file di configurazione. " + e.getMessage());
        }

    }

    private IProvider getProvider(String ente, String aoo) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, FascicolazioneException {

        String providerClassName = "";

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
        try {
            providerClassName = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode(xpathProvider).getText();
        } catch (Exception e) {
            throw new FascicolazioneException("Impossibile trovare il provider per ENTE: " + ente + " e AOO: " + aoo + " nel file di configurazione. " + e.getMessage());
        }

        IProvider prov = (IProvider) Class.forName(providerClassName).newInstance();

        return prov;
    }

    private DocerServicesStub.KeyValuePair[] buildFascMeta(OMElement fs) throws IOException, JaxenException, XMLStreamException {

        List<DocerServicesStub.KeyValuePair> lista = new ArrayList<DocerServicesStub.KeyValuePair>();

        XMLUtil util = new XMLUtil(fs.toString());

        OMElement node = util.getNode("//CodiceAmministrazione");
        DocerServicesStub.KeyValuePair kv = new DocerServicesStub.KeyValuePair();
        kv.setKey("COD_ENTE");
        kv.setValue(node.getText());
        lista.add(kv);

        node = util.getNode("//CodiceAOO");
        kv = new DocerServicesStub.KeyValuePair();
        kv.setKey("COD_AOO");
        kv.setValue(node.getText());
        lista.add(kv);

        node = util.getNode("//Classifica");
        kv = new DocerServicesStub.KeyValuePair();
        kv.setKey("CLASSIFICA");
        kv.setValue(node.getText());
        lista.add(kv);

        node = util.getNode("//Anno");
        kv = new DocerServicesStub.KeyValuePair();
        kv.setKey("ANNO_FASCICOLO");
        kv.setValue(node.getText());
        lista.add(kv);

        node = util.getNode("//Progressivo");
        kv = new DocerServicesStub.KeyValuePair();
        kv.setKey("PROGR_FASCICOLO");
        kv.setValue(node.getText());
        lista.add(kv);

        DocerServicesStub.KeyValuePair[] array = new DocerServicesStub.KeyValuePair[lista.size()];

        return lista.toArray(array);
    }

    private OMElement getDocumentBlock(String token, String documentoId, String docNumPrincipale) throws RemoteException, FascicolazioneException, UnsupportedEncodingException,
            DocerExceptionException {
        // getProfileDocument del principale
        OMFactory factory = OMAbstractFactory.getOMFactory();
        DocerServicesStub.GetProfileDocument doc = new DocerServicesStub.GetProfileDocument();

        doc.setDocId(documentoId);
        doc.setToken(token);
        DocerServicesStub.GetProfileDocumentResponse resp = documentale.getProfileDocument(doc);
        DocerServicesStub.KeyValuePair[] metadati = resp.get_return();

        // TipoComponente -> ALLEGATO, ANNESSO, ANNOTAZIONE

        OMElement docItem = factory.createOMElement("Documento", null);
        OMElement metaItem = factory.createOMElement("Metadati", null);
        docItem.addChild(metaItem);

        String docEpr = ClientManager.INSTANCE.getDocerServicesEpr();
        String docNum = "";
        String docName = "";
        String docDefExt = "";

        String cod_ente = "";

        String tipo_componente = "";
        String stato_archivistico = "";
        boolean pubblicato = false;

        for (DocerServicesStub.KeyValuePair meta : metadati) {
            OMElement parItem;

            if (meta.getKey().equalsIgnoreCase("cod_ente")) {
                cod_ente = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("stato_archivistico")) {
                stato_archivistico = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("pubblicato")) {
                pubblicato = Boolean.valueOf(meta.getValue());
            }

            if (meta.getKey().equalsIgnoreCase("tipo_componente")) {
                tipo_componente = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("num_pg")) {
                if (!meta.getValue().equalsIgnoreCase("")) {
                    this.protocollato = true;
                }
            }

            if (meta.getKey().equalsIgnoreCase("docname")) {
                docName = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("default_extension")) {
                docDefExt = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("docnum")) {
                docNum = meta.getValue();

                docItem.addAttribute("id", meta.getValue(), null);
                continue;
            }

            parItem = factory.createOMElement("Parametro", null);
            parItem.addAttribute("nome", meta.getKey(), null);
            String value = meta.getValue();
            if (value == null)
                value = "";
            parItem.addAttribute("valore", value, null);

            metaItem.addChild(parItem);
        }

        // Controllo su documento mastertipoComponente ammesso: PRINCIPALE o
        // VUOTO
        if (docNumPrincipale.equalsIgnoreCase(documentoId)) {
            // controllare stato_archivistico: NON ARCHIVIATO (7), NON
            // PUBBLICATO(6)
            if (stato_archivistico.equalsIgnoreCase("6") || pubblicato) {
                throw new FascicolazioneException("Non e' permesso fascicolare documenti archiviati o pubblicati");
            }

            if (!tipo_componente.equalsIgnoreCase("PRINCIPALE") && !tipo_componente.equalsIgnoreCase(""))
                throw new FascicolazioneException("Non Ã¨ permesso fascicolare allegati, annessi o annotazioni.");
        }

        String docFileName = String.format("%s.%s", docName, docDefExt);

        // costruire l'url per il donwload del documento
        String baseUrl = "";
        try {
            baseUrl = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//xformsServiceURL").getText();
        } catch (Exception e) {
            throw new FascicolazioneException("Parametro: xformsServiceURL non trovato nel file di configurazione.");
        }
        String docUri = String.format("%s?action=download&docnum=%s&filename=%s&token=%s", baseUrl, docNum, docFileName, token);

        docItem.addAttribute("uri", docUri, null);

        // Blocco acl
        OMElement aclItem = getAclBlock(token, documentoId);
        docItem.addChild(aclItem);
	/*
	 * OMElement item = null;
	 * 
	 * //Se non e' il documento principale allora controlla il
	 * tipocomponente ed e' ammesso anche vuoto //come allegato if
	 * (!docNumPrincipale.equalsIgnoreCase(documentoId)){ if
	 * (tipoComponente.equalsIgnoreCase("ALLEGATO") ||
	 * tipoComponente.equalsIgnoreCase("")) item =
	 * factory.createOMElement("Allegati", null); }
	 * 
	 * if (tipoComponente.equalsIgnoreCase("ANNESSO")) item =
	 * factory.createOMElement("Annessi", null);
	 * 
	 * if (tipoComponente.equalsIgnoreCase("ANNOTAZIONE")) item =
	 * factory.createOMElement("Annotazioni", null);
	 * 
	 * if (item!=null){ item.addChild(docItem); return item; }
	 */

        return docItem;
    }

    private OMElement getAclBlock(String token, String documentoId) throws RemoteException, DocerExceptionException {

        OMFactory factory = OMAbstractFactory.getOMFactory();

        // ACL
        DocerServicesStub.GetACLDocument aclDoc = new DocerServicesStub.GetACLDocument();
        aclDoc.setDocId(documentoId);
        aclDoc.setToken(token);
        DocerServicesStub.GetACLDocumentResponse aclResp = documentale.getACLDocument(aclDoc);
        DocerServicesStub.KeyValuePair[] acls = aclResp.get_return();

        OMElement aclItem = factory.createOMElement("Acl", null);

        if (acls != null) {
            for (DocerServicesStub.KeyValuePair acl : acls) {
                OMElement parItem;

                parItem = factory.createOMElement("Parametro", null);
                parItem.addAttribute("attore", acl.getKey(), null);
                parItem.addAttribute("valore", acl.getValue(), null);

                aclItem.addChild(parItem);
            }
        }

        return aclItem;
    }

    private OMElement getExtendedNode(String token, String documentoId) throws RemoteException, FascicolazioneException, UnsupportedEncodingException, DocerExceptionException {

        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMElement extItem = factory.createOMElement("Documenti", null);

        // Blocco documento principale
        OMElement docItem = getDocumentBlock(token, documentoId, documentoId);
        extItem.addChild(docItem);

        // getRelated -> lista docId
        DocerServicesStub.GetRelatedDocuments related = new DocerServicesStub.GetRelatedDocuments();
        related.setDocId(documentoId);
        related.setToken(token);
        DocerServicesStub.GetRelatedDocumentsResponse relResp = documentale.getRelatedDocuments(related);
        String[] rels = relResp.get_return();

        OMElement allegatiItem = factory.createOMElement("Allegati", null);
        OMElement annessiItem = factory.createOMElement("Annessi", null);
        OMElement annotazioniItem = factory.createOMElement("Annotazioni", null);

        if (rels != null) {
            // altri blocchi: allegati,annessi,annotazioni
            for (String rel : rels) {
                docItem = getDocumentBlock(token, rel, documentoId);
                // ******switch su tipocomponente (allegati,annessi,annotazioni)
                String tipoComponente = extractTipoComponente(docItem);

                if (tipoComponente.equalsIgnoreCase("ALLEGATO") || tipoComponente.equalsIgnoreCase(""))
                    allegatiItem.addChild(docItem);

                if (tipoComponente.equalsIgnoreCase("ANNESSO"))
                    annessiItem.addChild(docItem);

                if (tipoComponente.equalsIgnoreCase("ANNOTAZIONE"))
                    annotazioniItem.addChild(docItem);
            }
        }

        extItem.addChild(allegatiItem);
        extItem.addChild(annessiItem);
        extItem.addChild(annotazioniItem);

        return extItem;
    }

    private String extractTipoComponente(OMElement documentBlock) throws FascicolazioneException {
        try {
            String xpath = "//Metadati/Parametro[@nome='TIPO_COMPONENTE']";

            AXIOMXPath path = new AXIOMXPath(xpath);
            OMElement node = (OMElement) path.selectSingleNode(documentBlock);

            OMAttribute a = node.getAttribute(new QName("valore"));

            return a.getAttributeValue();

        } catch (JaxenException ex) {
            org.slf4j.LoggerFactory.getLogger(BusinessLogic.class.getName()).error(ex.getMessage(), ex);
            throw new FascicolazioneException(ex);
        }
    }

    private DocerServicesStub.KeyValuePair[] convertMeta(KeyValuePair[] metadati) {

        List<DocerServicesStub.KeyValuePair> meta = new ArrayList<DocerServicesStub.KeyValuePair>(metadati.length);
        for (int i = 0; i < metadati.length; i++) {
            String key = metadati[i].getKey();
            if (key != null && !key.equals("")) {
                DocerServicesStub.KeyValuePair pair = new DocerServicesStub.KeyValuePair();
                pair.setKey(metadati[i].getKey());
                pair.setValue(metadati[i].getValue());
                meta.add(pair);
            }
        }

        return meta.toArray(new DocerServicesStub.KeyValuePair[0]);
    }

    private String buildFascSecondari(List<OMElement> fSecondari) throws IOException, XMLStreamException, JaxenException {

        String fsecondari = "";

        if (fSecondari.size() == 0)
            return "";

        // for (OMElement fs : fSecondari) {
        // XMLUtil xml = new XMLUtil(fs.toString());
        // fsecondari += xml.getNodeValue("Classifica");
        // fsecondari += "/";
        //
        // fsecondari += xml.getNodeValue("Anno");
        // fsecondari += "/";
        //
        // fsecondari += xml.getNodeValue("Progressivo");
        //
        // fsecondari += ";";
        // }

        for (OMElement fs : fSecondari) {
            XMLUtil xml = new XMLUtil(fs.toString());

            String appo_fasc_secondari_format = fascicoloUtils.toFascicoloSecondarioString(xml.getNodeValue("Classifica"), xml.getNodeValue("Anno"), xml.getNodeValue("Progressivo"));

            fsecondari += appo_fasc_secondari_format + ";";
        }

        return fsecondari;
    }

    private List<String> getFascicoliDelta(String userId, String ticketDocumentale, DocerServicesStub.KeyValuePair[] metadatiDocumento, XMLUtil dom) throws FascicolazioneException,
            DocerExceptionException, JaxenException, IOException, XMLStreamException {

        List<String> fascicoli_current = new ArrayList<String>();
        List<String> fascicoli_new = new ArrayList<String>();
        List<String> fascicoli_delta = new ArrayList<String>();

        String cod_ente = null;
        String cod_aoo = null;
        String classifica = null;
        String anno_fascicolo = null;
        String progr_fascicolo = null;
        String fasc_secondari = null;

        for (DocerServicesStub.KeyValuePair pair : metadatiDocumento) {
            if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                cod_ente = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                cod_aoo = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                classifica = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                anno_fascicolo = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                progr_fascicolo = pair.getValue();
                continue;
            }
            if (pair.getKey().equalsIgnoreCase("FASC_SECONDARI")) {
                fasc_secondari = pair.getValue();
                continue;
            }
        }




        // FASCICOLO PRIMARIO da assegnare
        DocerServicesStub.KeyValuePair[] params = null;

        boolean sfascicolazione = false;

        OMElement fPrimario = dom.getNode("//Intestazione/FascicoloPrimario");
        if (fPrimario != null) {

            if (StringUtils.isNotEmpty(progr_fascicolo)) {
                String fascicolo_primario_current = fascicoloUtils.toFascicoloSecondarioString(metadatiDocumento);
                if (StringUtils.isNotEmpty(fascicolo_primario_current)) {
                    fascicoli_current.add(fascicolo_primario_current);
                }
            }

            params = buildFascMeta(fPrimario);

            String progrFascicolo = getValueFromKey(params, "PROGR_FASCICOLO");
            // se non e' una sfascicolazione
            if (StringUtils.isNotEmpty(progrFascicolo)) {
                String fascicolo_primario_new = fascicoloUtils.toFascicoloSecondarioString(params);
                fascicoli_new.add(fascicolo_primario_new);
            } else {
                sfascicolazione = true;
            }
        }

        OMElement fsecRoot = dom.getNode("//Intestazione/FascicoliSecondari");

        if (fsecRoot!=null) {
            if (StringUtils.isNotEmpty(fasc_secondari)) {
                String[] fascicoli_secondari_current = fasc_secondari.split(" *; *");

                for (String f : fascicoli_secondari_current) {

                    if (StringUtils.isEmpty(f) || fascicoli_current.contains(f)) {
                        continue;
                    }
                    fascicoli_current.add(f);
                }
            }
        }

        // FASCICOLI SECONDARI da assegnare
        List<OMElement> fSecondari = dom.getNodes("//Intestazione/FascicoliSecondari/FascicoloSecondario");
        if (fSecondari.size() > 0) {
            for (OMElement fs : fSecondari) {
                params = buildFascMeta(fs);

                String progrFascicoloSecondario = getValueFromKey(params, "PROGR_FASCICOLO");
                if (StringUtils.isEmpty(progrFascicoloSecondario)) {
                    continue;
                }

                String fascicolo_secondario_new = fascicoloUtils.toFascicoloSecondarioString(params);
                fascicoli_new.add(fascicolo_secondario_new);
            }
        }

        if (sfascicolazione && fascicoli_new.size() > 0) {
            throw new FascicolazioneException("Non e' ammesso assegnare fascicoli secondari senza assegnare un Fascicolo primario");
        }

        //nuova routine delta
        fascicoli_delta = DocerUtils.getFascicoliDelta(fascicoli_new, fascicoli_current);

//        for (String f : fascicoli_current) {
//            if (fascicoli_new.contains(f)) {
//                continue;
//            }
//
//            if (fascicoli_delta.contains(f)) {
//                continue;
//            }
//
//            // aggiungo fascicoli non presenti nel nuovo (quelli da rimuovere)
//            fascicoli_delta.add(f);
//        }
//
//        for (String f : fascicoli_new) {
//            if (fascicoli_current.contains(f)) {
//                continue;
//            }
//
//            if (fascicoli_delta.contains(f)) {
//                continue;
//            }
//
//            // aggiungo fascicoli non presenti tra gli attuali (quelli da
//            // aggiungere)
//            fascicoli_delta.add(f);
//        }

        return fascicoli_delta;

    }

    private void checkUserRights(it.kdm.docer.sdk.classes.KeyValuePair[] id, String ticketDocumentale, String userId, EnumACLRights minimumRight, String type) throws FascicolazioneException {

        DocerServicesStub.KeyValuePair[] kvp = fascicoloUtils.toDocerKeyValuePairArray(id);

        checkUserRights(kvp, ticketDocumentale, userId, minimumRight, type);
    }

    private void checkUserRights(DocerServicesStub.KeyValuePair[] id, String ticketDocumentale, String userId, EnumACLRights minimumRight, String type) throws FascicolazioneException {

        GetUserRightsAnagrafiche getUserRightsAnagrafiche = new GetUserRightsAnagrafiche();
        getUserRightsAnagrafiche.setId(id);
        getUserRightsAnagrafiche.setToken(ticketDocumentale);

        if (type.toLowerCase().contains("titolario")) {
            getUserRightsAnagrafiche.setType("TITOLARIO");
        } else {
            getUserRightsAnagrafiche.setType(type.toUpperCase());
        }

        getUserRightsAnagrafiche.setUserId(userId);

        GetUserRightsAnagraficheResponse getUserRightsAnagraficheResponse;
        try {
            getUserRightsAnagraficheResponse = documentale.getUserRightsAnagrafiche(getUserRightsAnagrafiche);
        } catch (Exception e) {
            throw new FascicolazioneException(e.getMessage());
        }

        int userRight = -1;

        if (getUserRightsAnagraficheResponse != null && getUserRightsAnagraficheResponse.get_return() > -1) {
            userRight = getUserRightsAnagraficheResponse.get_return();
        }

        // 0 fullAccess
        // 1 normalAccess
        // 2 readOnly

        if (userRight < 0) {
            throw new FascicolazioneException(type.toLowerCase() + " " + fascicoloUtils.toReadableString(id) + " non trovato nel documentale");
        }

        // la verifica va fatta al contrario...
        if (userRight > minimumRight.getCode()) {
            throw new FascicolazioneException("l'utente " + userId + " non ha diritti sufficienti sul " + type.toLowerCase() + " " + fascicoloUtils.toReadableString(id) + " nel documentale");
        }

    }

    private void checkUserRights(long docId, String ticketDocumentale, String userId, EnumACLRights minimumRight) throws FascicolazioneException, RemoteException, DocerExceptionException {

        GetUserRights getUserRights = new GetUserRights();
        getUserRights.setDocId(String.valueOf(docId));
        getUserRights.setToken(ticketDocumentale);
        getUserRights.setUserId(userId);
        GetUserRightsResponse getUserRightsResponse = documentale.getUserRights(getUserRights);

        int userRight = -1;

        if (getUserRightsResponse != null && getUserRightsResponse.get_return() > -1) {
            userRight = getUserRightsResponse.get_return();
        }

        // 0 fullAccess
        // 1 normalAccess
        // 2 readOnly

        if (userRight < 0) {
            throw new FascicolazioneException("documento " + docId + " non trovato nel documentale");
        }

        // la verifica va fatta al contrario...
        if (userRight > minimumRight.getCode()) {
            throw new FascicolazioneException("l'utente " + userId + " non ha diritti sufficienti sul documento " + docId + " nel documentale");
        }

    }

    private void initFascicoloUtils() {

        fascicoloUtils = new FascicoloUtils(fascicoliSecondariFormatDefault, regexpFascicoliSecondariDefault);

        OMElement fascSecondariMappingNode = null;
        try {
            fascSecondariMappingNode = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//section[@name='variables']/fascicoli_secondari_mapping");
            if (fascSecondariMappingNode != null) {
                fascicoliSecondariFormat = fascSecondariMappingNode.getAttribute(format_qname).getAttributeValue();
                regexpFascicoliSecondari = fascSecondariMappingNode.getAttribute(regexp_qname).getAttributeValue();

                int classifica_position = Integer.parseInt(fascSecondariMappingNode.getAttribute(classifica_position_qname).getAttributeValue().replace("$", ""));
                int anno_fascicolo_position = Integer.parseInt(fascSecondariMappingNode.getAttribute(anno_fascicolo_position_qname).getAttributeValue().replace("$", ""));
                int progr_fascicolo_position = Integer.parseInt(fascSecondariMappingNode.getAttribute(progr_fascicolo_position_qname).getAttributeValue().replace("$", ""));
                fascicoloUtils = new FascicoloUtils(fascicoliSecondariFormat, regexpFascicoliSecondari, classifica_position, anno_fascicolo_position, progr_fascicolo_position);
            }
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(BusinessLogic.class.getName()).error("initFascicoloUtils", e);
        }

    }

    public boolean addRemoveACLFascicolo(String token, KeyValuePair[] fascicoloid, KeyValuePair aclToAdd[], String aclToRemove[]) throws FascicolazioneException {
        DocerServicesStub.KeyValuePair[] metadatiDOC;
        String ente = null;
        String aoo = null;

        String progressivo = null;
        String classifica = null;
        String anno = null;

        try {
            entetoken = Utils.extractTokenKey(token, "ente");
            initFascicoloUtils();
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new FascicolazioneException(e1.getMessage());
        }

        for (KeyValuePair pair : fascicoloid) {

            if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                anno = pair.getValue();
            }
            if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                classifica = pair.getValue();
            }
            if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                progressivo = pair.getValue();
            }
            if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                aoo = pair.getValue();
            }
            if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                ente = pair.getValue();
            }
        }


        String docTicket = "calltype:internal|";
        if (classifica == null || ente == null || aoo == null || anno == null || progressivo == null) {
            throw new FascicolazioneException("521 - CLASSIFICA, COD_ENTE, COD_AOO, PROGR_FASCICOLO e ANNO_FASCICOLO sono metadati obbligatori");
        }

        if ((aclToAdd == null || aclToAdd.length < 1) && (aclToRemove == null || aclToRemove.length < 1)) {
            throw new FascicolazioneException("522 - liste acl da aggiungere e da rimuovere non valorizzate");
        }

        try {
            docTicket += Utils.extractTokenKey(token, "documentale");
            DocerServicesStub.GetACLFascicolo aclFasc = new DocerServicesStub.GetACLFascicolo();
            aclFasc.setToken(docTicket);
            it.kdm.docer.clients.DocerServicesStub.KeyValuePair kvpfascicolo[] = new it.kdm.docer.clients.DocerServicesStub.KeyValuePair[fascicoloid.length];

            int i = 0;
            for (KeyValuePair kk : fascicoloid) {
                it.kdm.docer.clients.DocerServicesStub.KeyValuePair kfasc = new it.kdm.docer.clients.DocerServicesStub.KeyValuePair();
                kfasc.setKey(kk.getKey());
                kfasc.setValue(kk.getValue());
                kvpfascicolo[i] = kfasc;
                i++;
            }

            aclFasc.setFascicoloId(kvpfascicolo);
            DocerServicesStub.GetACLFascicoloResponse response = documentale.getACLFascicolo(aclFasc);
            DocerServicesStub.KeyValuePair[] aclOnFascicoloK = response.get_return();

            if (aclOnFascicoloK == null) {
                aclOnFascicoloK = new DocerServicesStub.KeyValuePair[0];
            }

            Map<String, String> aclOnFascicolo = new HashMap<String, String>();

            for (DocerServicesStub.KeyValuePair pair : aclOnFascicoloK) {
                aclOnFascicolo.put(pair.getKey(), pair.getValue());
            }

            if (aclToRemove != null) {
                for (String removeAcl : aclToRemove) {
                    if (aclOnFascicolo.containsKey(removeAcl)) {
                        aclOnFascicolo.remove(removeAcl);
                    }
                }
            }

            if (aclToAdd != null) {
                for (KeyValuePair addAcl : aclToAdd) {
                    aclOnFascicolo.put(addAcl.getKey(), addAcl.getValue());
                }
            }
            KeyValuePair[] aclKvp = new KeyValuePair[aclOnFascicolo.size()];

            Iterator itacl = aclOnFascicolo.entrySet().iterator();
            int j = 0;
            while (itacl.hasNext()) {
                KeyValuePair kvpt = new KeyValuePair();
                Map.Entry pairs = (Map.Entry) itacl.next();
                kvpt.setKey((String) pairs.getKey());
                kvpt.setValue((String) pairs.getValue());
                itacl.remove(); // avoids a ConcurrentModificationException
                aclKvp[j] = kvpt;
                j++;
            }

            //setACLFascicolo(ticket, id, aclOnFascicolo);
            IProvider provider = getProvider(ente, aoo);
            boolean isUpdateACL = provider.updateACLFascicolo(docTicket, fascicoloid, aclKvp);

            if (isUpdateACL) {
                DocerServicesStub.SetACLFascicolo setAclFascicoloStub = new DocerServicesStub.SetACLFascicolo();
                it.kdm.docer.clients.DocerServicesStub.KeyValuePair kvpForWsDocer[] = new it.kdm.docer.clients.DocerServicesStub.KeyValuePair[aclKvp.length];
                int ii = 0;
                for (KeyValuePair temp : aclKvp) {
                    it.kdm.docer.clients.DocerServicesStub.KeyValuePair kvpTmp = new it.kdm.docer.clients.DocerServicesStub.KeyValuePair();
                    kvpTmp.setValue(temp.getValue());
                    kvpTmp.setKey(temp.getKey());
                    kvpForWsDocer[ii] = kvpTmp;
                    ii++;
                }
                setAclFascicoloStub.setAcls(kvpForWsDocer);
                setAclFascicoloStub.setFascicoloId(kvpfascicolo);
                setAclFascicoloStub.setToken(docTicket);
                DocerServicesStub.SetACLFascicoloResponse responseUpdateDocer = documentale.setACLFascicolo(setAclFascicoloStub);
                response.get_return();
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new FascicolazioneException(e.getMessage());
        }
    }


}
