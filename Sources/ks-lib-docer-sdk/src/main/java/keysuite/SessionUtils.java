package keysuite;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionUtils {

    public static String getBaseUrl(HttpServletRequest request){

        String url = request.getScheme().toLowerCase() +"://"+request.getServerName();
        int port = request.getServerPort();
        boolean isSSL = "https".equalsIgnoreCase(request.getScheme().toLowerCase());

        if (isSSL && port != 443 || !isSSL && port != 80)
            url += ":" + port;

        return url;

    }

    /*public static void attach(String jwtToken){

    }*/

    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes!=null)
            return attributes.getRequest();
        else {
            return null;
        }
    }

    public static HttpServletResponse getResponse(){
        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (attributes!=null)
            return attributes.getResponse();
        else {
            //logger.warn("ServletResponseAttributes null");
            return null;
        }
    }
}
