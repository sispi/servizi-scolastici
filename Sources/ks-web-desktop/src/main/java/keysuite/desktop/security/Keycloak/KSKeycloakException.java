package keysuite.desktop.security.Keycloak;

import keysuite.desktop.exceptions.KSExceptionForbidden;
import org.keycloak.adapters.spi.AuthenticationError;

public class KSKeycloakException extends KSExceptionForbidden implements AuthenticationError {
    public KSKeycloakException(Throwable throwable, String message) {
        super(throwable,message);
    }
}
