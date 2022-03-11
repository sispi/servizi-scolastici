
package it.kdm.solr.core;

//import org.apache.solr.util;

import com.google.common.base.Strings;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.interfaces.ILogin;
import it.kdm.solr.interfaces.IProvider;
import it.kdm.solr.interfaces.IRepository;
import it.kdm.solr.interfaces.IUserRoles;
import org.apache.lucene.search.BooleanQuery;
import org.apache.solr.cloud.CloudDescriptor;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.CompositeIdRouter;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.cloud.ZkStateReader;
import org.apache.solr.common.util.RetryUtil;
import org.apache.solr.common.util.RetryUtil.RetryCmd;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.zookeeper.data.Stat;
import org.noggit.ObjectBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SuppressWarnings("unchecked")
public class Schema {

    public static final int AclClauseCount = 9999;
    public static final String RESULTS_CACHE = "staticResults";
    public static final String CHANGED_FL = "changedFl";
    private static Logger log = LoggerFactory.getLogger(Schema.class);

	public static final String NOLOG = "__NOLOG__";

    public static final String AUTH_HEADER = "auth_challenge";

    public static final String FALSE = "FALSE";

    public static final String DC_ALIVE = "DC_ALIVE";
    public static final String DC_CLUSTER_IPS = "DC_CLUSTER_IPS";
	
	//public static final String MAX = "MAX";
	
	private static final String base = "abstract";

	public static class Fields {
		
		public static final String[] COMMON_FIELDS = { Fields.ID, Fields._VERSION_, Fields.LOCK_TO, Fields.CONTENT_ID, Fields.MODIFIED_BY, Fields.CREATED_BY,Fields.ACL_EXPLICIT, Fields.ACL_INHERITS, Fields.PARENT , Fields.ACL_PARENT , Fields.ACL_PARENTS } ;
		
		public static final String _VERSION_ = "_version_";
		public static final String SEPARATOR = "!";
		public static final String ID = "id";
        public static final String SCORE = "score";
		public static final String SID = "sid";
		public static final String NAME = "name";
        public static final String UNIQUE_NAME = "__unique_name__";
		public static final String SEQUENCE = "sequence";
        public static final String DISPLAY_NAME = "display_name";
		
		public static final String DIVISION = "division";
		public static final String LOCATION = "location";
		public static final String TYPE = "type";
		public static final String VIEWS = "views";
        public static final String ENABLED = "enabled";

        /* il campo HIDDEN non è l'opposto di ENABLED ma serve per dichiarare "nascosto" il padre cancellato ed i suoi figli */
        public static final String HIDDEN = "hidden";

        public static final String PARENT = "parent";
		//public static final String ANCESTORS = "ancestors";
		public static final String CREATED_ON = "created_on";
		public static final String CREATED_BY = "created_by";
        public static final String CREATED_DN_BY = "created_dn_by" ;
        public static final String MODIFIED_ON = "modified_on";
        public static final String MODIFIED_BY = "modified_by";
        public static final String MODIFIED_DN_BY = "modified_dn_by" ;
		public static final String INDEXED_ON = "indexed_on";
		//public static final String BUILT_ON = "built_on";
		public static final String ERROR = "error";
		public static final String STATE = "state";
		public static final String ACL_EXPLICIT = "acl_explicit";
		public static final String ACL_READ = "acl_read";
		public static final String ACL_INHERITS = "acl_inherits";
		public static final String ACL_PARENT = "acl_parent";
		public static final String ACL_PARENTS = "acl_parents";
		public static final String ACL_SEQUENCE = "acl_sequence";
		public static final String ACL_SEQUENCES = "acl_sequences";
		
		
		public static final String SHARD = "__shard__";
		
		
		public static final String LOCK_TO = "lock_to" ;
		//public static final String LOCK_FROM = "lock_from" ;
		//public static final String LOCK_BY = "lock_by" ;
		
