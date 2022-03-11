package it.kdm.docer.core.authentication.providers;

import java.util.Map;

import it.kdm.docer.core.authentication.bean.LoginInfo;
import org.apache.commons.httpclient.auth.AuthenticationException;

import it.kdm.docer.core.authentication.BaseAuthProvider;
import org.apache.commons.lang.NotImplementedException;

public class TestProvider extends BaseAuthProvider {

	@Override
	public LoginInfo login(String username, String password, String codiceEnte, String application)
			throws AuthenticationException {
            return new LoginInfo(username, password, codiceEnte, application);
            //throw new AuthenticationException("Authentication Failed");
	}

	@Override
	public boolean verifyTicket(String token, String ticket) {
		return true;
	}

	@Override
	public String renewTicket(String token, String ticket) {
		return "ticket";
	}

	@Override
	public Map<String, String> getUserInfo(String ticket, String uid)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public String[] getUserGroups(String token) throws Exception {
        
        String[] groups = new String[3];
        groups[0] = "SYS_ADMIN";
        groups[1] = "SYS_ADMINS";
        groups[2] = "TEST_GROUP";
        
        return groups;
    }

	@Override
	public String getEnteDescription(String token) throws Exception {
		//TODO: Implement
		throw new NotImplementedException();
	}

}
