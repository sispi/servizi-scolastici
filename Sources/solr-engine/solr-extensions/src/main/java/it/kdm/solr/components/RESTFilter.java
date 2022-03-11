package it.kdm.solr.components;

import com.google.common.base.Strings;
import it.kdm.solr.client.SolrClient;
import it.kdm.solr.core.Schema;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.servlet.SolrRequestParsers;
import org.eclipse.jetty.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Paolo_2 on 06/05/17.
 */
public class RESTFilter extends DispatchFilter {

    private transient static Logger log = LoggerFactory.getLogger(RESTFilter.class);

    protected int seachLDAP(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException {
        Hashtable env = new Hashtable();

        String qs = httpRequest.getQueryString();

        SolrParams params = SolrRequestParsers.parseQueryString(qs);

        String sp = "com.sun.jndi.ldap.LdapCtxFactory";
        env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

        String ldapUrl = params.get("url",httpRequest.getServletPath().substring(1)) ;
        // /ldap://server:port/dc=yourName, dc=com

        String principal = params.get("principal");

        if (principal!=null) {
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, principal);
            //"cn=admin,dc=kdm,dc=it"
            env.put(Context.SECURITY_CREDENTIALS, params.get("password"));
        }
        env.put(Context.PROVIDER_URL, ldapUrl);

        DirContext dctx = null;
        try {
            dctx = new InitialDirContext(env);
        } catch (NamingException e) {
            throw new ServletException(e);
        }

        String base = params.get("q","");
        //ou=People

        String filter = params.get("fq","objectClass=*");
        //String filter = "(&(sn=W*)(l=Criteria*))";

        String attribs = params.get("fl","cn");
        //String[] attributeFilter = { "cn", "mail" };

        if (attribs==null)
            throw new ServletException("fl mandatory");

        String[] attributeFilter = StringUtils.split(attribs,",");

        String[] aliases = new String[attributeFilter.length];

        for (int i=0; i<attributeFilter.length; i++) {
            if (attributeFilter[i].contains(":"))
            {
                aliases[i] = attributeFilter[i].split(":")[0];
                attributeFilter[i] = attributeFilter[i].split(":")[1];
            }
            else
            {
                aliases[i] = attributeFilter[i];
            }
        }

        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attributeFilter);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

        NamingEnumeration results = null;
        String outputDocument=StringUtils.join(aliases,",")+"\n";
        try {
            results = dctx.search(base, filter, sc);

            while (results.hasMore()) {
                String line = "";
                SearchResult sr = (SearchResult) results.next();
                Attributes attrs = sr.getAttributes();

                for( int i=0; i<aliases.length; i++)
                {
                    Attribute attr = attrs.get(attributeFilter[i]);

                    if (!Strings.isNullOrEmpty(line))
                            line += ",";
                    if (attr != null)
                    {
                        String v = "";
                        if (attr.size()>0)
                        {
                            for (int j=0; j<attr.size();j++) {
                                if (j > 0)
                                    v += ",";

                                String item = attr.get(j).toString();
                                String regexString = params.get(aliases[i]+".regex");
                                // ..&memberOf.regex=cn=([^\,]%2b)
                                if (regexString!=null)
                                {
                                    Pattern regex = Pattern.compile(regexString);
                                    Matcher m = regex.matcher(attr.get(j).toString());
                                    if (m.find())
                                        item = m.group(1);
                                    else
                                        item = "";
                                }
                                v += item;
                            }
                        }

                        if (v.contains(","))
                            v = "\"" + v + "\"";

                        line += v;
                    }
                }
                line += "\n";
                outputDocument+=line;
            }
        } catch (NamingException e) {
            throw new ServletException(e);
        }
        finally
        {
            try {
                dctx.close();
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }

        ServletOutputStream out = null;
        BufferedInputStream  bis = null;
        BufferedOutputStream bos = null;

        httpResponse.setContentLength(outputDocument.length());

        try
        {
            out = httpResponse.getOutputStream();

            httpResponse.setContentType(params.get("ct","text/csv"));
            //httpResponse.setHeader("Content-disposition","attachment; filename=export.csv");
            bis = new BufferedInputStream(new ByteArrayInputStream(outputDocument.getBytes()));

            bos = new BufferedOutputStream(out);

            byte[] buff = new byte[2048];
            int bytesRead;
            while(-1 != (bytesRead = bis.read(buff, 0, buff.length))){

                bos.write(buff, 0, bytesRead);
                bos.flush();
            }
            out.flush();

        } catch (Exception ex) {
            throw new ServletException(ex);
        }finally{
            if (out != null)
                out.close();

            if (bis != null)
                bis.close();

            if (bos != null)
                bos.close();
        }

        return HttpStatus.SC_OK;
    }

