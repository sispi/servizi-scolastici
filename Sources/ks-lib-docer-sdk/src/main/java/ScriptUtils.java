import io.jsonwebtoken.Claims;
import keysuite.docer.client.ClientUtils;

public class ScriptUtils {
    public static Claims parseJWT(String jwtToken){
        return ClientUtils.parseJWTTokenWithoutSecret(jwtToken);
    }
}