		public static final String CONTENT_ID = "content_id" ;
		//public static final String CONTENT_CACHE = "content_cache";
		public static final String CONTENT_VERSIONS = "content_versions";
		public static final String CONTENT_SIZE = "content_size";
        public static final String CONTENT_COMMENT = "content_comment";
        public static final String CONTENT_TYPE = "content_type";
        public static final String CONTENT_MODIFIED_BY = "content_modified_by";
        public static final String CONTENT_MODIFIED_DN_BY = "content_modified_dn_by";
		public static final String CONTENT_MODIFIED_ON = "content_modified_on";
		public static final String FULLTEXT = "fulltext";
        public static final String TEXT = "text";
		public static final String FULLTEXT_SIZE = "fulltext_size";
		public static final String FULLTEXT_ON = "fulltext_on";

        public static final String EDIT_LINK = "edit_link";

        //public static final String CONTENT_NAME = "content_name";
		
		//public static final String _CHILDREN_ = "[children]";
		//public static final String _PARENTIDS_ = "[parentIds]";
		//public static final String _REQUESTED_STATE_ = "[requested_state]";
		public static final String _OLD_NAME_ = "[oldname]";
		public static final String _OLD_PARENT_ = "[oldparent]";
		//public static final String _OLD_ACL_PARENT_ = "[old_acl_parent]";
		public static final String _OPERATION_ = "[operation]";
		
		//public static final String _DUPLICATES_ = "[duplicates]";
		
		public static final String _STREAM_FILE_ = "["+org.apache.solr.common.params.CommonParams.STREAM_FILE+"]";
		public static final String _STREAM_URL_ = "["+org.apache.solr.common.params.CommonParams.STREAM_URL+"]";
		//public static final String _STREAM_ = "[stream]";
		//public static final String _SHARD_NAME_ = "[shardname]";
		
		
		public static final String UNCOMMITTED = "uncommitted";
		public static final String CACHEENTRY = "__cacheEntry__";
		
		
		/*static String rf;
		public static String ROUTE()
		{
			return (String) config.get("routeField");
		}*/
		
		//public static       String ROUTE; 
		
		
		//public static final String EMPTYSLICER = "@";
		//public static String SLICER = "slicer" ;
	}
	
	public static class Operations {
	
		public static final String UPDATE = "update";
		public static final String CREATE = "create";
		public static final String SYNC = "sync";
	}
	
	public static class Params {
	
		public static final String OPERATION = "operation";
		//public static final String COMMITANY = "commitAny";
		//public static final String DEBUGQUEUE = "debugQueue";
		//public static final String SHARD = "shard";
		public static final String TICKET = "ticket";
		public static final String SHORTCUT = "shortcut";
        public static final String PROUTE = "proute";
		
		
	}
	
	public static class Rights {
	
		public static final int retrieve=1,
								search=1,
								readContent=2,
								retrieveACL=4,
								readVersion=8,
								listVersions=16,
								createChildren=32,
								update=64,
								lock=128,
								writeContent=256,
								createVersion=512,
								rename=1024,
								move=2048,
								unlock=4096,
								delete=8192,
								updateACL=16384 ,
								sync=32768,
								readonlymask = 31;
								
	}
	
	public static class docFlags {
        public static final String 	PROFILE = "profile" , 
									CONTENT = "content" , 
									SYNCED = "synced" ,
									/*SET_CLEAN = "force_clean" ,*/
									CLEAN = "clean" , 
									XHTML = "xhtml" ,
                                    SHARED = "shared",
									
									SERVER_ERROR = "server_error" ,
									NOTEXISTS = "notexists" , 
									/*NOTCIRCULAR = "notcircular" ,*/
									/*BUILD = "build",*/
									CACHE = "cache" ;
									 
    }
	
	private String collectionName;
    private final Map<String,Map<String,Object> > types  = new HashMap<>();
	private final Map<String,Map<String,Object> > providers  = new HashMap<>();
	private final Map<String,Object> config = new HashMap<>();
	
	//boolean initialized = false;
	//CloudSolrServer server;
	
