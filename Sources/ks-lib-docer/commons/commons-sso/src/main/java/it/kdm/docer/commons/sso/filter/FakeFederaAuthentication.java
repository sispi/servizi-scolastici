package it.kdm.docer.commons.sso.filter;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;

import it.kdm.docer.commons.configuration.ConfigurationLoadingException;
import it.kdm.docer.commons.configuration.ConfigurationUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Łukasz Kwasek on 13/01/15.
 */
public class FakeFederaAuthentication implements Filter {

    private String SERVICE_CONTEXT_MAP;
    private String SERVICE_URL_PREFIX;
    private String SESSION_SERVICE_URL_PREFIX;
    private Object authBean;

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

        String authBeanPath = filterConfig.getInitParameter("authBeanPath");

        InputStream is = this.getClass().getResourceAsStream(authBeanPath);
        authBean = SerializationUtils.deserialize(is);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Map<String, Object> serviceContextMap = new HashMap<String, Object>();
        serviceContextMap.put(SESSION_SERVICE_URL_PREFIX, authBean);

        request.getSession().setAttribute(SERVICE_URL_PREFIX, SESSION_SERVICE_URL_PREFIX);
        request.getSession().setAttribute(SERVICE_CONTEXT_MAP, serviceContextMap);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
