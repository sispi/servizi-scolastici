package it.kdm.docer.services.agid.impl;

import it.gov.digitpa.www.protocollo.EsitoConsegnaDocument;
import it.gov.digitpa.www.protocollo.EsitoType;
import it.gov.digitpa.www.protocollo.NotificaDocument;
import it.gov.digitpa.www.protocollo.ProtocolloRicezioneEsitiServiceSkeletonInterface;

/**
 * Created by Lukasz Kwasek on 3/12/14.
 */
public class ProtocolloRicezioneEsitiService implements ProtocolloRicezioneEsitiServiceSkeletonInterface {

    @Override
    public EsitoConsegnaDocument ricezioneEsiti(NotificaDocument notifica) {

        EsitoConsegnaDocument esitoConsegnaDocument = EsitoConsegnaDocument.Factory.newInstance();
        esitoConsegnaDocument.setEsitoConsegna(EsitoType.OK);

        return esitoConsegnaDocument;
    }
}