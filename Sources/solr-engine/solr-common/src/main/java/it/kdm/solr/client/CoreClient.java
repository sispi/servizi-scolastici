package it.kdm.solr.client;

import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CoreClient extends SolrClient {
	
	public static Logger log = LoggerFactory.getLogger(CoreClient.class);
	
	private volatile CoreContainer coreContainer = null;
	
	private CoreClient( String zkHost )
	{
		super(zkHost);
	}
	
	public static void init(CoreContainer coreContainer)
	{
		String zkHost1 = SolrClient.getZkHost_s() ;

		instance = new CoreClient(zkHost1);
		instance.coreContainer = coreContainer;
		instance.connect();
		
		//Schema.init(instance.getZkStateReader());
		
		/*if (coreContainer != null)
		{
			try
			{
				String threshold = java.lang.System.getProperty("threshold", "WARN");
				String level = java.lang.System.getProperty("level", "INFO");
			
				org.apache.solr.logging.LogWatcher watcher = coreContainer.getLogging();
			
				watcher.setThreshold(threshold);
				
				watcher.setLogLevel("root" , level);
			}
			catch(Exception e )
			{
			}
		}*/
	}
	
	public CoreContainer getCoreContainer()
	{
		return this.coreContainer;
	}
	
	/*public DocCollection getDocCollection( SolrQueryRequest req )
	{
		String collection = Schema.get(req).collectionName;
		return getZkStateReader().getClusterState().getCollection(collection);
	}*/
	
	static CoreClient instance=null;
	
	public static CoreClient getInstance()
	{
		if (instance!=null && instance.getDefaultCollection() == null)
		{
			String collName = System.getProperty("collection");
		
			if (collName == null)
			{
				Set<String> collNames = instance.getZkStateReader().getClusterState().getCollections();
				
				if (collNames.size()>0)
					collName = collNames.iterator().next();
			}
			
			if (collName != null)
				instance.setDefaultCollection(collName);
		}
		
		return instance;	
	}
	
	/*public void setTicket( String ticket )
	{
		threadTicket.set(ticket);
	}*/
	
	/*public String getTicket()
	{
		return threadTicket.get();
	}*/
	
	/*public ClusterState getClusterState()
	{
		return getZkStateReader().getClusterState();
	}
	
	public SolrDocument get( SolrQuery params ) throws SolrServerException 
	{
		if (params.get( CommonParams.QT ) == null )
			params.set( CommonParams.QT, "/get");
		
		SolrDocument doc = (SolrDocument) this.query( params ).getResponse().get("doc");
		
		if (doc==null)
			throw new SolrException(SolrException.ErrorCode.NOT_FOUND, "not found:"+ params.get("id") );
		
		return doc;
	}
	
	public InputStream getStream( SolrQuery params ) throws SolrServerException 
	{
		if (params.get( CommonParams.QT ) == null )
			params.set( CommonParams.QT, "/get");
		
		params.set( "wt" , "raw" );
		
		InputStream stream = (InputStream) this.query( params ).getResponse().get("stream");
		
		if (stream==null)
			throw new SolrException(SolrException.ErrorCode.NOT_FOUND, "not found:"+ params.get("id") );
		
		return stream;
	}*/
	
	public NamedList<Object> localRequest( SolrQueryRequest req , final SolrRequest request ) throws SolrServerException
	{
		String coreName = req.getCore().getName();
		return coreRequest( coreName , request );
	}
	
	public NamedList<Object> coreRequest( String coreName , final SolrRequest request ) throws SolrServerException
	{
		final EmbeddedSolrServer server = new EmbeddedSolrServer( coreContainer , coreName );
		
		try
		{
			//NamedList<Object> result;
			 
			//if (threaded)
			//{
			Callable< NamedList<Object> > callable = new Callable< NamedList<Object> >()
			{
				@Override
				public NamedList<Object> call() throws Exception {
					
					return server.request(request);
				}
			};
	
			ExecutorService service =  Executors.newSingleThreadExecutor();
			Future<NamedList<Object>> future = service.submit(callable);
			//NamedList<Object> result = future.get();
			//}
			/*else
			{
				//prevReq = Session. getRequestInfo().getReq();
				info = SolrRequestInfo.getRequestInfo();
				SolrRequestInfo.clearRequestInfo();
				result = server.request(request);
			}*/
			
			
						
			return future.get();
						
		}
		catch(Exception e)
		{
			if (e instanceof SolrServerException)
				throw  (SolrServerException) e;
			else
				throw new SolrServerException(e);
		}
		/*finally
		{
			if (!threaded)
			{
				SolrRequestInfo.setRequestInfo(info);
				//Session.setRequest(prevReq);
			}
		}*/
	}
	
	@Override
	public NamedList<Object> request( SolrRequest request, String collection ) throws SolrServerException
	{
		try
		{
			boolean shortcut = request.getParams().getBool("shortcut",false);
			
			SolrRequestInfo info = SolrRequestInfo.getRequestInfo();
			
			if ( info != null && shortcut && coreContainer != null)
			{
				
				
				//if (info==null)
				//	throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "This request must have SolrRequestInfo");
				
				SolrQueryRequest req = info.getReq();
				
				String coreName = req.getCore().getName() ;
				
				log.debug( "ready to execute direct request coreName:{} \nrequest:{}",coreName ,request.getParams() );
				
				NamedList<Object> result = this.coreRequest(coreName,request);
				
				log.trace( "executed direct request coreName:{} \nrequest:{} \nresult:{}",coreName ,request.getParams() , result );
				
				return result;
				
			}
			else
			{
				log.debug( "ready to execute http request:{}", request.getParams() );
				
				NamedList<Object> result = super.request(request,collection);
				
				log.trace( "executed http \nrequest:{} \nresult:{}", request.getParams() , result );
				
				return result;
			}
		}
		catch( IOException ioe )
		{
			throw new SolrServerException(ioe);
		}
	}
	
	//SearchUtils.AuthRequest( request, threadTicket.get() );
			
			
	
	/*@Override
	public void connect()
	{
		super.connect();
		Schema.init(this);
	}*/
	
	/*public ClusterState getClusterState()
	{
		//super.connect();
		return getZkStateReader().getClusterState();
	}*/
	
	/*@Override
	public String getDefaultCollection()
	{
		if ( super.getDefaultCollection() == null)
		{
			Set<String> colls = getClusterState().getCollections();
			
			if (colls.size()>0)
				setDefaultCollection(colls.iterator().next());
		}
		return super.getDefaultCollection();
	}*/
	

	
	/*public String getRoute Field()
	{
		if ( getDefaultCollection() == null)
			return null;
			
		DocCollection coll = getClusterState().getCollection(getDefaultCollection());
		
		Map routerProps = (Map) coll.get("router"); 
		
		return (String) routerProps.get("field") ;
	}
	
	public String get Route( String id )
	{
		String regex = Schema. getInstance().getRegex(id.split(" @")[1]);
	
		return id.replaceAll( regex , "$2/ 8$3!$4" );
	}*/
	
	/*public static DocCollection setCollection( String collection )
	{
		try
		{
			getServer().setDefaultCollection(collection);
			getServer().connect();
			
			org.apache.solr.common.cloud.ZkStateReader zr = getServer().getZkStateReader();
			
			org.apache.solr.common.cloud.ClusterState cs = zr.getClusterState();
			
			DocCollection coll = cs.getCollection(collection);
			return coll;
		}
		catch( Exception e )
		{
			log.error("error",e);
		}
		return null;
	}*/
	
	/*public static void setCloudUrl( String url )
	{
		serverUrl = url;
		server = null;
	}*/
	
	/*private static CloudSolrServer getServer()
	{	
		if (server==null)
			server = new CloudSolrServer(serverUrl);
		
		return server;
	}*/
	
	/*public SolrDocument get( String id , String route , String... fields  ) throws SolrServerException
	{
		SolrQuery query = new SolrQuery();
		query.setRequestHandler("/get");
		query.set( Fields.ID , id );
	
		params.setFields( fields );
		
		if (route!=null)
			params.set( ShardParams._ROUTE_ , route);
		
		//query.set("fl", fl );
		
		
		
		//query.set("ticket" , threadTicket.get() );
		
		SolrDocument doc;
		
		QueryResponse response = query(query);
		NamedList<Object> nl = response.getResponse();
		
		doc = (SolrDocument) nl.get("doc");
		
		if (doc==null)
			throw new SolrException(SolrException.ErrorCode.NOT_FOUND, "not found id:"+id );
		
		return doc;
	}*/
	
	/*public QueryResponse query( String querystring ) throws SolrServerException
	{
		SolrParams params = org.apache.solr.servlet.SolrRequestParsers.parseQueryString(querystring);
		
		return query( params );
	}*/
	
	/*@Override
	public QueryResponse query( SolrParams params ) throws SolrServerException
	{
		try
		{
		
			ModifiableSolrParams newparams = new ModifiableSolrParams(params);
			
			//newparams.set("qt",params.get("qt","/select") );
			
			//if (ticket!=null)
			
			newparams.set("ticket" , threadTicket.get() );
				
			if (params.get("fl")==null)
				newparams.set("fl" , fl );
				
			return super.query(newparams);
		}
		catch( Exception e )
		{
			log.error("********************* query exception:",e);
			throw new RuntimeException(e);
		}
		
		
	}*/
	
	
	
	/*public QueryResponse queryByPath( SolrDocument doc , String filter ) throws SolrServerException
	{
		ModifiableSolrParams params = new ModifiableSolrParams();
		
		String parent = (String) doc.getFieldValue( Fields.ID );
		
		String q = SearchUtils.makeClause( "parent" , parent );
		
		if (doc.containsKey( Fields.ENABLED ) )
			q+= SearchUtils.makeClause( "enabled" , doc.getFieldValue( Fields.ENABLED ) );
		else
			q+= SearchUtils.makeClause( "enabled" , true );
		
		if (filter != null )
			q+= SearchUtils.makeClause( "name" , filter );
		
		if (parent!=null)
			params.set( "_route_" , get Route(parent) ); 
			
		params.set("q" , q );
		params.set("fl" , fl );
		params.set("rows" , 1000 );
		params.set("qt" , "/selectall" );
		
		return query( params ).getResults();
	}*/
	
	/*public SolrDocument Create( SolrInputDocument doc ) throws SolrServerException
	{
		return __request( "/create" , doc, null, null);
	}
	
	public SolrDocument Create( SolrInputDocument doc, InputStream inputstream ) throws SolrServerException
	{
		return __request( "/create" , doc, null,inputstream);
	}
	
	public SolrDocument Update( SolrInputDocument doc ) throws SolrServerException
	{
		return __request( "/update" , doc, null, null);
	}
	
	public SolrDocument Update( SolrInputDocument doc, InputStream inputstream ) throws SolrServerException
	{
		return __request( "/update" , doc, null,inputstream);
	}*/
	
	/*public static void deleteById( String id )
	{
		try
		{
			String xml = "<delete><id>"+id+"</id></delete>";
			
			UpdateRequest sreq = new UpdateRequest("/update");
				
			sreq.setParam("ticket",ticket);
				
			sreq.setParam("stream.body" , xml );
			
			request(sreq); 
		}
		catch( Exception e )
		{
			log.error("********************* Delete exception:",e);
			throw new RuntimeException(e);
		}
	
	}*/
	

	
/*	public NamedList<Object> request( UpdateRequest request , final InputStream inputstream ) throws SolrServerException
	{
		try
		{
			File file = new File(Schema. getInstance().getFileCache() + "/temp/" + new Date().getTime() + ".dat" );
			
			FileUtils.copyInputStreamToFile( inputstream , file );
			
			request.setParam( "stream.file" , file.toString() );
		
			return request(request);
		
			ContentStreamBase stream = new ContentStreamBase() {
				@Override
				public InputStream getStream() throws java.io.IOException {
					return inputstream;
				}
			};
			
			ContentStreamUpdateRequest creq = new ContentStreamUpdateRequest( request.getPath() );
			
			creq.addContentStream(stream);
			creq.setParams(request.getParams());
			
			String xml = request.getXML();
				
			creq.setParam("ticket", threadTicket.get() );
				
			creq.setParam("stream.body" , xml );
			
			return request(creq);
			
			NamedList<Object> result = request(creq); 
			
			SolrDocument outdoc = (SolrDocument) result.get("processAdd");
			
			outdoc = get( (String) outdoc.get("id") );
			
			if (outdoc==null)
			{
				log.error("********** not found **********");
				throw new RuntimeException("problem update not found");
			}
			
			return outdoc;
		
		}
		catch( SolrException se )
		{
			log.error("********************* Update exception:",se);
			throw se;
		}
		catch( Exception e )
		{
			log.error("********************* Update exception:",e);
			throw new RuntimeException(e);
		}
	}*/
	
	/*private SolrDocument __request( String qt  , SolrInputDocument doc, ModifiableSolrParams params , final InputStream inputstream ) throws SolrServerException
	{
		try
		{
			org.apache.solr.client.solrj.request.AbstractUpdateRequest sreq;
			
			doc.remove(null);
			
			for( String key : doc.keySet() )
			{
				if ( doc.getFieldValue(key) == null || doc.getFieldValue(key).equals("") )
					doc.setField(key, Collections.singletonMap("set",null) ) ;
			}
			
			StringWriter writer = new StringWriter();
			writer.write("<add commitWithin=\"" + -1 + "\" " + "overwrite=\"" + true + "\">");
			ClientUtils.writeXML( doc , writer);
			writer.write("</add>");
			writer.flush();
			String xml = writer.toString();
			
			if (inputstream==null)
			{
				sreq = new UpdateRequest(qt);
			}
			else
			{
				ContentStreamBase stream = new ContentStreamBase() {
					@Override
					public InputStream getStream() throws java.io.IOException {
						return inputstream;
					}
				};
				
				ContentStreamUpdateRequest creq = new ContentStreamUpdateRequest(qt);
				creq.addContentStream(stream);
				
				sreq = creq;
			}
			
			if (params!=null)			
				sreq.setParams(params);
				
			sreq.setParam("ticket",ticket);
				
			sreq.setParam("stream.body" , xml );
			
			sreq.setParam("fl" , fl );
			
			NamedList<Object> result = request(sreq); 
			
			SolrDocument outdoc = (SolrDocument) result.get("processAdd");
			
			//outdoc = get( (String) outdoc.get("id") );
			
			if (outdoc==null)
			{
				log.error("********** not found **********");
				throw new RuntimeException("problem update not found");
			}
			
			return outdoc;
		
		}
		catch( SolrException se )
		{
			throw se;
		}
		catch( Exception e )
		{
			log.error("********************* Update exception:",e);
			throw new RuntimeException(e);
		}
	
	}*/
	
	/*public InputStream getStream( String id )
	{
		return getStream(id,null);
	}
	
	public InputStream getStream( String id , Date content_modified_on )
	{
		try
		{
			SolrDocument doc = new SolrDocument();
			doc.setField("id" , id );
			doc.setField("content_modified_on" , content_modified_on );
		
			ProviderProxy cp = new ProviderProxy(doc,req);
			
			File f = cp.getCacheFile();
			
			if (f!=null && f.exists())
				return new FileInputStream(f);
		
			String url = cp.get CacheURL();
			
			if (url != null )
				return new java.net.URL(url + "&ticket=" + threadTicket.get() ).openStream();
			else
				return null;
		}
		catch( Exception e )
		{
			log.error("********************* StreamGet exception:",e);
			throw new RuntimeException(e);
		}
	}*/
}

