package it.kdm.docer.commons.sso.filter;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import it.kdm.docer.commons.configuration.ConfigurationLoadingException;
import it.kdm.docer.commons.configuration.ConfigurationUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Łukasz Kwasek on 13/01/15.
 */
public class CrossSessionLoadFederaBeanAuthentication implements Filter {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(CrossSessionLoadFederaBeanAuthentication.class);

    private String SERVICE_CONTEXT_MAP;
    private String SERVICE_URL_PREFIX;
    private String SESSION_SERVICE_URL_PREFIX;
    private String cookieName = "docer_authbean_uuid";
    private String beanNamePrefix = "docer_authbean_";
    private String contextName = "/docer";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String configFile = filterConfig.getInitParameter("configFile");

        try {
            File cfg = ConfigurationUtils.loadResourceFile(configFile);
            PropertiesConfiguration config = new PropertiesConfiguration(cfg);

            SERVICE_CONTEXT_MAP = config.getString("icar.constants.service_context_map");
            SERVICE_URL_PREFIX = config.getString("icar.constants.service_url_prefix");
            SESSION_SERVICE_URL_PREFIX = config.getString("icar.session.service_url_prefix");

            if (StringUtils.isBlank(SESSION_SERVICE_URL_PREFIX)) {
                SESSION_SERVICE_URL_PREFIX = null;
            }

        } catch (ConfigurationLoadingException|ConfigurationException e) {
            SERVICE_CONTEXT_MAP = "serviceContextMap";
            SERVICE_URL_PREFIX = "service";
            SESSION_SERVICE_URL_PREFIX = null;
        }

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

        try {
            String cookieUuid = getCookieUuid(request);

            if (StringUtils.isNotBlank(cookieUuid)) {
                String beanName = getBeanName(cookieUuid);
                Object authBean = getBeanFromContext(request, beanName);

                putBeanInSession(request, authBean);
            }

        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Object getBeanFromContext(HttpServletRequest request, String beanName) {
        ServletContext context = request.getSession().getServletContext().getContext(contextName);

        if (context != null) {
            byte[] bytes = (byte[]) context.getAttribute(beanName);
            return SerializationUtils.deserialize(bytes);
        } else {
            String message = "Non è stato possibile trovare il contesto %s. E' stato abilitato il crossContext nell'application " +
                    "container? Se sì, verificare che il contesto sia correttamente impostato.";
            throw new NullPointerException(String.format(message, contextName));
        }
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

    private void putBeanInSession(HttpServletRequest request, Object authBean) {
        Map<String, Object> serviceContextMap = new HashMap<String, Object>();
        serviceContextMap.put(SESSION_SERVICE_URL_PREFIX, authBean);

        request.getSession().setAttribute(SERVICE_URL_PREFIX, SESSION_SERVICE_URL_PREFIX);
        request.getSession().setAttribute(SERVICE_CONTEXT_MAP, serviceContextMap);
    }

    @Override
    public void destroy() {

    }
}
