/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.kdm.docer.core.configuration;

import com.google.common.base.Strings;
import it.kdm.docer.commons.TicketCipher;
import it.kdm.docer.commons.Utils;
import it.kdm.docer.commons.XMLUtil;
import it.kdm.docer.core.SpringContextHolder;
import it.kdm.docer.core.authentication.AuthenticationService;
import it.kdm.docer.core.authentication.bean.LoginInfo;
import it.kdm.docer.core.authentication.bean.WSTicketAuthInfo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Stefano Vigna
 */
public class ConfigurationManager {

    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationService.class);
    private HttpClient client = new HttpClient();

    private JdbcTemplate jdbcTemplate;

    public ConfigurationManager() {
	ApplicationContext ctx = SpringContextHolder.getCtx();
	jdbcTemplate = ctx.getBean("jdbcTemplate", JdbcTemplate.class);
    }
	//***************************************************************************************
	// METODI DI GESTIONE DELLE SESSIONI UTENTE SU DB (CACHE TICKET E USERINFO)
	//***************************************************************************************
	public void putLoginInfo(String token, LoginInfo loginInfo) throws SQLException {
		logger.debug(String.format("ConfigurationManager:putLoginInfo(%s,%s)", loginInfo.hashCode(), token));

		token = Utils.validateField(token);

		String loginInfoData = loginInfo.getLoginInfoData();
		TicketCipher tc = new TicketCipher();
		String enc = tc.encryptTicket(loginInfoData);
		//TODO: inserire controllo se già esiste=update e non insert
		LoginInfo logInfo = getLoginInfo(token);
		if (logInfo==null)
			jdbcTemplate.update("insert into loginInfo (token,loginInfo,insertDate) values (?,?,GETDATE())", token, enc);
		else
			jdbcTemplate.update("update loginInfo set loginInfo=?, insertDate=GETDATE() where token=? ", enc, token);
	}

	public LoginInfo getLoginInfo(String token) throws SQLException {
		logger.debug(String.format("ConfigurationManager:getLoginInfo(%s)", token));

		List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from loginInfo where token=?", token);

		String value = "";

		LoginInfo loginInfo = null;
		for (Map<String, Object> result : results) {
			value = result.get("loginInfo").toString();
			if (!value.isEmpty()) {
				loginInfo = new LoginInfo();
				TicketCipher tc = new TicketCipher();
				String enc = tc.decryptTicket(value);
				loginInfo.setLoginInfoData(enc);
				break;
			}
		}

		return loginInfo;
	}

	public void putWSTicket(WSTicketAuthInfo wsTicketAuthInfo, String ticket) throws SQLException {
		logger.debug(String.format("ConfigurationManager:putWSTicket(%s,%s)", wsTicketAuthInfo.hashCode(), ticket));

		ticket = Utils.validateField(ticket);
		TicketCipher tc = new TicketCipher();
		String enctic = tc.encryptTicket(ticket);
		//TODO: inserire controllo se già esiste=update e non insert
		String tic = getWSTicket(wsTicketAuthInfo);
		if (Strings.isNullOrEmpty(tic))
			jdbcTemplate.update("insert into ticketsessions (authInfo,ticket,insertDate) values (?,?,GETDATE())", wsTicketAuthInfo.hashCode(), enctic);
		else
			jdbcTemplate.update("update ticketsessions set authInfo=?, insertDate=GETDATE() where ticket=? ", wsTicketAuthInfo.hashCode(), enctic);
	}

	public String getWSTicket(WSTicketAuthInfo wsTicketAuthInfo) throws SQLException {
		logger.debug(String.format("ConfigurationManager:getWSTicket(%s)", wsTicketAuthInfo.hashCode()));

		List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from ticketsessions where authInfo=?", wsTicketAuthInfo.hashCode());

		String value = null;

		for (Map<String, Object> result : results) {
			value = result.get("ticket").toString();
			if (!value.isEmpty()) {
				break;
			}
		}
		if (value!=null) {
			TicketCipher tc = new TicketCipher();
			String tic = tc.decryptTicket(value);
			return tic;
		}
		return value;
	}

	//***************************************************************************************

    public String readConfigParam(String category, String keyName) throws Exception {
	logger.debug(String.format("ConfigurationManager:readConfigParam(%s,%s)", category, keyName));

	category = Utils.validateField(category);
	keyName = Utils.validateField(keyName);

	String sql = String.format("", category, keyName);

	List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from commonconfig where category=? and keyname=?", category, keyName);

	String value = "";

	for (Map<String, Object> result : results) {
	    value = result.get("value").toString();
	    if (!value.isEmpty()) {
		break;
	    }
	}

	return value;
    }

    public List<Map<String, Object>> readConfigParam(String category) throws Exception {
	logger.debug(String.format("ConfigurationManager:readConfigParam(%s)", category));

	category = Utils.validateField(category);

	List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from commonconfig where category=?", category);

	return results;

	/*
	 * List<DefaultKeyValue> valueList = new ArrayList<DefaultKeyValue>();
	 * 
	 * int count = 0; while (rs.next ()) { valueList.add(new
	 * DefaultKeyValue(rs.getString("keyname"), rs.getString("value")));
	 * 
	 * count++; }
	 * 
	 * rs.close(); db.close();
	 * 
	 * return valueList;
	 */

    }

    public void writeConfigParam(String category, String keyName, String keyValue) throws Exception {
	logger.debug(String.format("ConfigurationManager:writeConfigParam(%s,%s,%s)", category, keyName, keyValue));

	String val = readConfigParam(category, keyName);

	category = Utils.validateField(category);
	keyName = Utils.validateField(keyName);
	keyValue = Utils.validateField(keyValue);

	String sql = "";

	if (val.equals("")) {
	    jdbcTemplate.update("insert into commonconfig (category,keyname,value) values (?,?,?)", category, keyName, keyValue);
	}
	else {
	    jdbcTemplate.update("update commonconfig set value=? where category=? and keyname=?", keyValue, category, keyName);
	}
    }

    public void deleteConfigParam(String category) throws Exception {
	logger.debug(String.format("ConfigurationManager:deleteConfigParam(%s)", category));

	category = Utils.validateField(category);

	jdbcTemplate.update("delete from commonconfig where category=?", category);
    }

    public void deleteConfigParam(String category, String keyName) throws Exception {
	logger.debug(String.format("ConfigurationManager:deleteConfigParam(%s,%s)", category, keyName));

	category = Utils.validateField(category);
	keyName = Utils.validateField(keyName);

	jdbcTemplate.update("delete from commonconfig where category=? and keyname=?", category, keyName);
    }

    public void addService(String url, int enabled, int idserv) throws Exception {
	logger.debug(String.format("ConfigurationManager:addService(%s,%s,%s)", url, enabled, idserv));

	url = Utils.validateField(url);

	String enabledString = enabled == 0 ? "false" : "true";

	jdbcTemplate.update("insert into serviceinstance (url,enabled,idserv) values (?,?,?)", url, enabledString, idserv);
	if (enabled == 1) {
	    jdbcTemplate.update("update services set status=1 where id=?", idserv);
	}

    }

    public void removeService(long id) throws Exception {
	logger.debug(String.format("ConfigurationManager:removeService(%s)", id));

	jdbcTemplate.update("delete from serviceinstance where id=?", id);

    }

    public void updateService(long id, String url, int enabled, int idserv) throws Exception {
	logger.debug(String.format("ConfigurationManager:updateService(%s,%s,%s,%s)", id, url, enabled, idserv));

	url = Utils.validateField(url);

	String enabledString = enabled == 0 ? "false" : "true";

	jdbcTemplate.update("update serviceinstance set url=?,enabled=?, idserv=?, status=1 where id=?", url, enabledString, idserv, id);

	if (enabled == 1) {
	    jdbcTemplate.update("update services set status=1 where id=?", idserv);
	}

    }

    public List<Map<String, Object>> readService(long id) throws Exception {
	logger.debug(String.format("ConfigurationManager:readService(%s)", id));

	return jdbcTemplate.queryForList("select * from serviceinstance where id=?", id);

    }

    public List<Map<String, Object>> getLoginServices() throws Exception {
	logger.debug(String.format("ConfigurationManager:getLoginServics"));

	return jdbcTemplate.queryForList("select * from serviceinstance i,services s where i.idserv=s.id and s.hasLogin='true' and i.enabled='true' and s.enabled='true'");
    }

    public Entry<String, String> getSystemCredentials(long servId, String application, String ente) throws Exception {
	logger.debug(String.format("ConfigurationManager:getSystemCredentials"));

	List<Map<String, Object>> results = jdbcTemplate.queryForList("select username, password from serviceusers " + "where idserv=? and (application=? or application='*') "
		+ "and (ente=? or ente='*') " + "and enabled=?", servId, application, ente, true);

	if (results.size() > 0) {
	    Map<String, Object> result = results.get(0);
	    String username = result.get("username").toString();
	    String password = result.get("password").toString();

	    return new AbstractMap.SimpleEntry<String, String>(username, password);
	}

	return null;
    }

    public List<Map<String, Object>> getServiceInstances(long idServ) throws Exception {
	logger.debug(String.format("ConfigurationManager:getServiceInstances"));

	if (idServ != -1) {
	    return jdbcTemplate.queryForList("select i.*,s.groupname,s.description,s.lastupdate from serviceinstance i, services s " + "where s.id=i.idserv and idserv=?", idServ);
	}
	else {
	    return jdbcTemplate.queryForList("select i.*,s.groupname,s.description,s.lastupdate from serviceinstance i, services s " + "where s.id=i.idserv");
	}
    }

    public List<Map<String, Object>> getServiceUsers(long idServ) throws Exception {
	logger.debug(String.format("ConfigurationManager:getServiceInstances"));

	return jdbcTemplate.queryForList("select * from serviceusers where idserv=?", idServ);

    }

    public void addServiceUser(long idServ, String application, String ente, String username, String password, boolean enabled) throws Exception {
	logger.debug("ConfigurationManager:addServiceUser()");

	jdbcTemplate.update("insert into serviceusers " + "(idserv,application,ente,username,password,enabled) " + "values (?,?,?,?,?,?)", idServ, application, ente, username, password, enabled);
    }

    public void updateServiceUser(long id, long idServ, String application, String ente, String username, String password, boolean enabled) throws Exception {
	logger.debug("ConfigurationManager:addServiceUser()");

	jdbcTemplate.update("update serviceusers set " + "idserv=?,application=?,ente=?,username=?," + "password=?,enabled=? where id=?", idServ, application, ente, username, password, enabled, id);
    }

    public void removeServiceUser(long id) throws Exception {
	logger.debug("ConfigurationManager:removeServiceUser()");

	jdbcTemplate.update("delete from serviceusers where id=?", id);
    }

    public void testServiceInstance(String token, String url) throws Exception {
	logger.debug(String.format("ConfigurationManager:testServiceInstance"));

	testCallService(token, url);

    }

    public long getIdService(String groupName) throws Exception {
	logger.debug(String.format("ConfigurationManager:getIdService"));

	List<Map<String, Object>> results = jdbcTemplate.queryForList("select * from services where groupname=?", groupName);

	long value = -1;

	for (Map<String, Object> result : results) {
	    value = Long.parseLong(result.get("id").toString());

	    break;
	}

	return value;
    }

    public List<Map<String, Object>> getServiceList(String token) throws Exception {
	logger.debug(String.format("ConfigurationManager:getServiceList"));

	return getServiceList(token, true);

    }

    public List<Map<String, Object>> getServiceList(String token, boolean enabled) throws Exception {

	String ente = Utils.extractTokenKey(token, "ente");

	List<Map<String, Object>> serviceList = jdbcTemplate.queryForList("select id, groupname, description, enabled, haslogin, lastupdate, status from services where enabled=?",
		Boolean.toString(enabled).toLowerCase());

	String select = "select TOP 1 id, ente, idserv, groupname, author, created, xmlconfig, status, lasterror" + " from servicesconfig" + " where" + " idserv = ? and UPPER(ente)=UPPER(?)"
		+ " order by id desc ";

	for (Map<String, Object> service : serviceList) {

	    Long idserv = (Long) service.get("id");

	    List<Map<String, Object>> servicesConfigurations = jdbcTemplate.queryForList(select, idserv, ente);

	    for (Map<String, Object> serviceConfig : servicesConfigurations) {

		Integer status = (Integer) serviceConfig.get("status");

		service.put("status", status);

		if (serviceConfig.get("created") != null) {
		    service.put("lastupdate", serviceConfig.get("created"));
		}

	    }

	}

	// jdbcTemplate.queryForList("select * from services where enabled=?",
	// Boolean.toString(enabled).toLowerCase());

	return serviceList;

    }


    public List<Map<String, Object>> getServiceConfigHistory(String token, long idserv) throws Exception {

		String ente = Utils.extractTokenKey(token, "ente");
	
		String select = "select TOP 15 id, ente, idserv, groupname, author, created, xmlconfig, status, lasterror" + " from servicesconfig" + " where" + " idserv = ? and UPPER(ente)=UPPER(?)"
			+ " order by created desc ";
		
		List<Map<String, Object>> serviceConfigList =  jdbcTemplate.queryForList(select, idserv, ente);
	
	
		return serviceConfigList;

	}

    public String getServiceConfig(String token, long id) throws Exception {

	String ente = Utils.extractTokenKey(token, "ente");

	List<Map<String, Object>> results = jdbcTemplate.queryForList("select id, xmlconfig from services where id=?", id);

	String value = "";

	for (Map<String, Object> result : results) {

	    String idserv = result.get("id").toString();

	    if (result.get("xmlconfig") != null) {
		value = result.get("xmlconfig").toString();
	    }

	    String select = "select top 1 xmlconfig" + " from servicesconfig" + " where" + " idserv = ? and UPPER(ente)=UPPER(?)" + " order by id desc";

	    List<Map<String, Object>> servicesConfigurations = jdbcTemplate.queryForList(select, idserv, ente);

	    for (Map<String, Object> serviceConfig : servicesConfigurations) {

		if (serviceConfig.get("xmlconfig") != null) {
		    value = serviceConfig.get("xmlconfig").toString();
		}
		break;
	    }

	    break;
	}

	return value;
    }

    public void setServiceConfig(String token, long id, String xmlConfig) throws Exception {

	xmlConfig = Utils.validateField(xmlConfig);

	try {
	    new XMLUtil(xmlConfig);
	}
	catch (Exception e) {
	    throw new Exception("Formato XML config non valido.");
	}

	String ente = Utils.extractTokenKey(token, "ente");
	String uid = Utils.extractTokenKey(token, "uid");

	String groupName = null;

	try {

	    // Stato: -1 -> Non configurabile da Front-end
	    // Stato: 0 -> Tutto aggiornato
	    // Stato: 1 -> Tutto da aggiornare
	    // Stato: 2 -> Errore anche parziale

	    List<Map<String, Object>> services = jdbcTemplate.queryForList("select groupname from services where id=? and status<>-1", id);

	    for (Map<String, Object> s : services) {

		groupName = String.valueOf(s.get("groupname"));

		jdbcTemplate.update("insert into servicesconfig (ente, idserv, groupname, author, created, xmlconfig,status) values (?,?,?,?,?,?,?)", ente, id, groupName, uid, new Timestamp(
			new DateTime().getMillis()), xmlConfig, 1);
	    }

	}
	catch (EmptyResultDataAccessException e) {
	    logger.error(e.getMessage(), e);
	    throw new Exception("Servizio con ID " + id + " non presente nella tabella services");
	}

    }

    public void publishServiceConfig(String token, long id) throws Exception {

	String uid = Utils.extractTokenKey(token, "uid");
	String ente = Utils.extractTokenKey(token, "ente");

	String idServiceByEnte = null;

	// se e' specificato l'ente ed e' presente il record su servicesconfig
	// la query non può essere fatta tra services e serviceistance ma tra
	// services e servicesconfig
	List<Map<String, Object>> serviceConfigByEnte = jdbcTemplate.queryForList("select TOP 1 id from servicesconfig where idserv=? and UPPER(ente)=UPPER(?) order by id desc", id, ente);
	for (Map<String, Object> sc : serviceConfigByEnte) {
	    if (sc.get("id") != null) {
		idServiceByEnte = String.valueOf(sc.get("id"));
	    }
	}

	if (idServiceByEnte == null) {
	    // non sono presenti record per il servizio
	    // qualcuno ha cliccato su pubblica senza aver salvato la configurazione per ente
	    List<Map<String, Object>> rx = jdbcTemplate.queryForList("select * from services s "
		    + "where s.id=? and s.status<>0 and s.enabled='true'", id);	   
	    
	    String idx = null;
	    String xmlconfigx = null;
	    String groupnamex = null;
	    String statusx = null;
	    
	    
		for (Map<String, Object> scx : rx) {

		    if (scx.get("id") != null) {
			idx = String.valueOf(scx.get("id"));		    
		    }
		    if (scx.get("groupname") != null) {
			groupnamex = String.valueOf(scx.get("groupname"));
		    }
		    if (scx.get("xmlconfig") != null) {
			xmlconfigx = String.valueOf(scx.get("xmlconfig"));
		    }
		    if (scx.get("status") != null) {
			statusx = String.valueOf(scx.get("status"));
		    }
		    
		    break;
		}
		
		if(idx!=null){
			jdbcTemplate.update("insert into servicesconfig (ente, idserv, groupname, author, created, xmlconfig,status) values (?,?,?,?,?,?,?)", ente, id, groupnamex, uid, new Timestamp(
				new DateTime().getMillis()), xmlconfigx, statusx);
			
			serviceConfigByEnte = jdbcTemplate.queryForList("select TOP 1 id from servicesconfig where idserv=? and UPPER(ente)=UPPER(?) order by id desc", id, ente);
			for (Map<String, Object> sc : serviceConfigByEnte) {
			    if (sc.get("id") != null) {
				idServiceByEnte = String.valueOf(sc.get("id"));
			    }
			}
		}

	}

	List<Map<String, Object>> results = null;

	if (idServiceByEnte != null) {
	    results = jdbcTemplate.queryForList(
		    "select si.id, si.url, sc.xmlconfig from serviceinstance si, servicesconfig sc where si.idserv=sc.idserv and sc.id=? and sc.status<>0 and si.enabled='true'", idServiceByEnte);
	}

	String url = "";
	String xmlConfig = "";
	int serviceStatus = 0;

	String lasterror = "";
	if (results != null) {
	    for (Map<String, Object> result : results) {
		url = result.get("url").toString();
		xmlConfig = result.get("xmlconfig").toString();

		try {
		    SendXmlConfig(token, url, xmlConfig);
		}
		catch (Exception e) {
		    lasterror = e.getMessage();
		    serviceStatus = 2;
		}
	    }
	}

	jdbcTemplate.update("update servicesconfig set status=?, lasterror=? where id=?", serviceStatus, lasterror, idServiceByEnte);
    }

    // non utilizzato NON TESTATO
    public List<String> publishServiceConfig() throws Exception {
	logger.debug(String.format("ConfigurationManager:publishServiceConfig()"));

	throw new Exception("Not yet implemented.");
	/*
	 * List<String> errors = new ArrayList<String>();
	 * 
	 * String sql = String.format(
	 * "select * from serviceinstance i, services s where i.idserv=s.id and i.enabled=1 and s.enabled=1"
	 * );
	 * 
	 * db = DatabaseUtils.newInstance(); ResultSet rs =
	 * db.dbExecuteQuery(sql);
	 * 
	 * String url = ""; String xmlConfig = "";
	 * 
	 * while (rs.next()) { try { url = rs.getString("url"); xmlConfig =
	 * rs.getString("xmlconfig"); SendXmlConfig("", url, xmlConfig); }
	 * catch(Exception err) { errors.add(err.getMessage()); }
	 * 
	 * }
	 * 
	 * return errors;
	 */
    }

    private void SendXmlConfig(String token, String url, String xmlConfig) throws ConfigurationException, AuthenticationException, IOException, XMLStreamException {
	String epr = url + "/writeConfig";
	PostMethod method = new PostMethod(epr);

	if (xmlConfig == null || xmlConfig.equals(""))
	    throw new ConfigurationException("Confgurazione non valida (blank).");

	method.addParameter("token", token);

	Base64 encoder = new Base64(true);
	method.addParameter("xmlConfig", encoder.encodeToString(xmlConfig.getBytes()));

	int code = client.executeMethod(method);

	if (code != 200) {
	    
	    String errorResponse = method.getResponseBodyAsString();
	    if (errorResponse.contains("[401] -")) {
		throw new AuthenticationException(
			"Il servizio richiesto non risulta correttamente autenticato. Eseguire nuovamente l'autenticazione al sistema. Se il problema persiste, contattare l'ammnistratore di sistema e segnalare il problema.");
	    }
	    else {
		
		Pattern pattern = Pattern.compile("<[^<>]+Exception.+Exception>");
		Matcher matcher = pattern.matcher(errorResponse);
		if(matcher.find()) {
		    errorResponse = matcher.group(0);		    
		}
		
		//throw new ConfigurationException("Impossibile eseguire la scrittura della configurazione per il servizio: " + url);
		errorResponse = errorResponse.replaceAll("<[^<>]+>","");
		throw new ConfigurationException("Impossibile eseguire la scrittura della configurazione per il servizio: " + url +": " +errorResponse);
				
	    }

	}
    }

    private void testCallService(String token, String url) throws Exception {
	try {
	    String epr = url + "?wsdl";
	    GetMethod method = new GetMethod(epr);

	    // method.addParameter("token", token);

	    client.executeMethod(method);

	    /*
	     * String content = method.getResponseBodyAsString().split("\n")[5];
	     * 
	     * XMLInputFactory factory = XMLInputFactory.newInstance();
	     * XMLStreamReader reader = factory.createXMLStreamReader( new
	     * StringReader(content)); StAXOMBuilder builder = new
	     * StAXOMBuilder(reader); OMElement doc =
	     * builder.getDocumentElement();
	     * 
	     * String responseString = doc.getFirstElement().getText();
	     */
	    if (method.getStatusCode() != 200) {
		throw new Exception(method.getStatusText());
	    }

	}
	catch (Exception e) {
	    throw e;
	}
    }

    // public void testConfig(String token, long id) throws Exception {
    //
    // String ente = Utils.extractTokenKey(token, "ente");
    // String uid = Utils.extractTokenKey(token, "uid");
    //
    //
    // System.out.println("ente: " +ente);
    //
    // String groupName =null;
    // String sql =
    // String.format("select groupname from services where id = '%s'",id);
    // try {
    // Object o = (String) jdbcTemplate.queryForObject(sql, String.class);
    // groupName = (String) o;
    //
    //
    //
    // //
    // jdbcTemplate.update("update servicesconfig set active=5 where ente=? and idserv=?",ente,
    // id);
    // //
    // jdbcTemplate.update("insert into servicesconfig (ente, idserv, groupname, author, created, xmlconfig, status, active) values (?,?,?,?,?,?,?,?)",
    // // ente, id, groupName, uid, new Timestamp(new DateTime().getMillis()),
    // "<xml />", 1, 1);
    //
    // jdbcTemplate.update("insert into servicesconfig (ente, idserv, groupname, author, created, xmlconfig, status, active) values (?,?,?,?,?,?,?,?)",
    // ente, id, groupName, uid, new Timestamp(new DateTime().getMillis()),
    // "<xml />", 1, 1);
    //
    // } catch (EmptyResultDataAccessException e) {
    // e.printStackTrace();
    // }
    //
    // }

    // public List<Map<String, Object>> testGetServiceList(String token) throws
    // Exception {
    //
    // String ente = Utils.extractTokenKey(token, "ente");
    // boolean enabled = true;
    // // List<Map<String, Object>> serviceList =
    // // jdbcTemplate.queryForList("select * from services where enabled=?",
    // // Boolean.toString(enabled).toLowerCase());
    //
    // List<Map<String, Object>> serviceList =
    // jdbcTemplate.queryForList("select id, groupname, description, enabled, haslogin, lastupdate, status from services where enabled=?",
    // Boolean.toString(enabled).toLowerCase());
    //
    // String select =
    // "select id, ente, idserv, groupname, author, created, xmlconfig, status, lasterror"
    // + " from servicesconfig" + " where"
    // + " idserv = ? and ucase(ente)=ucase(?)" + " order by id desc limit 1";
    //
    // for (Map<String, Object> service : serviceList) {
    //
    // Long idserv = (Long) service.get("id");
    //
    // List<Map<String, Object>> servicesConfigurations =
    // jdbcTemplate.queryForList(select, idserv, ente);
    //
    // for (Map<String, Object> serviceConfig : servicesConfigurations) {
    //
    // Integer status = (Integer) serviceConfig.get("status");
    //
    // service.put("status", status);
    //
    // if(serviceConfig.get("created")!=null){
    // service.put("lastupdate", serviceConfig.get("created"));
    // }
    //
    //
    // }
    //
    // }
    //
    // // jdbcTemplate.queryForList("select * from services where enabled=?",
    // // Boolean.toString(enabled).toLowerCase());
    //
    // return serviceList;
    // }

    // public String testGetServiceConfig(String token, long id) throws
    // Exception {
    //
    // String ente = Utils.extractTokenKey(token, "ente");
    //
    // List<Map<String, Object>> results =
    // jdbcTemplate.queryForList("select id, xmlconfig from services where id=?",
    // id);
    //
    // String value = "";
    //
    // for (Map<String, Object> result : results) {
    //
    // String idserv = result.get("id").toString();
    //
    // if (result.get("xmlconfig") != null) {
    // value = result.get("xmlconfig").toString();
    // }
    //
    // String select = "select xmlconfig" + " from servicesconfig" + " where" +
    // " idserv = ? and ucase(ente)=ucase(?)" + " order by id desc limit 1";
    //
    // List<Map<String, Object>> servicesConfigurations =
    // jdbcTemplate.queryForList(select, idserv, ente);
    //
    // for (Map<String, Object> serviceConfig : servicesConfigurations) {
    //
    // if (serviceConfig.get("xmlconfig") != null) {
    // value = serviceConfig.get("xmlconfig").toString();
    // }
    // break;
    // }
    //
    // break;
    // }
    //
    // return value;
    // }

    // public void testSetServiceConfig(String token, long id, String xmlConfig)
    // throws Exception {
    //
    // xmlConfig = Utils.validateField(xmlConfig);
    //
    // try {
    // new XMLUtil(xmlConfig);
    // }
    // catch (Exception e) {
    // throw new Exception("Formato XML config non valido.");
    // }
    //
    // String ente = Utils.extractTokenKey(token, "ente");
    // String uid = Utils.extractTokenKey(token, "uid");
    //
    // String groupName = null;
    //
    // try {
    //
    // // Stato: -1 -> Non configurabile da Front-end
    // // Stato: 0 -> Tutto aggiornato
    // // Stato: 1 -> Tutto da aggiornare
    // // Stato: 2 -> Errore anche parziale
    //
    // List<Map<String, Object>> services =
    // jdbcTemplate.queryForList("select groupname from services where id=? and status<>-1",
    // id);
    //
    //
    // for (Map<String, Object> s : services) {
    //
    // groupName = String.valueOf(s.get("groupname"));
    //
    // jdbcTemplate.update("insert into servicesconfig (ente, idserv, groupname, author, created, xmlconfig,status) values (?,?,?,?,?,?,?)",
    // ente, id, groupName, uid, new Timestamp(
    // new DateTime().getMillis()), xmlConfig, 1);
    //
    // System.out.println("nuovo record");
    // }
    //
    // if(services.size()==0)
    // System.out.println("nessun record inserito");
    //
    //
    // }
    // catch (EmptyResultDataAccessException e) {
    // e.printStackTrace();
    // throw new Exception("Servizio con ID " + id +
    // " non presente nella tabella services");
    // }
    //
    // }
    //

    // public void testPublishServiceConfig(String token, long id) throws
    // Exception {
    //
    // String ente = Utils.extractTokenKey(token, "ente");
    //
    // String idServiceByEnte = null;
    //
    // // se e' specificato l'ente ed e' presente il record su servicesconfig la
    // query non può essere fatta tra services e serviceistance ma tra services
    // e servicesconfig
    // List<Map<String, Object>> serviceConfigByEnte =
    // jdbcTemplate.queryForList("select id from servicesconfig where idserv=? and ucase(ente)=ucase(?) order by id desc limit 1",id,
    // ente);
    // for (Map<String, Object> sc : serviceConfigByEnte) {
    //
    // if(sc.get("id")!=null){
    // idServiceByEnte = String.valueOf(sc.get("id"));
    // }
    //
    // }
    //
    // List<Map<String, Object>> results = null;
    //
    // if(idServiceByEnte!=null){
    // results =
    // jdbcTemplate.queryForList("select si.id, si.url, sc.xmlconfig from serviceinstance si, servicesconfig sc where si.idserv=sc.idserv and sc.id=? and sc.status<>0 and si.enabled='true'",
    // idServiceByEnte);
    // }
    //
    //
    // String url = "";
    // String xmlConfig = "";
    // int serviceStatus = 0;
    //
    // if(results!=null){
    // for (Map<String, Object> result : results) {
    // url = result.get("url").toString();
    // xmlConfig = result.get("xmlconfig").toString();
    //
    // try {
    //
    // //SendXmlConfig(token, url, xmlConfig);
    // System.out.println("SendXmlConfig --> " +url +" -->" +"; config length: "
    // +xmlConfig.length());
    // }
    // catch (Exception e) {
    // serviceStatus = 2;
    // }
    // }
    // }
    //
    //
    // jdbcTemplate.update("update servicesconfig set status=? where id=?",
    // serviceStatus, idServiceByEnte);
    //
    // }

}
