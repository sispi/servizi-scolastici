package it.kdm.docer.core.authentication;

import it.kdm.docer.commons.sso.SsoUtils;
import it.kdm.docer.core.authentication.bean.LoginInfo;
import org.apache.commons.io.IOUtils;
import org.opensaml.saml2.core.Assertion;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Łukasz Kwasek on 15/12/14.
 */
public class SSOLoginService {

    // TODO controllare se questa classe è da eliminare
    public LoginInfo extractLoginInfo(String sa) throws IllegalArgumentException {

        try {
            Assertion assertion = SsoUtils.parseAssertion(sa);

            String userId = SsoUtils.getUserID(assertion);

            String realUser = SsoUtils.getAssertionAttributeByName(assertion, SsoUtils.REAL_USER_ID);
            if (realUser != null && !realUser.isEmpty()) {
                userId = realUser;
            }

            String codiceEnte = SsoUtils.getAssertionAttributeByName(assertion, SsoUtils.CODICE_ENTE);

            return new LoginInfo(userId, "", codiceEnte, "");

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }

    }

}
