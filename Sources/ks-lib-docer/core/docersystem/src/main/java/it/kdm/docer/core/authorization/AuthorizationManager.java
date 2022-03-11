package it.kdm.docer.core.authorization;

import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.configuration.ConfigurationUtils;
import it.kdm.docer.core.SpringContextHolder;
import it.kdm.docer.core.configuration.ConfigurationManager;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AuthorizationManager {

    private static final long FRONTEND_ID = 7;

    private final JdbcTemplate jdbcTemplate;
    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthorizationManager.class);

    public AuthorizationManager() {
        ApplicationContext ctx = SpringContextHolder.getCtx();
        jdbcTemplate = ctx.getBean("jdbcTemplate", JdbcTemplate.class);
    }

    private boolean isMaintenance(String token) throws Exception {

        logger.debug("isMaintenance -> START");

        // leggere da file docer_frontend.properties il nome del gruppo
        // superadmin
        Properties props = ConfigurationUtils.loadProperties("docer_frontend.properties");

        String superAdminGroup = props.getProperty("superadmin.group");
        logger.debug("isMaintenance -> superAdminGroup: " + superAdminGroup);

        // se l'utente corrente e' un super admin ritorna false
        String userGroups = ";" + Utils.extractTokenKey(token, "userGroup") + ";";
        if (userGroups.contains(";" + superAdminGroup + ";")) {
            logger.debug("isMaintenance -> userGroups from token: " + userGroups);
            logger.debug("isMaintenance -> return false");
            return false;
        }

        // altrimenti controlla se il sistema risulta in manutenzione
        // se in manutenzione ritorna true
        ConfigurationManager confMan = new ConfigurationManager();
        String value = confMan.readConfigParam("system", "isMaintenance");

        logger.debug("isMaintenance -> readConfigParam('system', 'isMaintenance'): " + value);

        if (value != null && value.equalsIgnoreCase("true")) {

            logger.debug("isMaintenance -> return true");
            return true;
        }

        logger.debug("isMaintenance -> return false");
        return false;
    }

    /*
     * private boolean isMaintenance(String token) throws Exception { Config
     * conf = this.getServiceConfiguration(token, FRONTEND_ID); String
     * userGroups = ";" + Utils.extractTokenKey(token, "userGroup")+";";
     * 
     * String xpath =
     * "//group[@name='tema']/section[@name='buttons']/actions/section/button[@action='login']/group"
     * ; List<OMElement> groupNodes = conf.getNodes(xpath);
     * 
     * if (groupNodes.size()==0) return false;
     * 
     * for(OMElement item:groupNodes) { if (userGroups.contains(";" +
     * item.getText() + ";")) return false; }
     * 
     * return true; }
     */
