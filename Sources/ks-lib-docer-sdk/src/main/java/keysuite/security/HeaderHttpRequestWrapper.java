package keysuite.security;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class HeaderHttpRequestWrapper extends HttpServletRequestWrapper {
    protected final static Logger logger = LoggerFactory.getLogger(HeaderHttpRequestWrapper.class);
    private static final String AUTHORIZATION ="authorization";
    Map<String, String> headers= new HashMap<>();

    public HeaderHttpRequestWrapper(HttpServletRequest request) {
        super(request);
        Enumeration<String> headersName = request.getHeaderNames();
        if(headersName != null) {
            while (headersName.hasMoreElements()) {
                String headerName = headersName.nextElement();
                this.headers.put(headerName.toLowerCase(), request.getHeader(headerName));
            }
        }
    }

    public void setHeader(String name, String value){
        this.headers.put(name.toLowerCase(),value);
    }

    public String getHeader(String name){
        String val = this.headers.get(name.toLowerCase());
        if(StringUtils.isEmpty(val)){
            try {
                val = super.getHeader(name);
            }catch (Exception e){
                logger.error("Header "+ name + " non definito");
            }
        }
        return val;
    }

    public void setBearer(String jwtToken){
        if(StringUtils.isNotEmpty(jwtToken)){
            if(!jwtToken.toLowerCase().startsWith("bearer")){
                jwtToken = "Bearer "+ jwtToken;
            }
            this.setHeader(AUTHORIZATION, jwtToken);
        }
    }

    public String getBearer(){
        String jwtToken = this.getHeader(AUTHORIZATION);
        if(!jwtToken.toLowerCase().startsWith("bearer"))
            jwtToken = null;
        return jwtToken;
    }
    @Override
    public Enumeration<String> getHeaderNames() {
        Enumeration<String> en = super.getHeaderNames();
        List<String> list = Collections.list(en);
        List<String> keyHEaderNames = new ArrayList<>(headers.keySet());
        for(String headN: list){
            headN = headN.toLowerCase();
            if(!keyHEaderNames.contains(headN)){
                keyHEaderNames.add(headN);
            }
        }
        return java.util.Collections.enumeration(keyHEaderNames);
    }
}