	//static CoreContainer coreContainer = null;
	//static String defaultCollection = null;


	private static  final Map<String,Schema > schemas  = new HashMap<>();
	
	private Schema( String collectionName )
	{
		this.collectionName = collectionName;
	}

    public String getCollection()
    {
        return collectionName;
    }
	
	/*public static Schema get( String collName )
	{
		return schemas.get(collName);
	}*/
	
	public static Schema get( SolrQueryRequest req )
	{
		String collName = (String) req.getContext().get("collection");
		
		if (collName==null)
		{
			//ZkController zkController = req.getCore().getCoreDescriptor().getCoreContainer().getZkController();
			CloudDescriptor cloudDescriptor = req.getCore().getCoreDescriptor().getCloudDescriptor();
			collName = cloudDescriptor.getCollectionName();
            req.getContext().put("collection",collName);
		}
		
		Schema instance = schemas.get(collName);
		
		if (instance==null)
		{
			ZkStateReader zkStateReader = req.getCore().getCoreContainer().getZkController().getZkStateReader() ;
			instance = new Schema( collName );
			instance.load( zkStateReader );
			
			log.info( "schema reloaded for collection {}" , collName ); 
			
			schemas.put( collName, instance );
		}
		
		return instance;
	}
		
	/*public static void init( ZkStateReader zkStateReader )
	{
		Set<String> collNames = zkStateReader.getClusterState().getCollections();
		
		for( String collName : collNames )
		{
			Schema instance = new Schema( collName );
			
			instance.load( zkStateReader );
			
			schemas.put( collName, instance );
		}
	}*/

    public static void unload( String collection )
    {
        if (collection!=null)
        {
            synchronized (schemas)
            {
                schemas.remove(collection);
            }
        }

        log.info("Schema '{}' unloaded.",collection);
    }

    static void unloadAll()
    {
        if (schemas.size() == 0)
        {
            log.warn("Schemas are empty");
            return;
        }


        synchronized (schemas)
        {
            schemas.clear();
        }

        log.info("All schemas unloaded.");
    }
	
	private void load( ZkStateReader zkStateReader )
	{
		//server = initserver;
		//this.collectionName = collectionName;
		
		//DocCollection collection = server.get DocCollection(collectionName);
		SolrZkClient zkClient = zkStateReader.getZkClient() ;
		DocCollection collection = zkStateReader.getClusterState().getCollection(collectionName);
		
		try	
		{
			//initialized = false;
			types.clear();
			config.clear();
			providers.clear();
			
			Set<String> ignorePatterns = new java.util.LinkedHashSet();
			ignorePatterns.add(".*"+NOLOG+".*");
			ignorePatterns.add(".*org.apache.tika.exception.TikaException.*");
			ignorePatterns.add(".*Multiple\\srequestHandler\\sregistered.*");
			
			org.apache.solr.common.SolrException.ignorePatterns = ignorePatterns;

            Stat stat = new Stat();

            String jsonString = loadJSONSchema(zkClient, collectionName, stat);

            config.put("version",stat.getVersion());

			jsonString = jsonString.replaceAll( "/\\*[^*]+\\*/" , "" );
			
			Map<String,Object> jschema = (Map<String,Object>) ObjectBuilder.fromJSON(jsonString);
						
			for ( String key : jschema.keySet() )
			{
				if (key.equals( "types" ))
				{
					List jtypes = (ArrayList) jschema.get(key);
					
					for ( Object o : jtypes )
					{
						Map<String,Object> type = (Map<String,Object>) o;
						
						String typeName = (String) type.get("name");
						
						if (type.get("inherits") == null && !base.equals(typeName) )
							type.put("inherits" , base );
							
						Map <String,Object> profiles = (Map<String,Object>) type.get("profiles");
						
						if (profiles != null)
						{	
							Map <String,Object> newprofiles = new java.util.TreeMap<>(String.CASE_INSENSITIVE_ORDER);
							
							for ( Map.Entry<String,Object> entry : profiles.entrySet() )
								newprofiles.put( entry.getKey() , entry.getValue() );
								
							type.put("profiles" , newprofiles );
						}
							
						types.put( typeName, type );
					}
				}
				if (key.equals("providers") )
				{
					List jproviders = (ArrayList) jschema.get(key);
					
					for ( Object o : jproviders )
					{
						Map<String,Object> provider = (Map<String,Object>) o;
						
						String name = (String) provider.get("name");
						
						providers.put( name , provider );			
					}
				}
				else
					config.put(key , jschema.get(key) );
					
				Map routerProps = (Map) collection.get("router");
                String routeField = (String) routerProps.get("field");
				//config.put("routeField" , routerProps.get("field") );
			}

			if (config.containsKey("maxClauseCount"))
			{
				Integer maxClauseCount = ((Number) config.get("maxClauseCount")).intValue();
				BooleanQuery.setMaxClauseCount(maxClauseCount);
				log.warn("maxClauseCount set to {}" , maxClauseCount);
			}
			else
			{
				log.warn("no maxClauseCount" );
			}
			
			//initialized = true;
		}
		catch( Throwable e )
		{
			log.error("Error initializing schema" , e);
			throw new RuntimeException(e);
		}
	}
	