    @Override
    protected int doUploadPut(File tempfile, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException
    {
        tempfile.getParentFile().mkdirs();

        String qs = httpRequest.getQueryString();

        SolrParams params = SolrRequestParsers.parseQueryString(qs);

        String byterange = params.get("Range",httpRequest.getHeader("Content-Range"));

        if (byterange!=null && byterange.startsWith("bytes="))
            byterange = byterange.substring(6);

        int start = 0;
        int contentLength = httpRequest.getContentLength();

        boolean randomAccess = false;

        long total = 0;

        if (byterange!=null)
        {
            if (byterange.contains("/"))
            {
                total = Long.parseLong( byterange.split("/")[1] );
                byterange = byterange.split("/")[0];
            }

            String[] interval = byterange.split("-");

            start = Integer.parseInt(interval[0]);

            if (start>0 && !tempfile.exists())
            {
                return HttpStatus.SC_NOT_FOUND;
                //throw new ServletException("file path:"+tempfile.toString());
            }

            if (start>tempfile.length() )
            {
                return HttpStatus.SC_BAD_REQUEST;
                //throw new ServletException("Start range over file length:"+tempfile.length());

            }

            if (interval.length>1 && !Strings.isNullOrEmpty(interval[1]))
            {
                int end = Integer.parseInt(interval[1]);

                if ( end != (start+contentLength-1) )
                {
                    return HttpStatus.SC_BAD_REQUEST;
                    //throw new ServletException("content length doesn't match range");
                }
            }

            /* content più corto del file */
            if (start==0 && contentLength<tempfile.length())
                randomAccess = true;

            /* non è un append */
            if (start>0 && start<tempfile.length())
                randomAccess = true;

        }

        InputStream is = httpRequest.getInputStream();

        int written = 0;

        if (randomAccess)
        {
            RandomAccessFile raf = null;

            try
            {

                raf = new RandomAccessFile(tempfile,"rws");
                raf.seek(start);

                byte[] buffer = new byte[8192]; // or more if you like
                int count;

                while ((count = is.read(buffer)) >= 0 )
                {
                    raf.write(buffer, 0, count);
                    written += count;
                }
            }
            catch(Exception e)
            {
                //httpResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                throw new ServletException(e);

            }
            finally
            {
                if (is!=null)
                    is.close();

                if (raf!=null)
                    raf.close();
            }

            httpResponse.setHeader("upload-mode","relative");

        }
        else
        {
            FileOutputStream fos=null;
            try
            {
                boolean append = (start>0);

                fos = new FileOutputStream(tempfile, append);

                written = IOUtils.copy(is, fos);

                if (append)
                    httpResponse.setHeader("upload-mode","append");
                else
                    httpResponse.setHeader("upload-mode","write");

                if(tempfile.length() != (start+contentLength) )
                {
                    written = -1;
                }
            }
            catch(Exception e)
            {
                //httpResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                throw new ServletException(e);

            }
            finally
            {
                if (is!=null)
                    is.close();

                if (fos!=null)
                    fos.close();
            }
        }

        httpResponse.setIntHeader("bytes-written", written );
        httpResponse.setHeader("total-size", "" + total);
        FileUtils.writeStringToFile(new File(tempfile.getPath() + ".size"), "" + total);

        if( written!=contentLength )
        {
            //httpResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException("Invalid state. temp file deleted.");
        }

        return HttpStatus.SC_OK;
    }

    @Override
    protected int doUploadGet(File tempfile, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException
    {
        if (!tempfile.exists())
        {
            return HttpStatus.SC_NOT_FOUND;
            //throw new ServletException("file path:"+tempfile.toString());
        }

        Long length = tempfile.length();

        String byterange = httpRequest.getHeader("Range");

        if (byterange!=null && byterange.startsWith("bytes="))
            byterange = byterange.substring(6);

        long start = 0;

        if (byterange!=null)
        {
            String[] interval = byterange.split("-");

            start = Integer.parseInt(interval[0]);

            if (start>tempfile.length() )
            {
                return HttpStatus.SC_BAD_REQUEST;
                //throw new ServletException("Start range over file length:"+tempfile.length());
            }

            if (interval.length>1 && !Strings.isNullOrEmpty(interval[1]))
            {
                int end = Integer.parseInt(interval[1]);

                if (end+1>length)
                    return HttpStatus.SC_BAD_REQUEST;

                length = end-start;
            }

            httpResponse.setHeader("Content-Range", String.format("bytes %s-%s/%s", start, start + length, tempfile.length()));
            httpResponse.setHeader("Accept-Ranges", "bytes");
        }

        httpResponse.setContentLength(length.intValue());

        ServletOutputStream sos = httpResponse.getOutputStream();

        File tf = new File(tempfile.getPath()+".size");

        if (tf.exists())
            httpResponse.setHeader("total-size",FileUtils.readFileToString(tf));

        if (httpRequest.getMethod().equals("HEAD"))
            return HttpStatus.SC_OK;

        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(tempfile);

            IOUtils.copyLarge(fis, sos, start, length);
        }
        catch(Exception e)
        {
            throw new ServletException(e);
        }
        finally
        {
            if (fis!=null)
                fis.close();

            if (sos!=null)
                sos.close();
        }

        if (httpResponse.getHeader("Content-Range") != null)
            return HttpStatus.SC_PARTIAL_CONTENT;
        return HttpStatus.SC_OK;
    }

    @Override
    protected int doUpload(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException
    {
        String qs = httpRequest.getQueryString();

        String path = httpRequest.getServletPath();

        SolrParams params = SolrRequestParsers.parseQueryString(qs);
        String id = params.get(Schema.Fields.ID);

        path = StringUtils.strip(path.substring(7).replace("\\", "/"), "/");

        if (id==null)
        {
            if (path.equals(""))
                path = "temp/" + UUID.randomUUID().toString();
            else if (path.indexOf("/")==-1 )
                path = "temp/"+path;
        }
        else
        {
            if (path.equals(""))
                path = "temp/" + id + ".bin";
            else
                path = path + "/" + id + ".bin";
        }

        String parent = path.substring(0,path.indexOf("/"));
        path = path.substring(parent.length());

        parent = System.getProperty("upload-path."+parent,"../"+parent);

        File tempfile = new File(parent,path);

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
        httpResponse.setHeader("Pragma", "no-cache"); // HTTP 1.0
        httpResponse.setDateHeader("Expires", 0);
        httpResponse.setHeader("file-path", tempfile.getPath());
        httpResponse.setHeader("file-name", tempfile.getName() );

        int status = 0;

        if (httpRequest.getMethod().equals("DELETE"))
        {
            FileUtils.deleteQuietly(tempfile);
            FileUtils.deleteQuietly( new File(tempfile.getPath()+".size") );
            status = HttpStatus.SC_OK;
        }
        else if (httpRequest.getMethod().equals("HEAD") || httpRequest.getMethod().equals("GET"))
        {
            status = doUploadGet(tempfile, httpRequest, httpResponse);
        }
        else if (httpRequest.getMethod().equals("PUT"))
        {
            status = doUploadPut(tempfile, httpRequest, httpResponse);
        }

        if (status>0)
        {

            httpResponse.setIntHeader("file-size", ((Long) tempfile.length()).intValue());
            httpResponse.setDateHeader("Last-Modified", tempfile.lastModified());
            return status;
        }
        else
        {
            return HttpStatus.SC_METHOD_NOT_ALLOWED;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (System.getProperty("location")==null)
        {
            try
            {
                System.setProperty("location", SolrClient.getInstance().getDefaultCollection());
            }
            catch(Exception e)
            {
                log.error("Error setting default location",e);
            }
        }

        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        httpResponse.addHeader("Access-Control-Allow-Headers","Content-Type");
        httpResponse.addHeader("Access-Control-Allow-Methods","GET, POST, PUT, PATCH, DELETE, OPTIONS");
        httpResponse.addHeader("Access-Control-Allow-Origin","*");

        String path = httpRequest.getServletPath();

        String method = httpRequest.getMethod();

        if ("OPTIONS".equals(method))
        {
            httpResponse.addHeader("Allow","GET, POST, PUT, PATCH, DELETE, OPTIONS");
            return;
        }

        if ("/upload".equals(path) || path.startsWith("/upload/"))
        {
            try
            {
                int status = doUpload(httpRequest,httpResponse);
                httpResponse.setStatus(status);
            }
            catch(Exception e)
            {
                e.printStackTrace(new java.io.PrintWriter(httpResponse.getOutputStream()));
                httpResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                httpResponse.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
            }
            return;
        }

        if ("/ldap".equals(path) || path.startsWith("/ldap://"))
        {
            try
            {
                int status = seachLDAP(httpRequest,httpResponse);
                httpResponse.setStatus(status);
            }
            catch(Exception e)
            {
                e.printStackTrace(new java.io.PrintWriter(httpResponse.getOutputStream()));
                httpResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                httpResponse.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
            }
            return;
        }

        super.doFilter(request, response, chain);

        if (httpResponse.getHeader("Content-Range") != null)
            httpResponse.setStatus(HttpStatus.SC_PARTIAL_CONTENT);
    }

    private class RequestWrapper extends HttpServletRequestWrapper
    {
        private final ModifiableSolrParams params;
        //private Map<String, String[]> allParameters = null;
        String method;
        String path;
        String contentType = null;

        public RequestWrapper(final HttpServletRequest request, String path, String method, final ModifiableSolrParams params)
        {
            super(request);
            this.path = path;
            this.method = method;
            this.params = params;
        }

        @Override
        public String getServletPath()
        {
            return path;
        }

        @Override
        public String getRequestURI()
        {
            return this.getContextPath() + path;
        }

        @Override
        public StringBuffer getRequestURL()
        {
            final StringBuffer url = new StringBuffer(128);
            URIUtil.appendSchemeHostPort(url, getScheme(), getServerName(), getServerPort());
            url.append(getRequestURI());
            return url;
        }

        public void setContentType(String contentType)
        {
            this.contentType = contentType;
        }

        @Override
        public String getContentType()
        {
            if (contentType==null)
                return super.getContentType();
            else
                return contentType;
        }

        @Override
        public String getQueryString()
        {
            String qs = params.toQueryString();
            if (!Strings.isNullOrEmpty(qs))
                return params.toQueryString().substring(1);
            else
                return null;
        }

        @Override
        public String getParameter(final String name)
        {
            String[] strings = getParameterMap().get(name);
            if (strings != null)
            {
                return strings[0];
            }
            return super.getParameter(name);
        }

        @Override
        public Map<String, String[]> getParameterMap()
        {
            return params.getMap();
        }

        @Override
        public Enumeration<String> getParameterNames()
        {
            return Collections.enumeration(getParameterMap().keySet());
        }

        @Override
        public String[] getParameterValues(final String name)
        {
            return getParameterMap().get(name);
        }

        public String getMethod() {
            return this.method;
        }

    }


}
