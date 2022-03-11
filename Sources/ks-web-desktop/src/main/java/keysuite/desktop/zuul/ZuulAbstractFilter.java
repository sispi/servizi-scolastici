package keysuite.desktop.zuul;

import com.netflix.zuul.ZuulFilter;

public abstract class ZuulAbstractFilter extends ZuulFilter {
    public boolean shouldFilter() {
        return true;
    }
}
