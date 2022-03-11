/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.kdm.solr.components;

import com.google.common.base.Strings;
import it.kdm.solr.client.CoreClient;
import it.kdm.solr.client.SolrClient;
import it.kdm.solr.core.Schema;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.solr.client.solrj.impl.ZkClientClusterStateProvider;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.Replica;
import org.apache.solr.common.cloud.Slice;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.servlet.SolrDispatchFilter;
import org.apache.solr.servlet.SolrRequestParsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("unchecked")
public class DispatchFilter extends SolrDispatchFilter 
{
    public static final String TIMER_SCHEDULE_RELOAD = "timer-schedule-reload";
    public static final String JETTY_PORT = "jetty.port";
    public static final int RELOAD_PERIOD = Integer.parseInt( System.getProperty("RELOAD_PERIOD","180") ) ; //seconds  ;
    public static final int RELOAD_DELAY = Integer.parseInt( System.getProperty("RELOAD_DELAY","180") ) ; //seconds  ;
    public static final String SYNC_PT00_00_60_SYNC = "#sync PT00:00:60 /sync";
    public static final String CONFIGS_SCHEDULER_PROPERTIES = "scheduler_path";
    public static final String TIMER_XXX = "timer-xxx-";
    public static final int TICK_DELAY = 5 * 1000;
    public static final String DEF_JETTY_PORT = "8983";
    //public static final String COLLECTION_CONFIG_NAME = "collection.configName";
    public static final String COLLECTION_CONFIG_SET = "collection.configSet";
    public static final String BOOTSTRAP_CONFDIR = "bootstrap_confdir";
    //public static final String BOOTSTRAP_RELOAD = "bootstrap_reload";

    private transient static Logger log = LoggerFactory.getLogger(DispatchFilter.class);

