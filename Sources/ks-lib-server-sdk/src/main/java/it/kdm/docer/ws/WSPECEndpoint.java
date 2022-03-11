package it.kdm.docer.ws;

import it.kdm.docer.PEC.PECException;
import it.kdm.docer.PEC.BusinessLogic;
import it.kdm.docer.wspec.InvioPEC;
import it.kdm.docer.wspec.InvioPECResponse;
import keysuite.docer.interceptors.Logging;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@Logging(group = "WSPEC")
public class WSPECEndpoint {

    public WSPECEndpoint() {
    }

    protected BusinessLogic newBL(){
        try{
            return new BusinessLogic();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PayloadRoot(namespace = "http://PEC.docer.kdm.it", localPart = "invioPEC")
    @ResponsePayload
    public InvioPECResponse invioPEC(@RequestPayload InvioPEC request) throws PECException {

        String ticket = request.getToken().getValue();
        Long docId = request.getDocumentoId();
        String datiPEC = request.getDatiPec().getValue();

        String respPEC = newBL().invioPEC(ticket,docId,datiPEC);
        InvioPECResponse response = WSTransformer.pecFactory.createInvioPECResponse();

        response.setReturn(WSTransformer.pecFactory.createInvioPECResponseReturn(respPEC));

        return response;
    }

}
