package keysuite.desktop.zuul;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import keysuite.desktop.DesktopUtils;
import keysuite.desktop.security.SecurityFilter;
import static keysuite.desktop.security.SecurityFilter.REQUEST_APP_CONFIG;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZuulRouteFilter extends ZuulAbstractFilter {

    private static final Logger logger = LoggerFactory.getLogger(ZuulRouteFilter.class);

    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();

        HttpServletRequest request = ctx.getRequest();
        String bearerValue = (String) request.getAttribute(SecurityFilter.AUTHORIZATION);

        if (bearerValue==null) {
            bearerValue = request.getHeader(SecurityFilter.AUTHORIZATION);
            if (bearerValue!=null && bearerValue.startsWith("Bearer "))
                bearerValue = bearerValue.substring("Bearer ".length());
        }

        Enumeration<String> headersEnum = request.getHeaderNames();

        Map<String,String> routeCookies = new HashMap<>();

        Map mappa = (Map) ctx.getRequest().getAttribute(REQUEST_APP_CONFIG);

        boolean debug = false;

        if (mappa!=null)
            debug = (boolean) mappa.getOrDefault("debug",false);

//        String cleanZck = ctx.getRequest().getParameter("cleanZck");

//        boolean skipCookies = "true".equalsIgnoreCase(cleanZck);

        while(headersEnum.hasMoreElements()){
            String headerName = headersEnum.nextElement();
            String headerValue = request.getHeader(headerName);

            if ("cookie".equalsIgnoreCase(headerName)){

                //String name = (String) ctx.get("appName");
                String domain = ctx.getRouteHost().getHost();
                String path = ctx.getRequest().getRequestURI();

                List<String> ptcookies = null;
                String bearer = null;

                if (mappa!=null){
                    ptcookies = (List) mappa.get("cookies");
                    bearer = (String) mappa.getOrDefault("bearer","SSO_USER");
                }

                Map<String,String> cookies = DesktopUtils.parseCookies(headerValue);

                for( String key : cookies.keySet() ){

                    String val = cookies.get(key);

                    if (key.equalsIgnoreCase(bearer)){
                        bearerValue = val;
                        //ctx.addZuulRequestHeader("Authorization","Bearer "+val);
                        continue;
                    }

                    boolean forward = false;
                    int idx = key.lastIndexOf(ZuulPostFilter.COOKIE_SEP);

                    if (ptcookies!=null && ptcookies.contains(key)) {
                        forward = true;

                        if (idx>0)
                            key = key.substring(idx+ZuulPostFilter.COOKIE_SEP.length());

                    } else if (idx>0){

                        String cdomain = key.substring(0,idx);
                        key = key.substring(idx + ZuulPostFilter.COOKIE_SEP.length());

                        idx = cdomain.indexOf(ZuulPostFilter.COOKIE_SEP);

                        String cpath = "";
                        if (idx>=0) {
                            cpath = cdomain.substring(idx + ZuulPostFilter.COOKIE_SEP.length()).replace(ZuulPostFilter.COOKIE_SEP, "/");
                            cdomain = cdomain.substring(0,idx);
                        }

                        //cdomain=.example.com
                        //domain=.www.example.com

                        if (("."+domain.toLowerCase()).endsWith("."+cdomain.toLowerCase())){
                            if (cpath.length()==0)
                                forward = true;
                            else {
                                if (!cpath.startsWith("/"))
                                    cpath = "/" + cpath;
                                if (!cpath.endsWith("/"))
                                    cpath += "/";
                                if (!path.endsWith("/"))
                                    path += "/";
                                forward = path.startsWith(cpath);
                            }
                        }
                    }

                    if (forward){
                        routeCookies.put(key, val);
                    }
                }

                //headerValue = DesktopUtils.toCookies(routeCookies);

            } else if(!ctx.getZuulRequestHeaders().containsKey(headerName)){
                ctx.addZuulRequestHeader(headerName, headerValue);
            }
        }

        Set<String> ignoredHeaders = (Set<String>) ctx.get("ignoredHeaders");

        if (ignoredHeaders==null){
            ignoredHeaders = new HashSet<String>();
            ctx.set("ignoredHeaders", ignoredHeaders);
        }

        if (routeCookies.size()>0){

            String v = DesktopUtils.toCookies(routeCookies);
            ctx.addZuulRequestHeader("cookie", v);

            if (debug)
                System.out.println("cookies forwarded to "+request.getRequestURI()+":"+ v );

            ignoredHeaders.remove("authorization");
        } else {
            ctx.getZuulRequestHeaders().remove("cookie");
            ctx.addZuulRequestHeader("cookie", "");

            if (debug)
                System.out.println("no cookies forwarded to "+request.getRequestURI() );

            ignoredHeaders.add("cookie");
        }

        if (bearerValue!=null){
            ctx.addZuulRequestHeader("Authorization","Bearer "+bearerValue);

            if (debug)
                System.out.println("bearer forwarded to "+request.getRequestURI()+":"+ bearerValue );

            ignoredHeaders.remove("authorization");
        } else {

            if (debug)
                System.out.println("no bearer forwarded to "+request.getRequestURI() );

            ignoredHeaders.add("authorization");
        }



        //ctx.addZuulRequestHeader("Authorization","Bearer "+ Session.getUserInfo().getJwtToken());
        return null;
    }
}
