package it.kdm.docer.ws;

import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.protocollazione.BusinessLogic;
import it.kdm.docer.protocollazione.ProtocollaById;
import it.kdm.docer.protocollazione.ProtocollaByIdResponse;
import it.kdm.docer.protocollazione.ProtocollazioneException;
import keysuite.docer.interceptors.Logging;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Logging(group = "WSProtocollazione")
public class WSProtocollazioneEndpoint {

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

    public WSProtocollazioneEndpoint() {

        /*try{
            businessLogic = new BusinessLogic();
        } catch(Exception e){
            throw new RuntimeException(e);
        }*/
    }

    public String protocollaById(String token,String docnum, String xml) throws ProtocollazioneException {
        return newBL().protocolla(
                token,
                Long.parseLong(docnum),
                xml);
    }

    @PayloadRoot(namespace = "http://protocollazione.docer.kdm.it", localPart = "protocollaById")
    @ResponsePayload
    public ProtocollaByIdResponse protocollaById(@RequestPayload ProtocollaById request) throws ProtocollazioneException {

        String result = protocollaById(
                request.getToken().getValue(),
                request.getDocumentoId().toString(),
                request.getDatiProtocollo().getValue());

        ProtocollaByIdResponse response = WSTransformer.protocollazioneFactory.createProtocollaByIdResponse();
        response.setReturn(WSTransformer.protocollazioneFactory.createProtocollaByIdResponseReturn(result));

        return response;
    }

}
