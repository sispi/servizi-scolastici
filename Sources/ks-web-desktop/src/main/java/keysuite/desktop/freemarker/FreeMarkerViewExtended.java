package keysuite.desktop.freemarker;

import com.google.common.base.Strings;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import it.kdm.orchestratore.session.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.exceptions.KSRuntimeException;
import keysuite.desktop.htmlview.IHTMLTemplate;
import keysuite.desktop.security.ConfigAppBean;
import keysuite.freemarker.TemplateUtils;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

public class FreeMarkerViewExtended extends FreeMarkerView {

    public static Map<String,Object> buildModel(IHTMLTemplate template, HttpServletRequest request, HttpServletResponse response, Map<String, Object> model) throws IOException, TemplateException{

        Map<String,Object> appBean = ConfigAppBean.getBeanForCurrentRequest();
        Map<String,Object> theme = ConfigAppBean.getCurrentTheme();
        Map<String,Object> header = ConfigAppBean.getCurrentHeaderConfig();

        for ( String key : appBean.keySet() ){
            if (key.startsWith("@"))
                model.put(key,appBean.get(key));
        }

        if (template!=null){
            model.put("head",template.getHead());
            model.put("title",template.getTitle());
            model.put("absolutePath",template.getAsbolutePath());
            model.put("lang",template.getLanguage());
            model.put("bodyAttributes",template.getBodyAttributes());

            Map<String,String> satts = template.getServerAttributes();

            for ( String key : satts.keySet() ){
                model.put(key,satts.get(key));
            }
        }

        String requestURL = (String) model.get("requestURL");

        if (requestURL==null){
            requestURL = DesktopUtils.getRequestURL(request);
            model.put("requestURL",requestURL);
        }

        //boolean isHtml = (response.getContentType()==null || response.getContentType().contains("html"));

        //model.put("isHtml",isHtml);

        if (!model.containsKey("context")){
            model.put("context", ConfigAppBean.getBeanForCurrentRequest().get("context") );
        }

        model.put("lang",ConfigAppBean.getCurrentLang());

        model.put("webClient", new DesktopUtils.Client());

        String appName = (String) appBean.get("appName");

        model.put("appName", appName );
        model.put("menu", appBean.get("menu") );
        model.put("header", header );

        model.put("app", appBean);
        model.put("theme", theme );

        model.put("apiClient", DesktopUtils.buildAPIClient());

        return new LinkedHashMap<>(model);
    }

