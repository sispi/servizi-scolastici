package it.kdm.solr.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import it.kdm.solr.common.FieldUtils;
import org.apache.commons.io.IOUtils;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.cloud.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;

import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;

import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.SolrRequest;
//import org.apache.solr.client.solrj.impl.LBHttpSolrServer;
//import org.apache.solr.client.solrj.impl.CloudSolrClient;

public class SolrClient extends CloudSolrClient {

    final public static String RETRY_AFTER = "retryAfter";
    final public static String DC_PREFIX = "DC.";

    public static class UploadInfo
    {
        long modified;
        long size;
        long totalsize;
        String path;
        long chunk;

        public UploadInfo(String path, long lastModified, long size, long totalsize, long chunk)
        {
            this.path = path;
            this.modified = lastModified;
            this.size = size;
            this.totalsize = totalsize;
            this.chunk = chunk;
        }

        public long length()
        {
            return size;
        }

        public long totalLength()
        {
            return totalsize;
        }

        public long lastModified()
        {
            return modified;
        }

        public long lastChunkSize()
        {
            return chunk;
        }

        public String getPath()
        {
            return path;
        }
    }



    public static class ContentInputStream extends ContentStreamBase
	{
		InputStream istream;
		
		public ContentInputStream( InputStream stream )
		{
			istream = stream;
			sourceInfo = "inputstream" ;
			contentType = "application/octet-stream" ;
			//size = stream.available().longValue();
		}
				
		@Override
		public InputStream getStream() throws java.io.IOException {
			return istream;
		}		
	}

    public static class Request extends UpdateRequest
	{
		ContentStream stream = null;
		
		public Request( String url )
		{
			super(url);
		}
		
		public Request()
		{
			super();
		}

        public UpdateResponse process() throws SolrServerException, IOException
        {
            UpdateResponse resp = this.process(getInstance());
            return resp;
        }

        public Request add(String id, Map<String,Object> updates ) throws SolrServerException
        {
            add(id,updates,null);
            return this;
        }

        public Request add(String id, Map<String,Object> updates, InputStream stream ) throws SolrServerException
        {
            SolrInputDocument idoc = new SolrInputDocument();
            idoc.setField("id",id);

            for( String key : updates.keySet() )
                idoc.setField(key,updates.get(key));

            this.setStream(stream);

            add(idoc);

            return this;
        }
		
		@Override
		public Map<String,LBHttpSolrClient.Req> getRoutes(DocRouter router, DocCollection col, Map<String,List<String>> urlMap, ModifiableSolrParams params, String idField)
		{
			Map<String,LBHttpSolrClient.Req> routes;
			if (this.stream!=null)
			{
				routes = new HashMap<>();
				
				List<SolrInputDocument> docs = getDocuments();
				
				if (docs.size()==0)
					return null;

                if (docs.size()>1)
                    throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,"Stream request can be done using only one document");

                Object id = docs.get(0).getFieldValue("id");
				
				if (id==null)
					return null;
				
				Slice slice = router.getTargetSlice(id.toString(), docs.get(0), null, null, col);
				if (slice == null) {
				  return null;
				}
				List<String> urls = urlMap.get(slice.getName());
				String leaderUrl = urls.get(0);
				
				Request updateRequest = new Request();
				updateRequest.setMethod(getMethod());
				updateRequest.setCommitWithin(getCommitWithin());
				updateRequest.setParams(params);
				updateRequest.setPath(getPath());
				updateRequest.setStream(stream);

                updateRequest.add(docs.get(0));
				
				LBHttpSolrClient.Req request = new LBHttpSolrClient.Req(updateRequest, urls);
				routes.put(leaderUrl, request);
			}
			else
			{
				routes = super.getRoutes(router,col,urlMap,params,idField);
			}
			return routes;
		}

        public Request setTicket( String ticket )
        {
            setParam( "ticket" , ticket );
            return this;
        }
		
