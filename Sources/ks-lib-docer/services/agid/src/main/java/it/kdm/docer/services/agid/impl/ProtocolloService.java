package it.kdm.docer.services.agid.impl;

import it.gov.digitpa.www.protocollo.*;
import it.kdm.docer.services.agid.bl.DocumentoBL;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukasz Kwasek on 3/12/14.
 */
public class ProtocolloService implements ProtocolloServiceSkeletonInterface {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(ProtocolloService.class);

    @Override
    public EsitoConsegnaDocument consegna(SegnaturaEnvelopeDocument segnaturaEnvelope) {

        EsitoConsegnaDocument ret = EsitoConsegnaDocument.Factory.newInstance();
        ret.setEsitoConsegna(EsitoType.KO);

        try {

            Segnatura se = getSegnatura(segnaturaEnvelope);

            DocumentoBL dbl = new DocumentoBL();

            Documento documento = se.getDescrizione().getDocumento();
            Documento[] allegati = se.getDescrizione().getAllegati().getDocumentoArray();

            int docNumPrincipale = dbl.create(documento, se);
            List<Integer> related = new ArrayList<Integer>();

            for (Documento d : allegati) {
                related.add(
                        dbl.create(d, se)
                );
            }

            if (related.size() > 0) {
                dbl.related(docNumPrincipale, related);
            }

            log.info(String.format("AGID: OK! [%s]", docNumPrincipale));
            ret.setEsitoConsegna(EsitoType.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    private Segnatura getSegnatura(SegnaturaEnvelopeDocument segnaturaEnvelope) {
        return segnaturaEnvelope.getSegnaturaEnvelope().getSegnatura();
    }
}
