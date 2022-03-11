package keysuite.desktop.zuul;

import com.google.common.base.Strings;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static keysuite.desktop.security.SecurityFilter.REQUEST_APP_CONFIG;

import keysuite.solr.SolrUtils;
import org.apache.solr.common.params.MultiMapSolrParams;

public class ZuulPreFilter extends ZuulAbstractFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 6;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();

        String url = ctx.getRequest().getRequestURL().toString();

        Map<String, List<String>> params = ctx.getRequestQueryParams();

        if (params!=null){
            for ( String key : params.keySet() ){
                if (key.startsWith("<") && key.endsWith(">")){
                    params.remove(key);
                    key = key.substring(1,key.length()-1);
                    key = System.getProperty(key);
                    if (!Strings.isNullOrEmpty(key)){
                        MultiMapSolrParams params2 = SolrUtils.parseQueryString(key);
                        for( String param : params2.getMap().keySet() ){
                            String[] ss = params2.getParams(param);
                            if (ss!=null)
                                params.put(param, Arrays.asList(ss));
                        }
                    }
                }
            }
        }

        Map mappa = (Map) ctx.getRequest().getAttribute(REQUEST_APP_CONFIG);

        if (mappa==null)
            return null;

        String targetUri = (String) mappa.get("targetUri");

        ctx.set("appName" , mappa.get("name"));

        if (targetUri==null)
            return null;

        if (targetUri.contains("$")){
            String regex = (String) mappa.get("regex");
            targetUri = url.replaceAll(regex,targetUri);
        }

        //HttpServletRequest request = ctx.getRequest();

        //ctx.put("serviceId", "flowdesigner2");
        ctx.put("retryable",false);
        //ctx.put("uri","http://localhost:9000");
        //ctx.put("routeHost","http://localhost:9000");
        try {
            ctx.setRouteHost(new URL(targetUri));
        } catch (MalformedURLException e) {
            throw new ZuulException(e,500,e.getMessage());
        }

        return null;

    }
}
