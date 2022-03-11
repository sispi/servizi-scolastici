package it.kdm.solr.providers;

import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.common.ModifiableURLStream;
import it.kdm.solr.core.Session;
import it.kdm.solr.interfaces.IRepository;
import it.kdm.solr.interfaces.IUserRoles;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.RetryUtil;
import org.apache.solr.common.util.RetryUtil.RetryCmd;
import org.apache.solr.request.SolrQueryRequest;
import org.noggit.ObjectBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;


public class AlfrescoProvider implements IRepository, /*ILogin,*/ IUserRoles {

	public static Logger log = LoggerFactory.getLogger(AlfrescoProvider.class);
	
	protected String alfrescoUrl;
	protected String alfrescoSecret;
	protected String alfrescoWorkspace;
	protected String alfrescoRoot;
	protected Boolean autologin;
	protected String name;
	
	protected String url;
	protected String driver;
	protected String usr;
	protected String pwd;
	
	@Override
	public void setConfig( Map<String,Object> config )
	{
		alfrescoUrl = (String) config.get("baseUrl");
		alfrescoSecret = (String) config.get("secret");
		alfrescoWorkspace = (String) config.get("Workspace");
		alfrescoRoot = (String) config.get("root");
		autologin = (Boolean) config.get("autologin");	
		name = (String) config.get("name");	
		
		url = (String) config.get("dbString");
		driver = (String) config.get("dbDriver");
		usr = (String) config.get("dbUser");
		pwd = (String) config.get("dbPass");
		
		if (driver!=null)
			DbUtils.loadDriver(driver);
	}
	
	protected SolrQueryRequest req;
	
