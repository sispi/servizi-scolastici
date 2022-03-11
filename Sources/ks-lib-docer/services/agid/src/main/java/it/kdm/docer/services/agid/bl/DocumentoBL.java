package it.kdm.docer.services.agid.bl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import it.gov.digitpa.www.protocollo.Documento;
import it.gov.digitpa.www.protocollo.Segnatura;
import it.kdm.docer.clients.*;
import it.kdm.docer.services.agid.utils.MessageUtils;
import org.apache.commons.lang.StringUtils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Łukasz Kwasek on 09/09/15.
 */
public class DocumentoBL {

    private String ticket;

    public DocumentoBL() throws RemoteException, AuthenticationServiceExceptionException, ExceptionException {
        this.ticket = new AuthBL().getToken();
    }

    public Integer create(Documento d, Segnatura se) throws RemoteException, DocerExceptionException {
        DocerServicesStub.CreateDocument req = new DocerServicesStub.CreateDocument();

        req.setToken(ticket);
        req.setFile(MessageUtils.getDataHandler(d.getId()));

        List<DocerServicesStub.KeyValuePair> metadata = new ArrayList<DocerServicesStub.KeyValuePair>();

        metadata.addAll(metadata(d));

        if (se != null) {
            metadata.addAll(metadata(se));
        }

        req.setMetadata(metadata.toArray(new DocerServicesStub.KeyValuePair[metadata.size()]));


        return Integer.valueOf(
                ClientManager.INSTANCE.getDocerServicesClient().createDocument(req).get_return()
        );
    }

    public void related(Integer docNumPrincipale, List<Integer> related) throws RemoteException, DocerExceptionException {

        DocerServicesStub.AddRelated req = new DocerServicesStub.AddRelated();

        req.setToken(ticket);
        req.setDocId(docNumPrincipale.toString());
        req.setRelated(
                Lists.transform(related, new Function<Integer, String>() {
                    @Override
                    public String apply(Integer input) {
                        return input.toString();
                    }
                }).toArray(new String[related.size()])
        );

        ClientManager.INSTANCE.getDocerServicesClient().addRelated(req);

    }

    public Integer create(Documento d) throws RemoteException, DocerExceptionException {
        return create(d, null);
    }

    private List<DocerServicesStub.KeyValuePair> metadata(Segnatura se) {
        List<DocerServicesStub.KeyValuePair> metadata = new ArrayList<DocerServicesStub.KeyValuePair>();

        // COD_ENTE_MITTENTE
        DocerServicesStub.KeyValuePair codiceEnteMit = new DocerServicesStub.KeyValuePair();
        codiceEnteMit.setKey("COD_ENTE_MITTENTE");
        codiceEnteMit.setValue(se.getIntestazione().getIdentificatore().getCodiceAmministrazione().newCursor().getTextValue());
        metadata.add(codiceEnteMit);

        // COD_ENTE
        DocerServicesStub.KeyValuePair codiceEnte = new DocerServicesStub.KeyValuePair();
        codiceEnte.setKey("COD_ENTE");
        codiceEnte.setValue(se.getIntestazione().getIdentificatore().getCodiceAmministrazione().newCursor().getTextValue());
        metadata.add(codiceEnte);

        // COD_AOO_MITTENTE
        DocerServicesStub.KeyValuePair codiceAooMit = new DocerServicesStub.KeyValuePair();
        codiceAooMit.setKey("COD_AOO_MITTENTE");
        codiceAooMit.setValue(se.getIntestazione().getIdentificatore().getCodiceAOO().newCursor().getTextValue());
        metadata.add(codiceAooMit);

        // COD_AOO
        DocerServicesStub.KeyValuePair codiceAoo = new DocerServicesStub.KeyValuePair();
        codiceAoo.setKey("COD_AOO");
        codiceAoo.setValue(se.getIntestazione().getIdentificatore().getCodiceAOO().newCursor().getTextValue());
        metadata.add(codiceAoo);

        // NUM_PG_MITTENTE
        DocerServicesStub.KeyValuePair numPg = new DocerServicesStub.KeyValuePair();
        numPg.setKey("NUM_PG_MITTENTE");
        numPg.setValue(se.getIntestazione().getIdentificatore().getNumeroRegistrazione().newCursor().getTextValue());
        metadata.add(numPg);

        // DATA_PG_MITTENTE
        DocerServicesStub.KeyValuePair dataPg = new DocerServicesStub.KeyValuePair();
        dataPg.setKey("DATA_PG_MITTENTE");
        dataPg.setValue(se.getIntestazione().getIdentificatore().getDataRegistrazione().newCursor().getTextValue());
        metadata.add(dataPg);

        // CLASSIFICA_MITTENTE
        DocerServicesStub.KeyValuePair classifica = new DocerServicesStub.KeyValuePair();
        String classificaStr = se.getDescrizione()
                .getAllegati()
                .getFascicoloArray(0)
                .getClassificaArray(0)
                .getLivelloArray(0).newCursor().getTextValue();
        classifica.setKey("CLASSIFICA_MITTENTE");
        classifica.setValue(
                String.format(
                        "CLASSIFICA_MITTENTE = Denominazione %s",
                        StringUtils.isBlank(classificaStr) ? "NULL" : classificaStr
                )
        );
        metadata.add(classifica);

        // FASCICOLO_MITTENTE
        DocerServicesStub.KeyValuePair fascicolo = new DocerServicesStub.KeyValuePair();
        String fascicoloStr = se.getDescrizione()
                .getAllegati()
                .getFascicoloArray(0)
                .getIdentificativo().newCursor().getTextValue();
        fascicolo.setKey("FASCICOLO_MITTENTE");
        fascicolo.setValue(String.format(
                        "FASCICOLO_MITTENTE = Oggetto %s",
                        StringUtils.isBlank(fascicoloStr) ? "NULL" : fascicoloStr
                )
        );
        metadata.add(fascicolo);

        // MITTENTE
        DocerServicesStub.KeyValuePair mittente = new DocerServicesStub.KeyValuePair();
        mittente.setKey("MITTENTI");
        mittente.setValue(
                se.getIntestazione().getOrigine().getMittente().newCursor().xmlText()
        );
        metadata.add(mittente);

        return metadata;
    }

    private List<DocerServicesStub.KeyValuePair> metadata(Documento d) {

        List<DocerServicesStub.KeyValuePair> metadata = new ArrayList<DocerServicesStub.KeyValuePair>();

        DocerServicesStub.KeyValuePair docname = new DocerServicesStub.KeyValuePair();
        docname.setKey("DOCNAME");
        docname.setValue(d.getTitoloDocumento().newCursor().getTextValue());
        metadata.add(docname);

        DocerServicesStub.KeyValuePair type = new DocerServicesStub.KeyValuePair();
        type.setKey("TYPE_ID");
        type.setValue(d.getTipoDocumento().newCursor().getTextValue());
        metadata.add(type);

        // TIPO_COMPONENTE
        DocerServicesStub.KeyValuePair componente = new DocerServicesStub.KeyValuePair();
        componente.setKey("TIPO_COMPONENTE");
        componente.setValue(d.getCollocazioneTelematica().newCursor().getTextValue());
        metadata.add(componente);

        return metadata;
    }
}
