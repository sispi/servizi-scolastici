package keysuite.desktop.zuul;

import com.google.common.base.Strings;
import com.google.common.io.CharStreams;
import com.netflix.util.Pair;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import freemarker.template.TemplateException;
import java.io.*;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import keysuite.desktop.security.ConfigAppBean;
import keysuite.freemarker.TemplateUtils;

import static keysuite.desktop.security.SecurityFilter.REQUEST_APP_CONFIG;

public class ZuulPostFilter extends ZuulAbstractFilter {

    public static final String COOKIE_SEP = "___";
    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    private String getExtra(Map app, HttpServletRequest request) throws IOException {
        Map mappa = app; //(Map) ConfigAppBean.getBeanForApp(app, request);
        request.setAttribute("currentAppBean",mappa);

        Map<String,Object> theme = ConfigAppBean.getCurrentTheme();

        Map<String,Object> model = new HashMap<>();
        model.put("menu",mappa.get("menu"));
        model.put("header",ConfigAppBean.getCurrentHeaderConfig());
        model.put("app",mappa);
        model.put("theme",theme);

        String template = (String) mappa.getOrDefault("template","proxy/proxy.ftl");

        try {
            String extra = TemplateUtils.ftlHandler(template, model);
            return extra;
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext context = RequestContext.getCurrentContext();

        if (context.getRequest()==null || context.getResponse()==null)
            return null;

        Map app = (Map) context.getRequest().getAttribute(REQUEST_APP_CONFIG);

        int status = context.getResponse().getStatus();

        String redir = (String) app.get("redirect-"+status);

        if (!Strings.isNullOrEmpty(redir)){
            context.getResponse().setStatus(302);
            context.addZuulResponseHeader("location", redir);
            return null;
        }

        List<Pair<String, String>> headers = context.getZuulResponseHeaders();
        List<String> cookieHeaders = new ArrayList<>();
        for (Pair p : headers.toArray(new Pair[0])){

            if ("Set-Cookie".equalsIgnoreCase(p.first().toString())){
                String val = (String) p.second();
                headers.remove(p);
                if (!Strings.isNullOrEmpty(val)){
                    //String name = (String) context.get("appName");
                    String host = context.getRouteHost().getHost();

                    List<HttpCookie> co = HttpCookie.parse(val);

                    for( HttpCookie c : co ){
                        String domain = c.getDomain();

                        if (Strings.isNullOrEmpty(domain))
                            domain = host;

                        String name = c.getName();

                        if (name.startsWith(".")){
                            name = name.substring(1);
                            int idx = domain.lastIndexOf(".");
                            if (idx>=-1){
                                idx = domain.lastIndexOf(".",idx+1);
                            }
                            if (idx>=-1)
                                domain = domain.substring(idx+1);
                        }

                        String path = c.getPath();

                        if (!Strings.isNullOrEmpty(path) &&  !"/".equals(path))
                            domain += path.replace("/",COOKIE_SEP);

                        long maxage = c.getMaxAge();

                        String cv = domain + COOKIE_SEP + name + "=" + c.getValue() + "; Path=/";
                        if (maxage>-1)
                            cv += "; Max-Age="+maxage;

                        cookieHeaders.add( cv );
                    }
                }
            }

            if ("Content-Type".equals(p.first())){
                Object ct = p.second();
                if (ct!=null && ct.toString().contains("text/html")){
                    //String url = context.getRequest().getRequestURL().toString();

                    if (app==null || !app.containsKey("targetUri"))
                        return null;

                    InputStream responseDataStream = context.getResponseDataStream();

                    if(responseDataStream == null) {
                        return null;
                    }

                    try {

                        /*if (!context.getResponseGZipped() && reverseRegex==null){

                            InputStream is = new SlidingInputStream(responseDataStream) {

                                @Override
                                public byte[] getBytesToInsert() {
                                    try {
                                        return getExtra(app,context.getRequest()).getBytes("utf-8");
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                @Override
                                public byte[] getBytesToFind() {
                                    try {
                                        return "</body>".getBytes("utf-8");
                                    } catch (UnsupportedEncodingException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            };

                            context.setResponseDataStream(is);
                        } */

                        String responseData;

                        if (context.getResponseGZipped()){
                            responseData = CharStreams.toString(new InputStreamReader(new GZIPInputStream(responseDataStream), "UTF-8"));
                        } else {
                            responseData = CharStreams.toString(new InputStreamReader(responseDataStream, "UTF-8"));
                        }

                        int idx = responseData.indexOf("</body>");

                        String extra = getExtra(app,context.getRequest());

                        if (idx>0){
                            responseData = responseData.substring(0,idx) + extra + responseData.substring(idx);
                        } else {
                            responseData += extra;
                        }

                        /*String reverseRegex = (String) app.get("reverseRegex");
                        String reverseTargetUri = (String) app.get("reverseTargetUri");

                        if (reverseRegex!=null && reverseTargetUri!=null){
                            responseData = responseData.replaceAll(reverseRegex,reverseTargetUri);
                        }*/

                        Map<String,String> replacers = (Map) app.get("replace");

                        if (replacers!=null){
                            for( String rule : replacers.keySet() ){
                                String str = replacers.get(rule);
                                if (str==null)
                                    str = "";
                                responseData = responseData.replaceAll(rule,str);
                            }
                        }

                        if (context.getResponseGZipped()){
                            ByteArrayOutputStream bos = new ByteArrayOutputStream(responseData.length());
                            GZIPOutputStream gzip = new GZIPOutputStream(bos);
                            gzip.write(responseData.getBytes("UTF-8"));
                            gzip.close();
                            byte[] compressed = bos.toByteArray();
                            bos.close();
                            context.setResponseDataStream(new ByteArrayInputStream(compressed));
                        } else {
                            context.setResponseBody(responseData);
                        }


                    } catch (Exception e) {
                        throw new ZuulException(e, 500, e.getMessage());
                    }
                }
            }
        }

        for( String val : cookieHeaders)
            context.addZuulResponseHeader("Set-Cookie", val);

        return null;

    }
}