	@Override
	public void setRequest( SolrQueryRequest req )
	{
		this.req = req;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	/*@Override
	public Map<String,Object> loginSSO( String ticket )
	{
		//String ticket = req.getParams().get("ticket");

		assert( ticket != null );

		String user = ticket;
		String password = null;
		
		if (ticket.indexOf(":") > 0)
		{
			user = ticket.split( ":" )[0];
			password = ticket.split( ":" )[1];
		}
		
		if (password != null)
		{
			String encpassword = DigestUtils.md5Hex( user + alfrescoSecret );
		
			if (!encpassword.equals(password) )
				throw new SolrException(SolrException.ErrorCode.UNAUTHORIZED, "Bad username or password" ) ;
		}
		else if (!autologin)
			throw new SolrException(SolrException.ErrorCode.UNAUTHORIZED, "Bad ticket" ) ;
		
		Map userInfo = GET( "/service/api/people/"+user );
		
		String userId = (String) userInfo.get("userName");
		String name = userId;
		
		//userId += "@user";
		

		
		if ( userInfo.containsKey( "lastName" ) )
		{
			name = ""+userInfo.get("lastName");
			
			if ( userInfo.containsKey( "firstName" ) )
				name += " ("+userInfo.get("firstName").toString()+")";
		}
		else if ( userInfo.containsKey( "firstName" ) )
		{
			name = (String) userInfo.get("firstName");
		}
		
		Map<String,Object> session = new HashMap<>();
		session.put("id" , userId );
		session.put("name" , name );
		
		return session;
	}*/
	
	@Override 
	public Collection<String> getUserRoles( String userId )
	{
		/*String organization = null;
		String userName = null;
		
		if (userId.indexOf("!")!=-1)
		{
			organization = userId.split("!")[0];
			userName = userId.split("!")[1].split(" @")[0];
		}
		else
		{
			userName = userId.split(" @")[0];
		}*/
		
		String userName = userId.split("@")[0];
		
		Map userInfo = GET( "/service/api/people/"+userName+"?groups=true" );
		
		Map capabilities = (Map) userInfo.get("capabilities");
		
		Boolean isAdmin = false;
		
		if (capabilities!=null)
		{
			Object __isa = capabilities.get("isAdmin");
			
			if (__isa != null && "true".equals(__isa.toString() ))
				isAdmin = true;
		}
		
		ArrayList groups = (ArrayList) userInfo.get("groups");
		
		Set<String> roles = new HashSet<>();
		
		roles.add( userId );
		
		if (groups != null)
		{
			for ( Object value : groups )
			{
				Map group = (Map) value;
				String displayName = group.get("displayName").toString();
				String itemName = group.get("itemName").toString();
				
				/*if (organization != null)
					roles.add( organization + "!" + displayName + "@group" );
				else*/
				roles.add( displayName );
			}
		}
		
		if (isAdmin)
			roles.add(Session.ROOTROLE);
			
		return roles;
	}

    @Override
    public Map<String, Object> getUserProperties(String userId) {
        return null;
    }

	/*@Override
	public Map<String,Object> readProfile( String content_id )
	{
		//GET /alfresco/service/api/node/{store_type}/{store_id}/{id}?filter={filter?}&returnVersion={returnVersion?}
		//GET /alfresco/s/api/metadata?nodeRef=workspace://SpacesStore/0b65c14d-7ada-4224-8910-69c8041dd3ab
		
		throw new java.lang.UnsupportedOperationException(); 
	}*/
	
	
	
	/*@Override
	public Collection<String> lastVersion( String content_id )
	{
		List<String> list = new ArrayList<>();
		
		Map<Integer,String> versions = getVersions(content_id);
		
		List<Integer> vs = new ArrayList<Integer>(versions.keySet());
		
		Collections.sort(vs , Collections.reverseOrder() );
		
		for( Integer v : vs )
			list.add(v.toString());
		
		return list;
	}*/
	
	/*@Override
	public String workingVersion( String content_id )
	{
		
		
		return lastVersion(content_id);
	}*/
	
	final static String version_sql = "select curl.id, node.uuid uuid, curl.content_url path " + 
										"from alf_node node "+
										"join alf_store store on store.id = node.store_id and store.protocol = 'workspace' and store.identifier = 'version2Store' "+
										"join alf_node_properties propv on propv.node_id = node.id "+
										"join alf_qname qtv on qtv.id = propv.qname_id and qtv.local_name = 'frozenNodeDbId' "+
										"join alf_node_properties propc on propc.node_id = node.id "+
										"join alf_qname qtc on qtc.id = propc.qname_id and qtc.local_name = 'content' "+
										"join alf_content_data cdata on propc.long_value = cdata.id "+
										"join alf_content_url curl on curl.id = cdata.content_url_id "+
										"join alf_node node2 on node2.id = propv.long_value and node2.uuid = ? "+
										
										" union select curl.id, node.uuid uuid, curl.content_url path "+
										"from alf_node node "+
										"join alf_node_properties propc on propc.node_id = node.id "+
										"join alf_qname qtc on qtc.id = propc.qname_id and qtc.local_name = 'content' "+
										"join alf_content_data cdata on propc.long_value = cdata.id "+
										"join alf_content_url curl on curl.id = cdata.content_url_id "+
										"where node.uuid = ? "+

										"order by id desc ";
										

	
	//@Override
	@SuppressWarnings("unchecked")
	private String lastVersion( String content_id )
	{
		if (driver != null)
		{
			String path;
			String nodeRef;
			
			Connection conn = null;
			try{
				QueryRunner run = new QueryRunner();
				
				conn = DriverManager.getConnection(url, usr, pwd);
				
				Map<String,Object> result = run.query(
					conn, version_sql, new MapHandler() , content_id, content_id );
					
				path = (String) result.get("path");
				nodeRef = (String) result.get("uuid");
				
				return FieldUtils.doVersionString(content_id, nodeRef, path);
				//return String.format( "version=%s&path=%s" , nodeRef , path.substring(8) );
			}
			catch(Exception e)
			{
				if (e instanceof RuntimeException)
					throw (RuntimeException) e;
				else
					throw new RuntimeException(e);
			}				
			finally {
				DbUtils.closeQuietly(conn);  
			}
		}
		else
		{
			Map versMap = GET( "/service/api/version?nodeRef=workspace://SpacesStore/" + content_id );
			
			List<Map> versList = (List<Map>) versMap.get("value");
			
			Map version = versList.iterator().next();
			
			String nodeRef = version.get("nodeRef").toString();
			
			int idx2 = nodeRef.lastIndexOf("/");
				
			nodeRef = nodeRef.substring(idx2+1);
			
			return FieldUtils.doVersionString(content_id, nodeRef, "");
		}
		//Map<Integer,String> versions = getVersions(content_id);
		
		//return Collections.max(versions.keySet()).toString();
	}
	
	
	/*@SuppressWarnings("unchecked")
	private Map<Integer,String> getVersions( String content_id )
	{
		int idx = content_id.indexOf("://");
			
		if (idx!=-1)
			content_id = content_id.substring(idx+3);
		
		Map versMap = GET( "/service/api/version?nodeRef=workspace://SpacesStore/" + content_id );
		
		List<Map> versList = (List<Map>) versMap.get("value");
		
		Map<Integer,String> versions = new HashMap<Integer,String>();
		
		String curVer = null;
		Integer lastVer = null;
		for( Map version : versList )
		{
			String nodeRef = version.get("nodeRef").toString();
			String label = version.get("label").toString().split("\\.")[0];
			
			if (label.equals(curVer))
				continue;
			
			if (lastVer==null)
				lastVer = Integer.parseInt(label);
			
			curVer = label;
			
			int idx2 = nodeRef.lastIndexOf("/");
			
			nodeRef = nodeRef.substring(idx2+1);
			
			versions.put( Integer.parseInt(label),nodeRef );
		}
		versions.put(lastVer,content_id);
		
		return versions;
	}*/
	
	public void	deleteVersion( String version_id )
	{
		throw new java.lang.UnsupportedOperationException(); 
	}
	
	public void	delete( String version_id )
	{
		throw new java.lang.UnsupportedOperationException();
	}
	
	@Override
	public ContentStream downloadVersion( String version_id )
	{
		Properties pairs = FieldUtils.getVersionProperties(version_id);
		
		String content_id = pairs.getProperty("id");
		
		String reqNodeRef = pairs.getProperty("version");
		
		String lastVersion = lastVersion(content_id);
		
		if (lastVersion != null)
		{
			Properties lastPairs = FieldUtils.getVersionProperties(lastVersion);
			lastVersion = lastPairs.getProperty("version");
		}
		
		String serviceUrl;
		
		if ( lastVersion!=null && !lastVersion.equals(reqNodeRef) )
			serviceUrl = "/service/api/node/content/workspace/version2Store/" + reqNodeRef;
		else
			serviceUrl = "/service/api/node/content/workspace/SpacesStore/" + content_id;
		
		String authStringEnc = getBasicAuth();
		
		try {
		
			String d_url = alfrescoUrl + serviceUrl; 
		
			java.net.URL url = new java.net.URL(d_url);
		
			ModifiableURLStream urlStream = new ModifiableURLStream( url );
			urlStream.setHeader("Authorization", "Basic " + authStringEnc);
			
			return urlStream;
		}
		catch (Exception e) 
		{
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			else
				throw new RuntimeException(e);
		} 
	}

	@Override
	public String create( ContentStream stream )
	{

		Map res = UPLOAD( stream , alfrescoWorkspace , null , false , true );
		
		String nodeRef = (String) res.get("nodeRef");
		
		int idx = nodeRef.lastIndexOf("/");
		
		nodeRef = nodeRef.substring(idx+1);
		
		return lastVersion(nodeRef);
	}
	
	@Override
	public String write( String version_id , boolean newVersion, ContentStream stream )
	{
		Properties pairs = FieldUtils.getVersionProperties(version_id);
		
		String nodeRef = pairs.getProperty("id");
		
		UPLOAD( stream , alfrescoWorkspace , nodeRef , newVersion , false );
		
		return lastVersion(nodeRef);
	}
	
	/*@Override
	public void createMajorVersion( String content_id , ContentStream stream )
	{
		UPLOAD( stream , alfrescoWorkspace , content_id , true , false );
	}*/
	
	private String getBasicAuth()
	{
	
		String password = alfrescoRoot;
		
		if (alfrescoSecret != null)
			password = org.apache.commons.codec.digest.DigestUtils.md5Hex( alfrescoRoot + alfrescoSecret );
		
		String basic = alfrescoRoot + ":" + password;
			
		byte[] authEncBytes = org.apache.commons.codec.binary.Base64.encodeBase64(basic.getBytes());
		return new java.lang.String(authEncBytes);
	}
	
	@SuppressWarnings("unchecked")
	private Map GET( final String destination )
	{
	
		final Map result = new HashMap();
		
		try {
		  RetryUtil.retryOnThrowable( RuntimeException.class, 3000, 500,
			  new RetryCmd() {
				
				@Override
				public void execute() throws Throwable {
				  
					Map res = __GET(destination);
					result.putAll(res);
					
				}
			  });
		} catch (Throwable t) {
			
			if (t instanceof RuntimeException)
				throw (RuntimeException) t;
			else
				throw new RuntimeException(t);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Map __GET( String destination )
	{
		String authStringEnc = getBasicAuth();
		Map json = null;
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		try {
		
			java.net.URL url = new java.net.URL(alfrescoUrl);
		
			HttpHost targetHost = new HttpHost( url.getHost() , url.getPort() , url.getProtocol() );
		
			HttpGet httpget = new HttpGet( url.getPath() + destination);
					
			httpget.setHeader("Authorization", "Basic " + authStringEnc);
			
			log.debug("executing request:" + httpget.getRequestLine());
			
			HttpResponse response = httpclient.execute(targetHost, httpget);
 
			HttpEntity resEntity = response.getEntity();
 
			log.debug("response status:" + response.getStatusLine());
 
			if (resEntity != null) {
				log.debug("response content length:"
						+ resEntity.getContentLength());
 
				String jsonString = EntityUtils.toString(resEntity);
				log.trace("response content:" + jsonString);
				
				Object temp = ObjectBuilder.fromJSON(jsonString);
				
				json = new HashMap();
				
				if (temp instanceof Map)
					json = (Map) temp;
				else 
					json.put("value",temp);
				
			}
			
			Map status = (Map) json.get("status");
			
			if (status!=null)
			{
				String code = status.get("code").toString();
				
				if (!"200".equals(code))
				{
					String desc = (String) status.get("description");
					throw new RuntimeException( desc );
				}
			}
 
			EntityUtils.consume(resEntity);
			
		} catch (Exception e) {
			
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			else
				throw new RuntimeException(e);
			
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		return json;
	}
	
	@SuppressWarnings("unchecked")
	private Map UPLOAD( final ContentStream stream, final String parentNodeRef, final String nodeRef , final boolean majorversion  , final boolean overwrite )
	{
	
		final Map result = new HashMap();
		
		try {
		  RetryUtil.retryOnThrowable( RuntimeException.class, 3000, 500,
			  new RetryCmd() {
				
				@Override
				public void execute() throws Throwable {
				  
					Map res = __UPLOAD( stream, parentNodeRef, nodeRef, majorversion, overwrite );
					result.putAll(res);
					
				}
			  });
		} catch (Throwable t) {
			
			if (t instanceof RuntimeException)
				throw (RuntimeException) t;
			else
				throw new RuntimeException(t);
		}
		
		return result;
	}

	private Map __UPLOAD( ContentStream stream, String parentNodeRef, String nodeRef , boolean majorversion  , boolean overwrite  ) {
	
		Map json = null;
		
		String authStringEnc = getBasicAuth();

		DefaultHttpClient httpclient = new DefaultHttpClient();
 
		try {
		
			java.io.InputStream file = stream.getStream();
			
			String fileName = stream.getName();
            String id = stream.getSourceInfo();
			
			//String ext = fileName.replaceAll(".*@.*(\\.[^\\.]+)","$1");
			
			//se ha estensione vuol dire che va trattato come semplice file (esempio per profile)
			if (fileName.endsWith(".json") || fileName.endsWith(".xml") )
			{
				nodeRef = null;
				majorversion = false;
				overwrite = true;
			}
			else
			{
				fileName = id + ".bin";
			}
			
			fileName = fileName.replaceAll( "[\\\\\\/\\:\\*\\?\\\"\\<\\>\\|]" , "_" );
		
			java.net.URL url = new java.net.URL(alfrescoUrl);
		
			HttpHost targetHost = new HttpHost( url.getHost() , url.getPort() , url.getProtocol() );
		
			HttpPost httppost = new HttpPost( url.getPath() + "/service/api/upload");
					
			httppost.setHeader("Authorization", "Basic " + authStringEnc);
			
			InputStreamBody bin = new InputStreamBody(file , ContentType.APPLICATION_OCTET_STREAM , fileName );
			
			//FileBody bin2 = new FileBody(new java.io.File("docs/1.docx") );
 
			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("filedata", bin);
			
			//if (uploadDirectory != null)
			//	reqEntity.addPart("uploaddirectory", new StringBody(uploadDirectory) );
			
			if (nodeRef != null)
			{
				if (!nodeRef.contains("//"))
					nodeRef = "workspace://SpacesStore/" + nodeRef;
				
				reqEntity.addPart("updatenoderef", new StringBody(nodeRef) );
				
				if (majorversion)
					reqEntity.addPart("majorversion", new StringBody("true") );
				else
					reqEntity.addPart("majorversion", new StringBody("false") );
			}
			
			if (parentNodeRef != null)
			{
				if (!parentNodeRef.contains("//"))
					parentNodeRef = "workspace://SpacesStore/" + parentNodeRef;
				
				reqEntity.addPart("destination", new StringBody(parentNodeRef) );
			}
				
			if (overwrite)
				reqEntity.addPart("overwrite" , new StringBody("true") );
			
			reqEntity.addPart("filename", new StringBody(fileName));
			
			httppost.setEntity(reqEntity);
 
			log.debug("executing request:" + httppost.getRequestLine());
 
			HttpResponse response = httpclient.execute(targetHost, httppost);
 
			HttpEntity resEntity = response.getEntity();
 
			log.debug("response status:" + response.getStatusLine());
 
			if (resEntity != null) {
				log.debug("response content length:"
						+ resEntity.getContentLength());
 
				String jsonString = EntityUtils.toString(resEntity);
				log.trace("response content:" + jsonString);
				
				json = (Map) ObjectBuilder.fromJSON(jsonString);
			}
			
			Map status = (Map) json.get("status");
			
			if (status!=null)
			{
				String code = status.get("code").toString();
				
				if (!"200".equals(code))
				{
					String desc = (String) status.get("description");
					log.error( "code:{} error:{}\n{}" , code , desc , json );
					throw new RuntimeException( String.format( "code:%s error:%s" , code , desc ) );
				}
			}
 
			EntityUtils.consume(resEntity);
			file.close();
			
		} catch (Exception e) {
			
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			else
				throw new RuntimeException(e);
			
		} finally {
			httpclient.getConnectionManager().shutdown();
			
		}
		
		return json;
    }

    @Override
    public String share(String version_ide) {
        return null;
    }

    @Override
    public void unshare(String version_id) {
    }
}