	/*public String getRouteField()
	{
		if (routeField == null)
		{
			DocCollection collection = CoreClient.getInstance().getZkStateReader().getClusterState().getCollection(collectionName);
			
			Map routerProps = (Map) collection.get("router"); 
			
			routeField = (String) routerProps.get("field");
		}
		
		if (routeField == null)
			throw new SolrException( SolrException.ErrorCode.SERVER_ERROR, "route field is not configured");
		
		return routeField;
	}*/
	
	public static CompositeIdRouter getRouter()
	{
		return new CompositeIdRouter();
	}
	
	/*public String getSeps()
	{
		String division = (String) config.get("division");
		
		if (division == null)
			division = "!!";
		
		return division;
	}*/
	
	
	/*public String getRoute(String id , boolean trimLast)
	{
		char[] seps = getSeps().toCharArray();
		
		int pos = 0;
		int i = 0;
		for ( i = 0; i< seps.length && pos != -1 ; i++)
			pos = id.indexOf(seps[i],pos+1 );
		
		if (pos==-1)
			pos = id.indexOf("@");
		
		String route =  id.substring(0,pos) + seps[i-1];
		
		if (!trimLast)
			route += id.substring(pos+1);
		
		return route;
	}*/
	
	/*public String get Route(String id)
	{
		if (id==null || id.indexOf("/ 8!") != -1)
			return id;
		
		String type = "node";
		if (id.indexOf("@") != -1)
			type = id.split ("@")[1];
		String regex = getRegex(type);
		
		String route = id.replaceAll( regex, "$1/ 8,$2,$3" );
		
		return route.replace("!",".").replaceAll(",+","!");
	}*/
	
	private Map<String,Object> getTypeConfig( String type )
	{
		type = FieldUtils.decode(type);
		
		Map<String,Object> typeConfig = types.get(type);
		
		/*if (!type.equals(Fields.LOCATION))
			typeConfig = types.get(type);
		else
			typeConfig = Collections.singletonMap("inherits" , (Object) base);*/
		
		if (typeConfig==null)
			return types.get("*");
		else
			return typeConfig;
	}
	
	/*public String getLocation()
	{
		return (String) config.get("location");
	}*/

    private static byte[] key = {
            0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    };//"thisIsASecretKey";

    public String getSecret()
    {
        String secret = (String) config.get("secret");

        if (secret==null)
            return new String(key);
        else
            return secret;
    }

    public Object getParam( String param )
    {
        return config.get(param);
    }
	
	public String getXHTMLCache()
	{
		String xhtmlcache = (String) config.get("xhtmlcache");
		
		if (xhtmlcache==null)
			return "../xhtmlcache";
		else
			return xhtmlcache;
	}

