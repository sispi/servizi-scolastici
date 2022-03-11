package keysuite.desktop.zuul;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class ZuulErrorFilter extends ZuulAbstractFilter {
    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext();


        Throwable throwable = ctx.getThrowable();

        if (throwable!=null){
            //throw new ZuulException( ctx.getThrowable(),500,ctx.getThrowable().getMessage());

            /*ctx.setThrowable(null); // response is not returned unless
            ctx.remove("error.status_code");

            ctx.setResponseBody("error");
            ctx.getResponse().setContentType("text/plain");
            ctx.setResponseStatusCode(500);*/
        }

        return null;
    }
}
