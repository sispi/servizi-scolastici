package it.kdm.docer.core.database;

import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.core.SpringContextHolder;
import it.kdm.docer.core.tracer.Trace;
import it.kdm.docer.core.tracer.Tracer.TYPE;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class systemDBProvider extends IDatabaseProvider {


    private JdbcTemplate jdbcTemplate;
    //private final String FRAMEWORK = "embedded";


    //private Connection conn = null;

    Logger logger = org.slf4j.LoggerFactory.getLogger(systemDBProvider.class);

    public systemDBProvider() throws SQLException {


    }

    @Override
    public void openConnection() throws SQLException {
        try {
            jdbcTemplate = SpringContextHolder.getCtx().getBean(JdbcTemplate.class);

            if (!isInitialized()) {
                initialize();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SQLException(e);
        }
    }

    private boolean isInitialized() throws SQLException {

        return true;
                /*
                ResultSet set = null;
		try {
			DatabaseMetaData metadata = conn.getMetaData();
			 set = metadata.getTables(null, null, "%", null);
			
			while(set.next()) {
				String tableName = set.getString(3); //Table Name
				if(tableName.equalsIgnoreCase("tracer")) {
					return true;
				}
			}
		} finally {
			if (set != null) {
				set.close();
			}
		}
		
		return false;
                * 
                */
    }

    private void initialize() throws SQLException {

        try {
            jdbcTemplate.execute("create table tracer(message VARCHAR(5000), livello VARCHAR(100), tipo int)");
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void closeConnection() throws SQLException {

    }

    @Override
    public boolean isConnected() throws SQLException {
        return jdbcTemplate != null;
    }

    @Override
    public void saveTracerLog(Trace traceItem, String level, TYPE type) throws SQLException {

        jdbcTemplate.update("insert into tracer (user,serviceName,methodName,livello,tipo,data,docnum,extradata,serviceUrl) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                traceItem.getUser(), traceItem.getServiceName(), traceItem.getMethodName(),
                level, type.getValue(), new Timestamp(new Date().getTime()), traceItem.getDocNum(),
                traceItem.getExtraData(), traceItem.getServiceUrl());
    }

    @Override
    public List<Map<String, Object>> readTracerLog(Collection<Long> docnum, String extradata, String user, Calendar from, Calendar to, String optype) throws SQLException, ParseException {
        /*
        select t.*, s.description as 'serviceNameLabel', m.displayname as 'methodNameLabel'
            from
              docersystem.tracer as t,
              docersystem.services as s,
              docersystem.servicemethods as m
            where
              docnum = 17864 and
              t.serviceName = s.groupname and
              t.methodName = m.methodname
         */

        String query = "select t.*, s.description as 'serviceNameLabel', m.displayname as 'methodNameLabel' " +
                "from " +
                "docersystem.tracer as t, " +
                "docersystem.servicemethods as m, " +
                "docersystem.services as s " +
                "where " +
                "t.serviceName = s.groupname and " +
                "t.methodName = m.methodname and " +
                "(t.data between ? and ?) ";


        List<Object> data = new ArrayList<Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        /*
           A chiunque passi di qui: di seguito c'è una pezza. Purtroppo a quanto pare c'è un disallineamento tra le
           timezone del database e quella di xforms. E' necessario riallinearle per avere una query corretta.
            */
//        String tz = "UTC";
//        sdf.setTimeZone(TimeZone.getTimeZone(tz));

        // FROM
        if (from == null) {
            from = Calendar.getInstance();
            from.setTime(sdf.parse("1900-01-01 00:00:00+0000"));
        }
        data.add(from);

        // TO
        if (to == null) {
            to = Calendar.getInstance();
            to.setTime(sdf.parse("2200-01-01 00:00:00+0000"));
        }
        data.add(to);

        // DOCNUM
        if (docnum != null && docnum.size() > 0) {
            String template = "and (%s) ";

            List<String> conditions = new ArrayList<String>();
            for (Long n : docnum) {
                conditions.add("t.docnum = ?");
                data.add(n);
            }
            String where = StringUtils.join(conditions, " or ");
            query += String.format(template, where);
        }

        // USER
        if (user != "" && user != null) {
            user = user.replace("*", "%");
            query += "and t.user like ? ";
            data.add(user);
        }

        // EXTRADATA
        if (extradata != "" && extradata != null) {
            extradata = extradata.replace("*", "%");
            extradata = "%" + extradata + "%";
            query += "and t.extradata like ? ";
            data.add(extradata);
        }

        // TYPE
        if (optype != "" && !optype.equals("*") && optype != null) {
            query += "and t.methodName = ? ";
            data.add(optype);
        }

        query += "order by t.data DESC ";

        // LIMIT
        try {
            Properties p = ConfigurationUtils.loadProperties("tracer.properties");
            String limit = p.getProperty("result-limit");

            if (limit == null || limit == "") {
                limit = "100";
            }

            query += "limit 0, " + limit;
        } catch (Exception e) {
            logger.error("Impossibile trovare tracer.properties");
        }

        return jdbcTemplate.queryForList(query, data.toArray());
    }

    @Override
    public void shutdown() throws SQLException {

    }

}
