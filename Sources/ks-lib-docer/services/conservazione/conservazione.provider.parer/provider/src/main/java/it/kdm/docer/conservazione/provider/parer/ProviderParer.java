package it.kdm.docer.conservazione.provider.parer;

import it.eng.parer.ccd.lib.IParerLib;
import it.eng.parer.ws.versamento.dto.FileBinario;
import it.eng.parer.ws.xml.versReq.*;
import it.eng.parer.ws.xml.versReq.types.TipoConservazioneType;
import it.eng.parer.ws.xml.versReq.types.TipoSupportoType;
import it.eng.parer.ws.xml.versResp.EsitoVersAggAllegati;
import it.eng.parer.ws.xml.versResp.EsitoVersamento;
import it.kdm.docer.conservazione.*;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.exolab.castor.types.AnyNode;
import org.exolab.castor.types.Date;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.jaxen.JaxenException;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class ProviderParer implements Provider {

    private IParerLib parerLib;
    private EntePasswordManager entePasswordManager;
    private boolean simulate;

    public IParerLib getParerLib() {
        return parerLib;
    }

    public void setParerLib(IParerLib parerLib) {
        this.parerLib = parerLib;
    }

    public EntePasswordManager getEntePasswordManager() {
        return entePasswordManager;
    }

    public void setEntePasswordManager(EntePasswordManager entePasswordManager) {
        this.entePasswordManager = entePasswordManager;
    }

    public boolean isSimulate() {
        return simulate;
    }

    public void setSimulate(boolean simulate) {
        this.simulate = simulate;
    }

    public ConservazioneResult versamento(OMElement xmlDocument, File[] files,
                                          TipoConservazione type, boolean forzaAccettazione,
                                          boolean forzaConservazione, boolean forzaCollegamento) throws ConservazioneException {

        try {

            UnitaDocumentaria ud;

            //if(!bypassTransformations) {
            //	ud = UnitaDocumentaria.unmarshal(new StringReader(xmlDocument.toString()));
            //} else {

            ud = new UnitaDocumentaria();

            UnitaDocumentariaIntestazione intestazione = new UnitaDocumentariaIntestazione();

            intestazione.setVersione(this.getChild("versione", xmlDocument).getText());

            Versatore versatore = new Versatore();
            String codiceEnte = this.getChild("codiceEnte", xmlDocument)
                    .getText();
            versatore.setEnte(codiceEnte); // COD_ENTE
            String codiceAoo = this.getChild("codiceAoo", xmlDocument)
                    .getText();
            versatore.setStruttura(codiceAoo); // COD_AOO

            versatore.setAmbiente(this.getChild("ambiente", xmlDocument)
                    .getText());

            intestazione.setVersatore(versatore);

            Chiave chiave = new Chiave();
            chiave.setNumero(this.getChild("numeroChiave", xmlDocument).getText()); // NUM_PG
            chiave.setAnno(Long.parseLong(this.getChild("annoChiave", xmlDocument)
                    .getText())); // ANNO_PG
            chiave.setTipoRegistro(this.getChild("registroChiave", xmlDocument)
                    .getText()); // REGISTRO_PG

            intestazione.setChiave(chiave);

            intestazione.setTipologiaUnitaDocumentaria(this.getChild(
                    "tipologia", xmlDocument).getText());

            ud.setIntestazione(intestazione);

            UnitaDocumentariaConfigurazione configurazione = new UnitaDocumentariaConfigurazione();

            OMElement conf = this.getChildOrNull("configurazione", xmlDocument);
            if (conf != null) {
                OMElement forzaAccettazioneElem = this.getChildOrNull(
                        "forzaAccettazione", conf);
                OMElement forzaConservazioneElem = this.getChildOrNull(
                        "forzaConservazione", conf);
                OMElement forzaCollegamentoElem = this.getChildOrNull(
                        "forzaCollegamento", conf);

                if (forzaAccettazioneElem != null) {
                    forzaAccettazione |= Boolean
                            .parseBoolean(forzaAccettazioneElem.getText());
                }

                if (forzaConservazioneElem != null) {
                    forzaConservazione |= Boolean
                            .parseBoolean(forzaConservazioneElem.getText());
                }

                if (forzaCollegamentoElem != null) {
                    forzaCollegamento |= Boolean
                            .parseBoolean(forzaCollegamentoElem.getText());
                }
            }

            configurazione.setForzaAccettazione(forzaAccettazione); // PARAMETRO
            configurazione.setForzaConservazione(forzaConservazione); // PARAMETRO
            configurazione.setForzaCollegamento(forzaCollegamento); // PARAMETRO

            ud.setConfigurazione(configurazione);

            ProfiloArchivistico profiloArch = new ProfiloArchivistico();

            OMElement fascicoloPrincipale = this.getChildOrNull(
                    "fascicoloPrincipale", xmlDocument);
            if (fascicoloPrincipale != null
                    && fascicoloPrincipale.getChildElements().hasNext()) {
                FascicoloPrincipale fascicoloP = new FascicoloPrincipale();

                OMElement classifica = this.getChildOrNull("codiceTitolario",
                        fascicoloPrincipale);
                if (classifica != null) {
                    fascicoloP.setClassifica(classifica.getText()); // COD_TITOLARIO
                }

                OMElement identificativo = this.getChildOrNull(
                        "codiceFascicolo", fascicoloPrincipale);
                if (identificativo != null) {
                    Fascicolo fascicolo = new Fascicolo();
                    fascicolo.setIdentificativo(identificativo.getText()); // COD_FASCICOLO
                    fascicolo.setOggetto(this.getChild("descrizioneFascicolo",
                            fascicoloPrincipale).getText()); // DES_FASCICOLO
                    fascicoloP.setFascicolo(fascicolo);
                }

                OMElement sottoFascicoloElem = this.getChildOrNull(
                        "sottoFascicolo", fascicoloPrincipale);
                if (sottoFascicoloElem != null
                        && sottoFascicoloElem.getChildElements().hasNext()) {
                    identificativo = this.getChildOrNull("codiceFascicolo",
                            sottoFascicoloElem);
                    if (identificativo != null) {
                        SottoFascicolo sottoFascicolo = new SottoFascicolo();
                        sottoFascicolo.setIdentificativo(identificativo
                                .getText()); // COD_SOTTOFASCICOLO
                        sottoFascicolo.setOggetto(this.getChild(
                                "descrizioneFascicolo", fascicoloPrincipale)
                                .getText()); // DES_SOTTOFASCICOLO
                        fascicoloP.setSottoFascicolo(sottoFascicolo);
                    }
                }

                profiloArch.setFascicoloPrincipale(fascicoloP);

                // Viene aggiunto solo se esiste un fascicolo principale
                ud.setProfiloArchivistico(profiloArch);
            }

            // Ciclo sui fascicoli secondari
            List<?> fascicoliSecondari = this.xPathSelectNodes(
                    "fascicoloSecondario", xmlDocument);
            if (!fascicoliSecondari.isEmpty()) {
                FascicoliSecondari fascicoliS = new FascicoliSecondari();

                for (Object fs : fascicoliSecondari) {
                    OMElement fascicoloSecondario = (OMElement) fs;
                    FascicoloSecondario fascicoloS = new FascicoloSecondario();
                    OMElement classifica = this.getChildOrNull("codiceTitolario",
                            fascicoloSecondario);
                    if (classifica != null) {
                        fascicoloS.setClassifica(classifica.getText()); // COD_TITOLARIO
                        // (secondario)
                    }

                    OMElement identificativo = this.getChildOrNull(
                            "codiceFascicolo", fascicoloSecondario);
                    if (identificativo != null) {
                        Fascicolo fascicolo = new Fascicolo();
                        fascicolo.setIdentificativo(identificativo.getText()); // COD_FASCICOLO
                        // (secondario)
                        fascicolo.setOggetto(this.getChild("descrizioneFascicolo",
                                fascicoloSecondario).getText()); // DES_FASCICOLO
                        // (secondario)
                        fascicoloS.setFascicolo(fascicolo);
                    }

                    OMElement sottoFascicoloElem = this.getChildOrNull(
                            "sottoFascicolo", fascicoloSecondario);
                    if (sottoFascicoloElem != null
                            && sottoFascicoloElem.getChildElements().hasNext()) {
                        SottoFascicolo sottoFascicolo = new SottoFascicolo();

                        identificativo = this.getChildOrNull("codiceFascicolo",
                                sottoFascicoloElem);
                        if (identificativo != null) {
                            sottoFascicolo.setIdentificativo(identificativo
                                    .getText()); // COD_SOTTOFASCICOLO
                            // (secondario)
                            sottoFascicolo.setOggetto(this.getChild(
                                    "descrizioneFascicolo", sottoFascicoloElem)
                                    .getText()); // DES_SOTTOFASCICOLO
                            // (secondario)
                            fascicoloS.setSottoFascicolo(sottoFascicolo);
                        }
                    }

                    fascicoliS.addFascicoloSecondario(fascicoloS);
                }

                profiloArch.setFascicoliSecondari(fascicoliS);
            }

            OMElement oggettoElem = this.getChildOrNull("oggetto", xmlDocument);
            if (oggettoElem != null) {
                ProfiloUnitaDocumentaria profiloUD = new ProfiloUnitaDocumentaria();
                profiloUD.setOggetto(oggettoElem.getText()); // OGGETTO_PG
                OMElement dateElem = this.getChildOrNull("data", xmlDocument);
                if (dateElem != null && !dateElem.getText().equals("")) {
                    Date date = Date.parseDate(dateElem.getText());
                    profiloUD.setData(date);
                }

                ud.setProfiloUnitaDocumentaria(profiloUD);
            }

            OMElement datiSpecificiElem = this.getChildOrNull("datiSpecifici", xmlDocument);
            if (datiSpecificiElem != null) {
                ud.setDatiSpecifici(parseDatiSpecifici(datiSpecificiElem));
            }

            DocumentiCollegati dc = new DocumentiCollegati();

            // Ciclare sui documenti collegati
            List<?> documentiCollegati = this.xPathSelectNodes(
                    "documentoCollegato", xmlDocument);
            for (Object dcObj : documentiCollegati) {
                OMElement documentoCollegato = (OMElement) dcObj;
                DocumentoCollegato doc = new DocumentoCollegato();

                ChiaveCollegamento chiaveColl = new ChiaveCollegamento();
                chiaveColl.setNumero(this.getChild("numeroChiave",
                        documentoCollegato).getText()); // NUM_PG
                chiaveColl.setAnno(Long.parseLong(this.getChild("annoChiave",
                        documentoCollegato).getText())); // ANNO_PG
                chiaveColl.setTipoRegistro(this.getChild("registroChiave",
                        documentoCollegato).getText()); // REGISTRO_PG

                doc.setChiaveCollegamento(chiaveColl);
                doc.setDescrizioneCollegamento(this.getChild(
                        "descrizioneCollegamento", documentoCollegato)
                        .getText());

                dc.addDocumentoCollegato(doc);
            }

            if (documentiCollegati.size() > 0) {
                ud.setDocumentiCollegati(dc);
            }

            // Questi saranno da calcolare in base alle occorrenze
            List<?> allegati = this.xPathSelectNodes("allegato", xmlDocument);
            ud.setNumeroAllegati(allegati.size());

            List<?> annessi = this.xPathSelectNodes("annesso", xmlDocument);
            ud.setNumeroAnnessi(annessi.size());

            List<?> annotazioni = this.xPathSelectNodes("annotazione",
                    xmlDocument);
            ud.setNumeroAnnotazioni(annotazioni.size());

            DocumentoPrincipale dp = new DocumentoPrincipale();
            OMElement documento = this.getChild("documento", xmlDocument);
            dp.setIDDocumento(this.getChild("docNum", documento).getText()); // DOCNUM
            dp.setTipoDocumento(this.getChild("typeId", documento).getText()); // TYPE_ID

            OMElement autoreElem = this.getChildOrNull("authorId", documento);
            if (autoreElem != null) {
                ProfiloDocumento profiloD = new ProfiloDocumento();
                profiloD.setAutore(autoreElem.getText()); // AUTHOR_ID
                profiloD.setDescrizione(this.getChild("description", documento)
                        .getText()); // DESCRIPTION

                dp.setProfiloDocumento(profiloD);
            }

            // Metadati custom da mappare in configurazione

            datiSpecificiElem = this.getChildOrNull("datiSpecifici",
                    documento);
            if (datiSpecificiElem != null) {
                dp.setDatiSpecifici(parseDatiSpecifici(datiSpecificiElem));
            }

            OMElement datiFiscali = this.getChildOrNull("datiFiscali",
                    documento);
            if (datiFiscali != null) {
                DatiFiscali df = getDatiFiscali(datiFiscali);

                dp.setDatiFiscali(df);
            }

            StrutturaOriginale so = new StrutturaOriginale();
            so.setTipoStruttura(this.getChild("tipoStruttura", documento)
                    .getText());

            Componenti comps = new Componenti();
            List<?> componenti = this.xPathSelectNodes("componente", documento);
            for (Object compElem : componenti) {
                OMElement componente = (OMElement) compElem;
                Componente comp = this.makeComponente(componente);
                comps.addComponente(comp);
            }

            so.setComponenti(comps);

            dp.setStrutturaOriginale(so);

            ud.setDocumentoPrincipale(dp);

            Allegati attachments = new Allegati();

            // Ciclare per ciascun allegato
            for (Object allegatoObj : allegati) {
                OMElement allegatoElem = (OMElement) allegatoObj;

                Allegato allegato = new Allegato();
                // IDENTICO A DOCUMENTO PRINCIPALE

                allegato.setIDDocumento(this.getChild("docNum", allegatoElem)
                        .getText()); // DOCNUM
                allegato.setTipoDocumento(this.getChild("typeId", allegatoElem)
                        .getText()); // TYPE_ID

                autoreElem = this.getChildOrNull("authorId", allegatoElem);
                if (autoreElem != null) {
                    ProfiloDocumento profiloD = new ProfiloDocumento();
                    profiloD.setAutore(autoreElem.getText()); // AUTHOR_ID
                    profiloD.setDescrizione(this.getChild("description",
                            allegatoElem).getText()); // DESCRIPTION

                    allegato.setProfiloDocumento(profiloD);
                }
                // Metadati custom da mappare in configurazione
                datiSpecificiElem = this.getChildOrNull("datiSpecifici",
                        allegatoElem);
                if (datiSpecificiElem != null) {
                    allegato.setDatiSpecifici(parseDatiSpecifici(datiSpecificiElem));
                }

                datiFiscali = this.getChildOrNull("datiFiscali", allegatoElem);
                if (datiFiscali != null) {
                    DatiFiscali df = getDatiFiscali(datiFiscali);

                    allegato.setDatiFiscali(df);
                }

                so = new StrutturaOriginale();
                so.setTipoStruttura(this
                        .getChild("tipoStruttura", allegatoElem).getText());

                comps = new Componenti();
                componenti = this.xPathSelectNodes("componente", allegatoElem);
                for (Object compElem : componenti) {
                    OMElement componente = (OMElement) compElem;
                    Componente comp = this.makeComponente(componente);
                    comps.addComponente(comp);
                }

                so.setComponenti(comps);

                allegato.setStrutturaOriginale(so);

                attachments.addAllegato(allegato);
            }
            if (allegati.size() > 0) {
                ud.setAllegati(attachments);
            }

            Annessi ann = new Annessi();

            // Ciclare per ciascun annesso
            for (Object annessoObj : annessi) {
                OMElement annessoElem = (OMElement) annessoObj;

                Annesso annesso = new Annesso();
                // IDENTICO A DOCUMENTO PRINCIPALE

                annesso.setIDDocumento(this.getChild("docNum", annessoElem)
                        .getText()); // DOCNUM
                annesso.setTipoDocumento(this.getChild("typeId", annessoElem)
                        .getText()); // TYPE_ID

                autoreElem = this.getChildOrNull("authorId", annessoElem);
                if (autoreElem != null) {
                    ProfiloDocumento profiloD = new ProfiloDocumento();
                    profiloD.setAutore(autoreElem.getText()); // AUTHOR_ID
                    profiloD.setDescrizione(this.getChild("description",
                            annessoElem).getText()); // DESCRIPTION

                    annesso.setProfiloDocumento(profiloD);
                }
                // Metadati custom da mappare in configurazione
                datiSpecificiElem = this.getChildOrNull("datiSpecifici",
                        annessoElem);
                if (datiSpecificiElem != null) {
                    annesso.setDatiSpecifici(parseDatiSpecifici(datiSpecificiElem));
                }

                datiFiscali = this.getChildOrNull("datiFiscali", annessoElem);
                if (datiFiscali != null) {
                    DatiFiscali df = getDatiFiscali(datiFiscali);

                    annesso.setDatiFiscali(df);
                }

                so = new StrutturaOriginale();
                so.setTipoStruttura(this.getChild("tipoStruttura", annessoElem)
                        .getText());

                comps = new Componenti();
                componenti = this.xPathSelectNodes("componente", annessoElem);
                for (Object compElem : componenti) {
                    OMElement componente = (OMElement) compElem;
                    Componente comp = this.makeComponente(componente);
                    comps.addComponente(comp);
                }

                so.setComponenti(comps);

                annesso.setStrutturaOriginale(so);

                ann.addAnnesso(annesso);
            }

            if (annessi.size() > 0) {
                ud.setAnnessi(ann);
            }

            Annotazioni annot = new Annotazioni();
            // Ciclare per ciascuna annotazione

            for (Object annotObj : annotazioni) {
                OMElement annotElem = (OMElement) annotObj;

                Annotazione annotazione = new Annotazione();
                // IDENTICO A DOCUMENTO PRINCIPALE

                annotazione.setIDDocumento(this.getChild("docNum", annotElem)
                        .getText()); // DOCNUM
                annotazione.setTipoDocumento(this.getChild("typeId", annotElem)
                        .getText()); // TYPE_ID

                autoreElem = this.getChildOrNull("authorId", annotElem);
                if (autoreElem != null) {
                    ProfiloDocumento profiloD = new ProfiloDocumento();
                    profiloD.setAutore(autoreElem.getText()); // AUTHOR_ID
                    profiloD.setDescrizione(this.getChild("description",
                            annotElem).getText()); // DESCRIPTION

                    annotazione.setProfiloDocumento(profiloD);
                }
                // Metadati custom da mappare in configurazione
                datiSpecificiElem = this.getChildOrNull("datiSpecifici",
                        annotElem);
                if (datiSpecificiElem != null) {
                    annotazione.setDatiSpecifici(parseDatiSpecifici(datiSpecificiElem));
                }

                datiFiscali = this.getChildOrNull("datiFiscali", annotElem);
                if (datiFiscali != null) {
                    DatiFiscali df = getDatiFiscali(datiFiscali);

                    annotazione.setDatiFiscali(df);
                }

                so = new StrutturaOriginale();
                so.setTipoStruttura(this.getChild("tipoStruttura", annotElem)
                        .getText());

                comps = new Componenti();
                componenti = this.xPathSelectNodes("componente", annotElem);
                for (Object compElem : componenti) {
                    OMElement componente = (OMElement) compElem;
                    Componente comp = this.makeComponente(componente);
                    comps.addComponente(comp);
                }

                so.setComponenti(comps);

                annotazione.setStrutturaOriginale(so);

                annot.addAnnotazione(annotazione);
            }

            if (annotazioni.size() > 0) {
                ud.setAnnotazioni(annot);
            }
            //}


            Vector<FileBinario> fileBinari = new Vector<FileBinario>();
            for (File file : files) {
                FileBinario fb = new FileBinario();
                fb.setFileSuDisco(file.getFile());
                fb.setId(file.getId());
                fb.setDimensione(file.getFile().length());
                fileBinari.add(fb);
            }

            //Versatore versatore = ud.getIntestazione().getVersatore();

            String username = entePasswordManager.getUsername(versatore.getEnte(),
                    versatore.getStruttura());
            String password = entePasswordManager.getPassword(versatore.getEnte(),
                    versatore.getStruttura());

            versatore.setUserID(username);

            //Configurazione configurazione = ud.getConfigurazione();

            switch (type) {
                case SOSTITUTIVA:
                    configurazione.setTipoConservazione(TipoConservazioneType.SOSTITUTIVA);
                    break;
                case FISCALE:
                    configurazione.setTipoConservazione(TipoConservazioneType.FISCALE);
                    break;
                default:
                    throw new ConservazioneException("Tipo specificato non valido");
            }

            configurazione.setForzaConservazione(configurazione.getForzaConservazione()
                    | forzaConservazione);
            configurazione.setForzaCollegamento(configurazione.getForzaCollegamento()
                    | forzaCollegamento);
            configurazione.setForzaAccettazione(configurazione.getForzaAccettazione()
                    | forzaAccettazione);

            if (simulate) {
                configurazione.setSimulaSalvataggioDatiInDB(true);
            }

            EsitoVersamento esito = this.parerLib.versamento(username,
                    password, ud, fileBinari);

            // forzo una chiamata singola per un errore di persistenza su eclispe link
            // codice da rimuovere quando verrà risolto da PARER il problema
            if (esito.getEsitoGenerale().getCodiceErrore().equalsIgnoreCase("666P")) {
                esito = this.parerLib.versamento(username,
                        password, ud, fileBinari);
            }

            ConservazioneResult result =
                    this.getResult(ud, esito);

            return result;


        } catch (Exception e) {
            throw new ConservazioneException(e);
        }
    }

    private DatiSpecifici parseDatiSpecifici(OMElement datiSpecificiElem) {
        DatiSpecifici datiSpecifici = new DatiSpecifici();
        Iterator<?> iter = datiSpecificiElem.getChildElements();
        while (iter.hasNext()) {
            OMElement currentElem = (OMElement) iter.next();
            String name = currentElem.getLocalName();
            if (name.equals("VersioneDatiSpecifici")) {
                datiSpecifici.setVersioneDatiSpecifici(currentElem
                        .getText());
            } else {
                AnyNode node = new AnyNode(AnyNode.ELEMENT, name,
                        null, null, null);
                node.addAnyNode(new AnyNode(AnyNode.TEXT,
                        null, null, null, currentElem.getText()));
                datiSpecifici.addAnyObject(node);
            }
        }
        return datiSpecifici;
    }

    private ConservazioneResult getResult(UnitaDocumentaria ud, EsitoVersamento esito) throws MarshalException, ValidationException, IOException {

        ConservazioneResult result = new ConservazioneResult();

        switch (esito.getEsitoGenerale().getCodiceEsito()) {
            case POSITIVO:
                result.setEsito(EsitoConservazione.POSITIVO.toString());
                break;
            case WARNING:
                result.setEsito(EsitoConservazione.WARNING.toString());
                break;
            case NEGATIVO:
                result.setEsito(EsitoConservazione.ERRORE.toString());
                break;
        }

        result.setErrorCode(esito.getEsitoGenerale().getCodiceErrore());
        result.setErrorMessage(esito.getEsitoGenerale().getMessaggioErrore());

        result.setXmlChiamata(this.getXmlString(ud));
        result.setXmlEsitoVersamento(this.getXmlString(esito));

        return result;
    }

    private ConservazioneResult getResult(UnitaDocAggAllegati ud, EsitoVersAggAllegati esito) throws MarshalException, ValidationException, IOException {

        ConservazioneResult result = new ConservazioneResult();

        switch (esito.getEsitoGenerale().getCodiceEsito()) {
            case POSITIVO:
                result.setEsito(EsitoConservazione.POSITIVO.toString());
                break;
            case WARNING:
                result.setEsito(EsitoConservazione.WARNING.toString());
                break;
            case NEGATIVO:
                result.setEsito(EsitoConservazione.ERRORE.toString());
                break;
        }

        result.setErrorCode(esito.getEsitoGenerale().getCodiceErrore());
        result.setErrorMessage(esito.getEsitoGenerale().getMessaggioErrore());

        result.setXmlChiamata(this.getXmlString(ud));
        result.setXmlEsitoVersamento(this.getXmlString(esito));

        return result;
    }

    private String getXmlString(Object obj) throws MarshalException, ValidationException, IOException {
        StringWriter writer = new StringWriter();
        Marshaller.marshal(obj, writer);
        String ret = writer.toString();
        writer.close();

        return ret;
    }

    private DatiFiscali getDatiFiscali(OMElement datiFiscali)
            throws ConservazioneException, ParseException {
        DatiFiscali df = new DatiFiscali();

        OMElement denominazione = this.getChildOrNull("denominazione",
                datiFiscali);
        if (denominazione != null) {
            df.setDenominazione(denominazione.getText());
        }

        OMElement nome = this.getChildOrNull("nome", datiFiscali);
        if (nome != null) {
            df.setNome(nome.getText());
        }

        OMElement cognome = this.getChildOrNull("cognome", datiFiscali);
        if (cognome != null) {
            df.setCognome(cognome.getText());
        }

        OMElement cf = this.getChildOrNull("cf", datiFiscali);
        if (cf != null) {
            df.setCF(cf.getText());
        }

        OMElement piva = this.getChildOrNull("pIVA", datiFiscali);
        if (piva != null) {
            df.setPIVA(piva.getText());
        }

        String dateText = this.getChild("dataEmissione", datiFiscali).getText();
        if (!dateText.equals("")) {
            df.setDataEmissione(Date.parseDate(dateText));
        }

        df.setNumeroProgressivo(Long.parseLong(this.getChild(
                "numeroProgressivo", datiFiscali).getText()));
        df.setRegistro(this.getChild("registro", datiFiscali).getText());
        df.setPeriodoFiscale(this.getChild("periodoFiscale", datiFiscali)
                .getText());

        dateText = this.getChild("dataTermineEmissione", datiFiscali).getText();
        if (!dateText.equals("")) {
            df.setDataTermineEmissione(Date.parseDate(dateText));
        }
        return df;
    }

    private Componente makeComponente(OMElement compElem)
            throws ConservazioneException, JaxenException, ParseException {
        Componente comp = new Componente();
        comp.setID(this.getChild("id", compElem).getText());
        comp.setOrdinePresentazione(Long.parseLong(this.getChild(
                "ordinePresentazione", compElem).getText()));
        comp.setTipoComponente(this.getChild("tipoComponente", compElem)
                .getText());

        String tipoSupporto = this.getChild("tipoSupportoComponente", compElem)
                .getText();
        if (tipoSupporto.equals("FILE")) {
            comp.setTipoSupportoComponente(TipoSupportoType.FILE);
        } else if (tipoSupporto.equals("METADATI")) {
            comp.setTipoSupportoComponente(TipoSupportoType.METADATI);
        } else {
            throw new ConservazioneException(
                    "tipoSupportoComponente non valido: " + tipoSupporto);
        }

        OMElement riferimento = this.getChildOrNull("riferimento", compElem);
        if (riferimento != null && riferimento.getChildElements().hasNext()) {
            Riferimento rifC = new Riferimento();
            rifC.setAnno(Long.parseLong(this.getChild("annoChiave", riferimento)
                    .getText()));
            rifC.setNumero(this.getChild("numeroChiave", riferimento).getText());
            rifC.setTipoRegistro(this.getChild("registroChiave", riferimento)
                    .getText());

            comp.setRiferimento(rifC);
        }

        OMElement currentElem = this.getChildOrNull(
                "tipoRappresentazioneComponente", compElem);
        if (currentElem != null) {
            comp.setTipoRappresentazioneComponente(currentElem.getText());
        }

        currentElem = this.getChildOrNull("nomeComponente", compElem);
        if (currentElem != null) {
            comp.setNomeComponente(currentElem.getText());
        }

        currentElem = this.getChildOrNull("formatoFileVersato", compElem);
        if (currentElem != null && !currentElem.getText().isEmpty()) {
            comp.setFormatoFileVersato(currentElem.getText());
        } else {
            comp.setFormatoFileVersato("err");
        }

        currentElem = this.getChildOrNull("hashVersato", compElem);
        if (currentElem != null) {
            comp.setHashVersato(currentElem.getText());
        }

        currentElem = this.getChildOrNull("urnVersato", compElem);
        if (currentElem != null) {
            comp.setUrnVersato(currentElem.getText());
        }

        currentElem = this.getChildOrNull("idComponenteVersato", compElem);
        if (currentElem != null) {
            comp.setIDComponenteVersato(currentElem.getText());
        }

        currentElem = this.getChildOrNull("utilizzoDataFirmaPerRifTemp", compElem);
        if (currentElem != null && !currentElem.getText().equals("")) {
            comp.setUtilizzoDataFirmaPerRifTemp(Boolean.parseBoolean(currentElem.getText()));
        }

        currentElem = this.getChildOrNull("riferimentoTemporale", compElem);
        if (currentElem != null && !currentElem.getText().equals("")) {
            String dateText = currentElem.getText();
            Date date = Date.parseDate(dateText);
            comp.setRiferimentoTemporale(date.toDate());
        }

        currentElem = this.getChildOrNull("descrizioneRiferimentoTemporale",
                compElem);
        if (currentElem != null) {
            comp.setDescrizioneRiferimentoTemporale(currentElem.getText());
        }

        // iterare per ciascun sottocomponente
        List<?> subComps = this.xPathSelectNodes("sottoComponente", compElem);
        if (subComps.size() > 0) {
            SottoComponenti sottoComps = new SottoComponenti();
            for (Object subCompObj : subComps) {
                OMElement subComp = (OMElement) subCompObj;
                SottoComponente sottoComp = new SottoComponente();
                // IDENTICO A COMPONENTE

                sottoComp.setID(this.getChild("id", subComp).getText());
                sottoComp.setOrdinePresentazione(Long.parseLong(this.getChild(
                        "ordinePresentazione", subComp).getText()));
                sottoComp.setTipoComponente(this.getChild("tipoComponente",
                        subComp).getText());

                tipoSupporto = this.getChild("tipoSupportoComponente", subComp)
                        .getText();
                if (tipoSupporto.equals("FILE")) {
                    sottoComp.setTipoSupportoComponente(TipoSupportoType.FILE);
                } else if (tipoSupporto.equals("METADATI")) {
                    sottoComp
                            .setTipoSupportoComponente(TipoSupportoType.METADATI);
                } else {
                    throw new ConservazioneException(
                            "tipoSupportoComponente non valido: "
                                    + tipoSupporto);
                }

                currentElem = this.getChildOrNull("nomeComponente", subComp);
                if (currentElem != null) {
                    sottoComp.setNomeComponente(currentElem.getText());
                }

                currentElem = this
                        .getChildOrNull("formatoFileVersato", subComp);
                if (currentElem != null) {
                    sottoComp.setFormatoFileVersato(currentElem.getText());
                }

                currentElem = this.getChildOrNull("urnVersato", subComp);
                if (currentElem != null) {
                    sottoComp.setUrnVersato(currentElem.getText());
                }

                currentElem = this.getChildOrNull("idComponenteVersato",
                        subComp);
                if (currentElem != null) {
                    sottoComp.setIDComponenteVersato(currentElem.getText());
                }

                sottoComps.addSottoComponente(sottoComp);
            }

            comp.setSottoComponenti(sottoComps);
        }

        return comp;
    }

    protected final OMElement xPathSelectSingleNode(String xpath, OMElement node)
            throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(node, xpath);
        return (OMElement) path.selectSingleNode(node);
    }

    protected final List<?> xPathSelectNodes(String xpath, OMElement node)
            throws JaxenException {
        AXIOMXPath path = new AXIOMXPath(node, xpath);
        return path.selectNodes(node);
    }

    protected final OMElement getChild(String nodeName, OMElement node)
            throws ConservazioneException {
        Iterator<?> nodes = node.getChildrenWithName(new QName(nodeName));
        if (!nodes.hasNext()) {
            throw new ConservazioneException(String.format(
                    "Node %s not found in node %s.", nodeName,
                    node.getLocalName()));
        }

        return (OMElement) nodes.next();
    }

    protected final OMElement getChildOrNull(String nodeName, OMElement node) {
        Iterator<?> nodes = node.getChildrenWithName(new QName(nodeName));
        if (!nodes.hasNext()) {
            return null;
        }

        return (OMElement) nodes.next();
    }

    @Override
    public ConservazioneResult modificaMetadati(OMElement xmlDocument,
                                                TipoConservazione tipo) throws ConservazioneException {

        try {
            UnitaDocAggAllegati ud = new UnitaDocAggAllegati();

            Intestazione intestazione = new Intestazione();

            intestazione.setVersione(this.getChild("versione", xmlDocument).getText());

            Versatore versatore = new Versatore();
            String codiceEnte = this.getChild("codiceEnte", xmlDocument)
                    .getText();
            versatore.setEnte(codiceEnte); // COD_ENTE
            String codiceAoo = this.getChild("codiceAoo", xmlDocument)
                    .getText();
            versatore.setStruttura(codiceAoo); // COD_AOO

            versatore.setAmbiente(this.getChild("ambiente", xmlDocument)
                    .getText());

            intestazione.setVersatore(versatore);

            Chiave chiave = new Chiave();
            chiave.setNumero(this.getChild("numeroChiave", xmlDocument).getText()); // NUM_PG
            chiave.setAnno(Long.parseLong(this.getChild("annoChiave", xmlDocument)
                    .getText())); // ANNO_PG
            chiave.setTipoRegistro(this.getChild("registroChiave", xmlDocument)
                    .getText()); // REGISTRO_PG

            intestazione.setChiave(chiave);

			/*intestazione.setTipologiaUnitaDocumentaria(this.getChild(
					"tipologia", xmlDocument).getText());*/

            ud.setIntestazione(intestazione);

            Configurazione configurazione = new Configurazione();

            configurazione.setForzaAccettazione(true); // PARAMETRO
            configurazione.setForzaConservazione(true); // PARAMETRO

            ud.setConfigurazione(configurazione);

            UnitaDocAggAllegatiChoice choice = new UnitaDocAggAllegatiChoice();
            Annesso annesso = new Annesso();

            OMElement documento = this.getChild("documento", xmlDocument);

            String idDocumento = String.format("%s|%s",
                    this.getChild("docNum", documento).getText(),
                    UUID.randomUUID());

            annesso.setIDDocumento(idDocumento);
            annesso.setTipoDocumento(this.getChild("typeId", documento).getText());

            OMElement autoreElem = this.getChildOrNull("authorId", documento);
            if (autoreElem != null) {
                ProfiloDocumento profiloD = new ProfiloDocumento();
                profiloD.setAutore(autoreElem.getText()); // AUTHOR_ID
                profiloD.setDescrizione(this.getChild("description", documento)
                        .getText()); // DESCRIPTION

                annesso.setProfiloDocumento(profiloD);
            }

            OMElement datiSpecificiElem = this.getChildOrNull("datiSpecifici",
                    documento);
            if (datiSpecificiElem != null) {
                annesso.setDatiSpecifici(parseDatiSpecifici(datiSpecificiElem));
            }

            OMElement datiFiscali = this.getChildOrNull("datiFiscali",
                    documento);
            if (datiFiscali != null) {
                DatiFiscali df = getDatiFiscali(datiFiscali);

                annesso.setDatiFiscali(df);
            }

            StrutturaOriginale so = new StrutturaOriginale();

            Componenti ci = new Componenti();

            Componente co = new Componente();

            co.setID(UUID.randomUUID().toString());
            co.setOrdinePresentazione(1);
            //co.setTipoComponente("token");
            co.setTipoSupportoComponente(TipoSupportoType.METADATI);

            ci.addComponente(co);

            so.setComponenti(ci);

            annesso.setStrutturaOriginale(so);

            choice.setAnnesso(annesso);

            ud.setUnitaDocAggAllegatiChoice(choice);

            String username = entePasswordManager.getUsername(versatore.getEnte(),
                    versatore.getStruttura());
            String password = entePasswordManager.getPassword(versatore.getEnte(),
                    versatore.getStruttura());

            versatore.setUserID(username);

            switch (tipo) {
                case SOSTITUTIVA:
                    configurazione.setTipoConservazione(TipoConservazioneType.SOSTITUTIVA);
                    break;
                case FISCALE:
                    configurazione.setTipoConservazione(TipoConservazioneType.FISCALE);
                    break;
                default:
                    throw new ConservazioneException("Tipo specificato non valido");
            }

            if (simulate) {
                configurazione.setSimulaSalvataggioDatiInDB(true);
            }

            EsitoVersAggAllegati esito = this.parerLib.modificaMetadati(username,
                    password, ud);

            // forzo una chiamata singola per un errore di persistenza su eclispe link
            // codice da rimuovere quando verrà risolto da PARER il problema
            if (esito.getEsitoGenerale().getCodiceErrore().equalsIgnoreCase("666P")) {
                esito = this.parerLib.modificaMetadati(username,
                        password, ud);
            }

            ConservazioneResult result =
                    this.getResult(ud, esito);

            return result;

        } catch (Exception e) {
            throw new ConservazioneException(e);
        }
    }

    @Override
    public ConservazioneResult aggiungiDocumento(OMElement xmlDocument,
                                                 File file, TipoConservazione tipo, boolean forzaAccettazione,
                                                 boolean forzaConservazione, boolean forzaCollegamento)
            throws ConservazioneException {

        try {
            UnitaDocAggAllegati ud = new UnitaDocAggAllegati();

            Intestazione intestazione = new Intestazione();

            intestazione.setVersione(this.getChild("versione", xmlDocument).getText());

            Versatore versatore = new Versatore();
            String codiceEnte = this.getChild("codiceEnte", xmlDocument)
                    .getText();
            versatore.setEnte(codiceEnte); // COD_ENTE
            String codiceAoo = this.getChild("codiceAoo", xmlDocument)
                    .getText();
            versatore.setStruttura(codiceAoo); // COD_AOO

            versatore.setAmbiente(this.getChild("ambiente", xmlDocument)
                    .getText());

            intestazione.setVersatore(versatore);

            Chiave chiave = new Chiave();
            chiave.setNumero(this.getChild("numeroChiave", xmlDocument).getText()); // NUM_PG
            chiave.setAnno(Long.parseLong(this.getChild("annoChiave", xmlDocument)
                    .getText())); // ANNO_PG
            chiave.setTipoRegistro(this.getChild("registroChiave", xmlDocument)
                    .getText()); // REGISTRO_PG

            intestazione.setChiave(chiave);
	
			/*intestazione.setTipologiaUnitaDocumentaria(this.getChild(
					"tipologia", xmlDocument).getText());*/

            ud.setIntestazione(intestazione);

            Configurazione configurazione = new Configurazione();

            OMElement conf = this.getChildOrNull("configurazione", xmlDocument);
            if (conf != null) {
                OMElement forzaAccettazioneElem = this.getChildOrNull(
                        "forzaAccettazione", conf);
                OMElement forzaConservazioneElem = this.getChildOrNull(
                        "forzaConservazione", conf);
                OMElement forzaCollegamentoElem = this.getChildOrNull(
                        "forzaCollegamento", conf);

                if (forzaAccettazioneElem != null) {
                    forzaAccettazione |= Boolean
                            .parseBoolean(forzaAccettazioneElem.getText());
                }

                if (forzaConservazioneElem != null) {
                    forzaConservazione |= Boolean
                            .parseBoolean(forzaConservazioneElem.getText());
                }

                if (forzaCollegamentoElem != null) {
                    forzaCollegamento |= Boolean
                            .parseBoolean(forzaCollegamentoElem.getText());
                }
            }

            configurazione.setForzaAccettazione(forzaAccettazione); // PARAMETRO
            configurazione.setForzaConservazione(forzaConservazione); // PARAMETRO

            ud.setConfigurazione(configurazione);

            UnitaDocAggAllegatiChoice choice = new UnitaDocAggAllegatiChoice();

            List<?> nodes = this.xPathSelectNodes("//allegato|annesso|annotazione", xmlDocument);

            if (nodes.size() == 0) {
                throw new ConservazioneException("Documento da aggiungere non specificato");
            }

            if (nodes.size() > 1) {
                throw new ConservazioneException("Non e' possibile aggiungere piu' di un documento per ciascuna chiamata");
            }

            OMElement allegatoElem = (OMElement) nodes.get(0);

            DocumentoType allegato;
            if (allegatoElem.getLocalName().equals("allegato")) {
                choice.setAllegato(new Allegato());
                allegato = choice.getAllegato();
            } else if (allegatoElem.getLocalName().equals("annesso")) {
                choice.setAnnesso(new Annesso());
                allegato = choice.getAnnesso();
            } else if (allegatoElem.getLocalName().equals("annotazione")) {
                choice.setAnnotazione(new Annotazione());
                allegato = choice.getAnnotazione();
            } else {
                throw new IllegalArgumentException();
            }

            allegato.setIDDocumento(this.getChild("docNum", allegatoElem)
                    .getText()); // DOCNUM
            allegato.setTipoDocumento(this.getChild("typeId", allegatoElem)
                    .getText()); // TYPE_ID

            OMElement autoreElem = this.getChildOrNull("authorId", allegatoElem);
            if (autoreElem != null) {
                ProfiloDocumento profiloD = new ProfiloDocumento();
                profiloD.setAutore(autoreElem.getText()); // AUTHOR_ID
                profiloD.setDescrizione(this.getChild("description",
                        allegatoElem).getText()); // DESCRIPTION

                allegato.setProfiloDocumento(profiloD);
            }

            StrutturaOriginale so = new StrutturaOriginale();
            so.setTipoStruttura(this
                    .getChild("tipoStruttura", allegatoElem).getText());

            Componenti comps = new Componenti();
            List<?> componenti = this.xPathSelectNodes("componente", allegatoElem);
            for (Object compElem : componenti) {
                OMElement componente = (OMElement) compElem;
                Componente comp = this.makeComponente(componente);
                comps.addComponente(comp);
            }

            so.setComponenti(comps);

            allegato.setStrutturaOriginale(so);

            ud.setUnitaDocAggAllegatiChoice(choice);

            FileBinario fb = new FileBinario();
            fb.setFileSuDisco(file.getFile());
            fb.setId(file.getId());
            fb.setDimensione(file.getFile().length());

            String username = entePasswordManager.getUsername(versatore.getEnte(),
                    versatore.getStruttura());
            String password = entePasswordManager.getPassword(versatore.getEnte(),
                    versatore.getStruttura());

            versatore.setUserID(username);

            switch (tipo) {
                case SOSTITUTIVA:
                    configurazione.setTipoConservazione(TipoConservazioneType.SOSTITUTIVA);
                    break;
                case FISCALE:
                    configurazione.setTipoConservazione(TipoConservazioneType.FISCALE);
                    break;
                default:
                    throw new ConservazioneException("Tipo specificato non valido");
            }

            configurazione.setForzaConservazione(configurazione.getForzaConservazione()
                    | forzaConservazione);
            configurazione.setForzaAccettazione(configurazione.getForzaAccettazione()
                    | forzaAccettazione);

            if (simulate) {
                configurazione.setSimulaSalvataggioDatiInDB(true);
            }

            EsitoVersAggAllegati esito = this.parerLib.aggiungiDocumento(username,
                    password, ud, fb);

            // forzo una chiamata singola per un errore di persistenza su eclispe link
            // codice da rimuovere quando verrà risolto da PARER il problema
            if (esito.getEsitoGenerale().getCodiceErrore().equalsIgnoreCase("666P")) {
                esito = this.parerLib.modificaMetadati(username,
                        password, ud);
            }

            ConservazioneResult result =
                    this.getResult(ud, esito);

            return result;

        } catch (Exception e) {
            throw new ConservazioneException(e);
        }
    }
}
