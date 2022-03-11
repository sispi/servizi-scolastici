/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.protocollazione;

import it.kdm.docer.clients.ClientManager;
import it.kdm.docer.clients.DocerExceptionException;
import it.kdm.docer.clients.DocerServicesStub;
import it.kdm.docer.clients.DocerServicesStub.GetUserRights;
import it.kdm.docer.clients.DocerServicesStub.GetUserRightsAnagrafiche;
import it.kdm.docer.clients.DocerServicesStub.GetUserRightsAnagraficheResponse;
import it.kdm.docer.clients.DocerServicesStub.GetUserRightsResponse;
import it.kdm.docer.commons.Config;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.commons.configuration.Configurations;
import it.kdm.docer.commons.docerservice.BaseService;
import it.kdm.docer.commons.docerservice.BaseServiceException;
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
import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lorenzo
 */
public class BusinessLogic extends BaseService implements IBusinessLogic {

    private static Configurations CONFIGURATIONS = new Configurations();

    private IProvider provider;

    private DocerServicesStub documentale;

    private String fascicoliSecondariFormatDefault = "classifica/anno_fascicolo/progr_fascicolo";
    private String regexpFascicoliSecondariDefault = "^([^/]+)/([12][0-9][0-9][0-9])/(.+)$";
    private String fascicoliSecondariFormat = fascicoliSecondariFormatDefault;
    private String regexpFascicoliSecondari = regexpFascicoliSecondariDefault;

    static private QName format_qname = new QName("format");
    static private QName regexp_qname = new QName("regexp");
    static private QName classifica_position_qname = new QName("classifica_position");
    static private QName anno_fascicolo_position_qname = new QName("anno_fascicolo_position");
    static private QName progr_fascicolo_position_qname = new QName("progr_fascicolo_position");

    static private Logger logger = org.slf4j.LoggerFactory.getLogger(BusinessLogic.class.getName());

    private FascicoloUtils fascicoloUtils = null;
    private String entetoken = null;
	private boolean controlloFirma;

	public BusinessLogic() throws AxisFault {

        // CONFIGURATIONS.setConfigurationProperties("configuration.properties",
        // "config.path");
        documentale = ClientManager.INSTANCE.getDocerServicesClient();
    }

    public String login(String userId, String password, String codiceEnte) throws ProtocollazioneException {

        entetoken = codiceEnte;

        try {
            return baseLogin(userId, password, codiceEnte);
        } catch (BaseServiceException e) {
            throw new ProtocollazioneException(e);
        }
    }

	public String loginSSO(String saml, String codiceEnte) throws ProtocollazioneException {
		entetoken = codiceEnte;

		try {
			return baseLoginSSO(saml, codiceEnte);
		} catch (BaseServiceException e) {
			throw new ProtocollazioneException(e);
		}
	}

	public void logout(String token) throws ProtocollazioneException {

        provider.logout(token);
    }

