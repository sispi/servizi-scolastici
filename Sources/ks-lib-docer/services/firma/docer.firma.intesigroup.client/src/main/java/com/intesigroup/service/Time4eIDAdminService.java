/**
 * 
 */
package com.intesigroup.service;

import com.intesigroup.ws.time4eid.Time4EIDAdministrationServiceStub;

/**
 * @author Daniele Ribaudo
 *
 */
public class Time4eIDAdminService extends BaseService<Time4EIDAdministrationServiceStub> {

	/**
	 * 
	 * @param endpoint
	 * @param p12Path
	 * @param p12Password
	 * @throws Exception
	 */
	public Time4eIDAdminService(String endpoint, String p12Path, String p12Password) throws Exception {
		super(new Time4EIDAdministrationServiceStub(endpoint), p12Path, p12Password);
	}

}
