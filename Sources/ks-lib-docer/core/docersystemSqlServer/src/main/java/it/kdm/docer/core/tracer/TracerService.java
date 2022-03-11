package it.kdm.docer.core.tracer;


import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.joda.time.DateTime;

import java.security.KeyException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class TracerService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(TracerService.class);

    public Trace[] read(String token, String docnum, String extradata, String user, Calendar from, Calendar to, String optype) throws Exception {
        logger.debug("read()");

        try {
            Tracer tracer = new Tracer();
            List<Trace> ret = tracer.read(docnum, extradata, user, from, to, optype);
            return ret.toArray(new Trace[0]);

        } catch (KeyException e) {
            logger.error(e.getMessage(), e);
            throw e;
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
        }
    }


    public void write(String token, it.kdm.docer.core.tracer.Trace traceItem, String level, int type) throws Exception {
        logger.debug(String.format("write(%s, %s, %s, %s, %d)", traceItem.getMessage(), traceItem.getServiceName(), traceItem.getMethodName(), level, type));

        //TODO: what to do with token?
        try {
            Tracer tracer = new Tracer();
            tracer.write(traceItem, Tracer.LEVEL.NORMAL, Tracer.TYPE.getType(type));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw AxisFault.makeFault(e);
        }

    }
}
