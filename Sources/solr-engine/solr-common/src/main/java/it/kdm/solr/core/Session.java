
package it.kdm.solr.core;

//import org.apache.solr.util;

import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.interfaces.ILogin;
import it.kdm.solr.interfaces.IUserRoles;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrException;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

public class Session extends org.apache.solr.common.util.SimpleOrderedMap<Object> implements ExpirableRegenerator.Expirable {

	private transient static Logger log = LoggerFactory.getLogger(Session.class);
	
	public static final String ROOTTICKET = "admin";
	private static final String ROOTID = "admin";
	public static final String ROOTROLE = "admins";
	public static final String ACL_CLAUSE = "{!join from=id to=acl_read}+enabled:true ";
	private static final String SESSION_CACHE = "sessions";
	
	public static final int SESSION_CACHE_DURATION = Integer.parseInt( System.getProperty("session.minutes","30") )*60;
	
	public static final long DURATION = SESSION_CACHE_DURATION*1000;
	
	protected final static ThreadLocal<SolrRequestInfo> threadRequest = new ThreadLocal<>();
	
	//private static final AtomicReference<SolrQueryRequest> adminReq = new AtomicReference<SolrQueryRequest>();
	
	
	
	
	/*public final String id;
	public final String identity;
	public String name;
	public Collection<String> roles;
	public int globalacl=0;
	String acl_clause;
	public String ticket;
	public Date logged_on;
	public String organization;*/
	
	private Session(String organization, String userId)
	{
		/*if (userId != null)
		{
			this.add("userId" , userId);
			this.add("identity" , userId);
			this.add("name" , userId );
		}*/
		
		//this.identity = userId;
		this.add("roles" , new TreeSet<String>() );
		
		if (organization != null)
			this.add("organization", organization);
		
		this.add("globalacl", 0);
		
		//this.ticket = null;
		this.add("logged_on" , new Date() );
		this.add("expires_on" , new Date( System.currentTimeMillis() + DURATION ) );
		this.add("acl_clause" , "-*:*" );
		
		if (userId != null)
		{
			if (organization!=null)
				this.add("id" ,  organization + "!" + userId + "@user" );
			else
				this.add("id" ,  userId + "@user" );
		}
		//else
		//	this.id = null;
	}
	
	public void set( String name , Object value )
	{
		this.removeAll(name);
		this.add(name,value);
	}

    public Object getUserProperty( String prop ,  Object defaultValue )
    {
        Map<String,Object> map = (Map<String,Object>) get("props");

        if (map!=null && map.containsKey("prop"))
            return map.get(prop);
        return defaultValue;
    }

    public String getDisplayName()
    {
        String id = getId();
        if (id!=null)
            return (String) getUserProperty("display_name",id.split("@")[0]);
        else
            return null;
    }
	
	public int getGlobalAcl()
	{
		return (int) get("globalacl");
	}
	
	public String getId()
	{
		return (String) get("id");
	}
	
	@SuppressWarnings("unchecked")
	public Collection<String> getRoles()
	{
		return (Collection<String>) get("roles");
	}
	
	@SuppressWarnings("unchecked")
	public void setRoles( Collection<String> roles )
	{
		synchronized(this)
		{
			set("roles" , roles );
		}
	}
	
	/*public String getAclClause()
	{
		return (String) get("acl_clause");
	}*/
	
	public ExpirableRegenerator.Expirable getAclQuery()
	{
		return (ExpirableRegenerator.Expirable) get("acl_query");
	}
	
	public void setAclQuery( ExpirableRegenerator.Expirable expirable )
	{
		synchronized(this)
		{
			set("acl_query" , expirable );
		}
	}
	
	public String getTicket()
	{
		return (String) get("ticket");
	}
	

	
	/*public String getName()
	{
		return (String) get("name");
	}

	public String getOrganization()
	{
		return (String) get("organization");
	}

	public String getIdentity()
	{
		return (String) get("identity");
	}*/
	

	
	
	/*@SuppressWarnings("unchecked")
	void save()
	{
		add("id" , id);
		add("identity" , identity);
		add("organization" , organization );
		add("name" , name);
		add("roles" , roles);
		add("globalacl" , globalacl);
		add("logged_on" , logged_on);
		add("ticket" , ticket );
		add("expires_on" , new Date( System.currentTimeMillis() + DURATION ) );
	}*/
	
	public boolean isExpired()
	{
		Date expires_on = (Date) get("expires_on");
		return expires_on.before(new Date());
		//long minutes = ( new Date().getTime() - this.logged_on.getTime())/(1000 * 60);
		//return (minutes>max_minutes);
	}
	
	public Object getObject()
	{
		return this;
	}
	
	@Override
	public String toString()
	{
		if (isExpired())
			return "EXPIRED";
		else
			return super.toString();
		/*if (isExpired())
			return "{ \"id\": \""+this.id+"\" logged_on:"+this.logged_on+", expired:true }";
		else
			return "{ \"id\": \""+this.id+"\", \"name\": \""+this.name+"\", \"globalacl\":"+this.globalacl+", \"roles\": \""+roles+"\", \"ticket\":\""+this.ticket+"\", \"logged_on\":\""+this.logged_on+"\" }";
		*/
	}
	