    public static void renderPage(HttpServletRequest request, HttpServletResponse response,String body, Map<String, Object> model) throws IOException, TemplateException{

        String ctype = (String) model.get("@content-type");

        if (ctype!=null)
            response.setContentType(ctype);

        boolean isHtml = (response.getContentType()==null || response.getContentType().contains("html"));

        if (!model.containsKey("body")){
            model.put("body",body);

            if (body.startsWith("[#ftl]")){
                DesktopUtils.setTimer("ftl-body");
                try {
                    body = TemplateUtils.ftlHandler(body, model);
                } catch (Exception e) {
                    List warnings = (List) model.getOrDefault("warnings", new ArrayList<>() );
                    warnings.add(new KSRuntimeException(e));
                    model.put("warnings", warnings);
                    body ="";
                }
                DesktopUtils.setTimer("ftl-body");
            }
        } else {
            Object b = model.get("body");

            if (b instanceof InputStream){
                if (response.getContentType()==null || response.getContentType().contains("text/html"))
                    response.setContentType("application/octet-stream");

                StreamUtils.copy( (InputStream) b, response.getOutputStream() );
                return;
            } else if (b instanceof String) {
                body = (String) b;
            } else {
                isHtml = false;
                response.setContentType("application/json");
                body = DesktopUtils.toJson(b);
            }
        }

        if (isHtml) {

            String xFragment = request.getHeader("X-fragment");

            //String title = (String) model.get("title");

            String title = Session.getRequest().getParameter(":title");
            if (Strings.isNullOrEmpty(title))
                title = (String) model.get("title");
            if (Strings.isNullOrEmpty(title))
                title = Session.getRequest().getRequestURI().substring(Session.getRequest().getRequestURI().lastIndexOf("/")+1);

            if (title.contains("${"))
                title = TemplateUtils.ftlHandler(title,model);

            model.put("title",title);

            model.put("body", body);

            if (!Strings.isNullOrEmpty(title))
                response.addHeader("Content-Title", title);

            if (!Strings.isNullOrEmpty(xFragment)) {

                Map<String, Object> fragmentModel = new HashMap<>(model);

                fragmentModel.put("xFragment", xFragment);

                body = TemplateUtils.ftlHandler("themes/common/fragment", fragmentModel);

            } else {

                Map<String, Object> masterModel = new HashMap<>(model);

                Map<String,Object> theme = (Map) model.get("theme");
                Map<String,Object> appBean = (Map) model.get("app");

                if ( request.getParameter("inspector")!=null){
                    Map<String,Object> inspector = (Map) model.get("inspector");
                    if (inspector == null){
                        inspector = new LinkedHashMap<>();
                    }
                    inspector.put("timersInfo",DesktopUtils.getTimersInfo());

                    Map<String,Object> excModel = (Map) request.getAttribute("exceptionModel");

                    if (excModel!=null){
                        inspector.put("model",getCleanedModel(excModel));
                    } else {
                        inspector.put("model",getCleanedModel(model));
                    }

                    masterModel.put("inspector",inspector);
                }

                String masterPath = "themes/"+theme.get("name") + "/";
                String masterPage = (String) appBean.get("master");
                if (Strings.isNullOrEmpty(masterPage))
                    masterPage = "master.ftl";

                try{
                    body = TemplateUtils.ftlHandler(masterPath+masterPage, masterModel);
                } catch (Exception e) {
                    List warnings = (List) masterModel.getOrDefault("warnings", new ArrayList<>() );
                    warnings.add(new KSRuntimeException(e,"Errore nel tema "+theme.get("name")+". Viene utilizzato il default."));
                    masterModel.put("warnings", warnings);

                    body = TemplateUtils.ftlHandler("themes/default/master.ftl", masterModel);
                }

                body = body.replace("$#noparse","$[");
            }
        }

        response.setCharacterEncoding(Charset.defaultCharset().name());

        Writer writer = response.getWriter();
        writer.write(body);
        writer.flush();
        writer.close();
    }

    static private Map<String, Object> getCleanedModel(Map<java.lang.String, java.lang.Object> model){
        Map m2 = new LinkedHashMap(model);
        m2.remove("springMacroRequestContext");
        m2.remove("org.springframework.validation.BindingResult.object");
        m2.remove("view");
        m2.remove("org.springframework.validation.BindingResult.utils");
        m2.remove("org.springframework.validation.BindingResult.response");
        m2.remove("utils");
        m2.remove("webClient");
        m2.remove("head");
        m2.remove("body");
        m2.remove("bodyAttributes");
        m2.remove("inspector");
        return m2;
    }


    @Override
    protected void processTemplate(Template template, SimpleHash hashModel, HttpServletResponse response)
            throws IOException, TemplateException {

        if (template==null)
            throw new ApplicationContextException("Failed to load ftl template");

        Locale locale = ConfigAppBean.getCurrentLocale();

        String lang;

        if (locale!=null)
            lang = locale.getLanguage();
        else
            lang = Session.getRequest().getLocale().getLanguage();

        IHTMLTemplate htmlTmpl = new IHTMLTemplate() {
            public String getHead() { return null; }
            public String getTitle() { return null; }
            public String getAsbolutePath() { return template.getSourceName(); }
            public String getLanguage() { return lang; }
            public Map<String, String> getBodyAttributes() { return new HashMap<>(); }
            public Map<String, String> getServerAttributes() { return new HashMap<>(); }
        };

        Map model = buildModel(htmlTmpl,Session.getRequest(), response, hashModel.toMap());

        String body = TemplateUtils.ftlHandler(template,model);

        renderPage(Session.getRequest(),response,body,model);
    }

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        boolean check = super.checkResource(locale);
        //return true;
        if (!check){
            return false;
            //throw new KSException(Code.F404,"Template non trovato:"+getUrl()).addDetail("template",getUrl());
        }
        else
            return true;
    }
}
