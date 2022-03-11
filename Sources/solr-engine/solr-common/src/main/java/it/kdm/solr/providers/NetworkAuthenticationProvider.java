package it.kdm.solr.providers;

import com.google.common.base.Strings;
import it.kdm.solr.core.Schema;
import it.kdm.solr.interfaces.ILogin;
import org.apache.solr.request.SolrQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

/**
 * Created by Paolo_2 on 29/04/16.
 */
public class NetworkAuthenticationProvider implements ILogin {

    private transient static Logger log = LoggerFactory.getLogger(NetworkAuthenticationProvider.class);

    protected Collection<String> hostfilter = null;
    protected String name;
    //protected String cookie;
    //protected String header;
    protected String secret;


    @Override
    public void setConfig( Map<String,Object> config )
    {
        name = (String) config.get("name");
        hostfilter = (Collection<String>) config.get("hostfilter");
        //cookie = (String) config.get("cookie");
        //header = (String) config.get("header");
        secret = (String) config.get("secret");

        log.trace("config:{}",config);
    }

    protected SolrQueryRequest req;

    @Override
    public void setRequest( SolrQueryRequest req )
    {
        this.req = req;

        log.trace("req:{}", req);
    }

    @Override
    public String getName()
    {
        return name;
    }

    private static long ipToLong(String ip) {

        if (ip.equals("localhost"))
            ip = "127.0.0.1";

        String[] octets = ip.split("\\.");
        long result = 0;
        for (String octet : octets) {
            result <<= 8;
            result |= Integer.parseInt(octet) & 0xff;
        }
        return result;
    }

    @Override
    public boolean loginSSO(String ticket)
    {
        log.info("enter loginSSO ticket:{}", ticket);

        /*if (this.hostfilter==null || this.hostfilter.contains("*"))
        {
            log.info("passthorugh authentication. hostfilter empty or jolly:",this.hostfilter);
            return true;
        }*/

        HttpServletRequest httpreq = (HttpServletRequest) req.getContext().get( "httpRequest" );

        if (httpreq==null)
        {
            log.info("passthorugh authentication. internal request.");
            return true;
        }

        String qt = req.getParams().get("qt","/select");

        if ( qt.startsWith("/admin") )
        {
            log.info("passthorugh authentication. admin request:{}",qt);
            return true;
        }

        String remoteAddr = req.getParams().get("remoteAddr", httpreq.getRemoteAddr() );

        String localAddr = httpreq.getLocalAddr();

        log.trace("localAddr:{} remoteAddr:{} req:{}", localAddr, remoteAddr, req);

        String clusterIps = System.getProperty(Schema.DC_CLUSTER_IPS);

        if (clusterIps!=null && remoteAddr.matches(clusterIps))
        {
            log.info("OK auth because address {} is in cluster",remoteAddr);
            return true;
        }

        if (this.hostfilter!=null)
        {
            long ipLong = ipToLong(remoteAddr);

            for( String host : this.hostfilter )
            {
                String[] parts = host.split("-");

                long lower = ipToLong(parts[0]);
                long upper = lower;

                if (parts.length>1)
                    upper = ipToLong(parts[1]);

                if (ipLong>=lower && ipLong<=upper)
                {
                    log.info("OK auth because remote address {} in range {}",remoteAddr,host);
                    return true;
                }
            }
        }

        /* autenticazione su cookie , http header o pw ticket se fallisce la network mask */

        if (secret==null)
        {
            log.warn("OK auth because secret is not set.");
            return true;
        }

        String challenge = org.apache.commons.codec.digest.DigestUtils.md5Hex( ticket.split( ":" )[0] + secret );
        String adminChallenge = org.apache.commons.codec.digest.DigestUtils.md5Hex( "admin" + secret );

        log.trace("challenge:{}",challenge);

        String headerValue = req.getParams().get("remoteHeader" , httpreq.getHeader(Schema.AUTH_HEADER) );

        /*if (headerValue==null)
        {
            if (header != null)
            {
                headerValue = httpreq.getHeader(header);
            }
            else
            {
                headerValue = httpreq.getHeader("auth_challenge");
                log.debug("header defaulted to 'auth_challenge'");
            }
        }*/

        if (!Strings.isNullOrEmpty(headerValue))
        {
            if ( adminChallenge.equals( headerValue ) )
            {
                log.info("OK auth because header equals admin challenge");
                return true;
            }

            if ( challenge.equals( headerValue ) )
            {
                log.info("OK auth because header equals challenge");
                return true;
            }
        }

        if (ticket.indexOf(":") > 0)
        {
            String password = ticket.split( ":" )[1];

            if ( adminChallenge.equals( password ) )
            {
                log.info("OK auth because password equals admin challenge");
                return true;
            }

            if ( challenge.equals( password ) )
            {
                log.info("OK auth because password equals challenge");
                return true;
            }
        }

        /*if (cookie != null)
        {
            Cookie[] cookies = httpreq.getCookies();

            for (Cookie cookie : cookies) {
                if (cookie.equals(cookie.getName()) && challenge.equals(cookie.getValue()) )
                {
                    log.info("OK auth because cookie:{}", cookie);
                    return true;
                }
            }
        }*/

        log.warn("KO Auth ticket:{}",ticket);

        /*if (log.isTraceEnabled())
        {
            if (cookie != null)
            {
                Cookie[] cookies = httpreq.getCookies();
                String cv = null;

                for (Cookie cookie : cookies) {
                    if (cookie.equals(cookie.getName()))
                        cv = cookie.getValue();
                }

                log.trace("KO Auth cookie:{} value:{}",cookie,cv);
            }

            //if (header != null)
            log.trace("KO Auth header:{} value:{}",header,httpreq.getHeader(header));

            if (ticket.indexOf(":") > 0)
                log.trace("KO Auth user:{} password:{}",ticket.split( ":" )[0],ticket.split( ":" )[1] );
        }*/

        return false;
    }
}