    private void initProperties()
    {
        String profile = System.getProperty("profile");

        if (profile != null && !"".equals(profile))
        {
            profile = profile + ".profile.properties";

            Properties props;

            log.info( "load property file '{}':", profile);

            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(profile))  {

                if (is==null)
                    throw new FileNotFoundException("property file '"+profile+"' not found.");

                props = new Properties();
                props.load(is);

                Enumeration e = props.propertyNames();

                while (e.hasMoreElements())
                {
                    String key = (String) e.nextElement();
                    String value = props.getProperty(key);

                    System.setProperty(key,value);

                    log.debug("loaded {}={}", key, value);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else
        {
            log.info( "No profile property file provided. Using default profile." );
            //return;
        }

        try
        {
            String def = System.getProperty("location");
            Set<String> locations = new LinkedHashSet<>();

            if (def!=null)
            {
                System.setProperty("DC."+def,"localhost");
                locations.add(def + "!@location");
            }

            for( String key : System.getProperties().stringPropertyNames() )
            {
                if (key.startsWith(SolrClient.DC_PREFIX))
                {
                    String dataCenter = key.substring(SolrClient.DC_PREFIX.length());

                    String location = dataCenter + "!@location";

                    locations.add( location );

                    String IP = System.getProperty(key); //ips[i];

                    if (def==null)
                    {
                        try
                        {
                            InetAddress addr = InetAddress.getByName(IP);

                            log.info("check IP {} for location. Address {}",IP,addr);

                            if (addr.isAnyLocalAddress() || addr.isLoopbackAddress() || NetworkInterface.getByInetAddress(addr) != null)
                            {
                                def = dataCenter;
                                System.setProperty("location",def);

                                log.info("local server is location:{} on address:{}", def, IP);
                                //break;
                            }
                        }
                        catch(IOException e )
                        {
                            log.error("Invalid address:{}", IP, e);
                        }
                    }
                }
            }

            if (def==null)
            {
                log.error("Default location not found");
                throw new SolrException( SolrException.ErrorCode.SERVER_ERROR,"Default location not set in system properties");
            }

            System.setProperty("locations", StrUtils.join(locations,','));

        }
        catch(Exception e)
        {
            log.error("Error setting location properties",e);
            throw e;
        }
    }

    /*private void checkSchemaVersion()
    {
        ZkStateReader zsr = this.cores.getZkController().getZkStateReader();
        String collection = zsr.getClusterState().getCollections().iterator().next();

        Schema.unloadIfOld(collection, zsr);
    }*/

    private void initClusterProperties()
    {
        try
        {
            List<String> dc_alive = new ArrayList<>();
            List<String> clusterIps = new ArrayList<>();
            String local = System.getProperty("location");

            for( String key : System.getProperties().stringPropertyNames() )
            {
                if (key.startsWith(SolrClient.DC_PREFIX))
                {
                    String dataCenter = key.substring(SolrClient.DC_PREFIX.length());

                    String IP = System.getProperty(key);

                    try
                    {
                        ZkStateReader zsr;

                        if (dataCenter.equals(local))
                            zsr = this.cores.getZkController().getZkStateReader();
                        else
                            zsr = SolrClient.getInstance(dataCenter).getZkStateReader();

                        List<String> shardUrls = getShardUrls(zsr);

                        clusterIps.addAll( getShardIps(zsr) );

                        System.setProperty("DC_"+dataCenter+"_SHARDS", StrUtils.join(shardUrls,','));

                        dc_alive.add(dataCenter);

                        log.info("datacenter '{}' added to clusters with url:{}",dataCenter,shardUrls);
                    }
                    catch(Exception e)
                    {
                        log.error("datacenter '{}' exluded from clusters with IP:{}",dataCenter,IP);
                    }
                }
            }

            System.setProperty(Schema.DC_ALIVE, StrUtils.join(dc_alive,','));
            System.setProperty(Schema.DC_CLUSTER_IPS, StrUtils.join(clusterIps,'|') );
        }
        catch(Exception e)
        {
            log.error("Error setting cluster properties",e);
            throw e;
        }
    }

    private List<String> getShardUrls( ZkStateReader zsr )
    {
        List<String> shards = new ArrayList<>();

        String collection = zsr.getClusterState().getCollections().iterator().next();

        Collection<Slice> slices = zsr.getClusterState().getActiveSlices(collection);

        for( Slice slice : slices )
        {
            List<String> replicas = new ArrayList<>();

            for( Replica replica : slice.getReplicas() )
            {
                String base_url = replica.getStr("base_url");
                String core = replica.getStr("core");

                String repUrl = String.format("%s/%s", base_url, core);

                replicas.add( repUrl );
            }

            shards.add( StrUtils.join(replicas,'|') );
        }

        return shards;
    }

    final static Pattern urlPattern = Pattern.compile( "^http://([^:]+).*" );
    private Set<String> getShardIps(ZkStateReader zsr)
    {
        //ZkStateReader zsr = this.cores.getZkController().getZkStateReader();

        HashSet<String> shards = new LinkedHashSet<>();

        String collection = zsr.getClusterState().getCollections().iterator().next();

        Collection<Slice> slices = zsr.getClusterState().getActiveSlices(collection);

        for( Slice slice : slices )
        {
            List<String> replicas = new ArrayList<>();

            for( Replica replica : slice.getReplicas() )
            {
                String url = replica.getStr("base_url");

                Matcher m = urlPattern.matcher(url);

                if (m.matches())
                    shards.add(m.group(1));
            }
        }

        return shards;
    }



    @Override
	public void init(FilterConfig config) throws ServletException
	{
        initProperties();

        super.init(config);

        CoreClient.init(this.cores);

        String port = ""+java.lang.System.getProperty(JETTY_PORT, DEF_JETTY_PORT);

        java.lang.System.setProperty(JETTY_PORT, port );

        ServletContext servletContext = config.getServletContext();

        Timer t = (Timer) servletContext.getAttribute(TIMER_SCHEDULE_RELOAD);

        if (t!=null)
            t.cancel();

        t = new Timer();

        final FilterConfig filterConfig = config;

        final String path = System.getProperty(CONFIGS_SCHEDULER_PROPERTIES,"/scheduler.json");

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                initProperties();
                initClusterProperties();
                initSchedulers(filterConfig,path);
            }
        };

        servletContext.setAttribute(TIMER_SCHEDULE_RELOAD, t);

        t.schedule(task, RELOAD_DELAY * 1000, RELOAD_PERIOD * 1000);

        initClusterProperties();

    }


