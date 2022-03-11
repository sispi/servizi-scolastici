package keysuite.desktop.htmlview;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.desktop.freemarker.FreeMarkerViewExtended;
import keysuite.desktop.security.ConfigAppBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContextException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.AbstractTemplateView;

public class HtmlView extends AbstractTemplateView {

    @Nullable
    private String encoding;

    public void setEncoding(@Nullable String encoding) {
        this.encoding = encoding;
    }

    @Nullable
    protected String getEncoding() {
        return this.encoding;
    }

    @Override
    protected void initServletContext(ServletContext servletContext) throws BeansException {
    }

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        String url = getUrl();
        Assert.state(url != null, "'url' not set");

        try {
            HtmlTemplate vm = HtmlTemplate.getTemplate(url, locale);
            if (vm!=null)
                return true;
            else
                return false;
        }
        catch (FileNotFoundException ex) {
            // Allow for ViewResolver chaining...
            return false;
        }
        catch (IOException ex) {
            throw new ApplicationContextException("Failed to load [" + url + "]", ex);
        }
    }

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Locale locale = ConfigAppBean.getCurrentLocale();
        String url = getUrl();

        HtmlTemplate template = null;
        String body;

        if (!model.containsKey("body")){
            if (url!=null)
                template = HtmlTemplate.getTemplate(url,locale);

            if (template==null)
                throw new ApplicationContextException("Failed to load [" + url + "]");

            model = FreeMarkerViewExtended.buildModel(template, request, response, model);

            body = template.renderBody(model);
        } else {
            body = null;
        }

        FreeMarkerViewExtended.renderPage(request,response,body,model);
    }
}