		public void setRoute( String route )
		{
			setParam( "_route_" , route );
		}
		
		/*public void addStream( final InputStream stream , boolean cached )
		{
			if (cached)
				addcached(stream);
			else
				addStream(stream);
		}
		
		private void addcached( InputStream stream  )
		{
			try
			{
				Path tempDir = Paths.get( System.getProperty("java.io.tmpdir") );
				Path tempFilePath = Files.createTempFile( tempDir , "temp", "a",new java.nio.file.attribute.FileAttribute<?>[0]);
				Files.copy(stream, tempFilePath );
				getParams().set( "stream.file"  , tempFilePath.toString() );
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}*/
		
		public ContentStream getStream()
		{
			return this.stream;
		}
		
		
		public void setStream( final InputStream istream )
		{
			/*ContentStreamBase cs = new ContentStreamBase() {
				
				@Override
				public InputStream getStream() throws java.io.IOException {
					return istream;
				}
				
				@Override
				public String getName()
				{
					return java.util.UUID.randomUUID().toString();
				}
				
				@Override
				public String getSourceInfo()
				{
					return "inputstream";
				}
				
				@Override
				public String getContentType()
				{
					return "application/octet-stream";
				}
				
			};*/
			
			setStream( new ContentInputStream(istream) );
		}

		public void setStream( File file )  {
			ContentStreamBase cs = new ContentStreamBase.FileStream(file);
			//cs.setContentType(contentType);
			setStream(cs);
		}
		
		public void setStream( String str )  {
			ContentStreamBase cs = new ContentStreamBase.StringStream(str);
			//cs.setContentType(contentType);
			setStream(cs);
		}

