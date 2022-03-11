package it.kdm.docer.core.tracer;


import it.kdm.docer.core.Core;
import it.kdm.docer.core.SpringContextHolder;
import it.kdm.docer.core.authorization.AuthorizationManager;
import it.kdm.docer.core.database.IDatabaseProvider;
import it.kdm.docer.core.tracer.bean.Configuration;
import it.kdm.docer.core.tracer.bean.TraceMessage;
import it.kdm.docer.core.tracer.jmsclient.JmsClient;
import it.kdm.docer.core.utils.StringServiceUtils;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.security.KeyException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Tracer {

    Logger logger = org.slf4j.LoggerFactory.getLogger(Tracer.class);
    private LEVEL maxLevel;



    public enum LEVEL {
        NORMAL,
        DEBUG
    }

    public enum TYPE {
        INCOMING_MESSAGE(MessageContext.IN_FLOW),
        INCOMING_FAULT(MessageContext.IN_FAULT_FLOW),
        OUTGOING_MESSAGE(MessageContext.OUT_FLOW),
        OUTGOING_FAULT(MessageContext.OUT_FAULT_FLOW);

        private int type;

        TYPE(int type) {
            this.type = type;
        }

        public int getValue() {
            return this.type;
        }

        public static TYPE getType(int type) throws KeyException {
            for (TYPE typeVal : TYPE.values()) {
                if (typeVal.type == type) {
                    return typeVal;
                }
            }

            throw new KeyException("Type could not be found");
        }
    }

    public Tracer() {
        this(LEVEL.NORMAL);
    }

    public Tracer(LEVEL maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void write(Trace traceItem, LEVEL level, TYPE type) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        IDatabaseProvider provider = null;

        if (level.ordinal() > this.maxLevel.ordinal()) {
            return;
        }
        
        //Todo aggiungere if con enabledEventJMS enabled if true push on queue

        try {

            //recupero la configurazione jms
            Configuration conf = SpringContextHolder.getCtx().getBean(Configuration.class);
            String methodNameToTrace=","+traceItem.getMethodName()+",";

            if(StringUtils.containsIgnoreCase(conf.getMethodToSkipJMS(),methodNameToTrace) || !conf.isEnabledJMS()){
                provider = Core.getInstance().getDatabaseProvider();
                provider.openConnection();
                provider.saveTracerLog(traceItem, level.toString(), type);
            }else if(conf.isEnabledJMS()){
                TraceMessage traceMessage = new TraceMessage();
                traceMessage.setLevel(level.toString());
                traceMessage.setTrace(traceItem);
                traceMessage.setType(type);
                JmsClient.addToJMS(traceMessage);
            }

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

    public String readMethodList() throws Exception {
        logger.debug("readMethodList()");
        String joined = ";";

        try {
            AuthorizationManager auth = new AuthorizationManager();
            List<Map<String, Object>> results = auth.getTracerMethodList();


            for (Map<String, Object> result : results) {
                joined += result.get("groupname") + "!" + result.get("methodname") + ";";
            }

            return joined;

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

    private String stringify(Object o){
        if(o != null){
            return o.toString();
        } else {
            return "";
        }
    }

    public List<Trace> read(String docnum, String extradata, String user, Calendar from, Calendar to, String optype) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, KeyException, ParseException {
        logger.debug("read()");

        IDatabaseProvider provider = null;

        provider = Core.getInstance().getDatabaseProvider();
        provider.openConnection();

        Collection<Long> ldocnum = null;
        if(!docnum.equals("-1")){
            ldocnum = StringServiceUtils.parseLongQuery(docnum);
        }

        List<Map<String, Object>> set = provider.readTracerLog(ldocnum, extradata, user, from, to, optype);

        List<Trace> logs = new ArrayList<Trace>();

        Trace record;

        for (Map<String, Object> result : set) {
            try{
                record = new Trace();

                record.setDocNum(stringify(result.get("docnum")));
                record.setServiceName(stringify(result.get("serviceName")));
                record.setMethodName(stringify(result.get("methodName")));
                record.setLivello(stringify(result.get("livello")));
                record.setExtraData(stringify(result.get("extradata")));
                record.setServiceUrl(stringify(result.get("serviceUrl")));
                record.setUser(stringify(result.get("user")));
                record.setServiceNameLabel(stringify(result.get("serviceNameLabel")));
                record.setMethodNameLabel(stringify(result.get("methodNameLabel")));

                if(result.get("tipo") != null){
                    TYPE type = TYPE.getType(Integer.parseInt(result.get("tipo").toString()));
                    record.setTipo(type.toString());
                }


                if (result.get("data") != null) {
                    Timestamp date = Timestamp.valueOf(result.get("data").toString());
                    Calendar cal = GregorianCalendar.getInstance(Locale.ITALY);
                    cal.setTime(date);
                    record.setDate(cal);

                    // Aggiungiamo una data leggibile
                    SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy HH:mm:ss");
                    record.setUrData(sdf.format(cal.getTime()));
                }


                logs.add(record);
            } catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }

        return logs;
    }
}
