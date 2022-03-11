package it.kdm.docer.commons.sso.filter;

import it.cefriel.icar.inf3.ICARConstants;
import it.cefriel.icar.inf3.web.beans.AuthenticationSessionBean;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Łukasz Kwasek on 13/01/15.
 */
public class CrossSessionSaveFederaBeanAuthentication implements Filter {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(CrossSessionSaveFederaBeanAuthentication.class);

    private String cookieName = "docer_authbean_uuid";
    private String beanNamePrefix = "docer_authbean_";
    private String contextName = "/docer";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String confCookieName = filterConfig.getInitParameter("cookieName");
        String confBeanNamePrefix = filterConfig.getInitParameter("beanNamePrefix");
        String confContextName = filterConfig.getInitParameter("contextName");

        if (StringUtils.isNotBlank(confCookieName)) {
            cookieName = confCookieName;
        }

        if (StringUtils.isNotBlank(confBeanNamePrefix)) {
            beanNamePrefix = confBeanNamePrefix;
        }

        if (StringUtils.isNotBlank(confContextName)) {
            contextName = confContextName;
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            AuthenticationSessionBean authBean = getBeanFromSession(request);

            if (authBean != null) {
                String cookieUuid = getCookieUuid(request);

                if (StringUtils.isBlank(cookieUuid)) {
                    cookieUuid = UUID.randomUUID().toString();
                    addCookie(response, cookieUuid);
                }

                String beanName = getBeanName(cookieUuid);

                putBeanInContext(request, beanName, authBean);
            } else {
                log.warn("AuthBean non trovato in sessione!");
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    private void putBeanInContext(HttpServletRequest request, String beanName, AuthenticationSessionBean authBean) {
        ServletContext context = request.getSession().getServletContext().getContext(contextName);

        if (context != null) {
            byte[] bytes = SerializationUtils.serialize(authBean);
            request.getSession().getServletContext().getContext(contextName).setAttribute(beanName, bytes);
        } else {
            String message = "Non è stato possibile trovare il contesto %s. E' stato abilitato il crossContext nell'application " +
                    "container? Se sì, verificare che il contesto sia correttamente impostato.";
            throw new NullPointerException(String.format(message, contextName));
        }
    }

    private void addCookie(HttpServletResponse response, String cookieUuid) {
        Cookie c = new Cookie(cookieName, cookieUuid);
        c.setPath("/");
        response.addCookie(c);
    }

    private String getBeanName(String cookieUuid) {
        return beanNamePrefix + cookieUuid;
    }

    private String getCookieUuid(HttpServletRequest request) {
        Cookie[] cs = request.getCookies();

        for (Cookie c : cs) {
            if (c.getName().equalsIgnoreCase(cookieName)) {
                return c.getValue();
            }
        }

        return null;
    }

    private AuthenticationSessionBean getBeanFromSession(HttpServletRequest request) {
        Map serviceContextMap = (Map) request.getSession().getAttribute(ICARConstants.SERVICE_CONTEXT_MAP);
        String serviceURLPrefix = (String) request.getSession().getAttribute(ICARConstants.SERVICE_URL_PREFIX);
        return (AuthenticationSessionBean) serviceContextMap.get(serviceURLPrefix);
    }

    @Override
    public void destroy() {

    }
}
