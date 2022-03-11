package it.emilia_romagna.regione.www.rer_fonte_raccoglitore.handler;


import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;

public class SamlHandler extends BasicHandler  {

	@Override
	public void invoke(MessageContext context) throws AxisFault {
		try{
			SOAPMessage soapMsg = context.getMessage();
			SOAPEnvelope soapEnv = soapMsg.getSOAPPart().getEnvelope();
			SOAPHeader soapHeader = soapEnv.getHeader();
			if(soapHeader!=null){
				Iterator<?> iterator = soapHeader.extractHeaderElements(SOAPConstants.URI_SOAP_ACTOR_NEXT);

				String federaData = null;
				while(iterator.hasNext()){
					Node macNode = (Node) iterator.next();
					if(macNode.getNodeName().contains("securityData")){
						federaData = macNode.getValue();
					}
				}

				if(federaData!=null){
					context.setProperty("middlewareData", federaData);
				}
				

			}
		}catch(SOAPException e){
			System.err.println(e);
		}

	}
}