    public String getFileCache()
    {
        return (String) config.get("filecache");
    }

    public int getVersion()
    {
        return (int) config.get("version");
    }

    public String getDefaultLocation()
    {
        return System.getProperty( Fields.LOCATION ) ;
    }

    public String getDivision(String id)
    {
        if (Strings.isNullOrEmpty(id) || !id.contains("@"))
            return "";

        Object tmpl = getValue(id.split("@")[1],"routes");

        if (tmpl==null)
            return "";

        Collection<Object> defs;

        if (!(tmpl instanceof Collection))
            defs = Collections.singletonList(tmpl);
        else
            defs = (Collection<Object>) tmpl;

        for( Object t : defs )
        {
            List<String> parts = StrUtils.splitSmart(t.toString(), '/');

            if (parts.size()==3 && id.matches(parts.get(1)))
            {
                id = id.replaceAll(parts.get(1),parts.get(2));

                return id;
            }
        }

        return "";


/*
        String[] parts = id.split("!");

        //se contiene "!" si prendono solo i primi due livelli coinvolti nel routing

        if (parts.length==1)
            id = parts[0];
        else
            id = parts[0]+"!"+parts[1];

        if (id.indexOf(".")==-1) //se non contiene il "." la division finisce per punto per garantire la location
            return id + ".";
        else
            return id + "!";*/
    }
	
	public int getMaxQueueSize()
	{
		Integer maxqueuesize = ((Number) config.get("maxqueuesize")).intValue();
		
		if (maxqueuesize==null)
			return 20;
		else
			return maxqueuesize;
	}

    /*public Collection<String> getRTignoredFields()
    {
        Collection<String> fields = (Collection<String>) config.get("rtignoredfield");

        if (fields==null)
            return Arrays.asList(Fields.FULLTEXT);
        else
            return fields;
    }

    public Collection<String> getRTindexFields()
    {
        Collection<String> fields = (Collection<String>) config.get("rtindexfield");

        if (fields==null)
            return Arrays.asList(new String[] {});
        else
            return fields;
    }*/
	
	/*public static Map<String,Object> getHashers()
	{
		return (Map<String,Object>) config.get("hashers");
	}*/
	
	/*public String getRegex( String type )
	{
		return (String) getValue( type , "regex" );
	}*/
	
	/*public static String get Division( String type )
	{
		return (String) getValue( type , "division" );
	}
	
	public static String get Route( String type )
	{
		return (String) getValue( type , "route" );
	}*/
	
	/*public static int getSlicing( String type )
	{
		Number d = (Number) getValue(type, "slicing" );
		if (d!=null)
			return d.intValue();
		else
			return -1;
	}*/
	
	public int getProfileRight( String type, String profile )
	{
		Number obj = (Number) getMapValue(type,"profiles",profile );
		if (obj==null)
			return 0;
		else
			return obj.intValue();
	}
	
	public Set<String> getProfiles( String type )
	{
		return getMapKeys( type , "profiles" );
	}
	
	public Set<String> getValidations( String type )
	{
		return getMapKeys( type , "validation" );
	}
	
	public String getValidation( String type, String alias )
	{
		return (String) getMapValue( type , "validation" , alias );
	}
	
	public Set<String> getAliases( String type )
	{
		return getMapKeys( type , "aliases" );
	}

	public Object getAlias( String type, String alias )
	{
		return getMapValue( type , "aliases" , alias );
		
		/*if (aliasTo != null && aliasTo.indexOf("{type}") != -1 )
			aliasTo = aliasTo.replace("{type}" , type );
		
		return aliasTo;*/
	}
	
	public Set<String> getDefaults( String type , String operation )
	{
		Set<String> opDefs = getMapKeys( type , "on_"+operation );
		Set<String> hooks = getMapKeys( type , "hooks" );
		
		HashSet<String> defs = new LinkedHashSet<>();
		defs.addAll(hooks);
		defs.addAll(opDefs);
		
		return defs;
	}
	
