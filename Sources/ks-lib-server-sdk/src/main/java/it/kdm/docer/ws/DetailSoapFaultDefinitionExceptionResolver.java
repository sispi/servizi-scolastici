package it.kdm.docer.ws;

import it.kdm.docer.fascicolazione.FascicolazioneException;
import it.kdm.docer.protocollazione.ProtocollazioneException;
import it.kdm.docer.registrazione.RegistrazioneException;
import it.kdm.docer.sdk.exceptions.DocerException;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.soap.soap11.Soap11Fault;
import org.springframework.ws.soap.soap12.Soap12Fault;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMSource;
import java.lang.reflect.UndeclaredThrowableException;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("statusCode");
    private static final QName MESSAGE = new QName("message");

    private void setCode(SoapFault fault, String text){

        NodeList nl = ((DOMSource)fault.getSource()).getNode().getChildNodes();

        if (fault instanceof Soap11Fault){
            for ( int i = 0; i< nl.getLength(); i++){
                if (nl.item(i).getLocalName().equals("faultcode") )
                    nl.item(i).getFirstChild().setTextContent(text);
            }
        }

        if (fault instanceof Soap12Fault){
            for ( int i = 0; i< nl.getLength(); i++){
                if (nl.item(i).getLocalName().equals("Code")){
                    nl = nl.item(i).getChildNodes();
                    for ( int j = 0; j< nl.getLength(); j++){
                        if (nl.item(j).getLocalName().equals("Value"))
                            nl.item(j).getFirstChild().setTextContent(text);
                    }
                }
            }
        }


    }

    private void setMessage(SoapFault fault, String text){
        NodeList nl = ((DOMSource)fault.getSource()).getNode().getChildNodes();

        if (fault instanceof Soap11Fault){
            for ( int i = 0; i< nl.getLength(); i++){
                if (nl.item(i).getLocalName().equals("faultstring") )
                    nl.item(i).getFirstChild().setTextContent(text);
            }
        }

        if (fault instanceof Soap12Fault){
            for ( int i = 0; i< nl.getLength(); i++){
                if (nl.item(i).getLocalName().equals("Reason")){
                    nl = nl.item(i).getChildNodes();
                    for ( int j = 0; j< nl.getLength(); j++){
                        if (nl.item(j).getLocalName().equals("Text"))
                            nl.item(j).getFirstChild().setTextContent(text);
                    }
                }
            }
        }
    }

    private void removeDetail(SoapFault fault){

        /*NodeList nl = ((DOMSource)fault.getSource()).getNode().getChildNodes();

        for ( int i = 0; i< nl.getLength(); i++){
            if (nl.item(i).getLocalName().equals("Detail"))
                ((DOMSource)fault.getSource()).getNode().removeChild(nl.item(i));
        }*/

    }

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {

        Throwable t = ex;
        if (t instanceof UndeclaredThrowableException){
            t = ((UndeclaredThrowableException)ex).getUndeclaredThrowable();
        }

        if (t instanceof AuthenticationException) {
            //removeDetail(fault);
            setCode(fault,t.getClass().getSimpleName());
            setMessage(fault,t.getMessage());

            AuthenticationException de = (AuthenticationException) t;

            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(CODE).addText("invalid token");
            detail.addFaultDetailElement(MESSAGE).addText(de.getMessage());
        } else if (t instanceof DocerException) {
            removeDetail(fault);
            setCode(fault,t.getClass().getSimpleName());
            setMessage(fault,t.getMessage());

            DocerException de = (DocerException) t;
            //ServiceStatus status = ((ServiceFaultException) ex).getServiceStatus();
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(CODE).addText(""+de.getErrNumber());
            detail.addFaultDetailElement(MESSAGE).addText(de.getErrDescription());
        } else if (t instanceof RegistrazioneException) {
            removeDetail(fault);
            setCode(fault,t.getClass().getSimpleName());
            setMessage(fault,t.getMessage());

            RegistrazioneException de = (RegistrazioneException) t;
            //ServiceStatus status = ((ServiceFaultException) ex).getServiceStatus();
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(CODE).addText("RegistrazioneException");
            detail.addFaultDetailElement(MESSAGE).addText(de.getMessage());
        } else if (t instanceof FascicolazioneException) {
            removeDetail(fault);
            setCode(fault,t.getClass().getSimpleName());
            setMessage(fault,t.getMessage());

            FascicolazioneException de = (FascicolazioneException) t;
            //ServiceStatus status = ((ServiceFaultException) ex).getServiceStatus();
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(CODE).addText("FascicolazioneException");
            detail.addFaultDetailElement(MESSAGE).addText(de.getMessage());
        } else if (t instanceof ProtocollazioneException) {
            removeDetail(fault);
            setCode(fault,t.getClass().getSimpleName());
            setMessage(fault,t.getMessage());

            ProtocollazioneException de = (ProtocollazioneException) t;
            //ServiceStatus status = ((ServiceFaultException) ex).getServiceStatus();
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(CODE).addText("ProtocollazioneException");
            detail.addFaultDetailElement(MESSAGE).addText(de.getMessage());
        }
    }

}
