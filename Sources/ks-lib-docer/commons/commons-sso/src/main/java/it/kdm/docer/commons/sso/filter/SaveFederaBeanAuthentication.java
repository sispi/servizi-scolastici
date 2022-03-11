package it.kdm.docer.commons.sso.filter;

import it.cefriel.icar.inf3.ICARConstants;
import it.cefriel.icar.inf3.web.beans.AuthenticationSessionBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Łukasz Kwasek on 13/01/15.
 */
public class SaveFederaBeanAuthentication implements Filter {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(SaveFederaBeanAuthentication.class);

    private File authBeanFile;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String authBeanPath = filterConfig.getInitParameter("authBeanPath");
        authBeanFile = new File(authBeanPath);

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        try {
            Map serviceContextMap = (Map) request.getSession().getAttribute(ICARConstants.SERVICE_CONTEXT_MAP);
            String serviceURLPrefix = (String) request.getSession().getAttribute(ICARConstants.SERVICE_URL_PREFIX);
            AuthenticationSessionBean authBean = (AuthenticationSessionBean) serviceContextMap.get(serviceURLPrefix);

            if (authBean != null) {
                byte[] bytes = SerializationUtils.serialize(authBean);
                FileUtils.writeByteArrayToFile(authBeanFile, bytes);
            } else {
                log.warn("AuthBean non trovato in sessione!");
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
