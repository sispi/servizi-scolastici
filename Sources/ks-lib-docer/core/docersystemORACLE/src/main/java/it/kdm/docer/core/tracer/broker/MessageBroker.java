package it.kdm.docer.core.tracer.broker;

import it.kdm.docer.core.Core;
import it.kdm.docer.core.database.IDatabaseProvider;
import it.kdm.docer.core.tracer.bean.TraceMessage;
import org.apache.camel.Message;
import org.apache.camel.Consume;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import it.kdm.docer.core.tracer.Trace;
import it.kdm.docer.core.tracer.Tracer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;
import java.util.Map;


public class MessageBroker {

	Logger logger = org.slf4j.LoggerFactory.getLogger(MessageBroker.class);

    @EndpointInject(uri = "direct:docer-events")
    ProducerTemplate template;
    public void produce(TraceMessage msg) {
        logger.info("init method produce");
        template.sendBody(msg);
    }

    @Consume(uri = "direct:audit_queue")
    public void consumeMessage(TraceMessage msg)  throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        IDatabaseProvider provider = null;
        logger.info("init method consumeMessage direct:audit_queue1 ");
        try {
            provider = Core.getInstance().getDatabaseProvider();
            provider.openConnection();
            provider.saveTracerLog(msg.getTrace(), msg.getLevel(), msg.getType());
            logger.info("end method consumeMessage direct:audit_queue1 ");
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            try {
                if (provider != null) {
                    provider.closeConnection();
                }
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
                throw e;
            }
        }
    }
   
}