	/*public static Schema getSchema()
	{
		//SolrQueryRequest req = getRequestInfo().getReq();
		
		
		return Schema.get();
	}*/
	
	//public static SolrRequestInfo setRequestInfo(SolrQueryRequest req,SolrQueryResponse rsp)
	//{
		/*SolrRequestInfo info = new SolrRequestInfo(req,rsp);
		threadRequest.set( info );
		return info;*/
		
		//SolrRequestInfo info = SolrRequestInfo.getRequestInfo();
		
		//if (info==null)
		//{
			/*if (req==null)
			{
				List<SolrCore> cores = new ArrayList<>( Schema.coreContainer.getCores() );
				Collections.shuffle(cores);
				req = new LocalSolrQueryRequest( cores.get(0) , new ModifiableSolrParams() );
			}
			
			if (rsp==null)
				rsp = new SolrQueryResponse();*/
			
			/*info = new SolrRequestInfo(req,rsp);
			SolrRequestInfo.setRequestInfo(info);
		}
		else
		{
			if ( info.getReq() != req || info.getRsp() != rsp )
				throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , "Previous SolrRequestInfo was not closed!" );
		}
		return info;
	}*/
	
	/*public static void setAdminRequest(SolrQueryRequest request)
	{
		synchronized (adminReq) {
			adminReq.set(request);
		} 
	}*/
	
	/*public static DocCollection get DocCollection()
	{
		return getDoc Collection( getRequestInfo().getReq() );
	}*/
	
	/*public static SolrRequestInfo getRequestInfo()
	{
		SolrRequestInfo info = SolrRequestInfo.getRequestInfo(); //threadRequest.get();
		
		if (info == null) 
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "This request must have SolrRequestInfo");
		
		return info;*/
		
		/*if (info!=null)
			return info;
		
		List<SolrCore> cores = new ArrayList<>( Schema.coreContainer.getCores() );
		
		Collections.shuffle(cores);
		
		SolrQueryRequest req = new LocalSolrQueryRequest( cores.get(0) , new ModifiableSolrParams() );
		SolrQueryResponse rsp = new SolrQueryResponse();
		
		return setRequestInfo(req,rsp);*/
			
		/*SolrRequestInfo info = SolrRequestInfo.getRequestInfo();
		
		if (info!=null)
			return info.getReq();
			
		return adminReq.get();*/
	//}
	
	/*public static SolrCore getCore()
	{
		SolrQueryRequest req = getRequest();
		
		if (req==null)
			return null;
			
		return req.getCore();
	}*/
	
	/*public static Slice getSlice()
	{
		String shard = getCore().getCoreDescriptor().getCloudDescriptor().getShardId() ;
		String collName = getCore().getCoreDescriptor().getCollectionName() ;
		
		Slice slice = getCore().getCoreDescriptor().getCoreContainer().getZkController().getClusterState().getSlice( collName ,shard) ;
		return slice;
	}*/
	
	/*public static CompositeIdRouter getRouter()
	{
		return null;
		//return (CompositeIdRouter) getCollection().getRouter();
	}*/
	
	/*public static DocCollection getCollection()
	{
		String collName = getRequest().getCore().getCoreDescriptor().getCollectionName() ;
		
		CoreContainer coreContainer = getRequest().getCore().getCoreDescriptor().getCoreContainer();		
		
		//String collName = req.getParams().get("collection" , Schema. getInstance().get("collection") );
		
		DocCollection collection = coreContainer.getZkController().getClusterState().getCollection(collName);
		
		return collection;
	}*/
	
	@SuppressWarnings("unchecked")
	public static Session get( SolrQueryRequest sreq )
	{
		/*SolrQueryRequest sreq = getRequestInfo().getReq();
		
		if (sreq == null)
			return null;*/
			
		Session session = (Session) sreq.getContext().get("session");
			
		if (session != null)
			return session;
			
		session = getUserSession(sreq);
		
		sreq.getContext().put("session",session);
		
		return session;
	}
	
