/**
 * 
 */
package com.intesigroup.service;

import com.intesigroup.ws.time4user.Time4MindAdminUserManagementStub;

/**
 * @author Daniele Ribaudo
 *
 */
public class Time4UserAdminService extends BaseService<Time4MindAdminUserManagementStub> {

	/**
	 * 
	 * @param endpoint
	 * @param p12Path
	 * @param p12Password
	 * @throws Exception 
	 */
	public Time4UserAdminService(String endpoint, String p12Path, String p12Password) throws Exception {
		super(new Time4MindAdminUserManagementStub(endpoint), p12Path, p12Password);
	}

}