    public String protocolla(String ticket, long documentoId, String datiProtocollo) throws ProtocollazioneException {

        try {
            entetoken = Utils.extractTokenKey(ticket, "ente");
            initFascicoloUtils();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new ProtocollazioneException(e1.getMessage());
        }

        String userId;
        // String library;
        String providerData;
        String docTicket = "calltype:internal|";

        String ente = "";
        String aoo = "";
        String currentClassifica = "";
        String currentProgrFascicolo = "";

        XMLUtil dom = null;

        DocerServicesStub.KeyValuePair[] metadatiDOC;

        try {
            dom = new XMLUtil(datiProtocollo);

            docTicket += Utils.extractTokenKey(ticket, "documentale");

            // getProfileDocument del principale
            metadatiDOC = Utils.getProfile(docTicket, String.valueOf(documentoId));

            // TipoComponente -> ALLEGATO, ANNESSO, ANNOTAZIONE

            String archiveType = "";

            for (DocerServicesStub.KeyValuePair pair : metadatiDOC) {
                if (pair.getKey().equalsIgnoreCase("COD_ENTE")) {
                    ente = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("COD_AOO")) {
                    aoo = pair.getValue();
                    continue;
                }
                if (pair.getKey().equalsIgnoreCase("CLASSIFICA")) {
                    currentClassifica = pair.getValue();
                    continue;
                }
                // if (pair.getKey().equalsIgnoreCase("ANNO_FASCICOLO")) {
                // annoFascicolo = pair.getValue();
                // continue;
                // }
                if (pair.getKey().equalsIgnoreCase("PROGR_FASCICOLO")) {
                    currentProgrFascicolo = pair.getValue();
                    continue;
                }
                // if (pair.getKey().equalsIgnoreCase("FASC_SECONDARI")) {
                // fascSecondari = pair.getValue();
                // continue;
                // }

                if (pair.getKey().equalsIgnoreCase("ARCHIVE_TYPE")) {
                    archiveType = pair.getValue();
                    continue;
                }
            }

//            if (StringUtils.isEmpty(archiveType)) {
//                throw new Exception("Metadato ARCHIVE_TYPE mancante");
//            }

            if (!Utils.checkArchiveTypeConsistency(docTicket, String.valueOf(documentoId), archiveType)) {
                throw new Exception("I documenti correlati devono avere la stessa natura del documento principale");
            }

        } catch (Exception e) {
            throw new ProtocollazioneException(e);
        }

        try {
            // spostato il controllo XSD per switchare il file
            // validazione del formato
            String xsdFileName = getProviderXsdAttribute(ente, aoo, "xsd"); // "/input-validation.xsd"
            
            File schema = ConfigurationUtils.loadResourceFile(xsdFileName);
            dom.validate(schema.getAbsolutePath());
        } catch (Exception e) {
            throw new ProtocollazioneException(e);
        }

        String inoltroFirmaProvider = getInoltroFirmaProvider(ente, aoo);
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
            throw new ProtocollazioneException("Ticket non valido o provider per ENTE: " + ente + " AOO: " + aoo + " non disponibile. " + e.getMessage());
        }

        String tipoRichiesta = "";
        String firma = "";
        boolean forzaRegistrazione = false;