	public static Session createUserSession(SolrQueryRequest sreq, String ticket)
	{
        String user_pass = ticket.replaceFirst("@user$","");
		String organization = null;
		
		int idx = ticket.indexOf("!");
		if (idx>0)
		{
			organization = ticket.substring(0,idx);
            user_pass = ticket.substring(idx+1);
		}
		
		Schema schema = Schema.get(sreq);

        String userId = user_pass.split(":")[0];

        Session session = new Session(organization,userId);
        session.set("ticket", ticket);

        String origTicket = sreq.getOriginalParams().get("ticket");

        if (origTicket!=null && !origTicket.equals(ticket))
        {
            user_pass = origTicket.replaceFirst("@user$","");

            organization = null;

            idx = origTicket.indexOf("!");
            if (idx>0)
            {
                organization = origTicket.substring(0,idx);
                user_pass = origTicket.substring(idx+1);
            }
            log.info("login done by orig ticket but auth on:{}",ticket);
        }
			
		ILogin loginProvider = schema.getLoginProvider( sreq, organization );

        if (!loginProvider.loginSSO( user_pass ))
            throw new SolrException(SolrException.ErrorCode.FORBIDDEN, "Invalid ticket or challenge");

        //String id = (String) response.get("id");
        if (userId.equals(Session.ROOTID))
        {
            session.set("globalacl", -1);
            return session;
        }
		
		//Session session = new Session( organization , id );
		
		/*String name = (String) response.get("name");
		if (name!=null)
			session.set("name", name);*/
		
		//session.addAll( (Map<String,Object>) response);

        //session.set("ticket", ticket);
		
		IUserRoles rolesProvider = schema.getRolesProvider( sreq, organization );

        Collection<String> roles = rolesProvider.getUserRoles( userId );
        roles.add(userId);
        roles.add("everyone");
		
		Collection<String> hroles = new TreeSet<>(String.CASE_INSENSITIVE_ORDER) ;
		
		for( String role : roles )
		{
            String type = role.equals(userId) ? "user" : "group";
			String acl = role.toLowerCase() + "@" + type;
			
			if (organization != null)
				acl = organization + "!" + acl;
			
			hroles.add( acl );

            /* fix per compatibiltà on utenti inseriti senza encoding */
            if (StringUtils.containsAny(role, FieldUtils.idChars ) )
                hroles.add(FieldUtils.encodeId(role.toLowerCase()) + "@" + type );
		}
		
		session.set("roles" , hroles );

        Map<String,Object> userProps = rolesProvider.getUserProperties( userId );
        session.set("props" , userProps );
		
		if (roles.contains(Session.ROOTROLE))
			session.set("globalacl" , -1);
		/*else
		{
			String acl_clause = ACL_CLAUSE + CoreClient.Query.makeClause( Fields.ACL_READ , (Collection) hroles )  ;
			session.set("acl_clause" , acl_clause);
			
			session.setAclQuery( RealTimeAclQParserPlugin.makeAclQuery( sreq, hroles ) );
		}*/
		
		return session;
	}
	
	@SuppressWarnings("unchecked")
	private static Session getUserSession(SolrQueryRequest sreq)
	{
		/*String ticket;
		
		if (sreq.getContext().get("httpRequest") == null)
			ticket = Session.ROOTTICKET;
		else
			ticket = sreq.getParams().get("ticket");*/
		
		//Session session;
			
		String ticket = sreq.getOriginalParams().get("ticket");

        if (ticket == null)
			return new Session(null,null);

        HttpServletRequest httpreq = (HttpServletRequest) sreq.getContext().get( "httpRequest" );

        String addr = "_"+sreq.getParams().get("remoteAddr");

        if (addr==null)
        {
            if (httpreq != null)
            {
                addr = "_"+httpreq.getRemoteAddr();
                log.trace("call with remoteAddr:{}",addr);
            }
            else
            {
                addr = "_internal"; //System.getProperty("DC."+System.getProperty("location"));
                log.trace("internal call on local host:{}",addr);
            }
        }
        else
        {
            log.trace("call with remoteAddr explicited:{}",addr);
        }

        String sessionKey = ticket + addr;

        //Schema schema = Schema.get(sreq);

        /* PER RETROCOMPATIBILITA' E' POSSIBILE RIATTIVARE IL BUCO DI SICUREZZA
        if ( ticket.equals(Session.ROOTTICKET) && ( schema.rootHole() || httpreq==null ) )
		{
            if (httpreq==null)
                log.info("root authorized because internal request");
            else
                log.info("root authorized because roothole");

			Session session = new Session(null,Session.ROOTID);
			session.set("globalacl", -1);
			session.set("ticket", Session.ROOTTICKET);
			return session;
		}*/

        Session session = (Session) sreq.getSearcher().cacheLookup(SESSION_CACHE, sessionKey );

        Boolean refresh = sreq.getParams().getBool("session.refresh",false);

        if (session==null || session.isExpired() || refresh )
        {
            session = createUserSession(sreq, ticket);

            sreq.getSearcher().cacheInsert(SESSION_CACHE, sessionKey, session );

            log.info("ticket {} authorized and cached on {}", ticket, sessionKey, session);
        }
        else
        {
            log.info("ticket {} found in cache:{}",ticket,sessionKey);
        }

        log.trace("session authorized:{}",session);
		
		return session;		
	}
	
	/*public static Solr Client getClient()
	{
		Solr Client server = threadClient.get();
		if (server==null)
		{
			synchronized(threadClient)
			{
				server = new LocalSolr Client( getCore() );
				//server.setTicket(ROOTTICKET);
				threadClient.set(server);
			}
		}
		return server;
	}*/
	


}
	
