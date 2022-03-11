package it.kdm.docer.ws;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.registrazione.*;
import it.kdm.docer.sdk.classes.xsd.KeyValuePair;
import keysuite.docer.interceptors.Logging;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
import java.util.Map;

@Endpoint
@Logging(group = "WSRegistrazione")
public class WSRegistrazioneEndpoint {

    //private BusinessLogic businessLogic;

    TicketCipher cipher = new TicketCipher();

    public WSRegistrazioneEndpoint() {
        /*try{
            businessLogic = new BusinessLogic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    protected BusinessLogic newBL(){
        try{
            return new BusinessLogic();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String registraById(String token, String docnum,String registro, String xml) throws RegistrazioneException {
        return newBL().registra(
                token,
                Long.parseLong(docnum),
                registro.toUpperCase(),
                xml);
    }

    @PayloadRoot(namespace = "http://registrazione.docer.kdm.it", localPart = "registraById")
    @ResponsePayload
    public RegistraByIdResponse registraById(@RequestPayload RegistraById request) throws RegistrazioneException {

        String result = registraById(
                request.getToken().getValue(),
                request.getDocumentoId().toString(),
                request.getRegistroId().getValue().toUpperCase(),
                request.getDatiRegistrazione().getValue());

        RegistraByIdResponse response = WSTransformer.registrazioneFactory.createRegistraByIdResponse();
        response.setReturn(WSTransformer.registrazioneFactory.createRegistraByIdResponseReturn(result));

        return response;
    }

    public Map<String,String> getRegistri(String token, String codEnte, String codAoo) throws RegistrazioneException {
        it.kdm.docer.sdk.classes.KeyValuePair[] result = newBL().getRegistri(
                token,
                codEnte,
                codAoo);
        List<KeyValuePair> pairs = WSTransformer.toPairs(result);
        return WSTransformer.toMap(pairs,false);
    }

    @PayloadRoot(namespace = "http://registrazione.docer.kdm.it", localPart = "getRegistri")
    @ResponsePayload
    public GetRegistriResponse getRegistri(@RequestPayload GetRegistri request) throws RegistrazioneException {

        Map<String,String> map = getRegistri(
                request.getToken().getValue(),
                request.getCodEnte().getValue(),
                request.getCodAoo().getValue()
        );

        GetRegistriResponse response = WSTransformer.registrazioneFactory.createGetRegistriResponse();
        response.getReturn().addAll(WSTransformer.toPairs(map));

        return response;
    }

}
