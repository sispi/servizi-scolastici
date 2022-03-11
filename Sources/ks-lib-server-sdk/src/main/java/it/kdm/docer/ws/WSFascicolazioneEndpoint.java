package it.kdm.docer.ws;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.fascicolazione.*;
import it.kdm.docer.sdk.classes.xsd.KeyValuePair;
import keysuite.docer.interceptors.Logging;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Endpoint
@Logging(group = "WSFascicolazione")
public class WSFascicolazioneEndpoint {

    //private BusinessLogic businessLogic;
    TicketCipher cipher = new TicketCipher();

    protected BusinessLogic newBL(){
        try{
            return new BusinessLogic();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public WSFascicolazioneEndpoint() {
        try {
            //businessLogic = new BusinessLogic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String creaFascicolo(String token, Map<String, String> metadata, boolean forzaNuovoFascicolo) throws FascicolazioneException {
        return newBL().creaFascicolo(token, WSTransformer.toKeyValuePairsArray(metadata, false), forzaNuovoFascicolo);
    }

    public String creaFascicolo(String token, List<KeyValuePair> pairs, boolean forzaNuovoFascicolo) throws FascicolazioneException {
        return newBL().creaFascicolo(token, WSTransformer.toKeyValuePairsArray(pairs, false), forzaNuovoFascicolo);
    }

    @PayloadRoot(namespace = "http://fascicolazione.docer.kdm.it", localPart = "forzaNuovoFascicolo")
    @ResponsePayload
    public ForzaNuovoFascicoloResponse forzaNuovoFascicolo(@RequestPayload ForzaNuovoFascicolo request) throws FascicolazioneException {

        String result = creaFascicolo(request.getToken().getValue(),
                request.getMetadati(), true);

        return WSTransformer.getReturn(ForzaNuovoFascicoloResponse.class, result);
    }

    @PayloadRoot(namespace = "http://fascicolazione.docer.kdm.it", localPart = "creaFascicolo")
    @ResponsePayload
    public CreaFascicoloResponse creaFascicolo(@RequestPayload CreaFascicolo request) throws FascicolazioneException, AuthenticationException {

        String result = creaFascicolo(request.getToken().getValue(),
                request.getMetadati(), false);

        return WSTransformer.getReturn(CreaFascicoloResponse.class, result);
    }

    public String updateFascicolo(String token, Map<String, String> id, Map<String, String> metadata) throws FascicolazioneException {
        return updateFascicolo(token, WSTransformer.toPairs(id), WSTransformer.toPairs(metadata));
    }

    public String updateFascicolo(String token, List<KeyValuePair> id, List<KeyValuePair> metadata) throws FascicolazioneException {
        return newBL().updateFascicolo(token,
                WSTransformer.toKeyValuePairsArray(id, false),
                WSTransformer.toKeyValuePairsArray(metadata, false));
    }

    @PayloadRoot(namespace = "http://fascicolazione.docer.kdm.it", localPart = "updateFascicolo")
    @ResponsePayload
    public UpdateFascicoloResponse updateFascicolo(@RequestPayload UpdateFascicolo request) throws FascicolazioneException {

        String result = updateFascicolo(request.getToken().getValue(),
                request.getFascicoloid(),
                request.getMetadati());

        return WSTransformer.getReturn(UpdateFascicoloResponse.class, result);
    }

    public String fascicolaById(String token, String docnum, String xml) throws FascicolazioneException {
        return newBL().fascicola(token,
                Long.parseLong(docnum),
                xml);
    }

    @PayloadRoot(namespace = "http://fascicolazione.docer.kdm.it", localPart = "fascicolaById")
    @ResponsePayload
    public FascicolaByIdResponse fascicolaById(@RequestPayload FascicolaById request) throws FascicolazioneException {

        String result = fascicolaById(request.getToken().getValue(),
                request.getDocumentId().toString(),
                request.getDatiFascicolo().getValue());

        return WSTransformer.getReturn(FascicolaByIdResponse.class, result);
    }

    private it.kdm.docer.sdk.classes.KeyValuePair[][] toArray2(List<ArrayOfKeyValuePair> la){
        List<it.kdm.docer.sdk.classes.KeyValuePair[]> list = new ArrayList<>();
        if (la!=null){
            for( ArrayOfKeyValuePair arr : la ){
                List<KeyValuePair> arr2 = arr.getArray();
                it.kdm.docer.sdk.classes.KeyValuePair[] arr3 = WSTransformer.toKeyValuePairsArray(arr2,false);
                list.add(arr3);
            }
        }
        return list.toArray(new it.kdm.docer.sdk.classes.KeyValuePair[0][]);
    }

    //changeFascicoliById
    @PayloadRoot(namespace = "http://fascicolazione.docer.kdm.it", localPart = "changeFascicoliById")
    @ResponsePayload
    public ChangeFascicoliByIdResponse changeFascicoliById(@RequestPayload ChangeFascicoliById request) throws FascicolazioneException {

        List<ArrayOfKeyValuePair> la = request.getFascicoliToAdd();

        boolean res = newBL().changeFascicoli(request.getToken().getValue(),
                request.getDocNum(),
                toArray2(request.getFascicoliToRemove()),
                toArray2(request.getFascicoliToAdd()));

        return WSTransformer.getReturn(ChangeFascicoliByIdResponse.class, res);
    }

    //updateACLFascicolo
    @PayloadRoot(namespace = "http://fascicolazione.docer.kdm.it", localPart = "updateACLFascicolo")
    @ResponsePayload
    public UpdateACLFascicoloResponse updateACLFascicolo(@RequestPayload UpdateACLFascicolo request) throws FascicolazioneException {

        boolean res = newBL().updateACLFascicolo(request.getToken().getValue(),
                WSTransformer.toKeyValuePairsArray(request.getFascicoloid(),false),
                WSTransformer.toKeyValuePairsArray(request.getAcl(),true));

        return WSTransformer.getReturn(UpdateACLFascicoloResponse.class, res);
    }

    //changeACLFascicolo
    @PayloadRoot(namespace = "http://fascicolazione.docer.kdm.it", localPart = "changeACLFascicolo")
    @ResponsePayload
    public ChangeACLFascicoloResponse changeACLFascicolo(@RequestPayload ChangeACLFascicolo request) throws FascicolazioneException {

        String[] toRemove = new String[request.getAclToRemove().size()];
        for( int i=0; i<toRemove.length; i++  ){
            toRemove[i] = WSTransformer.addPrefix(request.getAclToRemove().get(i));
        }

        boolean res = newBL().addRemoveACLFascicolo(request.getToken().getValue(),
                WSTransformer.toKeyValuePairsArray(request.getFascicoloId(),false),
                WSTransformer.toKeyValuePairsArray(request.getAclToAdd(),true),
                toRemove );

        return WSTransformer.getReturn(ChangeACLFascicoloResponse.class, res);
    }

}