	public Object getDefault( String type , String def , String operation )
	{
		Object opDefs = getMapValue( type , "on_"+operation , def );
		//Object hooks = getMapValue( type , "hooks" , def );

		/*if (opDefs != null && hooks != null){
			log.warn("hook for field {} ignored because already defined as 'on_{}' for type {}",def,operation,type);
			hooks = null;
		}*/
		
		List<Object> defs = new ArrayList<>();
		
		if (opDefs instanceof Collection)
			defs.addAll( (Collection) opDefs );
		else if (opDefs != null)
			defs.add(opDefs);
		
		/*if (hooks instanceof Collection)
			defs.addAll( (Collection) hooks );
		else if (hooks != null)
			defs.add(hooks);*/
		
		return defs;
	}

	public Set<String> getAfterDefaults( String type )
	{
		return getMapKeys( type , "after_defaults" );
	}

	public Object getAfterDefault( String type , String def )
	{
		Object opDefs = getMapValue( type , "after_defaults" , def );

		List<Object> defs = new ArrayList<>();

		if (opDefs instanceof Collection)
			defs.addAll( (Collection) opDefs );
		else if (opDefs != null)
			defs.add(opDefs);

		return defs;
	}
	
	/*public static List<String> getParents( String type )
	{
		return (List<String>) getValue(type,"parents"); 
	}*/
	
	public Integer getCreateRight( String parentType , String type )
	{
		/*if (type.equals(base))
			return null;
		
		if (parentType==null && !type.equals(Fields.LOCATION) )
			return null;
		
		if (parentType==null || parentType.equals(Fields.LOCATION))
			return -1;*/
		
		Map<String,Object> typeConfig = getTypeConfig(type);
		
		while(typeConfig != null)
		{
			Number d = (Number) getMapValue( parentType , "children" , type );
		
			if (d!=null)
				return d.intValue();
				
			String inherits = (String) typeConfig.get( "inherits" );
			
			if (inherits==null)
				break;
				
			typeConfig = getTypeConfig(inherits);
			type = inherits;
		}
		return null;
	}

	public String getProcessAdd( String type )
	{
		return (String) getValue( type , "processAdd" );
	}
	
	public boolean canHaveChildren( String type )
	{
		return (getMapKeys(type,"children").size() > 0);
	}

    public boolean isStream( String type )
	{
		Boolean isStream = (Boolean) getValue( type, "stream" );
        return isStream != null && isStream;
	}
	
	public String guessChildType( String parenttype , boolean stream )
	{
		//if (parenttype == null)
		//	return Fields.LOCATION; // (String) getValue( "node", "defaultRoot" );
	
		java.util.Collection<String> children = getMapKeys(parenttype,"children");
		java.util.List<String> candidates = new ArrayList<>();
		
		for( String childtype : children )
		{
			if (isStream(childtype) == stream )
				candidates.add(childtype);
		}
		
		if ( candidates.size() == 1)
			return candidates.get(0);
		
		String defaultChild = (String) ( stream ? getValue( parenttype, "defaultStream" ) : getValue( parenttype, "defaultNode" ) );
		
		if (candidates.contains(defaultChild))
			return defaultChild;
		else
			return null;
	}
	
	/*public String getSidField( String type )
	{
		Object v = getValue( type, "sid" );
		
		return (v == null) ? null : v.toString();
	}*/
	
	/*public String getAlias( String type )
	{
		Object v = getValue( type, "alias" );
		
		return (v == null) ? null : v.toString();
	}*/
	
	/*public String getRegistry( String type )
	{
		Object v = getValue( type, "registry" );
		
		return "registry_" + ( (v == null) ? type : v.toString() );
	}*/
	
	public List<String> getViews( String type )
	{
		List<String> list = new ArrayList<>();
		
		while(type != null)
		{
			list.add(type);
			
			Map<String,Object> typeConfig = getTypeConfig(type);
		
			type = (String) typeConfig.get( "inherits" );
		}
		
		return list;
	}
	