    private void initSchedulers(FilterConfig config,String path)
    {
        Properties schedulers;

        try {

            SolrZkClient zkClient = this.cores.getZkController().getZkClient();

            if (!zkClient.exists(path,true))
            {
                byte[] bytes = SYNC_PT00_00_60_SYNC.getBytes(StandardCharsets.UTF_8);
                zkClient.makePath(path, true);
                zkClient.setData(path, bytes, true);
            }

            byte[] data = zkClient.getData(path, null, null, true);

            InputStream in = new ByteArrayInputStream(data);

            schedulers = new Properties();
            schedulers.load(in);
        }
        catch (Exception e) {
            log.warn("could not init schedulers", e);
            return;
        }

        Enumeration props = schedulers.propertyNames();

        if (schedulers.size()==0)
        {
            log.info("No task to schedule");
            return;
        }

        ServletContext servletContext = config.getServletContext();

        Enumeration<String> attrs = servletContext.getAttributeNames();

        Set<String> timers = new LinkedHashSet<>();

        while (attrs.hasMoreElements()) {

            String key = attrs.nextElement();

            Object obj = servletContext.getAttribute(key);

            if (obj instanceof ScheduledTask && key.startsWith(TIMER_XXX) )
                timers.add(key);

        }

        while (attrs.hasMoreElements()) {

            String key = attrs.nextElement();

            Object obj = servletContext.getAttribute(key);

            if (obj instanceof Timer && key.startsWith(TIMER_XXX) )
            {
                ((Timer)obj).cancel();
                servletContext.removeAttribute(key);
                log.info("task '{}' cancelled", key.substring(TIMER_XXX.length()));
            }
        }

        while (props.hasMoreElements()) {

            String key = (String) props.nextElement();

            String value = schedulers.getProperty(key);

            int sidx = value.indexOf(" ");

            if (sidx==-1)
            {
                log.error("invalid schedule line for key '{}'");
                continue;
            }

            String period = value.substring(0,sidx);

            String handler = value.substring(sidx + 1);

            try
            {
                String timerkey = TIMER_XXX+(key + value).hashCode();

                if (servletContext.getAttribute(timerkey)==null)
                {
                    ScheduledTask task = new ScheduledTask( key, period , handler);
                    task.schedule(TICK_DELAY);
                    servletContext.setAttribute(timerkey, task);
                }
                else
                {
                    timers.remove(timerkey);
                }
            }
            catch(Exception e)
            {
                log.error("error saving schedule '{}'", key , e);
                continue;
            }
        }

        for( String timerkey : timers) {

            ScheduledTask task = (ScheduledTask) servletContext.getAttribute(timerkey);
            task.cancel();

            servletContext.removeAttribute(timerkey);
        }
    }

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

                written = IOUtils.copy(is,fos);

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
        FileUtils.writeStringToFile(new File(tempfile.getPath()+".size"),""+total);

        if( written!=contentLength )
        {
            //httpResponse.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException("Invalid state. temp file deleted.");

        }

        return HttpStatus.SC_OK;
    }

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

    protected int doUpload(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException, ServletException
    {
        String qs = httpRequest.getQueryString();

        String path = httpRequest.getServletPath();

        SolrParams params = SolrRequestParsers.parseQueryString(qs);
        String id = params.get(Schema.Fields.ID);

        path = StringUtils.strip( path.substring(7).replace("\\","/") , "/" );

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

        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;

        String path = httpRequest.getServletPath();

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

        String qs = httpRequest.getQueryString();

        if ( qs != null && "/admin/collections".equals(path) )
		{
            SolrParams params = SolrRequestParsers.parseQueryString(qs);

            if ("RELOAD".equals(params.get("action")))
            {
                String collection = params.get("name");

                Schema.unload(collection);

                log.info("configuration unloaded for collection \"{}\"", collection );

                initProperties();

                String configName = System.getProperty(COLLECTION_CONFIG_SET);

                if (configName==null)
                    configName = collection;

                Path configPath = this.cores.getConfig().getConfigSetBaseDirectory().resolve(configName+"/conf");

                if (configPath.toFile().exists())
                {
                    try
                    {
                        ZkClientClusterStateProvider prov = (ZkClientClusterStateProvider) SolrClient.getInstance().getClusterStateProvider();

                        prov.uploadConfig(configPath,configName);

                        log.warn("configuration \"{}\" realoaded from \"{}\"", configName, configPath);
                    }
                    catch(Exception e)
                    {
                        httpResponse.setStatus(500);

                        log.error("error uploading config",e);
                        throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , e ) ;
                    }
                }
            }
		}

        try
        {
            super.doFilter(request,response,chain);
        }
        catch(Exception rte)
        {
            log.error("method:{} path:{} query:{}",((HttpServletRequest) request).getMethod(),((HttpServletRequest) request).getPathInfo() , ((HttpServletRequest) request).getQueryString(),rte);
            throw rte;
        }


        if (httpResponse.getHeader("Content-Range") != null)
            httpResponse.setStatus(206);
	}
}