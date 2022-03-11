package keysuite.desktop.htmlview;

import it.kdm.orchestratore.session.Session;
import it.kdm.orchestratore.utils.ResourceUtils;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.exceptions.Code;
import keysuite.desktop.exceptions.KSException;
import keysuite.desktop.security.ConfigAppBean;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

public class HtmlViewResolver extends AbstractTemplateViewResolver {

    Environment env;

    public HtmlViewResolver(Environment env) {
        setViewClass(requiredViewClass());
        setPrefix("templates/");
        setSuffix(".html");
        this.env = env;
    }

    //@Nullable
    //private VueView.Configuration configuration = new VueView.Configuration(env);

    //@Nullable
    //public VueView.Configuration defaultConfiguration() {
    //    return this.configuration;
    //}

    /*public boolean useCache(){
        return env.getProperty("spring.vue.cache", Boolean.class, true);
    }*/

    /*public boolean usePreRender(){
        return env.getProperty("spring.vue.prerender", Boolean.class, true);
    }*/

    /*public String getDefaultMasterView(){
        return env.getProperty("spring.vue.default-view","master.ftl");
    }*/

    @Override
    protected Class<?> requiredViewClass() {
        return HtmlView.class;
    }

    protected Object getCacheKey(String viewName, Locale locale) {
        return viewName + "-"+ ResourceUtils.getDomain();
    }

    @Override
    @Nullable
    public View resolveViewName(String viewName, Locale locale) throws Exception {

        locale = ConfigAppBean.getCurrentLocale();

        View view;

        if (viewName!=null && viewName.startsWith("/"))
            viewName = viewName.substring(1);

        if ("".equals(viewName)){
            view = new HtmlView();
            ((HtmlView)view).setExposeSpringMacroHelpers(false);
        } else {
            view = super.resolveViewName(viewName,locale);
        }

        if (view==null)
            view = DesktopUtils.getBean(FreeMarkerViewResolver.class).resolveViewName(viewName,locale);

        if (view==null) {

            HttpServletRequest request = Session.getRequest();

            if (viewName.endsWith("/"))
                viewName = viewName.substring(0,viewName.length()-1);

            String path = "templates/"+viewName;
            List<String> list = ResourceUtils.getResources(path);
            if (list!=null && list.size()>0){
                request.setAttribute("dirlist", viewName );
                if (list.contains("index.html"))
                    return this.resolveViewName(viewName+"/index",locale);
                else
                    return this.resolveViewName("admin/dirlist",locale);
            }

            KSException exc = new KSException(Code.F404, "Pagina non trovata:" + viewName){
                @Override
                public StackTraceElement[] getStackTrace() {
                    return new StackTraceElement[] {super.getStackTrace()[0]};
                }
            };
            exc.addDetail("page", viewName);

            String requestURL = request.getServletPath();
            String queryString = request.getQueryString();
            if (queryString != null)
                requestURL += "?" + queryString;

            exc.setUrl(requestURL);

            throw exc;
        } else
            return view;
    }
}