	private Object getMapValue( String type , String mapName, String param )
	{
		Map<String,Object> typeConfig = getTypeConfig(type);
		
		while(typeConfig != null)
		{
			Map<String,Object> map = (Map<String,Object>) typeConfig.get(mapName) ;

			if ("on_create".equals(mapName) || "on_update".equals(mapName)){

				Map<String,Object> hmap = (Map<String,Object>) typeConfig.get("hooks") ;

				if (hmap!=null && hmap.containsKey( param )) {
					if (map!=null && map.containsKey( param )){
							/* vince quello specifico */
						log.warn("ambiguous configuration for type:{} map:{} param:{}",type,mapName,param);
						return map.get(param);
					} else {
						return hmap.get(param);
					}
				}
			}

			if (map != null){

				if (map.containsKey( param ) )
					return map.get(param);
			}

			String inherits = (String) typeConfig.get( "inherits" );
			
			if (inherits==null)
				break;
				
			typeConfig = getTypeConfig(inherits);
		}
		return null;
	}
	
	private Set<String> getMapKeys( String type , String mapName )
	{
		HashSet<String> list = new LinkedHashSet<>();
		
		Map<String,Object> typeConfig = getTypeConfig(type);
		
		while(typeConfig != null)
		{
			Map<String,Object> map = (Map<String,Object>) typeConfig.get(mapName) ;
			
			if ( map != null)
			{
				for ( String key : map.keySet() )
					list.add(key);
			}
				
			String inherits = (String) typeConfig.get( "inherits" );
			
			if (inherits==null)
				break;
				
			typeConfig = getTypeConfig(inherits);
		}
		
		return list;
	}
	
	private Object getValue( String type , String param )
	{
		Map<String,Object> typeConfig = getTypeConfig(type);
		
		Object val = typeConfig.get( param );
		
		if (val != null)
			return val;
			
		String inherits = (String) typeConfig.get( "inherits" );
		
		if (inherits==null)
			return null;
		
		return getValue( inherits , param );
	}
	
