package it.kdm.docer.core.configuration;

import it.kdm.docer.commons.Utils;

import java.util.List;

import org.slf4j.Logger;

public class ConfigurationService {

	Logger logger = org.slf4j.LoggerFactory.getLogger(ConfigurationService.class);

	public String readConfigParam(String token, String category, String keyName)
			throws Exception {
		logger.debug(String.format("readConfigParam(%s,%s)", category, keyName));

		ConfigurationManager manager = new ConfigurationManager();
		return manager.readConfigParam(category, keyName);
	}

	public String readConfigCategory(String token, String category)
			throws Exception {
		logger.debug(String.format("readConfigParam(%s)", category));

		ConfigurationManager manager = new ConfigurationManager();
		return Utils.convertToXML(manager.readConfigParam(category));
	}

	public boolean writeConfigParam(String token, String category,
			String keyName, String keyValue) throws Exception {
		logger.debug(String.format("writeConfigParam(%s,%s,%s)", category,
				keyName, keyValue));

		ConfigurationManager manager = new ConfigurationManager();
		manager.writeConfigParam(category, keyName, keyValue);

		return true;
	}

	public boolean deleteConfigCategory(String token, String category)
			throws Exception {
		logger.debug(String.format("deleteConfigParam(%s)", category));

		ConfigurationManager manager = new ConfigurationManager();
		manager.deleteConfigParam(category);

		return true;
	}

	public boolean deleteConfigParam(String token, String category,
			String keyName) throws Exception {
		logger.debug(String.format("deleteConfigParam(%s,%s)", category,
				keyName));

		ConfigurationManager manager = new ConfigurationManager();
		manager.deleteConfigParam(category, keyName);

		return true;
	}

	public boolean addService(String token, String url, int enabled, int idserv)
			throws Exception {
		logger.debug(String
				.format("addService(%s,%s,%s)", url, enabled, idserv));

		ConfigurationManager manager = new ConfigurationManager();
		manager.addService(url, enabled, idserv);

		return true;
	}

	public boolean removeService(String token, long id) throws Exception {
		logger.debug(String.format("removeService(%s)", id));

		ConfigurationManager manager = new ConfigurationManager();
		manager.removeService(id);

		return true;
	}

	public boolean updateService(String token, long id, String url,
			int enabled, int idserv) throws Exception {
		logger.debug(String.format("updateService(%s,%s,%s,%s)", id, url,
				enabled, idserv));

		ConfigurationManager manager = new ConfigurationManager();
		manager.updateService(id, url, enabled, idserv);

		return true;
	}

	public String readService(String token, long id) throws Exception {
		logger.debug(String.format("readService(%s)", id));

		ConfigurationManager manager = new ConfigurationManager();
		String xml = Utils.convertToXML(manager.readService(id));

		return xml;
	}

	public String getServiceList(String token) throws Exception {
		logger.debug(String.format("getServiceList"));

		ConfigurationManager manager = new ConfigurationManager();
		String xml = Utils.convertToXML(manager.getServiceList(token));

		return xml;
	}

	public String getLoginServices(String token) throws Exception {
		logger.debug(String.format("getLoginServices()"));

		ConfigurationManager manager = new ConfigurationManager();
		String xml = Utils.convertToXML(manager.getLoginServices());

		return xml;
	}

	public String getServiceConfigHistory(String token, long id) throws Exception {
		logger.debug(String.format("getServiceConfigList"));

		ConfigurationManager manager = new ConfigurationManager();
		String xml = Utils.convertToXML(manager.getServiceConfigHistory(token, id));

		return xml;
	}
	
	public String getServiceInstances(String token, long id) throws Exception {
		logger.debug(String.format("getServiceInstances(%s)", id));

		ConfigurationManager manager = new ConfigurationManager();
		String xml = Utils.convertToXML(manager.getServiceInstances(id),
				String.valueOf(id));

		return xml;
	}
	
	public String getServiceUsers(String token, long idServ) throws Exception {
		logger.debug("getServiceUsers()");

		ConfigurationManager manager = new ConfigurationManager();
		String xml = Utils.convertToXML(manager.getServiceUsers(idServ),
				String.valueOf(idServ));

		return xml;
	}
	
	public boolean addServiceUser(String token, long idServ, String application,
			String ente, String username, String password, boolean enabled)
					throws Exception {
		logger.debug("addServiceUser()");

		ConfigurationManager manager = new ConfigurationManager();
		manager.addServiceUser(idServ, application, ente, 
				username, password, enabled);

		return true;
	}
	
	public boolean updateServiceUser(String token, long id, long idServ, 
			String application, String ente, String username, 
			String password, boolean enabled) throws Exception {
		logger.debug("updateServiceUser()");
		
		ConfigurationManager manager = new ConfigurationManager();
		manager.updateServiceUser(id, idServ, application, ente, 
				username, password, enabled);

		return true;
	}
	
	public boolean removeServiceUser(String token, long id) throws Exception {
		logger.debug("removeServiceUser()");

		ConfigurationManager manager = new ConfigurationManager();
		manager.removeServiceUser(id);

		return true;
	}

	public boolean testServiceInstance(String token, String url)
			throws Exception {
		logger.debug(String.format("testServiceInstance(%s)", url));

		ConfigurationManager manager = new ConfigurationManager();
		try {
			manager.testServiceInstance(token, url);
		} catch (Exception e) {
			throw e;
		}

		return true;
	}

	/*
	 * public long getIdService(String token, String groupName) throws Exception
	 * { logger.debug(String.format("getIdService(%s)", groupName));
	 * 
	 * ConfigurationManager manager = new ConfigurationManager(); long idserv =
	 * manager.getIdService(groupName);
	 * 
	 * 
	 * return idserv; }
	 */

	public String getServiceConfig(String token, long id) throws Exception {
		logger.debug(String.format("getServiceConfig(%s)", id));

		ConfigurationManager manager = new ConfigurationManager();
		return manager.getServiceConfig(token,id);

	}

	public boolean setServiceConfig(String token, long id, String xmlConfig)
			throws Exception {
		logger.debug(String.format("setServiceConfig(%s,%s)", id, xmlConfig));

		ConfigurationManager manager = new ConfigurationManager();
		manager.setServiceConfig(token, id, xmlConfig);

		return true;
	}

	public boolean publishServiceConfig(String token, long id) throws Exception {
		logger.debug(String.format("publishServiceConfig(%s)", id));

		ConfigurationManager manager = new ConfigurationManager();
		manager.publishServiceConfig(token, id);

		return true;
	}

	public List<String> publishAllServiceConfig(String token) throws Exception {
		logger.debug(String.format("publishServiceConfig()"));

		ConfigurationManager manager = new ConfigurationManager();
		return manager.publishServiceConfig();

	}

}