        try {

            tipoRichiesta = dom.getNodeValue("//Flusso/TipoRichiesta");
            // exception se tipoRichiesta=null ****************************

            firma = dom.getNodeValue("//Flusso/Firma");

            String appo = dom.getNodeValue("//Flusso/ForzaRegistrazione");

            if (appo != null && appo.equals("1")) {
                forzaRegistrazione = true;
            }

            // ********************************************************************************************
            // Controlli preventivi su parametri XML di input
            // ********************************************************************************************
            if (!tipoRichiesta.equalsIgnoreCase("E") && !tipoRichiesta.equalsIgnoreCase("U") && !tipoRichiesta.equalsIgnoreCase("I")) {
                throw new ProtocollazioneException("Valore 'TipoRichiesta' non valido.");
            }

            this.controlloFirma = getControlloFirma(ente,aoo);

		if (controlloFirma) {
			if (!firma.equalsIgnoreCase("FD") && !firma.equalsIgnoreCase("FE") && !firma.equalsIgnoreCase("F") && !firma.equalsIgnoreCase("NF")) {
				throw new ProtocollazioneException("Valore 'Firma' non valido.");
			}

			if (tipoRichiesta.equalsIgnoreCase("E")) {
				if (!firma.equalsIgnoreCase("FD") && !firma.equalsIgnoreCase("FE") && !forzaRegistrazione) {
					throw new ProtocollazioneException("Valore 'Firma' non ammesso per il 'TipoRichiesta'.");
				}
			}

			if (tipoRichiesta.equalsIgnoreCase("U")) {
				if (!firma.equalsIgnoreCase("FD") && !firma.equalsIgnoreCase("F") && !forzaRegistrazione) {
					throw new ProtocollazioneException("Valore 'Firma' non ammesso per il 'TipoRichiesta'.");
				}
			}
		}

	    // ********************************************************************************************
	    // FINE: Controlli preventivi su parametri XML di input
	    // ********************************************************************************************

            // ho trovato il documento quindi ho almeno readOnly
            // ora verifico che abbia almeno normalAccess per modificare il
            // profilo con i metadati di protocollo
            checkUserRights(documentoId, docTicket, userId, EnumACLRights.normalAccess);

            OMElement fPrimario = dom.getNode("//Intestazione/FascicoloPrimario");

            List<OMElement> fSecondari = dom.getNodes("//Intestazione/FascicoliSecondari/FascicoloSecondario");

            OMElement classificaNode = null;
            String newClassificaEnte = null;
            String newClassificaAOO = null;
            String newClassificaClassifica = null;
            // nel caso di protocollazione in entrata e' ammessa la
            // classificazione senza fascicolazione (se non presenti i dati dei
            // fascicoli)
            boolean soloClassificazione = false;

            DocerServicesStub.ClassificaDocumento claDoc = null;

            if (tipoRichiesta.equalsIgnoreCase("E") && fPrimario == null) {

                // controlla se esiste il nodo classifica
                classificaNode = dom.getNode("//Intestazione/Classifica");

                if (classificaNode != null) {

                    OMElement paramNode = dom.getNode("//Intestazione/Classifica/CodiceAmministrazione");
                    if (paramNode != null) {
                        newClassificaEnte = paramNode.getText();
                    }

                    paramNode = dom.getNode("//Intestazione/Classifica/CodiceAOO");
                    if (paramNode != null) {
                        newClassificaAOO = paramNode.getText();
                    }

                    paramNode = dom.getNode("//Intestazione/Classifica/Classifica");
                    if (paramNode != null) {
                        newClassificaClassifica = paramNode.getText();
                    }

                    if (newClassificaClassifica != null && currentClassifica != null && !currentClassifica.equals(newClassificaClassifica)) {

                        if (StringUtils.isNotEmpty(currentProgrFascicolo)) {
                            throw new ProtocollazioneException("Non e' ammesso modificare la classifica di un documento con Fascicolo primario assegnato");
                        }

                        if (!String.valueOf(ente).equals(String.valueOf(newClassificaEnte))) {
                            throw new ProtocollazioneException("Modifica classifica: la nuova classifica appartiene ad un altro ente");
                        }

                        if (!String.valueOf(aoo).equals(String.valueOf(newClassificaAOO))) {
                            throw new ProtocollazioneException("Modifica classifica: la nuova classifica appartiene ad un'altra AOO");
                        }

                        KeyValuePair[] classificaId = new KeyValuePair[3];
                        classificaId[0] = new KeyValuePair("COD_ENTE", newClassificaEnte);
                        classificaId[1] = new KeyValuePair("COD_AOO", newClassificaAOO);
                        classificaId[2] = new KeyValuePair("CLASSIFICA", newClassificaClassifica);

                        // controllo diritti sulla voce di titolario
                        checkUserRights(classificaId, docTicket, userId, EnumACLRights.readOnly, "voce di titolario");

                        // deve classificare
                        claDoc = new DocerServicesStub.ClassificaDocumento();
                        claDoc.setToken(docTicket);
                        claDoc.setDocId(String.valueOf(documentoId));

                        DocerServicesStub.KeyValuePair kvp1 = new DocerServicesStub.KeyValuePair();

                        kvp1.setKey("COD_ENTE");
                        kvp1.setValue(newClassificaEnte);
                        claDoc.addMetadata(kvp1);

                        DocerServicesStub.KeyValuePair kvp2 = new DocerServicesStub.KeyValuePair();

                        kvp2.setKey("COD_AOO");
                        kvp2.setValue(newClassificaAOO);
                        claDoc.addMetadata(kvp2);

                        DocerServicesStub.KeyValuePair kvp3 = new DocerServicesStub.KeyValuePair();

                        kvp3.setKey("CLASSIFICA");
                        kvp3.setValue(newClassificaClassifica);
                        claDoc.addMetadata(kvp3);

                        soloClassificazione = true;
                    }

                }

                // si desidera la sola classificazione
            }

            // controllare se richiesta fascicolazione
            // se richiesta, chiamare docer per allineare il fascicolo e i
            // fascicoli secondari

            List<String> fascicoli_delta = getFascicoliDelta(userId, docTicket, metadatiDOC, dom);

            // controllo i diritti sui fascicoli da aggiungere o da rimuovere
            // (almeno normalAccess)
            for (String f : fascicoli_delta) {

                DocerServicesStub.KeyValuePair[] kvp = fascicoloUtils.toDocerKeyValuePairArray(ente, aoo, f);

                checkUserRights(kvp, docTicket, userId, EnumACLRights.normalAccess, "FASCICOLO");
            }

            // ********************************************************************************************

            // ***Recupero metadati e file dei documenti (principale, allegati ,
            // annessi e annotazioni)
            OMElement nodeToAdd = getExtendedNode(docTicket, String.valueOf(documentoId));
            // OMElement root = dom.getNode("/Segnatura");
            // root.addChild(nodeToAdd);

            dom.addNode("/Segnatura", nodeToAdd);

            String datiProtocolloFull = dom.toXML();
            // String datiProtocolloFull = root.toString();

            // gestione sincrona
            String xmlRet = "";

            if (!firma.equalsIgnoreCase("F")) {

                xmlRet = this.provider.protocolla(datiProtocolloFull);

                // controllare solo esito (se OK formatta output e return
                XMLUtil util = new XMLUtil(xmlRet);

                String esito = util.getNodeValue("/esito/codice");
                String errDescription = util.getNodeValue("/esito/descrizione");

                if (!esito.equalsIgnoreCase("0"))
                    throw new ProtocollazioneException(errDescription);

                List<OMElement> metaProt = util.getNodes("/esito/dati_protocollo/*");

                try {
                    DocerServicesStub.ProtocollaDocumento protDoc = new DocerServicesStub.ProtocollaDocumento();

                    for (OMElement m : metaProt) {
                        DocerServicesStub.KeyValuePair kvp = new DocerServicesStub.KeyValuePair();

                        kvp.setKey(m.getLocalName());
                        kvp.setValue(m.getText());

                        protDoc.addMetadata(kvp);
                    }

                    // MITTENTI
                    OMElement mitNode = dom.getNode("//Mittenti");
                    if (mitNode != null) {
                        DocerServicesStub.KeyValuePair kvp = new DocerServicesStub.KeyValuePair();

                        kvp.setKey("MITTENTI");
                        kvp.setValue(mitNode.toString());
                        protDoc.addMetadata(kvp);
                    }

                    // DESTINATARI
                    OMElement desNode = dom.getNode("//Destinatari");
                    if (desNode != null) {
                        DocerServicesStub.KeyValuePair kvp = new DocerServicesStub.KeyValuePair();

                        kvp.setKey("DESTINATARI");
                        kvp.setValue(desNode.toString());
                        protDoc.addMetadata(kvp);
                    }

                    // TIPO_PROTOCOLLAZIONE
                    OMElement tipoProtNode = dom.getNode("//Flusso/TipoRichiesta");
                    if (tipoProtNode != null) {
                        DocerServicesStub.KeyValuePair kvp = new DocerServicesStub.KeyValuePair();

                        kvp.setKey("TIPO_PROTOCOLLAZIONE");
                        kvp.setValue(tipoProtNode.getText());
                        protDoc.addMetadata(kvp);
                    }

                    // TIPO_FIRMA
                    OMElement tipoFirmaNode = dom.getNode("//Flusso/Firma");
                    if (tipoFirmaNode != null) {
                        DocerServicesStub.KeyValuePair kvp = new DocerServicesStub.KeyValuePair();

                        kvp.setKey("TIPO_FIRMA");
                        kvp.setValue(tipoFirmaNode.getText());
                        protDoc.addMetadata(kvp);
                    }

                    // FIRMATARIO
                    OMElement tipoFirmatarioNode = dom.getNode("//Flusso/Firmatario");
                    if (tipoFirmatarioNode != null) {
                        DocerServicesStub.KeyValuePair kvp = new DocerServicesStub.KeyValuePair();

                        kvp.setKey("FIRMATARIO");
                        kvp.setValue(tipoFirmatarioNode.toString());
                        protDoc.addMetadata(kvp);
                    }

                    // <Flusso>
                    // <TipoRichiesta>E</TipoRichiesta>
                    // <Firma>FD</Firma>
                    // <ForzaRegistrazione>0</ForzaRegistrazione>
                    // <ProtocolloMittente>
                    // <CodiceAmministrazione>COD_ENTE_MITTENTE</CodiceAmministrazione>
                    // <CodiceAOO>COD_AOO_MITTENTE</CodiceAOO>
                    // <Classifica>CLASSIFICA MITTENTE</Classifica>
                    // <Data>2014-06-10</Data>
                    // <Fascicolo>FASCICOLO MITTENTE</Fascicolo>
                    // <Numero>1001</Numero>
                    // </ProtocolloMittente>
                    // </Flusso>

                    List<OMElement> protocolloMittente = dom.getNodes("//Flusso/ProtocolloMittente/*");

                    for (OMElement m : protocolloMittente) {
                        DocerServicesStub.KeyValuePair kvp = new DocerServicesStub.KeyValuePair();

                        String propName = m.getLocalName();
                        if (propName.equals("CodiceAmministrazione")) {
                            propName = "COD_ENTE_MITTENTE";
                        } else if (propName.equals("CodiceAOO")) {
                            propName = "COD_AOO_MITTENTE";
                        } else if (propName.equals("Classifica")) {
                            propName = "CLASSIFICA_MITTENTE";
                        } else if (propName.equals("Data")) {
                            propName = "DATA_PG_MITTENTE";
                        } else if (propName.equals("Fascicolo")) {
                            propName = "FASCICOLO_MITTENTE";
                        } else if (propName.equals("Numero")) {
                            propName = "NUM_PG_MITTENTE";
                        } else {
                            continue;
                        }
                        kvp.setKey(propName);
                        kvp.setValue(m.getText());

                        protDoc.addMetadata(kvp);
                    }

                    // Aggiornamento dei metadati di protocollazione sul
                    // documentale
                    protDoc.setDocId(String.valueOf(documentoId));
                    protDoc.setToken(docTicket);

                    documentale.protocollaDocumento(protDoc);

                } catch (Exception e) {

                    throw new ProtocollazioneException("Impossibile aggiornare il documento in Doc/er con i metadati di protocollo. " + e.getMessage());
            /*
             * String errorMessage =
		     * "Impossibile aggiornare il documento con i dati di protocollo. "
		     * + e.getMessage();
		     * 
		     * XMLUtil warningResp = new XMLUtil(xmlRet);
		     * 
		     * OMElement esitoNode =
		     * warningResp.getNode("/esito/codice");
		     * esitoNode.setText("2");
		     * 
		     * OMElement descrNode =
		     * warningResp.getNode("/esito/descrizione");
		     * descrNode.setText(errorMessage);
		     * 
		     * return warningResp.toXML(); //ritorno esito warning
		     */
                }

                if (soloClassificazione) {
                    // deve classificare
                    try {
                        documentale.classificaDocumento(claDoc);
                    } catch (Exception e) {
                        throw new ProtocollazioneException("Impossibile aggiornare il documento in Doc/er con i metadati di classificazione. " + e.getMessage());
                    }
                }
                // aggiorno i metadati di fascicolazione solo se cambiano
                else if (fascicoli_delta.size() > 0) {

                    try {
                        // Altrimenti e' fascicolazione interna
                        DocerServicesStub.FascicolaDocumento fascDoc = new DocerServicesStub.FascicolaDocumento();
                        // Aggiornamento dei metadati di fascicolazione sul
                        // documentale
                        fascDoc.setDocId(String.valueOf(documentoId));
                        fascDoc.setToken(docTicket);

                        DocerServicesStub.KeyValuePair[] params = null;

                        if (fPrimario != null) {

                            params = buildFascMeta(fPrimario);
                            fascDoc.setMetadata(params);
                        }

                        if (fSecondari.size() > 0) {
                            it.kdm.docer.clients.DocerServicesStub.KeyValuePair fSecMeta = new it.kdm.docer.clients.DocerServicesStub.KeyValuePair();
                            fSecMeta.setKey(Constants.doc_fascicoli_secondari);
                            fSecMeta.setValue("");
                            fascDoc.addMetadata(fSecMeta);
                        }

                        fSecondari = dom.getNodes("//Intestazione/FascicoliSecondari/FascicoloSecondario");

                        if (fSecondari.size() > 0) {
                            String fsecStr = buildFascSecondari(fSecondari);
                            it.kdm.docer.clients.DocerServicesStub.KeyValuePair fSecMeta = new it.kdm.docer.clients.DocerServicesStub.KeyValuePair();
                            fSecMeta.setKey(Constants.doc_fascicoli_secondari);
                            fSecMeta.setValue(fsecStr);
                            fascDoc.addMetadata(fSecMeta);
                        }

                        documentale.fascicolaDocumento(fascDoc);
                    } catch (Exception e) {
                        throw new ProtocollazioneException("Impossibile aggiornare il documento in Doc/er con i metadati di fascicolazione. " + e.getMessage());
                    }
                }

                return xmlRet; // ritorno esito positivo

            } else {

                // se e' specificato inoltro firma sul provider
                if (inoltroFirmaProvider != null) {
                    if (!inoltroFirmaProvider.equalsIgnoreCase("true")) {
                        throw new Exception("Il provider non supporta l'inoltro alla Firma.");
                    }
                } else {
                    boolean inoltroFirmaSistema = false;
                    OMElement omeInoltroFirmaSistema = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//inoltroFirma");
                    // se e' specificato nodo inoltroFirma generale (fino a
                    // Docer
                    // 1.3.4)
                    if (omeInoltroFirmaSistema != null) {
                        inoltroFirmaSistema = Boolean.valueOf(omeInoltroFirmaSistema.getText());
                    }

                    //Il sistema di protocollo non supporta l'inoltro alla Firma.
                    if (!inoltroFirmaSistema) {

                        //TODO: chiamare bpm flow
                        //avvia il woekflow di firma
                        Utils.runWorkflowSignProcess(docTicket, Utils.enumSignProcessAction.PROTOCOLLARE, "", userId, userId, datiProtocollo, metadatiDOC);
                        //*******************************************************************************
                        OMFactory factory = OMAbstractFactory.getOMFactory();
                        OMElement docItem = factory.createOMElement("esito", null);

                        OMElement metaItem = factory.createOMElement("codice", null);
                        metaItem.setText("1"); // warning
                        docItem.addChild(metaItem);

                        metaItem = factory.createOMElement("descrizione", null);

                        metaItem.setText("");

                        docItem.addChild(metaItem);

                        return docItem.toString(); // ritorna esito warning
                        //throw new Exception("Il sistema di protocollo non supporta l'inoltro alla Firma.");
                    }

                }


                xmlRet = this.provider.protocolla(datiProtocolloFull);

                XMLUtil util = new XMLUtil(xmlRet);

                String esito = util.getNodeValue("/esito/codice");
                String errDescription = util.getNodeValue("/esito/descrizione");

                if (!esito.equalsIgnoreCase("0"))
                    throw new ProtocollazioneException(errDescription);

                // // Setta l'esito a warning
                util.setNodeValue("/esito/codice", "1");


                return util.toXML(); // ***ritorno esito warning

            }

        } catch (Exception e) {
            throw new ProtocollazioneException(e);
        }

    }

    private static QName controlloFirmaQName = new QName("controlloFirma");
    private boolean getControlloFirma(String ente, String aoo) throws ProtocollazioneException {
        try {
            String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
            Config config = CONFIGURATIONS.getConfiguration(entetoken).getConfig();
            OMElement omelement = config.getNode(xpathProvider);
            OMAttribute attr = omelement.getAttribute(controlloFirmaQName);
            if (attr != null) {
                return Boolean.parseBoolean(attr.getAttributeValue());
            } else {
                return true;
            }
        }
        catch (JaxenException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new ProtocollazioneException("Configurazione malformata");
        }
        catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new ProtocollazioneException("Configurazione malformata");
        }
        catch (XMLStreamException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new ProtocollazioneException("Configurazione malformata");
        }
    }
    public boolean writeConfig(String token, String xml) throws ProtocollazioneException {

        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            e.printStackTrace();
            throw new ProtocollazioneException(e.getMessage());
        }

        try {
            CONFIGURATIONS.writeConfig(entetoken, xml);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ProtocollazioneException("Configurazione malformata");
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ProtocollazioneException("Configurazione malformata");
        }

        return true;

    }

    public String readConfig(String token) throws ProtocollazioneException {

        try {
            entetoken = Utils.extractTokenKey(token, "ente");
        } catch (KeyException e) {
            e.printStackTrace();
            throw new ProtocollazioneException(e.getMessage());
        }

        try {
            return CONFIGURATIONS.readConfig(entetoken);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ProtocollazioneException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ProtocollazioneException(e.getMessage());
        }

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

    private OMElement getDocumentBlock(String token, String documentoId, String docNumPrincipale) throws RemoteException, ProtocollazioneException, UnsupportedEncodingException,
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

            if (meta.getKey().equalsIgnoreCase("stato_archivistico")) {
                stato_archivistico = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("pubblicato")) {
                pubblicato = Boolean.valueOf(meta.getValue());
            }

            if (meta.getKey().equalsIgnoreCase("tipo_componente")) {
                tipo_componente = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("docname")) {
                docName = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("default_extension")) {
                docDefExt = meta.getValue();
            }

            if (meta.getKey().equalsIgnoreCase("cod_ente")) {
                cod_ente = meta.getValue();
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
            // controllare stato_archivistico: ARCHIVIATO (6) oppure pubblicato
            if (stato_archivistico.equalsIgnoreCase("6") || pubblicato) {
                throw new ProtocollazioneException("Non e' permesso protocollare documenti archiviati o pubblicati");
            }

            if (!tipo_componente.equalsIgnoreCase("PRINCIPALE") && !tipo_componente.equalsIgnoreCase(""))
                throw new ProtocollazioneException("Non è permesso protocollare allegati, annessi o annotazioni.");
        }

        String docFileName = String.format("%s.%s", docName, docDefExt);
        String baseUrl = "";
        try {
            baseUrl = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//xformsServiceURL").getText();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ProtocollazioneException("Parametro: xformsServiceURL non trovato nel file di configurazione.");
        }
        // costruire l'url per il donwload del documento
        // String encodedtoken = URLEncoder.encode(token,"UTF-8");
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

    private OMElement getExtendedNode(String token, String documentoId) throws RemoteException, ProtocollazioneException, UnsupportedEncodingException, DocerExceptionException {

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

    private String extractTipoComponente(OMElement documentBlock) throws ProtocollazioneException {
        try {
            String xpath = "//Metadati/Parametro[@nome='TIPO_COMPONENTE']";

            AXIOMXPath path = new AXIOMXPath(xpath);
            OMElement node = (OMElement) path.selectSingleNode(documentBlock);

            OMAttribute a = node.getAttribute(new QName("valore"));

            return a.getAttributeValue();

        } catch (JaxenException ex) {
            logger.error("WSProtocollazione.extractTipoComponente()", ex);
            throw new ProtocollazioneException(ex);
        }
    }

    private IProvider getProvider(String ente, String aoo) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException, ProtocollazioneException {

        String providerClassName = "";

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
        try {
            providerClassName = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode(xpathProvider).getText();
        } catch (Exception e) {
            throw new ProtocollazioneException("Impossibile trovare il provider per ENTE: " + ente + " e AOO: " + aoo + " nel file di configurazione. " + e.getMessage());
        }

        IProvider prov = (IProvider) Class.forName(providerClassName).newInstance();

        return prov;
    }


    private static QName inoltroFirmaQName = new QName("inoltroFirma");

    protected String getInoltroFirmaProvider(String ente, String aoo) {

        try {

            String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);
            OMElement omelement = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode(xpathProvider);

            if (omelement != null) {
                OMAttribute attr = omelement.getAttribute(inoltroFirmaQName);
                if (attr != null) {
                    return attr.getAttributeValue();
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }

    }

    private String getProviderXsdAttribute(String ente, String aoo, String attributeName) throws JaxenException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            ProtocollazioneException, DocerException {

        String providerAttribute = "";
        String defaultXsdFile = "";

        OMElement ome1;
        try {
            ome1 = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode("//section[@name='Providers']");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new ProtocollazioneException(e1.getMessage());
        } catch (XMLStreamException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            throw new ProtocollazioneException(e1.getMessage());
        }
        if (ome1 == null) {
            throw new ProtocollazioneException("xpath //section[@name='Providers'] non trovato nel file di configurazione.");
        }

        defaultXsdFile = ome1.getAttributeValue(new QName("default-xsd"));
        if (defaultXsdFile == null) {
            throw new ProtocollazioneException("Impossibile trovare l'attributo 'default-xsd' del nodo 'Providers' nel file di configurazione.");
        }

        String xpathProvider = String.format("//provider[@ente='%s' and @aoo='%s']", ente, aoo);

        OMElement ome2;
        try {
            ome2 = CONFIGURATIONS.getConfiguration(entetoken).getConfig().getNode(xpathProvider);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ProtocollazioneException(e.getMessage());
        } catch (XMLStreamException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ProtocollazioneException(e.getMessage());
        }
        if (ome2 == null) {
            throw new ProtocollazioneException("provider non definito per ente=" + ente + " e aoo=" + aoo);
        }

        providerAttribute = ome2.getAttributeValue(new QName(attributeName));
        if (providerAttribute == null) {
            return defaultXsdFile;
        }

        return providerAttribute;
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

        // DocerServicesStub.KeyValuePair[] array = new
        // DocerServicesStub.KeyValuePair[lista.size()];

        return lista.toArray(new DocerServicesStub.KeyValuePair[0]);
    }

    private List<String> getFascicoliDelta(String userId, String ticketDocumentale, DocerServicesStub.KeyValuePair[] metadatiDocumento, XMLUtil dom) throws ProtocollazioneException,
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
            throw new ProtocollazioneException("Non e' ammesso assegnare fascicoli secondari senza assegnare un Fascicolo primario");
        }

        //nuova routine delta
        fascicoli_delta = DocerUtils.getFascicoliDelta(fascicoli_new, fascicoli_current);
//
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

    // private void checkFascicoliUserRights(String userId, String
    // ticketDocumentale, String cod_ente, String cod_aoo, List<String>
    // fascicoli_delta) throws ProtocollazioneException {
    //
    // for (String f : fascicoli_delta) {
    //
    // DocerServicesStub.KeyValuePair[] kvp =
    // fascicoloUtils.toDocerKeyValuePairArray(cod_ente, cod_aoo, f);
    //
    // checkUserRights(kvp, ticketDocumentale, userId,
    // EnumACLRights.normalAccess, "FASCICOLO");
    // }
    //
    // }

    private void checkUserRights(KeyValuePair[] id, String ticketDocumentale, String userId, EnumACLRights minimumRight, String type) throws ProtocollazioneException {

        DocerServicesStub.KeyValuePair[] kvp = fascicoloUtils.toDocerKeyValuePairArray(id);

        checkUserRights(kvp, ticketDocumentale, userId, minimumRight, type);
    }

    private void checkUserRights(DocerServicesStub.KeyValuePair[] id, String ticketDocumentale, String userId, EnumACLRights minimumRight, String type) throws ProtocollazioneException {

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
            throw new ProtocollazioneException(e.getMessage());
        }

        int userRight = -1;

        if (getUserRightsAnagraficheResponse != null && getUserRightsAnagraficheResponse.get_return() > -1) {
            userRight = getUserRightsAnagraficheResponse.get_return();
        }

        // 0 fullAccess
        // 1 normalAccess
        // 2 readOnly

        if (userRight < 0) {
            throw new ProtocollazioneException(type.toLowerCase() + " " + fascicoloUtils.toReadableString(id) + " non trovato nel documentale");
        }

        // la verifica va fatta al contrario...
        if (userRight > minimumRight.getCode()) {
            throw new ProtocollazioneException("l'utente " + userId + " non ha diritti sufficienti sul " + type.toLowerCase() + " " + fascicoloUtils.toReadableString(id) + " nel documentale");
        }

    }

    private void checkUserRights(long docId, String ticketDocumentale, String userId, EnumACLRights minimumRight) throws ProtocollazioneException, RemoteException, DocerExceptionException {

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
            throw new ProtocollazioneException("documento " + docId + " non trovato nel documentale");
        }

        // la verifica va fatta al contrario...
        if (userRight > minimumRight.getCode()) {
            throw new ProtocollazioneException("l'utente " + userId + " non ha diritti sufficienti sul documento " + docId + " nel documentale");
        }

    }

    private String getValueFromKey(DocerServicesStub.KeyValuePair[] profile, String key) {
        for (DocerServicesStub.KeyValuePair kvp : profile) {
            if (kvp.getKey().equalsIgnoreCase(key)) {
                return kvp.getValue();
            }
        }

        return null;
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
            logger.error("initFascicoloUtils", e);
        }

    }

}
