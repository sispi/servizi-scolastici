package it.kdm.solr.client;

//import it.kdm.solr.components.ContentManager;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.cloud.ZooKeeperException;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.RetryUtil;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;
import org.apache.zookeeper.data.Stat;

/**
 * Created by Paolo_2 on 29/08/15.
 */
public class zkClient {

    private final static String sequence_path = "global_sequence";
    //final static String guid_path = "global_guid";

    private transient static Logger log = LoggerFactory.getLogger(zkClient.class);

    public static Properties readZkProperties( String shortpath )
    {
        return readZkProperties(shortpath,null);
    }
    public static Properties readZkProperties( String shortpath, Stat stat )
    {
        log.debug("path:{}",shortpath);
        //SolrCore core = req.getCore();

        //SolrZkClient zkClient = core.getCoreDescriptor().getCoreContainer().getZkController().getZkClient();
        //String collection = core.getCoreDescriptor().getCloudDescriptor().getCollectionName();

        SolrZkClient zkClient = CoreClient.getInstance().getZkStateReader().getZkClient() ;
        String collection = CoreClient.getInstance().getDefaultCollection();

        String path = "/configs/" + collection + "/" + shortpath;

        Properties props = new Properties();
        try {
          byte[] data = zkClient.getData(path, null, stat, false);
          if (data != null) {
            props.load(new StringReader(new String(data, StandardCharsets.UTF_8)));
          }
        }
        catch( KeeperException.NoNodeException e )
        {
            log.warn( "Not existing properties from " + path + " :" + e.getClass(), e );
            //throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,"Could not read properties from " + path,e);
        }
        catch (Exception e) {
            log.error( "Could not read properties from " + path + " :" + e.getClass(), e );
            throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,"Could not read properties from " + path,e);
        }

        log.trace("props:",props);

