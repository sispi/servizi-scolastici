package it.kdm.docer.core.tracer.jmsclient;

import it.kdm.docer.core.SpringContextHolder;
import it.kdm.docer.core.tracer.Trace;
import it.kdm.docer.core.tracer.bean.TraceMessage;
import it.kdm.docer.core.tracer.broker.MessageBroker;

public class JmsClient {

	public static void addToJMS(TraceMessage traceItem){
		MessageBroker broker = SpringContextHolder.getCtx().getBean(MessageBroker.class);
		broker.produce(traceItem);
	}
	
	
	
}