//    private Config getServiceConfiguration(String token, long serviceId) throws Exception {
//	try {
//	    ConfigurationManager confMan = new ConfigurationManager();
//	    String servConfig = confMan.getServiceConfig(token, serviceId);
//
//	    Config conf = new Config();
//	    conf.writeConfig(servConfig);
//
//	    return conf;
//	}
//	catch (Exception e) {
//	    throw e;
//	}
//    }

    public boolean isAuthorized(String token, String username, String service, String method) throws Exception {

        logger.debug(String.format("isAuthorized(%s, %s, %s, %s)", token, username, service, method));

        // implementare il controllo se sito in manutenzione
        if (!method.equalsIgnoreCase("getGroupsOfUser") && !method.equalsIgnoreCase("getUserInfo") && !method.equalsIgnoreCase("getUser") && !method.equalsIgnoreCase("readConfigParam")) {
            if (isMaintenance(token))
                return false;
        }
        // *********************************************************************

        String ipCaller = Utils.extractTokenKey(token, "ipaddr");
        String authType = Utils.extractTokenKey(token, "auth");

        String userGroup = "";
        String application = "";

        if (Utils.hasTokenKey(token, "userGroup"))
            userGroup = Utils.extractTokenKey(token, "userGroup");

        if (Utils.hasTokenKey(token, "app"))
            application = Utils.extractTokenKey(token, "app");

        // passa username vuoto perche' viene applicato il filtro sui gruppi nel
        // while
        List<Map<String, Object>> rules = getRules(service, method, "", authType);

        userGroup = ";" + userGroup + ";";

        for (Map<String, Object> rule : rules) {

            boolean enabled = Boolean.parseBoolean(rule.get("enabled").toString());
            if (!enabled) {
                continue;
            }

            boolean ipCheck = true;
            boolean userGroupCheck = true;
            boolean kfCheck = true;

            String ips = rule.get("callerip").toString();
            if (!ips.equals("*"))
                ipCheck = filterIp(ips, ipCaller);

            String ug = rule.get("userGroup").toString();

            if (!ug.equals("*"))
                userGroupCheck = (userGroup.contains(";" + ug + ";") || username.equalsIgnoreCase(ug));

            String kf = rule.get("keyfilter").toString();

            if (!kf.equals("*"))
                kfCheck = kf.equalsIgnoreCase(application);

            if (ipCheck == true && userGroupCheck == true && kfCheck == true)
                return true;
        }

        throw new Exception("Token non autorizzato ad eseguire la chiamata.");
    }

    public List<Map<String, Object>> getRules(String serviceName, String methodName, String userName, String authType) throws Exception {

        logger.debug(String.format("getRules(%s,%s,%s,%s)", serviceName, methodName, userName, authType));

        serviceName = Utils.validateField(serviceName);
        methodName = Utils.validateField(methodName);
        userName = Utils.validateField(userName).replace("\\", "\\\\");
        authType = Utils.validateField(authType);
        String sql = "select a.*,m.methodname,m.displayname,m.visible from authorizations a,servicemethods m where a.idmethod=m.id";

        if (!serviceName.equalsIgnoreCase(""))
            sql += " and (a.servicename='" + serviceName + "' or a.servicename='*')";

        if (!methodName.equalsIgnoreCase(""))
            sql += " and (m.methodName='" + methodName + "' or m.methodName='*')";

        if (!authType.equalsIgnoreCase(""))
            sql += " and (a.authType='" + authType + "' or a.authType='*')";

        if (!userName.equalsIgnoreCase(""))
            sql += " and (a.userGroup='" + userName + "' or a.userGroup='*')";

        logger.debug("getRules query: " + sql);

        return jdbcTemplate.queryForList(sql);
    }

    public void addRule(String serviceName, String methodId, String callerIp, String keyFilter, String userGroup, String authType, String systemRule, String enabled) throws Exception {

        logger.debug(String.format("addRule(%s,%s,%s,%s,%s,%s,%s,%s)", serviceName, methodId, callerIp, keyFilter, userGroup, authType, systemRule, enabled));

        serviceName = Utils.validateField(serviceName, "*");
        callerIp = Utils.validateField(callerIp, "*");
        keyFilter = Utils.validateField(keyFilter, "*");
        userGroup = Utils.validateField(userGroup, "*");
        authType = Utils.validateField(authType, "*");
        systemRule = Utils.validateField(systemRule, "false");
        enabled = Utils.validateField(enabled, "false");
        methodId = Utils.validateField(methodId, "-1");

        String sql = "";

        sql = String.format("insert into authorizations (servicename,idmethod,callerip,keyfilter, usergroup, authtype,systemrule, enabled) values ('%s',%s,'%s','%s','%s','%s', '%s', '%s')",
                serviceName, methodId, callerIp, keyFilter, userGroup, authType, systemRule, enabled);

        jdbcTemplate.update(sql);
    }

    public void updateRule(long id, String serviceName, String methodId, String callerIp, String keyFilter, String userGroup, String authType, String systemRule, String enabled) throws Exception {

        logger.debug(String.format("updateRule(%s,%s,%s,%s,%s,%s,%s,%s)", serviceName, methodId, callerIp, keyFilter, userGroup, authType, systemRule, enabled));

        serviceName = Utils.validateField(serviceName, "*");
        callerIp = Utils.validateField(callerIp, "*");
        keyFilter = Utils.validateField(keyFilter, "*");
        userGroup = Utils.validateField(userGroup, "*");
        authType = Utils.validateField(authType, "*");
        enabled = Utils.validateField(enabled, "false");
        systemRule = Utils.validateField(systemRule, "false");
        methodId = Utils.validateField(methodId, "-1");

        jdbcTemplate.update("update authorizations " + "set servicename=?,idmethod=?,callerip=?,keyfilter=?, usergroup=?, " + "authtype=?,systemrule=?, enabled=? where id=?", serviceName, methodId,
                callerIp, keyFilter, userGroup, authType, systemRule, enabled, id);
    }

    public void deleteRule(long id) throws Exception {

        logger.debug(String.format("deleteRule(%s)", id));

        jdbcTemplate.update("delete from authorizations where id=?", id);
    }

    public List<Map<String, Object>> getTracerMethodList() throws Exception {

        logger.debug(String.format("getTracerMethodList"));

        return jdbcTemplate.queryForList("select * from servicemethods m, services s where s.id=m.idserv and m.trace=1 ");
    }

    public List<Map<String, Object>> getMethodList(int serviceId) throws Exception {

        logger.debug(String.format("getMethodList(%s)", serviceId));

//	String sql = "select * from servicemethods m, services s where s.id=m.idserv ";

        if (serviceId != -1) {
            return jdbcTemplate.queryForList("select m.id,m.idserv,m.methodname,m.displayname,m.visible,s.groupname,s.description,s.prevxml," +
                    "s.xmlconfig,s.enabled,s.haslogin,s.lastupdate,s.status from servicemethods m, services s where s.id=m.idserv and idserv=?", serviceId);
        } else {
            return jdbcTemplate.queryForList("select m.id,m.idserv,m.methodname,m.displayname,m.visible,s.groupname,s.description,s.prevxml," +
                    "s.xmlconfig,s.enabled,s.haslogin,s.lastupdate,s.status from servicemethods m, services s where s.id=m.idserv");
        }
    }

    private boolean filterIp(String ips, String ipCaller) {

        String[] ipParts = ips.split("\\.");
        String[] ipCallerParts = ipCaller.split("\\.");

        for (int i = 0; i < ipParts.length; i++) {

            if (ipParts[i].equals("*"))
                continue;
            if (!ipParts[i].equals(ipCallerParts[i]))
                return false;
        }

        return true;
    }

}
