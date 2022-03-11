package keysuite.desktop.zuul;

import com.netflix.zuul.context.RequestContext;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import static keysuite.desktop.security.SecurityFilter.REQUEST_APP_CONFIG;
import org.springframework.cloud.netflix.zuul.filters.pre.FormBodyWrapperFilter;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;

public class ZuulFormPreFitler extends FormBodyWrapperFilter {

    public ZuulFormPreFitler() {
        super();
    }

    public ZuulFormPreFitler(FormHttpMessageConverter formHttpMessageConverter) {
        super(formHttpMessageConverter);
    }

    @Override
    public boolean shouldFilter() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String contentType = request.getContentType();
        // Don't use this filter on GET method
        if (contentType == null) {
            return false;
        }
        // Only use this filter for form data and only for multipart data in a
        // DispatcherServlet handler
        try {
            MediaType mediaType = MediaType.valueOf(contentType);
            if (MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType)) {
                Map mappa = (Map) ctx.getRequest().getAttribute(REQUEST_APP_CONFIG);

                if (mappa!=null && !((Boolean)mappa.getOrDefault("FormBodyWrapperFilter",true))){
                    return false;
                }
                return super.shouldFilter();
                //return false;
            }else
                return super.shouldFilter();
        }
        catch (InvalidMediaTypeException ex) {
            return false;
        }
    }

}