		public void setStream( ContentStream contentStream ){
			this.stream = contentStream;
		}
		
	}

	public static class Query extends SolrQuery
	{
        public SolrDocument getDocument(String id) throws SolrServerException
        {
            this.setId(id);
            return getInstance().get(this);
        }

        public InputStream getStream() throws SolrServerException
        {
            return getInstance().getStream(this);
        }

        public SolrDocumentList getDocumentList() throws SolrServerException, IOException
        {
            return getInstance().query(this).getResults();
        }

        public SolrDocument getFirstDocument() throws SolrServerException, IOException
        {
            SolrDocumentList list = getInstance().query(this).getResults();

            if (list.size()>0)
                return list.get(0);
            else
                return null;
        }

        public Query setTicket( String ticket )
        {
            set("ticket", ticket);
            return this;
        }

        public Query setId( String id )
        {
            set("id", id);
            return this;
        }

        public Query setPath( String path )
        {
            set("PATH", path);
            return this;
        }

        public Query setRoute( String route )
        {
            set("_route_", route);
            return this;
        }

        public Query addClause( String field, Object arg )
        {
            add( makeClause(field, arg) );
            return this;
        }

        public Query addClause( String field, Collection<Object> args )
        {
            add( makeClause(field, args) );
            return this;
        }

        public Query addClause( String field, Object[] args )
        {
            add( makeClause(field, args ) );
            return this;
        }

        public Query addNotClause( String field, Object arg )
        {
            add( makeNotClause(field, arg) );
            return this;
        }

        public Query addNotClause( String field, Collection<Object> args )
        {
            add( makeNotClause(field, args ) );
            return this;
        }

        public Query addNotClause( String field, Object[] args )
        {
            add( makeNotClause(field, args ) );
            return this;
        }
		
		/*publicoid addFilterClause ( String field, Object arg )
		{
			addfilter( makeClause(field, arg ) );
		}

		public void addFilterClause ( String field, Collection<Object> args )
		{
			addfilter( makeClause( field, args ) );
		}*/

        public Query addText( String text )
        {
            add( makeTextClause(text) );
            return this;
        }

        public Query addRange( String field, Object from , Object to )
        {
            add( makeRangeClause(field,from,to) );
            return this;
        }

        @Override
        public Query setRows(Integer rows)
        {
            return (Query) super.setRows(rows);
        }

        protected static String term( Object arg )
        {
            if ("".equals(arg))
                return "\"\"";
            else
                return arg.toString().replaceAll("([\\(\\)\\s\\+\\-\\&\\!\\{\\}\\[\\]\\^\\~\\?\\:\\\\\\/])", "\\\\$1");
        }
		
		protected void add( String clause )
		{
			String q = this.get( "q", "");
			q += " " + clause + " ";
			setQuery(q);
		}
		
		/*protected void addfilter( String clause )
		{
			String fq = this.get( "fq", "");
			fq += " " + clause + " ";
			setQuery(fq);
		}*/
		
		public static String makeTextClause( Object arg )
		{
			return "+" + term(arg) ;
		}
		
		
		
		public static String makeRangeClause( String field, Object from, Object to )
		{
			if (from == null)
				from = "*";
			
			if (to == null)
				to = "*";
			
			
		
			if (from instanceof java.util.Date)
				from = FieldUtils.formatDate((java.util.Date) from);
			
			if (to instanceof java.util.Date)
				to = FieldUtils.formatDate((java.util.Date) to);
			
			String clause = String.format( "+%s:[%s TO %s]" ,field, from, to ) ;
			
			return clause;
		}
		
		public static String makeNotClause( String field, Object arg )
		{
			return makeNotClause( field, Collections.singletonList(arg) );
		}
		
		public static String makeNotClause( String field, Object[] args )
		{
			return makeNotClause( field, Arrays.asList(args) );
		}
		
		public static String makeNotClause( String field, Collection<Object> args )
		{
			String clause;
			
			if (args == null || args.size()==0 || args.iterator().next() == null)
				clause = String.format( " +%s:* " , field );
			else if (args.size()==1)
				clause = String.format( " +( *:* -%s:%s ) " , field, term( args.iterator().next() ) );
			else
			{
				String clauses = "";
				for( Object arg : args )
					clauses += String.format( " %s " , term(arg) );
					
				clause = String.format( " +( *:* -%s:(%s) ) " , field, clauses );
			}
			return clause;
		}
		
		public static String makeClause( String field, Object arg )
		{
			return makeClause( field, Collections.singletonList(arg) );
		}
		
		public static String makeClause( String field, Object[] args )
		{
			return makeClause( field, Arrays.asList(args) );
		}
			
		public static String makeClause( String field, Collection<Object> args )
		{
			String clause;
			
			if (args == null || args.size()==0 || args.iterator().next() == null)
				clause = String.format( " +( *:* -%s:* ) " , field );
			else if (args.size()==1)
				clause = String.format( " +%s:%s " , field, term( args.iterator().next() ) );
			else
			{
				String clauses = "";
				for( Object arg : args )
					clauses += String.format( " %s " , term(arg) );
					
				clause = String.format( " +%s:(%s) " , field, clauses );
			}
			return clause;
		}
		
	}
	
	
	private static Logger log = LoggerFactory.getLogger(SolrClient.class);
	
	public SolrClient( String zkHost )
	{
		super(zkHost);
	}
	
	/*public static void setZkHost( String zkHost )
	{
		SolrClient.zkHost = zkHost;
	}*/
	
	public static String getZkHost_s()
	{
		//return SolrClient.zkHost;

        String zkHost1 = System.getProperty("zkHost");

        if (zkHost1!=null)
            return zkHost1;

        String jp = System.getProperty("jetty.port","8983");

        Integer port = Integer.parseInt(jp)+1000;

        return "localhost:"+port.toString();
	}
	
	//private static String zkHost = "localhost:9983";
	
	private static void init()
	{
		String zkHost1 = getZkHost_s() ;
		String collName = System.getProperty("collection");
		instance = new SolrClient(zkHost1);
		
		BinaryRequestWriter brw = new BinaryRequestWriter() {
			@Override
			public Collection<ContentStream> getContentStreams(SolrRequest req) throws IOException {

				if (req instanceof Request) {
					Request creq = (Request) req;

					if (creq.getStream() != null) {
						Collection<ContentStream> streams = new ArrayList<>();
						streams.add(creq.getStream());

						ContentStreamBase streamBase = new ContentStreamBase.StringStream(((UpdateRequest) req).getXML());

						streams.add(streamBase);

						for (ContentStream stream : streams) {
							if (stream.getName() == null && stream instanceof ContentStreamBase) {
								((ContentStreamBase) stream).setName(java.util.UUID.randomUUID().toString());
							}
						}
						return streams;
					}
				}
				return super.getContentStreams(req);
			}
		};

		instance.setRequestWriter(brw);
		
		instance.connect();
		
		if (collName == null)
		{
			collName = instance.getZkStateReader().getClusterState().getCollections().iterator().next();
		}
		
		instance.setDefaultCollection(collName);
	}
	
	private static SolrClient instance=null;

    static Map<String,SolrClient> datacenters = new HashMap<>();
	
	public static SolrClient getInstance()
	{
		if (instance==null)
			init();
		
		return instance;	
	}

    public static String getZkHost(String datacenter)
    {
        String remotezkhost = System.getProperty( DC_PREFIX + datacenter , datacenter) ;

        String zkHost = System.getProperty("zkHost","localhost:"+ (Integer.parseInt(System.getProperty("jetty.port", System.getProperty("hostPort","8983") ))+1000) );

        String zkPort = zkHost.split(":")[1];

        /* il primo deve essere quello con zookeeper */

        return remotezkhost + ":" + zkPort;
    }

    public static SolrClient getInstance(String datacenter)
    {
        if (datacenters.containsKey(datacenter))
            return datacenters.get(datacenter);



        SolrClient dc;

        synchronized (datacenters)
        {
            dc = new SolrClient(getZkHost(datacenter));
            dc.setDefaultCollection(getInstance().getDefaultCollection());

            dc.connect();

            datacenters.put(datacenter,dc);
        }

        return dc;
    }


	
	public SolrDocument get( SolrQuery params ) throws SolrServerException 
	{
        try {
            String id = params.get("id");

            if (params.get( CommonParams.QT ) == null )
                params.set( CommonParams.QT, "/get");

            SolrDocument doc = (SolrDocument) this.query( params ).getResponse().get("doc");

            if (doc==null)
            {
                long retryAfter = params.getInt(SolrClient.RETRY_AFTER, 0);

                while (retryAfter>0 && doc==null)
                {
                    log.warn("retryAfter {}ms id:{}",retryAfter,params.get("id"));
                    try {
                        Thread.sleep(100);
                        retryAfter-=100;
                        doc = (SolrDocument) this.query( params ).getResponse().get("doc");
                    }
                    catch(Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            }

            if (doc==null && id!=null && id.contains("@"))
            {
                String[] parts = id.split("@");

                String fq = Query.makeClause( "sid" , parts[0] ) + Query.makeClause( "views" , parts[1] )
                        + Query.makeNotClause( "type" , parts[1] ) ;

                params.addFilterQuery( fq );
                params.remove(CommonParams.QT);

                SolrDocumentList docs = this.query(params).getResults();

                if (docs.size()==1)
                    doc = docs.get(0);
            }

            if (doc==null)
            {
                log.warn("not found\n{}", params);
                throw new SolrException(SolrException.ErrorCode.NOT_FOUND, "not found:"+ params.get("id") );
            }

            return doc;
        }
        catch( IOException ioe)
        {
            throw new SolrServerException(ioe);
        }

	}
	
	/*public InputStream getStream( SolrQuery params ) throws SolrServerException
	{
        try {

            if (true)
            {
                List<String> liveNodes = new ArrayList<>( this.getZkStateReader().getClusterState().getLiveNodes() );

                Collections.shuffle(liveNodes, new java.util.Random() );

                String baseUrl = liveNodes.iterator().next();

                String url = String.format( "%s/get?id=%s&wt=raw&ticket=%s&getVersion=%s" , baseUrl, params.get("id") , params.get("ticket") , params.get("getVersion") );

                try
                {
                    return new java.net.URL(url).openStream();
                }
                catch(Exception e)
                {
                    throw new RuntimeException(e);
                }
            }

            if (params.get( CommonParams.QT ) == null )
                params.set( CommonParams.QT, "/get");

            //params.set( "wt" , "raw" );

            Object content = this.query( params ).getResponse().get("content");

            if (content instanceof InputStream)
            {
                return (InputStream) content;
            }
            else if (content instanceof byte[] )
            {
                return new java.io.ByteArrayInputStream((byte[]) content);
            }
            else
                throw new SolrException(SolrException.ErrorCode.NOT_FOUND, "content not found:"+ params.get("id") );

            //InputStream stream = (InputStream) this.query( params ).getResponse().get("stream");

        }
        catch( IOException ioe)
        {
            throw new SolrServerException(ioe);
        }

	}*/

    public UploadInfo getUploadInfo(String path) throws SolrServerException
    {
        String baseUrl = (String) this.getZkStateReader().getClusterState().getActiveSlices(this.getDefaultCollection()).iterator().next().getReplicas().iterator().next().get("base_url");

        String url = String.format( "%s/upload/%s" , baseUrl, path );

        HttpURLConnection httpConn=null;

        try{

            URLConnection urlconnection = new URL(url).openConnection();

            httpConn = (HttpURLConnection)urlconnection;

            httpConn.setRequestMethod("HEAD");
            httpConn.connect();

            int responseCode=httpConn.getResponseCode();

            if ((responseCode>= 200) &&(responseCode<=202) )
            {
                String filepath = httpConn.getHeaderField("file-path");
                long size = httpConn.getHeaderFieldLong("file-size",0);
                long modified = httpConn.getLastModified();
                long totalsize = httpConn.getHeaderFieldLong("total-size",0);

                if (totalsize<size)
                    totalsize = size;

                UploadInfo f = new UploadInfo(filepath,modified,size,totalsize,0);

                return f;
            }
            else if (responseCode== 404)
            {
                return null;
            }
            else
            {
                SolrException.ErrorCode solrEc = SolrException.ErrorCode.getErrorCode(responseCode);
                throw new SolrException( solrEc ,"error putting stream");
            }
        }
        catch(Exception e)
        {
            throw new SolrServerException(e);
        }
        finally
        {
            if (httpConn!=null)
                httpConn.disconnect();
        }
    }

    public void deleteStream( String path ) throws SolrServerException
    {
        uploadStream(path,0,null,0,0);
    }

    public UploadInfo uploadStream( String path, long start, InputStream stream ) throws SolrServerException
    {
        return uploadStream(path,start,stream,0,-1);
    }

    public UploadInfo uploadStream( String path, long start, InputStream stream , int skip, int length ) throws SolrServerException
    {
        String baseUrl = (String) this.getZkStateReader().getClusterState().getActiveSlices(this.getDefaultCollection()).iterator().next().getReplicas().iterator().next().get("base_url");

        String url = String.format( "%s/upload/%s?Range=%s-" , baseUrl, path, start );

        HttpURLConnection httpConn=null;

        try{

            URLConnection urlconnection = new URL(url).openConnection();

            httpConn = (HttpURLConnection)urlconnection;

            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);

            if (stream!=null)
            {
                httpConn.setRequestMethod("PUT");
                httpConn.connect();

                OutputStream out = urlconnection.getOutputStream();

                if (length>=0)
                    IOUtils.copyLarge(stream, out,skip,length );
                else
                    IOUtils.copyLarge(stream, out );

                out.close();
            }
            else
            {
                httpConn.setRequestMethod("DELETE");
                httpConn.connect();
            }

            int responseCode=httpConn.getResponseCode();

            if ((responseCode>= 200) &&(responseCode<=202) )
            {
                String filepath = httpConn.getHeaderField("file-path");
                long size = httpConn.getHeaderFieldLong("file-size",0);
                long modified = httpConn.getLastModified();
                long totalsize = httpConn.getHeaderFieldLong("total-size",0);
                long lastchunk = httpConn.getHeaderFieldLong("bytes-written",0);

                if (totalsize<size)
                    totalsize = size;

                UploadInfo f = new UploadInfo(filepath,modified,size,totalsize,lastchunk);

                return f;
            }
            else
            {
                SolrException.ErrorCode solrEc = SolrException.ErrorCode.getErrorCode(responseCode);
                throw new SolrException( solrEc ,"error putting stream");
            }
        }
        catch(Exception e)
        {
            throw new SolrServerException(e);
        }
        finally
        {
            if (httpConn!=null)
                httpConn.disconnect();
        }


    }

    public InputStream getUploadStream( String path , String range ) throws SolrServerException
    {
        String baseUrl = (String) this.getZkStateReader().getClusterState().getActiveSlices(this.getDefaultCollection()).iterator().next().getReplicas().iterator().next().get("base_url");

        String url = String.format( "%s/upload/%s" , baseUrl, path );

        HttpURLConnection conn=null;
        try
        {
            if (range!=null)
            {
                URL u = new URL(url);
                conn = (HttpURLConnection) u.openConnection();

                conn.setRequestProperty("Range", range);

                return conn.getInputStream();
            }

            return new java.net.URL(url).openStream();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (conn!=null)
                conn.disconnect();
        }
    }

    public InputStream getStream( SolrQuery params ) throws SolrServerException
    {
        //TODO si potrebbe fare in modo di utilizzare il core da TargetShard

        String id = params.get("id");

        /*String collName = params.get("collection",this.getDefaultCollection());

        ClusterState state = this.getZkStateReader().getClusterState();

        DocCollection coll = state.getCollection( collName );

        String targetSlice = new CompositeIdRouter().getTargetSlice( id , null , null , coll ).getName();

        Slice slice = state.getSlice(collName,targetSlice);

        String baseUrl = (String) slice.getReplicas().iterator().next().get("base_url");*/

        String baseUrl = (String) this.getZkStateReader().getClusterState().getActiveSlices(this.getDefaultCollection()).iterator().next().getReplicas().iterator().next().get("base_url");

        String range = params.get("Range");

        /*List<String> liveNodes = new ArrayList<>( this.getZkStateReader().getClusterState().getLiveNodes() );

        Collections.shuffle(liveNodes, new java.util.Random() );

        String baseUrl = liveNodes.iterator().next();

        String url = String.format( "%s/get?id=%s&wt=raw&ticket=%s&getVersion=%s" , baseUrl, params.get("id") , params.get("ticket") , params.get("getVersion") );
    */

        String version = params.get("getVersion","");

        String url = String.format( "%s/%s/get?id=%s&wt=raw&ticket=%s" , baseUrl, getDefaultCollection(), id , params.get("ticket") );


        if (!"".equals(version))
            url = String.format( "%s/%s/get?id=%s&wt=raw&ticket=%s&getVersion=%s" , baseUrl, getDefaultCollection(), params.get("id") , params.get("ticket") , version );

        if (range!=null)
            url += String.format( "&Range=%s" , range );

        try
        {
            /*if (range!=null)
            {
                URL u = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();

                conn.setRequestProperty("Range", range);

                return conn.getInputStream();
            }*/

            return new java.net.URL(url).openStream();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }


    }
	
}

