/**
 * 
 */
package com.intesigroup.service;

import com.intesigroup.ws.time4eid.Time4EIDAuthenticationServiceStub;

/**
 * @author Daniele Ribaudo
 *
 */
public class Time4eIDAuthService extends BaseService<Time4EIDAuthenticationServiceStub> {

	/**
	 * 
	 * @param endpoint
	 * @param p12Path
	 * @param p12Password
	 * @throws Exception
	 */
	public Time4eIDAuthService(String endpoint, String p12Path, String p12Password) throws Exception {
		super(new Time4EIDAuthenticationServiceStub(endpoint), p12Path, p12Password);
	}

}