	private IProvider getProvider( SolrQueryRequest req, String name )
	{
		try
		{
			Map<String,Object> config = providers.get( name );
			
			if (config==null)
			{
				throw new SolrException( SolrException.ErrorCode.SERVER_ERROR,"provider not found in config: " + name);
			}
			
			String alias = (String) config.get("alias");
			
			if (alias!=null)
			{
				config = providers.get( alias  );
				
				if (config==null)
				{
					throw new SolrException( SolrException.ErrorCode.SERVER_ERROR,"provider not found in config: " + name);
				}
				
				config = new HashMap<>( config );
				config.put("name" , name );
				config.remove("alias");
				
				providers.put(name , config);
			}
			
			String className = (String) config.get("class");
			
			if (className.indexOf("kdm.") == 0)
				className =  className.replace("kdm.", "it.kdm.solr.providers." );
				
			Class cls = Class.forName( className );
				
			IProvider provider = (IProvider) cls.newInstance() ;
			
			provider.setConfig( config );
			provider.setRequest( req );
			
			return provider;
		}
		catch( Exception e )
		{
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public IRepository getRepository( SolrQueryRequest req, String content_id)
	{
		String name = (String) config.get( "repository" );
		if (content_id!=null)
		{
			int idx = content_id.indexOf( "://" );
			
			if (idx != -1)
				name = content_id.substring(0,idx);
		}
		
		return (IRepository) getProvider( req, name );
	}
	
	@SuppressWarnings("unchecked")
	public ILogin getLoginProvider( SolrQueryRequest req, String organization )
	{
		String name = null; //(String) config.get( "login" );
		
		if (organization!=null)
		{
			for( Map<String,Object> config : providers.values() )
			{
				String provName = (String) config.get("name");
				
				List orgs = (ArrayList) config.get("organizations");
				
				if (orgs==null)	
					continue;
				
				IProvider provInstance = getProvider( req, provName );
				
				if (! (provInstance instanceof ILogin ) )
					continue;
				
				for ( Object org : orgs )
				{
					if (org.equals(organization))
					{
						name = provName;
						break;
					}
				}
			}
			
			if (name==null)
				throw new SolrException( SolrException.ErrorCode.BAD_REQUEST, "invalid organization: "+organization);
		}
		else
		{
			name = (String) config.get( "login" );
		}
		
		return (ILogin) getProvider( req, name );
	}	
	
	@SuppressWarnings("unchecked")
	public IUserRoles getRolesProvider( SolrQueryRequest req, String organization )
	{
		String name = null; //(String) config.get( "userroles" );
		
		if (organization != null)
		{
			for( Map<String,Object> config : providers.values() )
			{
				String provName = (String) config.get("name");
				
				List orgs = (ArrayList) config.get("organizations");
				
				if (orgs==null)
					continue;
				
				IProvider provInstance = getProvider( req, provName );
				
				if (! (provInstance instanceof IUserRoles) )
					continue;
				
				for ( Object org : orgs )
				{
					if (org.equals(organization))
					{
						name = provName;
						break;
					}
				}
			}
			
			if (name==null)
				throw new SolrException( SolrException.ErrorCode.BAD_REQUEST, "invalid organization: "+organization);
		}
		else
		{
			name = (String) config.get( "userroles" );
		}
		
		return (IUserRoles) getProvider( req, name );
	}

    public static String loadJSONFile( final SolrZkClient zkClient , final String path, final Stat stat )
    {
        try
        {
            final StringWriter output = new StringWriter();

            RetryUtil.retryOnThrowable(SolrException.class, 4000, 1000,
                    new RetryCmd() {

                        @Override
                        public void execute() throws Throwable {


                            try
                            {

                                byte[] data = zkClient.getData(path, null, null, false);

                                output.write( new String(data, StandardCharsets.UTF_8) );
                            }
                            catch (Exception e)
                            {
                                throw new SolrException(SolrException.ErrorCode.CONFLICT, "Could not load json: " + path , e ) ;
                            }
                        }
                    });

            //String jsonString = output.toString();
            return output.toString();
        }
        catch( Throwable e )
        {
            throw new RuntimeException(e);
        }
    }

    private static String loadJSONSchema(final SolrZkClient zkClient, final String collection, final Stat stat)
    {
        String fileName = java.lang.System.getProperty("schema", "local.json");
        String path = "/configs/" + collection + "/" + fileName;

        return loadJSONFile(zkClient,path,stat);
    }
	
}

	/*public static Schema getInstance()
	{
		String defaultCollection = Solr Client.getInstance().defaultCollection;
		
		return schemas.get(defaultCollection);
	}*/
	
	
	/*public static void initCustomRouters()
	{
		try
		{
			Class drc = Class.forName("org.apache.solr.common.cloud.DocRouter");
			java.lang.reflect.Field field = drc.getDeclaredField("routerMap");

			field.setAccessible(true);
			Map routerMap = (Map) field.get(null);

			routerMap.put( "custom" , new CustomRouter() );
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}*/
	
	
	/*public static void init( CloudSolrServer server )
	{
		String collectionName = server.getDefaultCollection();
		DocCollection collection = server.getZkStateReader().getClusterState().getCollection(collectionName);
		SolrZkClient zkClient = server.getZkStateReader().getZkClient() ;
		
		init(zkClient,collection);
	}*/
	
	/*public static void init( CoreContainer coreContainer )
	{
		Session.coreContainer = coreContainer;
		
		SolrZkClient zkClient = coreContainer.getZkController().getZkClient();
		
		String collName = coreContainer.getCores().iterator().next().getCoreDescriptor().getCollectionName() ;
		
		//CoreContainer coreContainer = core.getCoreDescriptor().getCoreContainer();		
		
		DocCollection collection = coreContainer.getZkController().getClusterState().getCollection(collName);
		
		init(zkClient,collection);
	}*/
	
	//private static void init( SolrZkClient zkClient , DocCollection collection )
	//{