        return props;
    }

    public static void writeZkProperties( String shortpath , Properties propObjs)
    {
        writeZkProperties(shortpath,propObjs,-1);
    }

    public static void writeZkProperties( String shortpath , Properties propObjs, int version)
    {
        log.debug("path:{} properties:{}",shortpath,propObjs);
        //SolrCore core = req.getCore();

        SolrZkClient zkClient = CoreClient.getInstance().getZkStateReader().getZkClient() ;

        //SolrZkClient zkClient = core.getCoreDescriptor().getCoreContainer().getZkController().getZkClient();
        //String collection = core.getCoreDescriptor().getCloudDescriptor().getCollectionName();

        String collection = CoreClient.getInstance().getDefaultCollection();

        String path = "/configs/" + collection + "/" + shortpath;

        Properties existing = readZkProperties(shortpath) ;
        existing.putAll(propObjs);

        StringWriter output = new StringWriter();
        try {
          existing.store(output, null);
          byte[] bytes = output.toString().getBytes(StandardCharsets.UTF_8);
          if (!zkClient.exists(path, false)) {
            try {
              zkClient.makePath(path, false);
              zkClient.makePath(path, false);
            } catch (KeeperException.NodeExistsException e) {}
          }

            if (version!=-1)
                zkClient.setData(path,bytes,version,false);
            else
                zkClient.setData(path, bytes, false);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          log.warn(
              "Could not persist properties to " + path + " :" + e.getClass(), e);
          throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,"Could not persist properties to " + path,e);
        } catch (Exception e) {
          log.error(
                  "Could not persist properties to " + path + " :" + e.getClass(), e);
          throw new ZooKeeperException(SolrException.ErrorCode.SERVER_ERROR,"Could not persist properties to " + path,e);
        }
    }

    public static long getCounter()
    {
        return checkCounter(null);
    }

    synchronized public static long checkCounter(final Long value )
    {
        final java.util.concurrent.atomic.AtomicLong counter = new java.util.concurrent.atomic.AtomicLong();
        //final java.util.concurrent.atomic.AtomicLong idx = new java.util.concurrent.atomic.AtomicLong();

        final String guid = UUID.randomUUID().toString();

        log.debug( "make atomic counter op {} start",guid);

        try {
            RetryUtil.retryOnThrowable(SolrException.class, 4000, 1000,
                    new RetryUtil.RetryCmd() {

                        @Override
                        public void execute() throws Throwable {

                            log.debug("retry op {}",guid);

                            Stat stat = new Stat();

                            Properties props = readZkProperties(sequence_path,stat);

                            int version = stat.getVersion();

                            String reg = props.getProperty("counter");

                            if (reg==null && value==null)
                                throw new SolrException(SolrException.ErrorCode.CONFLICT, "counter not initialized" ) ;

                            if (reg==null)
                            {
                                reg = "0";
                                version = -1;
                            }

                            Long oldvalue = Long.parseLong(reg);
                            Long newvalue = oldvalue+1;

                            if (value!=null)
                                newvalue = value;

                            counter.set(newvalue);

                            log.debug("props getted op {} new value {} old value {} version {}", guid, newvalue, oldvalue, version);

                            if (newvalue>oldvalue)
                            {
                                props.setProperty("counter", newvalue.toString() );
                                writeZkProperties(sequence_path, props, version);

                                log.debug("props setted op {} new value {} old value {} version {}", guid, newvalue, oldvalue, version);
                            }
                            else
                            {
                                log.debug("props not setted op {} new value {} old value {} version {}", guid, newvalue, oldvalue, version);
                            }


                        }
                    });
        } catch (Throwable t) {
            if (t instanceof OutOfMemoryError) {
                throw (OutOfMemoryError) t;
            }
            log.error( "Could not increment counter (2) {} op {} " , sequence_path , guid, t );
            throw new SolrException(SolrException.ErrorCode.CONFLICT, "Could not increment counter (2): " + t.getMessage() ) ;
        }

        log.debug( "make atomic counter {} op {} end",counter,guid);

        return counter.get();
    }

    /*public static long getCounter2()
    {
        final java.util.concurrent.atomic.AtomicLong counter = new java.util.concurrent.atomic.AtomicLong();
        final java.util.concurrent.atomic.AtomicLong idx = new java.util.concurrent.atomic.AtomicLong();

        log.debug( "make counter");

        try {
          RetryUtil.retryOnThrowable(SolrException.class, 4000, 1000,
                  new RetryUtil.RetryCmd() {

                      @Override
                      public void execute() throws Throwable {

                          log.debug("retry :" + idx.incrementAndGet());

                          String guid0 = UUID.randomUUID().toString();

                          Properties props = new Properties();

                          props.setProperty("guid",guid0);

                          writeZkProperties(guid_path, props);

                          props = readZkProperties(sequence_path);

                          String reg = props.getProperty("counter");
                          if (reg == null)
                              reg = "1";
                          else
                              reg = "" + (Long.parseLong(reg) + 1);

                          //String unixTime0 = "" + (System.currentTimeMillis() / 1000L);

                          props.setProperty("counter", reg);
                          //props.setProperty("timestamp", unixTime0);

                          writeZkProperties(sequence_path, props);

                          props = readZkProperties(guid_path);

                          //String unixTime1 = props.getProperty("timestamp");
                          String guid1 = props.getProperty("guid");

                          //String counter1 = props.getProperty("counter");

                          //if (unixTime0.equals(unixTime1) && counter1.equals(reg))
                          if (guid0.equals(guid1))
                              counter.set(Long.parseLong(reg));
                          else {
                              log.warn("retry increment counter:" + sequence_path);
                              throw new SolrException(SolrException.ErrorCode.CONFLICT, "Could not increment counter " + sequence_path);
                          }
                      }
                  });
        } catch (Throwable t) {
          if (t instanceof OutOfMemoryError) {
            throw (OutOfMemoryError) t;
          }
          log.debug( "Could not increment counter (2) {} " , sequence_path , t );
          throw new SolrException(SolrException.ErrorCode.CONFLICT, "Could not increment counter (2) " + sequence_path ) ;
        }

        return counter.get();
    }*/

    @SuppressWarnings("unchecked")
    public static long initCounter()
    {
        SolrQuery params = new SolrQuery("*:*");
        params.setRequestHandler( "/realselect" );
        params.set( "stats" , true );
        params.set( "q" , "location:" + System.getProperty("location") );
        params.set( "shards.qt" , "/realselect" );
        params.set( "stats.field" , "sequence" );
        params.setRows(0);

        NamedList nl;

        try
        {
            nl = CoreClient.getInstance().query( params ).getResponse();
            nl = (NamedList) nl.findRecursive( "stats" , "stats_fields" , "sequence" );
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }

        Number value = null;

        if (nl!=null)
        {
            value = (Number) nl.get("max");
        }

        if (value==null)
        {
            value = 0;

            Properties props = new Properties();
            props.setProperty("counter",value.toString() );

            writeZkProperties(sequence_path, props, -1);
        }

        long newValue = checkCounter(value.longValue());

        log.warn("value:{} newValue:{}",value,newValue);

        return newValue;
    }

    /*public static long checkCounter( final long value )
	{
		final java.util.concurrent.atomic.AtomicLong counter = new java.util.concurrent.atomic.AtomicLong();

		log.info( "check counter:{}", value );


		try {
		  RetryUtil.retryOnThrowable(SolrException.class, 4000, 1000,
			  new RetryUtil.RetryCmd() {

				@Override
				public void execute() throws Throwable {

					Properties props = readZkProperties(sequence_path);

					String counter0 = props.getProperty("counter");

                    if (counter0 != null && Long.parseLong(counter0) >= value && value!=0)
					{
						counter.set( Long.parseLong(counter0) );
						return;
					}

					counter0 = new Long(value).toString();
					String unixTime0 = ""+(System.currentTimeMillis() / 1000L);

					props.setProperty( "counter" , counter0 );
					props.setProperty( "timestamp" , unixTime0 );

					writeZkProperties(sequence_path, props);

					props = readZkProperties(sequence_path);

					String counter1 = props.getProperty("counter");
					String unixTime1 = props.getProperty("timestamp");


					if (!unixTime0.equals(unixTime1) || !counter1.equals(counter0) )
					{
						log.warn( "retry check counter");
						throw new SolrException(SolrException.ErrorCode.CONFLICT, "Could not set counter " + sequence_path ) ;
					}

					counter.set( value );
				}
			  });
		} catch (Throwable t) {
		  if (t instanceof OutOfMemoryError) {
			throw (OutOfMemoryError) t;
		  }
		  throw new SolrException(SolrException.ErrorCode.CONFLICT, "Could not set counter " + sequence_path ) ;
		}

		return counter.get();

	}*/
}
