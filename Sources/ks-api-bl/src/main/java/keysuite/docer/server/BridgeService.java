package keysuite.docer.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import keysuite.docer.bl.IBridge;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class BridgeService extends BaseService implements IBridge {

    private static final String ATTR_TARGET_URI="targetUri";
    private static final String ATTR_TARGET_HOST="targetHost";
    private static final Logger log = LoggerFactory.getLogger(BridgeService.class);

    HeaderGroup hopByHopHeaders = new HeaderGroup();

    public void bridge(String prefix, HttpServletRequest request, HttpServletResponse response){

        String[] myheaders = new String[] {
                "Connection", "Keep-Alive", "Proxy-Authenticate", "Proxy-Authorization",
                "TE", "Trailers", "Transfer-Encoding", "Upgrade" };
        for (String header : myheaders) {
            hopByHopHeaders.addHeader(new BasicHeader(header, null));
        }

        String targetUri = env.getProperty("bl.bridge."+prefix, request.getRequestURI());

        HttpHost targetHost = null;
        try {
            targetHost = URIUtils.extractHost(new URI(targetUri));
        } catch ( URISyntaxException e) {
            e.printStackTrace();
        }

        if (request.getAttribute(ATTR_TARGET_URI) == null) {
            request.setAttribute(ATTR_TARGET_URI, targetUri);
        }
        if (request.getAttribute(ATTR_TARGET_HOST) == null) {
            request.setAttribute(ATTR_TARGET_HOST, targetHost);
        }

        String method = request.getMethod();
        String proxyRequestUri = rewriteUrlFromRequest(request,targetUri,prefix);

        HttpRequest proxyRequest=null;
        if (request.getHeader(HttpHeaders.CONTENT_LENGTH) != null ||
                request.getHeader(HttpHeaders.TRANSFER_ENCODING) != null) {
            try {
                proxyRequest = newProxyRequestWithEntity(method, proxyRequestUri, request);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            proxyRequest = new BasicHttpRequest(method, proxyRequestUri);
        }


        copyRequestHeaders(request, proxyRequest);

        setXForwardedForHeader(request, proxyRequest);

        HttpResponse proxyResponse = null;
        try {
            proxyResponse = doExecute(request, response, proxyRequest);

            int statusCode = proxyResponse.getStatusLine().getStatusCode();

            response.setStatus(statusCode, proxyResponse.getStatusLine().getReasonPhrase());

            copyResponseHeaders(proxyResponse, request, response);

            if (statusCode == HttpServletResponse.SC_NOT_MODIFIED) {
                response.setIntHeader(HttpHeaders.CONTENT_LENGTH, 0);
            } else {
                copyResponseEntity(proxyResponse, response, proxyRequest, request);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (proxyResponse != null) {
                EntityUtils.consumeQuietly(proxyResponse.getEntity());
            }
        }
    }

    protected RequestConfig buildRequestConfig() {
        return RequestConfig.custom()
                .setRedirectsEnabled(false)
                .setCookieSpec(CookieSpecs.IGNORE_COOKIES) // we handle them in the servlet instead
                .setConnectTimeout(100000)
                .setSocketTimeout(100000)
                .setConnectionRequestTimeout(100000)
                .build();
    }

    protected SocketConfig buildSocketConfig() {

        return SocketConfig.custom()
                .setSoTimeout(100000)
                .build();
    }


    protected HttpResponse doExecute(HttpServletRequest servletRequest, HttpServletResponse servletResponse, HttpRequest proxyRequest) throws IOException {

        log.info("bkKeysuite " + servletRequest.getMethod() + " uri: " + servletRequest.getRequestURI() + " -- " +  proxyRequest.getRequestLine().getUri());


        HttpClientBuilder clientBuilder = HttpClientBuilder.create()
                .setDefaultRequestConfig(buildRequestConfig())
                .setDefaultSocketConfig(buildSocketConfig());

        clientBuilder.setMaxConnTotal(500);

        clientBuilder = clientBuilder.useSystemProperties();

        HttpClient client = clientBuilder.build();


        return client.execute((HttpHost) servletRequest.getAttribute(ATTR_TARGET_HOST), proxyRequest);
    }

    protected void copyResponseEntity(HttpResponse proxyResponse, HttpServletResponse servletResponse, HttpRequest proxyRequest, HttpServletRequest servletRequest) throws IOException {

        HttpEntity entity = proxyResponse.getEntity();

        String content = null;
        content= getHtml(entity.getContent());
        byte[] b = content.getBytes(Charset.forName("UTF-8"));

        System.out.println(content);

        servletResponse.setContentLength(b.length);
        servletResponse.setContentType(entity.getContentType().getValue());
        servletResponse.getOutputStream().write(b);
        servletResponse.setStatus(HttpServletResponse.SC_OK);

    }

    private String getHtml(InputStream is){
        StringBuffer sb = new StringBuffer();
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (is),"UTF-8"));


            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                sb.append(output+"\n");

            }
        }catch (Exception e){

        }finally {
            try {
                is.close();
            }catch (Exception e1){

            }
        }

        return sb.toString();

    }

    @Override
    public void setEnvironment(Environment environment) {

    }

    protected HttpRequest newProxyRequestWithEntity(String method, String proxyRequestUri,
                                                    HttpServletRequest servletRequest)
            throws IOException {
        HttpEntityEnclosingRequest eProxyRequest =
                new BasicHttpEntityEnclosingRequest(method, proxyRequestUri);
        // Add the input entity (streamed)
        //  note: we don't bother ensuring we close the servletInputStream since the container handles it
        eProxyRequest.setEntity(
                new InputStreamEntity(servletRequest.getInputStream(), getContentLength(servletRequest)));
        return eProxyRequest;
    }
    private long getContentLength(HttpServletRequest request) {
        String contentLengthHeader = request.getHeader("Content-Length");
        if (contentLengthHeader != null) {
            return Long.parseLong(contentLengthHeader);
        }
        return -1L;
    }

    protected String rewriteUrlFromRequest(HttpServletRequest servletRequest, String targetUri,String prefix) {

        StringBuilder uri = new StringBuilder(500);
        uri.append(targetUri);
        boolean isDNS = false;
        try {
            URI myUri = new URI(uri.toString());
            String host = myUri.getHost();
            String regexIp = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";
            Pattern pattern = Pattern.compile(regexIp,Pattern.CASE_INSENSITIVE);
            if(!pattern.matcher(host).find()){
                isDNS = true;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String pathInfo = servletRequest.getServletPath();
        if(!isDNS) {
            if (pathInfo != null) {
                String[] arrUri = uri.toString().split("\\/");
                String toFind = null;
                String toFind1 = null;
                if (arrUri != null && arrUri.length > 0) {
                    toFind = arrUri[arrUri.length - 1] + "/";
                    toFind1 = "/" + arrUri[arrUri.length - 1];
                }
                if (toFind != null) {
                    if (pathInfo.startsWith(toFind)) {

                        pathInfo = pathInfo.substring(pathInfo.indexOf("/"));

                    } else if (pathInfo.startsWith(toFind1)) {
                        pathInfo = pathInfo.substring(1);
                        try {
                            pathInfo = pathInfo.substring(pathInfo.indexOf("/"));
                        } catch (Exception ex2) {
                            pathInfo = "";
                            log.error(ex2.getMessage());
                        }
                    }
                }
                if (pathInfo.equalsIgnoreCase("/") && !uri.toString().endsWith("/")) {
                    uri = new StringBuilder(appendURL(uri.toString(),pathInfo));
                } else if (!pathInfo.equalsIgnoreCase("/")) {
                    pathInfo = (String) encodeUriQuery(pathInfo, true);
                    uri = new StringBuilder(appendURL(uri.toString(),pathInfo));
                }
            }
        }
        else{
            String link = "/bridge/"+prefix;
            if(pathInfo.startsWith(link)){
                pathInfo = pathInfo.substring(link.length());
                uri = new StringBuilder(appendURL(uri.toString(),pathInfo));
            }else{
                uri = new StringBuilder(appendURL(uri.toString(),pathInfo));
            }
        }
        String queryString = servletRequest.getQueryString();//ex:(following '?'): name=value&foo=bar#fragment
        String fragment = null;
        if (queryString != null) {
            int fragIdx = queryString.indexOf('#');
            if (fragIdx >= 0) {
                fragment = queryString.substring(fragIdx + 1);
                queryString = queryString.substring(0,fragIdx);
            }
        }

        if (queryString != null && queryString.length() > 0) {
            uri.append('?');
            uri.append(encodeUriQuery(queryString, false));
        }
        return uri.toString();
    }

    private String appendURL(String url, String toAppend){
        if(StringUtils.isNotEmpty(url)){
            if(url.endsWith("/")){
                url = url.substring(0, url.length()-1);
            }
            if(StringUtils.isNotEmpty(toAppend)){
                if(toAppend.startsWith("/")){
                    toAppend = toAppend.substring(1);
                }

                url = new StringBuilder(url+ "/"+toAppend).toString();
            }
        }
        return url;
    }
    protected static CharSequence encodeUriQuery(CharSequence in, boolean encodePercent) {
        StringBuilder outBuf = null;
        Formatter formatter = null;
        for(int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            boolean escape = true;
            if (c < 128) {
                if (asciiQueryChars.get((int)c) && !(encodePercent && c == '%')) {
                    escape = false;
                }
            } else if (!Character.isISOControl(c) && !Character.isSpaceChar(c)) {//not-ascii
                escape = false;
            }
            if (!escape) {
                if (outBuf != null)
                    outBuf.append(c);
            } else {
                if (outBuf == null) {
                    outBuf = new StringBuilder(in.length() + 5*3);
                    outBuf.append(in,0,i);
                    formatter = new Formatter(outBuf);
                }
                formatter.format("%%%02X",(int)c);
            }
        }
        return outBuf != null ? outBuf : in;
    }

    protected void copyRequestHeaders(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
        if (servletRequest != null && servletRequest.getHeaderNames() != null) {
            @SuppressWarnings("unchecked")
            Enumeration<String> enumerationOfHeaderNames = servletRequest.getHeaderNames();
            while (enumerationOfHeaderNames.hasMoreElements()) {
                String headerName = enumerationOfHeaderNames.nextElement();
                copyRequestHeader(servletRequest, proxyRequest, headerName);
            }
        }
    }

    protected void copyRequestHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest,
                                     String headerName) {
        if(headerName != null && servletRequest != null && proxyRequest != null) {
            if (headerName.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH))
                return;
            if (hopByHopHeaders.containsHeader(headerName))
                return;

            @SuppressWarnings("unchecked")
            Enumeration<String> headers = servletRequest.getHeaders(headerName);
            while (headers.hasMoreElements()) {//sometimes more than one value
                String headerValue = headers.nextElement();
                if (headerName.equalsIgnoreCase(HttpHeaders.HOST)) {
                    HttpHost host = (HttpHost) servletRequest.getAttribute(ATTR_TARGET_HOST);
                    headerValue = host.getHostName();
                    if (host.getPort() != -1)
                        headerValue += ":" + host.getPort();
                } else if (headerName.equalsIgnoreCase(org.apache.http.cookie.SM.COOKIE)) {
                    headerValue = getRealCookie(headerValue);
                }
                proxyRequest.addHeader(headerName, headerValue);
            }
        }
    }

    protected String getRealCookie(String cookieValue) {
        StringBuilder escapedCookie = new StringBuilder();
        String cookies[] = cookieValue.split("[;,]");
        for (String cookie : cookies) {
            String cookieSplit[] = cookie.split("=");
            if (cookieSplit.length == 2) {
                String cookieName = cookieSplit[0].trim();
                if (cookieName.startsWith(getCookiePrefix())) {
                    cookieName = cookieName.substring(getCookiePrefix().length());
                    if (escapedCookie.length() > 0) {
                        escapedCookie.append("; ");
                    }
                    escapedCookie.append(cookieName).append("=").append(cookieSplit[1].trim());
                }
            }
        }
        return escapedCookie.toString();
    }
    protected String getCookiePrefix() {
        return "!BLKeysuite!";
    }

    private void setXForwardedForHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
        String forHeaderName = "X-Forwarded-For";
        String forHeader = servletRequest.getRemoteAddr();
        String existingForHeader = servletRequest.getHeader(forHeaderName);
        if (existingForHeader != null) {
            forHeader = existingForHeader + ", " + forHeader;
        }
        proxyRequest.setHeader(forHeaderName, forHeader);

        String protoHeaderName = "X-Forwarded-Proto";
        String protoHeader = servletRequest.getScheme();
        proxyRequest.setHeader(protoHeaderName, protoHeader);
    }

    protected void copyResponseHeaders(HttpResponse proxyResponse, HttpServletRequest servletRequest,
                                       HttpServletResponse servletResponse) {
        for (Header header : proxyResponse.getAllHeaders()) {
            copyResponseHeader(servletRequest, servletResponse, header);
        }
    }

    protected void copyResponseHeader(HttpServletRequest servletRequest,
                                      HttpServletResponse servletResponse, Header header) {
        String headerName = header.getName();
        if (hopByHopHeaders.containsHeader(headerName))
            return;
        String headerValue = header.getValue();
        if (headerName.equalsIgnoreCase(org.apache.http.cookie.SM.SET_COOKIE) ||
                headerName.equalsIgnoreCase(org.apache.http.cookie.SM.SET_COOKIE2)) {
            copyProxyCookie(servletRequest, servletResponse, headerValue);
        } else if (headerName.equalsIgnoreCase(HttpHeaders.LOCATION)) {
            servletResponse.addHeader(headerName, rewriteUrlFromResponse(servletRequest, headerValue));
        } else {
            servletResponse.addHeader(headerName, headerValue);
        }

    }

    protected void copyProxyCookie(HttpServletRequest servletRequest,
                                   HttpServletResponse servletResponse, String headerValue) {

        for (HttpCookie cookie : HttpCookie.parse(headerValue)) {
            String cookiePath = cookie.getPath();
            if(cookiePath == null){
                cookiePath = "/";
            }else if (!cookiePath.startsWith("/")){
                String path = servletRequest.getContextPath();
                path += servletRequest.getServletPath();
                if(path.isEmpty()){
                    path = "/";
                }
                cookiePath = path;
            }
            String prefixCookie = servletRequest.getServletPath();
            prefixCookie = StringUtils.isEmpty(prefixCookie) ?"":prefixCookie;
            String proxyCookiePath = prefixCookie + cookiePath;
            String proxyCookieName =   cookie.getName();
            Cookie servletCookie = new Cookie(proxyCookieName, cookie.getValue());
            servletCookie.setComment(cookie.getComment());
            servletCookie.setMaxAge((int) cookie.getMaxAge());
            servletCookie.setPath(proxyCookiePath);
            servletCookie.setSecure(cookie.getSecure());
            servletCookie.setVersion(cookie.getVersion());
            servletCookie.setHttpOnly(cookie.isHttpOnly());
            servletResponse.addCookie(servletCookie);
        }
    }

    protected String rewriteUrlFromResponse(HttpServletRequest servletRequest, String theUrl) {
        final String targetUri = (String) servletRequest.getAttribute(ATTR_TARGET_URI);
        if (theUrl.startsWith(targetUri)) {

            return theUrl.replace(targetUri, servletRequest.getRequestURL().toString());

        }
        return theUrl;
    }

    protected static final BitSet asciiQueryChars;
    static {
        char[] c_unreserved = "_-!.~'()*".toCharArray();//plus alphanum
        char[] c_punct = ",;:$&+=".toCharArray();
        char[] c_reserved = "?/[]@".toCharArray();//plus punct

        asciiQueryChars = new BitSet(128);
        for(char c = 'a'; c <= 'z'; c++) asciiQueryChars.set((int)c);
        for(char c = 'A'; c <= 'Z'; c++) asciiQueryChars.set((int)c);
        for(char c = '0'; c <= '9'; c++) asciiQueryChars.set((int)c);
        for(char c : c_unreserved) asciiQueryChars.set((int)c);
        for(char c : c_punct) asciiQueryChars.set((int)c);
        for(char c : c_reserved) asciiQueryChars.set((int)c);

        asciiQueryChars.set((int)'%');
    }

}
