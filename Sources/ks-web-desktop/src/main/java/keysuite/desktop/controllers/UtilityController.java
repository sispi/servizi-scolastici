package keysuite.desktop.controllers;

import java.lang.reflect.Method;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.cache.ClientCache;
import keysuite.desktop.components.DesktopResourceWatcher;
import keysuite.desktop.exceptions.CodeException;
import keysuite.desktop.security.ConfigAppBean;
import keysuite.docer.client.ClientUtils;
import keysuite.docer.client.DocerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.ui.Model;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class UtilityController {

    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected DesktopResourceWatcher desktopResourceWatcher;

    @Autowired
    protected HttpServletRequest request;

    public static Map<String,String> extractMessages(MessageSource messageSource, Locale locale, String... filters) throws Exception{

        Method proSetMethod = ReloadableResourceBundleMessageSource.class.getDeclaredMethod("getMergedProperties", new Class[] { Locale.class });
        ReflectionUtils.makeAccessible(proSetMethod);

        Object holder = proSetMethod.invoke(messageSource,locale);

        Properties props = (Properties) holder.getClass().getDeclaredMethod("getProperties").invoke(holder);

        Map<String,String> messages = new LinkedHashMap<>();

        String regex = null;

        if (filters!=null){
            regex = "^(";
            for( int i=0; i<filters.length; i++){
                String filter = filters[i];

                if (i>0)
                    regex += "|";

                regex += filter.replace(".","\\.").replace("**",".+").replace("*","[^\\.]+");


            }
            regex += ")$";
        }

        for( String key : props.stringPropertyNames()) {

            if (regex==null || key.matches(regex))
                messages.put(key, props.getProperty(key));
        }
        return messages;
    }

    @GetMapping(value = "/messages", produces = {"application/json"})
    @ResponseBody
    public Map<String,String> getMessages(@RequestParam(required = false) String locale, @RequestParam(name = "filter",required = false) String... filters) throws Exception  {

        Locale loc = ConfigAppBean.getCurrentLocale();

        Map<String,String> messages = extractMessages(messageSource, loc,filters);

        return messages;
    }

    @PostMapping(value = "/validation", produces = {"application/json"})
    @ResponseBody
    public List<String> validate(@RequestParam int errors) throws Exception  {

        List<String> messages = new ArrayList<>();
        for(int i=0; i<errors; i++)
            messages.add("errore generico "+i);
        return messages;
    }

    public static Map<String,Object> extractNames(String... keys) throws Exception{
        Map<String,Object> messages = new LinkedHashMap<>();
        for( String key: keys) {
            String sid = key.split("@")[0];
            String type = key.contains("@") ? key.split("@")[1] : null;
            DocerBean bean = ClientUtils.getItem(sid,type);
            if (bean!=null)
                messages.put(key,bean);
            else
                messages.put(key,key);
        }
        return messages;
    }

    @GetMapping(value = "/names", produces = {"application/json"})
    @ResponseBody
    public Map<String,Object> getNames(@RequestParam(name = "key") String... keys) throws Exception  {
        return extractNames(keys);
    }

    @GetMapping(value = "/caches/count")
    public Integer countcaches() throws Exception  {
        return ClientCache.getInstance().getCount();
    }

    @GetMapping(value = "/caches/clear")
    public Integer clearcaches() throws Exception  {
        ClientCache.getInstance().init();
        return ClientCache.getInstance().getCount();
    }

    @GetMapping(value = "/caches/watch")
    public Integer watch() throws Exception  {
        desktopResourceWatcher.initAll();
        return 0;
    }

    @GetMapping(value = "/caches/refresh")
    public Integer refreshcaches() throws Exception  {
        return ClientCache.getInstance().refresh();
    }

    @Autowired
    protected ExceptionController exceptionController;

    @ExceptionHandler(Exception.class)
    public Object handleError(HttpServletResponse response, Model model, Throwable exception) {
        CodeException ce = CodeException.getCodeException(exception); //.setUrl(URI);
        if (ce.getUrl()==null){
            ce.setUrl(request.getRequestURL().toString());
        }
        int status = ce.getCode().getHttpStatus();

        response.setStatus(status);
        return ce;
    }
}
