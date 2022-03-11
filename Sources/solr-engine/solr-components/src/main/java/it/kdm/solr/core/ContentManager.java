
package it.kdm.solr.core;

//import org.apache.solr.util;

import com.google.common.base.Strings;
import it.kdm.solr.client.CoreClient;
import it.kdm.solr.client.SolrClient;
import it.kdm.solr.client.zkClient;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.common.FieldSubstitutor;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.common.QueueUtils;
import it.kdm.solr.components.ContentUpdateRequestHandler;
import it.kdm.solr.core.Schema.*;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.SimpleOrderedMap;
import org.apache.solr.common.util.StrUtils;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.response.SolrQueryResponse;
import org.apache.solr.schema.IndexSchema;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.update.AddUpdateCommand;
import org.apache.solr.update.DeleteUpdateCommand;
import org.apache.solr.update.UpdateCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.solr.handler.UpdateRequestHandler;

public class ContentManager {

    public static final int RESTORE_TOLERANCE = 60;
    private static Logger log = LoggerFactory.getLogger(ContentManager.class);

    //public static SolrInputDocument lastDoc = null;

    protected final Collection<String> forbiddenFields = Arrays.asList( Fields.TYPE, Fields.VIEWS, Fields.SID, Fields.LOCATION ) ;

	//SimpleDateFormat sdf = new SimpleDateFormat(FieldUtils.isoDateFormat);

    protected SolrInputDocument doc = null;
    protected SolrQueryRequest req = null;

    protected String id = null;

    public String getId()
    {
        return id;
    }

    protected String type = null;
    protected Long sequence = null;
    protected String operation = null;

    protected SolrDocument oldDoc = null;
    protected Schema schema = null;
    protected Session session = null;
    protected Collection<Object> requestedState;
    protected ProviderProxy proxy;
	//String origShard = null;
    protected ContentStream contentStream = null;
    protected FieldSubstitutor substitutor = null;
    protected Collection<String> changedFields = null;
    protected Collection<String> nullFields = null;

    protected Integer newVersion = null;

    protected UpdateCommand cmd = null;

	public ContentManager( UpdateCommand cmd )
    {
		this.cmd = cmd;
		
		this.req = cmd.getReq();
		this.schema = Schema.get(req);
		this.session = Session.get(req);
		
		if ( cmd instanceof AddUpdateCommand )
		{
			this.doc = ((AddUpdateCommand)cmd).getSolrInputDocument();
			this.proxy = new ProviderProxy(doc,req);
		}
		
		if (cmd instanceof DeleteUpdateCommand )
		{
			throw new java.lang.UnsupportedOperationException(); 
		}
	}
	
	public SolrInputDocument getUpdatedDocument()
	{
		return this.doc;
	}
	


    protected void initAdd()
	{
        //if ( doc.getFieldValues( Fields._REQUESTED _STATE_ )==null )
        //	this.doc.setField( Fields._REQUESTED _STATE_ , new ArrayList<Object>() );

		/*final ContentManager cm = this;

		StrLookup lookup = new StrLookup() {

			@Override
			public String lookup(String key)
			{
                log.debug("key:{}",key);

				boolean encode = false;
				boolean decode = false;
				boolean isId = false;
				int hash = 0;

				if (key.charAt(0) == FieldUtils.encFsChar)
				{
					encode=true;
					isId = false;
					key = key.substring(1,key.length());
				}

				if (key.charAt(0) == FieldUtils.encIdChar)
				{
					encode=true;
					isId = true;
					key = key.substring(1,key.length());
				}

				if (key.charAt(0) == '~')
				{
					hash = cm.sequence.hashCode();
					key = key.substring(1,key.length());
				}

				if (key.charAt(key.length() -1) == FieldUtils.encPerc)
				{
					decode=true;
					key = key.substring(0,key.length()-1);
				}

                log.trace("key:{} encode:{} decode:{} isId:{} hash:{}", key, encode, decode, isId, hash);

				key = substitutor.replace(key);

                log.trace("after substitutor:{}", key);

				String def = "__NULL__";
				String regex = null;
				String replace = null;

				int idx = key.indexOf("/");

				if (idx != -1)
				{
                    // la forma attesa è <key>/<regex>/<replace>[:def]

                    // cerco la fine del blocco /...../ saltando gli slash escapati
					int idx2 = key.indexOf("/",idx+1);
					if (idx2 > 0)
					{
						for(int i=idx2;i<key.length();i++)
						{
							idx2 = -1;
							if (key.charAt(i)=='/' && key.charAt(i-1)!='\\' )
							{
								idx2 = i;
								break;
							}
						}
					}

					if (idx2>0)
					{
						regex = key.substring(idx+1,idx2);
						replace = key.substring(idx2+1);
						key = key.substring(0,idx);
					}
                    else
                    {
                        throw new RuntimeException("invalid syntax:"+key);
                    }

                    idx = replace.indexOf(":");

                    if (idx != -1)
                    {
                        def = replace.substring(idx+1);
                        replace = replace.substring(0,idx);
                    }
				}
				else
				{
                    // <key[:def>
					idx = key.indexOf(":");

					if (idx != -1)
					{
						def = key.substring(idx+1);
						key = key.substring(0,idx);
					}
				}

                Collection<Object> vals = ContentManager.this.doc.getFieldValues(key);

                log.trace("substiturion key:{} regex:{} replace:{} def:{} vals:",key,regex,replace,def,vals);

                if (vals==null || vals.size()==0)
					return def;

				String ret = "";

				for( Object val : vals )
				{
					if (val!=null)
					{
						if (val instanceof java.util.Date)
							val = FieldUtils.formatDate((java.util.Date) val);

						if (regex!=null)
						{
							if (replace.startsWith("?"))
								val = val.toString().replaceAll( regex, replace.substring(1) );
							else if (val.toString().matches(regex))
								val = val.toString().replaceAll( regex, replace );
							else
								val = "";
						}
						ret += val;
					}
				}

                if (encode && !ret.equals(""))
					ret = FieldUtils.encode(ret.toString(), hash, isId);

				if (decode)
					ret = FieldUtils.decode(ret.toString());

                log.trace("ret:{} def:{}",ret,def);

				if (ret.equals(""))
					return def;
				else
					return ret;
			}
		};

		this.substitutor = new StrSubstitutor(lookup, "{", "}", '\\' );
		this.substitutor.setEnableSubstitutionInVariables(true);*/

        this.operation = (String) doc.getFieldValue( Fields._OPERATION_ );

        this.substitutor = new FieldSubstitutor(this.doc);

        if (operation==null)
            this.operation = req.getParams().get( Params.OPERATION );

        ensureInputDoc();

        if (!doc.containsKey(Fields.ID) && !Strings.isNullOrEmpty(req.getParams().get( Fields.ID )) )
            doc.setField(Fields.ID,req.getParams().get( Fields.ID ));

        if (!doc.containsKey(Fields.TYPE) && !Strings.isNullOrEmpty(req.getParams().get( Fields.TYPE )))
            doc.setField(Fields.TYPE,req.getParams().get( Fields.TYPE ));

        this.id =  (String) doc.getFieldValue( Fields.ID );
		this.type = (String) doc.getFieldValue(Fields.TYPE);

        applyDefault( Fields.LOCATION );

        if ( !doc.containsKey(Fields.LOCATION) )
            doc.setField( Fields.LOCATION , schema.getDefaultLocation() );

        if (Strings.isNullOrEmpty(this.operation))
        {
            /* lo prova a calcolare senza sequence */
            this.id = (String) applyDefault(Fields.ID);

            if (Strings.isNullOrEmpty(this.id) || ContentCacheManager.getInstance().getCacheEntry(req,id) == null)
                this.operation = Operations.CREATE;
            else
                this.operation = Operations.UPDATE;
        }

        boolean hasStream = (this.contentStream!=null);

        log.debug("id:{} type:{} hasStream:{}",id,type,hasStream);
		
		if (this.type==null)
		{
			if ( this.id != null)
			{
				this.type = FieldUtils.decode(id.split("@")[1]) ;
                log.trace("id:{} type:{}",id,type);
			}
			else if (Operations.CREATE.equals(this.operation))
			{
				String parentId = (String) doc.getFieldValue(Fields.PARENT); 
				
				if (parentId != null)
				{
					String parenttype = parentId.split("@")[1];
					this.type = schema.guessChildType( parenttype , hasStream );
				}
                log.trace("parentId:{} type:{}",id,type);
			}
			
			if ( this.type==null )
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, Schema.NOLOG+ "id or type is mandatory");
			
			doc.setField(Fields.TYPE, this.type ) ;
		}

        if (!schema.isStream(type) )
        {
            hasStream = false;
            this.contentStream = null;
        }


        applyDefault( Fields.PARENT );

        if ( Operations.CREATE.equals(this.operation) )
            checkSequence();

        if (this.id==null)
		{
            //applyDefault(Fields.DIVISION);
            this.id = (String) applyDefault(Fields.ID);

            log.debug("id was null:{}",id);

            if (this.id==null)
                throw new SolrException( SolrException.ErrorCode.BAD_REQUEST , "id must be provided or defaulted" );
		}

        /* l'id deve avere la forma {division}{sid}@{type} oppure {division}@{type} quando il SID è l'ultima parte della division */

        /* la division è la parte di id usata dal compositeId routing */
        doc.setField(Fields.DIVISION, schema.getDivision(id));

        //applyDefault( Fields.DIVISION );

        /* il SID è un identificatore che deve essere univoco almeno nel dominio del type e della division */
		applyDefault(Fields.SID);

		if ( Operations.CREATE.equals(this.operation) && schema.isStream(this.type) != hasStream )
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, Schema.NOLOG+ "violated stream contstraint");
		
		if ( Operations.SYNC.equals(operation) && ( session.getGlobalAcl() & Rights.sync ) == 0 )
			throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "Current user can't run sync" );

	}
	
	/*private void initAliases()
	{
		Set<String> aliases = schema.getAliases( type );
		
		for ( String alias : aliases )
		{
			String aliasTo = schema.getAlias( type, alias );
			
			if (!doc.containsKey(aliasTo) )
				continue;
			
			SolrInputField field = doc.get(aliasTo);
			field.setName(alias);
			doc.put(alias , field );
			doc.removeField(aliasTo);
		}
	}*/



    protected void prepareDocument() throws Exception
	{
		if ( this.doc.getFieldValues( Fields.STATE )==null )
			this.doc.setField( Fields.STATE , new ArrayList<>() );
		
		if ( Operations.CREATE.equals(operation) )
		{
			doc.setField(Fields.VIEWS, schema.getViews( type ) ) ;
			
			/*doc.setField( Fields._REQUESTED _STATE_ , doc.getFieldValues( Fields.STATE ) ) ;
			doc.addField( Fields._REQUESTED _STATE_ , docFlags.NOTEXISTS );
			doc.addField( Fields._REQUESTED _STATE_ , docFlags.CLEAN );*/
			
			requestedState = new HashSet<>( doc.getFieldValues( Fields.STATE ) );
			
			requestedState.add( docFlags.NOTEXISTS );
			requestedState.add( docFlags.CLEAN );
			
			if (!doc.containsKey(Fields.CREATED_BY))
				doc.setField(Fields.CREATED_BY, session.getId() );

            if (!doc.containsKey(Fields.CREATED_DN_BY))
                doc.setField(Fields.CREATED_DN_BY, session.getDisplayName() );

            if (!doc.containsKey(Fields.MODIFIED_BY))
                doc.setField(Fields.MODIFIED_BY , doc.getFieldValue(Fields.CREATED_BY ) );

            if (!doc.containsKey(Fields.MODIFIED_DN_BY))
                doc.setField(Fields.MODIFIED_DN_BY, doc.getFieldValue(Fields.CREATED_DN_BY ) );

			doc.setField( Fields._VERSION_ , -1L );
			
			applyDefaults();
			
			applySolrDefaults();

			applyAfterDefaults();
			
			checkParentRights();
			
		}
		else
		{
			
			//if (  !Operations.UPDATE.equals(operation) )
			//	throw new SolrException( SolrException.ErrorCode.BAD_REQUEST , "Invalid operation:"+operation );

			SolrQuery params = new SolrQuery();
			params.set( Params.TICKET , Session.ROOTTICKET );
			params.set( Params.SHORTCUT , true );
			params.set( Fields.ID , id );
			params.setFields( "*" );
			
			/*String route = null;
			String shard = req.getParams().get( ShardParams.SHARDS );

			if (doc.containsKey( schema.getRouteField() ))
				route = (String) doc.getFieldValue( schema.getRouteField() );
			else
				route = req.getParams().get( ShardParams._ROUTE_ );

			if (shard!=null)
				params.set( ShardParams.SHARDS , shard );
			else if (route != null)
				params.set( ShardParams._ROUTE_ , route );*/

			//params.setFields( "*", /*Fields._DUPLICATES_,*/ "docid:[docid]" );

			this.oldDoc = CoreClient.getInstance().get( params );

			if ( doc.containsKey( Fields._VERSION_) )
			{
                long version = (long) doc.getFieldValue( Fields._VERSION_ );

                log.debug("version:{}",version);
				
				if (version < 0)
					throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, Schema.NOLOG+ "You must use create operation if version is -1");
					
				if (version > 1 && version != (Long) oldDoc.getFieldValue( Fields._VERSION_ ) )
					throw new SolrException(SolrException.ErrorCode.CONFLICT, Schema.NOLOG+ "version doesn't match");
					
				doc.remove( Fields._VERSION_ );
			}
			
			requestedState = new HashSet<>( doc.getFieldValues( Fields.STATE ) );
			
			String[] req_states = req.getParams().getParams(Fields.STATE);
				
			if (req_states!=null)
			{
				requestedState.addAll( Arrays.asList(req_states) );
			}
			
			if ( Operations.UPDATE.equals(operation) )
			{
				oldDoc.setField(Fields.MODIFIED_ON , new Date() );
				oldDoc.setField(Fields.MODIFIED_BY , session.getId() );
                oldDoc.setField(Fields.MODIFIED_DN_BY, session.getDisplayName());
			}
			
			//doc.setField(Fields._REQUESTED _STATE_ , doc.getFieldValues( Fields.STATE ) ) ;
			
			this.sequence = (Long) oldDoc.getFieldValue(Fields.SEQUENCE) ;

            this.substitutor.setSequence(this.sequence);
		
			applyDefaults();
			
			ensureUpdatedDoc();
			
			applySolrDefaults();

			applyAfterDefaults();
			
			handleUpdateRules();
			
			//this.origShard = getTargetShard();
			
			doc.setField( Fields.STATE, oldDoc.getFieldValues( Fields.STATE ) );

            /* vengono ricalcolati automaticamente */
			doc.setField( Fields.ACL_READ , null );

            /* il profile va aggiornato sul repository in caso di update o comunque se è cambiato il repository */
			if ( Operations.UPDATE.equals(operation) || proxy.repoIsChanged() )
				doRemove( doc, Fields.STATE, docFlags.PROFILE );
		
			//doc.setField( Fields. SHARD , getTargetShard() );
			
		}

        ContentManager.doRemove( doc, Fields.STATE, docFlags.SYNCED );
			
		if ( requestedState.contains( docFlags.SYNCED ))
		{
			requestedState.add( docFlags.CLEAN );
			requestedState.add( docFlags.PROFILE );
			requestedState.add( docFlags.CONTENT );
			requestedState.add( docFlags.XHTML );
		}
        else if (schema.getFileCache() == null)
        {
            requestedState.add( docFlags.CONTENT );
        }
		
		if ( this.doc.getFieldValues( Fields.STATE )==null )
			this.doc.setField( Fields.STATE , new ArrayList<>() );

         log.debug("requestedState:{} doc:\n{}",requestedState,doc);
	}

    protected void handleUpdateRules()
	{
		/*if ( Operations.CREATE.equals(operation) )
		{
			checkParentRights();
		}
		else */
		
		/*if ( Operations.SYNC.equals(operation) )
		{
			if ( ( session.getGlobalAcl() & Rights.sync ) == 0 )
				throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "Current user can't run sync" );
		}*/
		//if ( Operations.UPDATE.equals(operation) )
		//{		
			//RealTimeGetComponent.checkRights( oldDoc, Rights.update , true );
		int mask = Rights.update;

        log.info("updated fields for {}: {}",id,changedFields);

        Collection<String> cf = new ArrayList<>(changedFields);

        if (cf.removeAll( forbiddenFields ) )
        {
            changedFields.retainAll(forbiddenFields);
            throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, Schema.NOLOG+ "Forbidden field update:"+changedFields.toString());
        }
		
		if ( changedFields.contains(Fields.ENABLED) )
		{
            log.debug("'enabled' is changed");

			mask |= Rights.delete;
            requestedState.add( docFlags.CLEAN );
			requestedState.add( docFlags.NOTEXISTS );
		}

		if ( changedFields.contains(Fields.NAME) )
		{
            log.debug("'name' is changed");
			//RealTimeGetComponent.checkRights( oldDoc, Rights.rename , true );
			
			mask |= Rights.rename;
            doc.removeField(Fields._OLD_NAME_);
            doc.removeField(Fields._OLD_PARENT_);
			doc.addField( Fields._OLD_NAME_ , oldDoc.getFieldValue( Fields.NAME ) );
			doc.addField( Fields._OLD_PARENT_ , oldDoc.getFieldValue( Fields.PARENT ) );
			requestedState.add( docFlags.NOTEXISTS );
			//doc.addField( Fields._REQUESTED _STATE_, docFlags.NOTEXISTS );
		}
		
		if ( changedFields.contains(Fields.ACL_EXPLICIT) ) 
		{
            log.debug("'acl_explicit' is changed");
			//RealTimeGetComponent.checkRights( oldDoc, Rights.updateACL , true );
			mask |= Rights.updateACL;
		}

		if ( changedFields.contains(Fields.ACL_INHERITS) )
		{
            log.debug("'acl_inherits' is changed");
			//RealTimeGetComponent.checkRights( oldDoc, Rights.updateACL , true );
			mask |= Rights.updateACL;
			requestedState.add( docFlags.CLEAN );
			//doc.addField( Fields._REQUESTED _STATE_, docFlags.CLEAN );
		}

        if ( changedFields.contains(Fields.ACL_PARENTS) )
        {
            requestedState.add( docFlags.CLEAN );
        }
		
		if ( changedFields.contains(Fields.PARENT) )
		{
            log.debug("'parent' is changed");
			//RealTimeGetComponent.checkRights( oldDoc, Rights.move , true );
			checkParentRights();
			
			mask |= Rights.move;
            doc.removeField(Fields._OLD_NAME_);
            doc.removeField(Fields._OLD_PARENT_);
			doc.addField( Fields._OLD_NAME_ , oldDoc.getFieldValue( Fields.NAME ) );
			doc.addField( Fields._OLD_PARENT_ , oldDoc.getFieldValue( Fields.PARENT ) );
			
			requestedState.add( docFlags.CLEAN );
			requestedState.add( docFlags.NOTEXISTS );
			
			
			//doc.addField( Fields._REQUESTED _STATE_, docFlags.CLEAN );
			//doc.addField( Fields._REQUESTED _STATE_, docFlags.NOTEXISTS );
			//doc.addField( Fields._REQUESTED _STATE_, docFlags.NOTCIRCULAR );
		}

		int rights = DocUtils.getUserRights( req, oldDoc , mask );

        log.debug("mask:{} user rights:{}",mask,rights);
		
		if (rights != mask)
			throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "Current user can't operation:"+ (mask & (~rights) ) );
			
		/*}
		else
		{
			throw new SolrException( SolrException.ErrorCode.BAD_REQUEST , "Invalid operation:"+operation );
		}*/
	}

    protected void checkParentRights()
	{
        String parentId = (String) doc.getFieldValue(Fields.PARENT);

        log.info("parent:{}",parentId);
		
		if (parentId==null)
		{
			Integer rootRight = schema.getCreateRight( null , type );
			
			if ( rootRight==null || ( session.getGlobalAcl() & rootRight ) == 0)
				throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "Not possibile to create "+type+" in root" );
			return;
		}
		
		String pType = parentId.split("@")[1];
		
		Integer createRight = schema.getCreateRight( pType , type );
		
		if (createRight==null)
			throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "Not possibile to create "+type+" in "+pType );
		
		SolrQuery params = new SolrQuery();
		params.set( Params.TICKET , Session.ROOTTICKET );
		params.set( Params.SHORTCUT , true );
		params.set( Fields.ID , parentId );
        params.set(SolrClient.RETRY_AFTER, QueueUtils.QUEUE_DELAY );
        params.setFields( Fields.COMMON_FIELDS );
		
		try
		{
			SolrDocument pDoc = CoreClient.getInstance().get( params );
			int rights = DocUtils.getUserRights( req,pDoc , createRight );

            log.debug("rights:{} createRight:{}",rights,createRight);
			
			if (rights==0)
				throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "User can't create "+type+" in "+pType );
		}
		catch( SolrServerException sse )
		{
			throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , sse );
		}
	}
	
	/*private String getTargetShard()
	{
		//String route = (String) doc.getFieldValue(schema.getRouteField());
		
		DocCollection coll = CoreClient.getInstance().getZkStateReader().getClusterState().getCollection( schema.collectionName );
		
		return Schema.getRouter().getTargetSlice( id , null , null , coll ).getName();
	}*/

    /*private void queueDeleteOlds( String id, String shard )
	{	
		String query = CoreClient.Query.makeClause( Fields.ID, id ) + CoreClient.Query.makeNotClause( Fields.SHARD , shard ) + " +NOW:" + new Date().getTime();
		getQueue(ContentUpdateRequestHandler.PRE_QUEUE).deleteByQuery(query);
	}*/
	
	
	/*@SuppressWarnings("unchecked")
	protected static void queue( String command , Object obj , String queue)
	{	
		Object val = Session. getRequestInfo().getReq().getContext().get( queue );
		
		if (val==null)
		{
			val = ...
			Session. getRequestInfo().getReq().getContext().put( queue , val );
		}
		else
		{
			nl = (NamedList<Object>) val;
		}
			
		nl.add( command , som );
		
		
		int commitAny = Session. getRequestInfo().getReq().getParams().getInt( Params.COMMITANY , Schema. getInstance().getMaxQueueSize() +1 );
		
		if ( (nl.size() % commitAny) == 0 )
		{
			SimpleOrderedMap<Object> cSom = new SimpleOrderedMap<>();
			
			cSom.add("softCommit" , true);
			cSom.add("waitSearcher" , false);
			
			nl.add( "commit" , cSom );
		}
		
		
	}*/
	
	
	
	

	
	/*private static File getSlashedPath( String path, String id , String filename )
	{
		int hash = id.hashCode();
	
		String cache_path = path + "/" + HashAugmenter.formatHash(hash).replace("-","/") + "/" + filename;
			
		return new java.io.File( cache_path );
	}*/
	
	/*@SuppressWarnings("unchecked")
	public static String getXHTML( String id ) throws SolrException
	{
		try
		{
			String path = Schema. getInstance().getXHTMLCache();
			
			java.io.File file = getSlashedPath(path,id,id+".xhtml");
			
			if (file.exists())
			{
				log.info("*************** found cache_path: " + file.getPath() );
				return org.apache.commons.io.FileUtils.readFileToString( file , " UTF-8" );
			}
			else
			{
				log.info("*************** not found cache_path: " + file.getPath() );
				return null;
			}
		}
		catch( Exception e )
		{
			log.error( "Problem getting xhtml content: "+id + "\n", e);
			throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , e );
		}
	}*/
	
	/*@SuppressWarnings("unchecked")
	public static Collection<Object> extractXHTML( SolrInputDocument doc, ContentStream stream ) 
	{
		String id=null;
		try
		{
			id = (String) doc.getFieldValue( Fields.ID );

			if ( stream == null)
			{
				IProvider provider = Schema. getInstance().getRepositoryProvider();
				
				SolrDocument solrDoc = Session. RealTimeGet ( id , Fields.CONTENT_ID );
				
				stream = provider.downloadVersion( solrDoc, null );
			}
			
			//NamedList nl = (NamedList) Session. request( "/extract", stream  );
			NamedList nl = Session.extract( stream  );
			
			String contentField = null;
				
			for (int i=0; i< nl.size() ; i++)
			{
				String key = nl.getName(i);
				
				int idx = key.indexOf("_metadata");
				
				if (idx >0)
				{
					contentField = key.substring(0,idx) ;
					break;
				}
			}
			
			if (contentField == null)
				throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Invalid response from tika" );
			
			String contentText = (String) nl.get( contentField );
			
			log.info("*************** correct tika response ************ size:"+ contentText.length() );
			
			String path = Schema. getInstance().getXHTMLCache();
		
			java.io.File file = getSlashedPath(path,id,id+".xhtml");
			
			org.apache.commons.io.FileUtils.writeStringToFile( file , contentText , " UTF-8" );
			
			doc.setField(Fields.CONTENT_ FT , contentText);
			doc.setField(Fields.CONTENT_ FTSIZE, contentText.length() ) ;
			doc.setField(Fields.CONTENT_INDEXED_ON, new Date() );
			
			doc.addField(Fields.STATE, docFlags.XHTML );
			ContentManager.doRemove( doc, Fields.ERROR, docFlags.XHTML );
			
		}
		catch(Exception e)
		{
			doc.setField(Fields.CONTENT_INDEXED_ON, null);
			doc.doRemove(Fields.STATE, docFlags.XHTML );
			doc.addField(Fields.ERROR, docFlags.XHTML );
			
			log.error( "Problem extracting content: "+id + "\n", e);
			
		}
		
		return doc.getFieldValues(Fields.STATE);
	}*/

    protected boolean initStream()
	{
		//TODO NTH si potrebbe gestire campo version e campo stream implicitamente qui e sul client

        if (doc.containsKey( Fields._STREAM_FILE_ ))
		{
            log.debug("_STREAM_FILE_");

            this.contentStream = new ContentStreamBase.FileStream( new File( (String) doc.getFieldValue( Fields._STREAM_FILE_ ) ) );
		}
		else if (doc.containsKey( Fields._STREAM_URL_ ))
		{
            log.debug("_STREAM_URL_");

            try
            {
                this.contentStream = new ContentStreamBase.URLStream( new java.net.URL( (String) doc.getFieldValue( Fields._STREAM_URL_ ) ) );
            }
            catch(Exception e)
            {
                this.contentStream = new ContentStreamBase.FileStream( new File( (String) doc.getFieldValue( Fields._STREAM_URL_ ) ) );
            }
		}
		else
		{
            java.lang.Iterable<ContentStream> streams = req.getContentStreams();

            log.debug("streams:",streams);

            if (streams != null)
			{	
				for (ContentStream str : streams) 
				{
					String contentType = str.getContentType();
					
					if (!ContentUpdateRequestHandler.hasLoader(contentType) )
					{
						this.contentStream = str;
                        break;
					}
				}
			}
		}

        log.debug("contentStream:{}",contentStream);
		
		return (this.contentStream != null);
		
		/*if (this.contentStream != null)
		{
			requestedState.add( docFlags.CACHE );
			log.info( "content stream found id:{} ", id );
		}*/
	}
	
	/*private void checkStream() throws Exception
	{
		
		if ( this.contentStream!=null)
		{
			requestedState.add( docFlags.CACHE );
			
			//doc.addField( Fields._REQUESTED _STATE_ , docFlags.CACHE );
			//doc.setField( Fields. _STREAM_ , stream );
			
			if (doc.containsKey(Fields.NAME) && this.contentStream instanceof ContentStreamBase )
				((ContentStreamBase) stream).setName( (String) doc.getFieldValue(Fields.NAME)  );
			
			if (!doc.containsKey(Fields.NAME))
				doc.setField( Fields.NAME , stream.getName() );
		}
	}*/
	
	/*private String getDivision(String id)
	{
		String route = schema.get Route(id);
		
		return route.replaceAll( "(.*!)" , "$1" ).replace("/ 8!" , "!" );
	}*/
	
	//final static String sequence_path = "global_sequence";
	
	

	
	/*
	
	{
  "responseHeader":{
    "status":0,
    "QTime":29,
    "params":{
      "q":"*:*",
      "stats":"true",
      "rows":"0",
      "stats.field":"sequence"}},
  "response":{"numFound":1657,"start":0,"maxScore":1.0,"docs":[]
  },
  "stats":{
    "stats_fields":{
      "sequence":{
        "min":0.0,
        "max":38785.0,
        "count":1657,
        "missing":0,
        "sum":3.1024993E7,
        "sumOfSquares":6.44786844557E11,
        "mean":18723.59263729632,
        "stddev":6211.233105984522,
        "facets":{}}}}}
	
	
	*/

	@SuppressWarnings("unchecked")
	protected void runProcessAdd()
	{
		String processAdd = schema.getProcessAdd( type );

		Object res = DocUtils.eval(this.operation,this.doc,"javascript:"+processAdd);

		log.debug("run processAdd script on id:{} res:{}",this.id,res);
	}

    @SuppressWarnings("unchecked")
    protected void applyAliases()
	{
		Set<String> aliases = schema.getAliases( type );
			
		for ( String key : aliases )
		{
			Object tmpl = schema.getAlias( type, key );
			
			key = this.substitutor.replace( key );

			if (	key.equals(Fields.PARENT) ||
					key.equals(Fields.ACL_PARENTS) ||
					key.equals(Fields.ACL_INHERITS) ||
					key.equals(Fields.ENABLED ) )
			{
				log.error("field {} can't be aliased. Use after_defaults instead",key);
				throw new SolrException( SolrException.ErrorCode.BAD_REQUEST, "alias not valid:"+ key );
			}

			Object vals = applyTemplates( key, tmpl );

			if (vals==null)
				doc.removeField( key );
		}
	}

    @SuppressWarnings("unchecked")
    protected void applyValidations()
	{
		Set<String> validations = schema.getValidations( type );
		
		List<String> report = new ArrayList<>();
			
		for ( String key : validations )
		{
			//String origKey = key;
			String validation = schema.getValidation( type, key );
			
			String rfield = null;
			String regex = null;
			String replace = null;
			
			if (key.contains("::"))
			{
				rfield = key.split("::")[1];
				key = key.split("::")[0];
			}	
			
			int idxr = key.indexOf("/");
			if (idxr != -1)
			{
				int idx2 = key.indexOf("/",idxr+1);
				if (idx2 > 0)
				{
					for(int i=idx2;i<key.length();i++)
					{
						idx2 = -1;
						if (key.charAt(i)=='/' && key.charAt(i-1)!='\\' )
						{
							idx2 = i;
							break;
						}
					}
				}
				
				regex = key.substring(idxr+1,idx2);
				replace = key.substring(idx2+1);
				key = key.substring(0,idxr);
			}

            log.trace("validation:{} key:{} regex:{} replace:{} rfield:{}",validation,key,regex,replace,rfield);
			
			if (changedFields != null && !changedFields.contains(key))
				continue;
			
			key = this.substitutor.replace( key );
			
			Collection<Object> vals = doc.getFieldValues( key );
			
			int size = 0;
			
			if (vals != null)
			{
				List<Object> othervals = new ArrayList<>();
				
				/* tratto solo quelli non vuoti */
				for( Object val : vals )
					if (val!=null && !"".equals(val))
						othervals.add(val);
				
				vals = othervals;	
				
				size = othervals.size();
				
				/* rimuovo quelli già presenti nel vecchio */
				if (oldDoc != null)
				{
					Collection<Object> oldVals = oldDoc.getFieldValues(key);
					if (oldVals!=null)
						othervals.removeAll(oldVals);
				}
				
				/* risolvo i ref prendendo in considerazione la proprietà citata */
				for( int i=0; i<othervals.size();i++ )
				{
					Object val = othervals.get(i);
					
					if (regex!=null)
					{
						val = val.toString().replaceFirst( regex , replace );
						othervals.set(i, val );
					}
					
					if (rfield!=null)
					{
						try
						{
							SolrDocument doc = DocUtils.getDocument( req, (String) val, rfield );
							othervals.set(i, doc.getFieldValue(rfield) );
						}
						catch( SolrException se )
						{
							if (se.code() == SolrException.ErrorCode.NOT_FOUND.code)
								report.add( String.format("key:%s \nNOT FOUND id:%s",key,val) );
							else
								throw se;
						}
					}	
					
				}
				
			}
			
			int idx0 = 0;
			int idx1 = 0;
			
			while(idx0<validation.length())
			{
				char ch = validation.charAt(idx0);
				boolean result;
				
				if (ch=='*')
				{
					idx1=idx0;
					result = true;
				}
				else if (ch=='[')
				{
					idx1 = validation.indexOf(']',idx0+1);
					result = DocUtils.validateRange(vals, validation.substring(idx0 + 1, idx1));
				}
				else if (ch=='/')
				{
					idx1 = validation.indexOf('/',idx0+1);
					result = DocUtils.validateRegex(vals, validation.substring(idx0 + 1, idx1));
				}
				else if (ch=='{')
				{
					idx1 = validation.indexOf('}',idx0+1);
					result = DocUtils.validateSize(size, validation.substring(idx0 + 1, idx1));
				}
				else
					result = false;
				
				if (!result)
					report.add(String.format("key:%s validation:%s values:%s",key,validation,vals));
				
				idx0 = idx1+1;
			}
		}
		
		if (report.size()>0)
		{
			String msg = StrUtils.join( report, '\n' );
			log.debug( "validation not passed:"+ msg + "\n"+this.doc.toString() );
			throw new SolrException( SolrException.ErrorCode.BAD_REQUEST, "validation not passed:"+ msg );
		}
	}

	final static Pattern jsRegex = Pattern.compile("^\\s*eval\\((.*)\\)\\s*$");

	@SuppressWarnings("unchecked")
    protected Object applyTemplates( String key , Object tmpl )
	{
		Collection<Object> defs;
			
		if (!(tmpl instanceof Collection))
			defs = Collections.singletonList(tmpl);
		else
			defs = (Collection<Object>) tmpl;

		/*Matcher m = jsRegex.matcher(key);

		if (m.find())
		{
			Object res = DocUtils.eval(this.operation,this.doc, defs.iterator().next().toString());

			String field = m.group(1);
			if (!Strings.isNullOrEmpty(field))
				doc.setField(field, res);

			return res;
		}*/
			
		Object vals = null;
		for( Object def : defs )
		{
            log.debug("key:{} tmpl:{}",key,def);

            if (def == null)
            	continue;

			Matcher m = jsRegex.matcher(def.toString());

            if (m.find())
			{
				vals = DocUtils.eval(this.operation,this.doc, "javascript:"+m.group(1));
			}
            // template "puro" cioè punta direttamente il campo, e può gestire nativamente il multivalue senza conversione in stringa
			else if ( def.toString().matches("^\\{[^\\}:\\/]+\\}$") )
			{
				String subKey = def.toString().substring(1, def.toString().length()-1);
				
				boolean encode = false;
				boolean decode = false;
				boolean isId = false;
				int hash = 0;
				
				if (subKey.charAt(0) == FieldUtils.encFsChar)
				{
					encode = true;
					isId = false;
					subKey = subKey.substring(1,subKey.length());
				}
				
				if (subKey.charAt(0) == FieldUtils.encIdChar)
				{
					encode = true;
					isId = true;
					subKey = subKey.substring(1,subKey.length());
				}
				
				if (subKey.charAt(0) == '~')
				{
					hash = this.sequence.hashCode();
					subKey = subKey.substring(1,subKey.length());
				}
				
				if (subKey.charAt(subKey.length() -1) == FieldUtils.encPerc)
				{
					decode = true;
					subKey = subKey.substring(0,subKey.length()-1);
				}
				
				Collection<Object> fieldValues = doc.getFieldValues( subKey );
				
				if (fieldValues==null || fieldValues.size()==0)
					continue;
				
				List<Object> values = new ArrayList<>( fieldValues );
				
				for( int i=0; i< values.size(); i++ )
				{
					if (values.get(i) instanceof Date)
						values.set(i, FieldUtils.formatDate((Date) values.get(i)));
					
					if (encode && !"".equals(values.get(i)))
						values.set(i, FieldUtils.encode(values.get(i).toString(), hash, isId));
					
					if (decode)
						values.set(i, FieldUtils.decode(values.get(i).toString()));
				}
				
				if (values.size()==1)
					vals = values.iterator().next();
				else
					vals = values;
			}
			else
			{
				String sval = this.substitutor.replace(def);

                sval = sval.replaceAll(FieldSubstitutor.ESCAPE_MARK, "{");
				
				if (sval==null || sval.contains("__NULL__"))
					continue;
				
				if ( sval.startsWith("[") && sval.endsWith("]") )
					vals = StrUtils.splitSmart( sval.substring(1,sval.length()-1), ',' ) ;
				else
					vals = sval;
			}

            log.trace("key:{} vals:{}",key,vals);
			
			doc.setField(key, vals);
			break;
		}
		return vals;
	}
	
	@SuppressWarnings("unchecked")
    protected Object applyDefault( String key )
	{
		Object tmpl = schema.getDefault( type, key, this.operation );
		
		key = this.substitutor.replace( key );
		
		Object val = doc.getFieldValue(key);

        log.debug("key:{} val:{} tmpl:{}",key,val,tmpl);
		
		if (tmpl==null || val!=null )
			return val;
		else
			return applyTemplates( key , tmpl );
	}
	
	//final String divRegex = "([^!@]+(?:!?[^!@]+)?).*";
	//final String locRegex = "([^\\.!@]+).*";
	//final String sidRegex = ".+[\\.!]([^!@]+).*|([^\\.!@]+)[!@]+[^!@]+";

    protected void applyDefaults()
	{
		Set<String> defaults = schema.getDefaults( type , this.operation );
		
		for ( String key : defaults )
			applyDefault(key);
	}

	protected void applyAfterDefaults()
	{
		Set<String> defaults = schema.getAfterDefaults( type );

		for ( String key : defaults ){
			Object tmpl = schema.getAfterDefault( type, key );
			key = this.substitutor.replace( key );
			applyTemplates( key, tmpl );
		}
	}

    protected void applySolrDefaults()
	{
		IndexSchema solrSchema = req.getSchema();
		
		for (SchemaField field : solrSchema.getRequiredFields()) {
			
			if ( doc.getFieldValue( field.getName() ) == null && field.getDefaultValue() != null ) {
				
				IndexableField f = field.getType().createField(field, field.getDefaultValue() , 1.0f);

                Object val = field.getType().toObject(f);

				doc.setField( field.getName() , val );

                log.trace("field:{} value:{}",field.getName(),val);
			} 
		}
	}

    protected void checkSequence()
	{
		log.debug( "checkSequence id:{}", id );
		
		this.sequence = null;
		Object seqObj = doc.getFieldValue(Fields.SEQUENCE);
		
		if ( seqObj != null )
		{
			this.sequence = Long.parseLong( seqObj.toString() );
			zkClient.checkCounter(this.sequence);
		}
		
		if (this.sequence==null)
			this.sequence = zkClient.getCounter();

        this.substitutor.setSequence(this.sequence);

        log.trace("sequence:{}",this.sequence);
		
		doc.setField( Fields.SEQUENCE , this.sequence );
	}

    protected void checkName() throws Exception
	{
		log.info("checkName id:{}", id);
		
		String name = (String) doc.getFieldValue(Fields.NAME);
		
		if (name==null && this.contentStream != null)
		    name = this.contentStream.getName();
		
		if (name==null || "".equals(name))
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, Schema.NOLOG+ "name must be valued");

		name = name.trim();

		Object value = doc.getFieldValue(Fields.ENABLED);
		boolean enabled = FieldUtils.parseBool(value, true); // value != null ? !Schema.FALSE.equalsIgnoreCase( value.toString() ) : true ;
        boolean hidden = !enabled;

        int hash = this.sequence.hashCode();
        String prefix = "~" + String.format("%8s", Integer.toHexString(hash) ).replace(' ', '0');

        log.debug("checkName name:{} enabled:{} prefix:{}", name, enabled,prefix);

		if (enabled)
        {
            if (name.startsWith(prefix))
                name = name.substring(prefix.length());

            String parent = (String) doc.getFieldValue(Fields.PARENT);

            if (parent != null)
            {
                ContentCacheManager.CacheEntry pdoc = ContentCacheManager.getInstance().getCacheEntry(req,parent);
                if (pdoc!=null)
                    hidden = pdoc.isHidden();
                else
                {
                    log.warn("parent not found:"+id);
                }
            }

            String q = CoreClient.Query.makeClause( Fields.PARENT, parent ) ;
            q += CoreClient.Query.makeClause( Fields.NAME, name );
            q += CoreClient.Query.makeNotClause( Fields.ID, id );

            SolrQuery params = new SolrQuery();
            params.set( Params.TICKET , Session.ROOTTICKET );
            params.set( Params.SHORTCUT , true );
            //params.addField( Fields.ID );
            //params.addField( Fields.INDEXED_ON );
            params.setRows(0);
            params.setQuery(q);
            params.set( Params.PROUTE , parent );

            SolrDocumentList children = CoreClient.getInstance().query( params ).getResults();

            if ( children.getNumFound() > 0 )
            {
                String unique_name = (String) applyDefault(Fields.UNIQUE_NAME);

                if (Strings.isNullOrEmpty(unique_name))
                    throw new SolrException(SolrException.ErrorCode.CONFLICT, Schema.NOLOG+ "The destination already exists parent:"+ parent + " name:"+name);
                else
                    name = unique_name;
             }
        }
        else if (!name.startsWith(prefix))
        {
            log.debug("not enabled and name to mangle");
            name = prefix + name;
        }

        doc.setField( Fields.NAME , name );
        doc.setField(Fields.ENABLED , enabled);
        doc.setField( Fields.HIDDEN , hidden );

        log.trace("after checkName name:{} enabled:{} hidden:{}",name,enabled,hidden);

		if ( !operation.equals(Operations.CREATE) )
			QueueUtils.queueDeleteCacheEntry(req, id);
	}
	
	/*private void saveCache() throws Exception
	{
		assert( this.contentStream != null );
		
		proxy.write( this.contentStream );
		
		//ContentStream contentStream = (ContentStream) doc.getFieldValue( Fields._STREAM_ );
		
		//ProviderProxy provider = new ProviderProxy(doc,req);
	}*/
	
	/*private void syncProvider() throws Exception
	{
		if (  !doc.getFieldValues( Fields.STATE ).contains( docFlags.CONTENT ) )
			proxy.sync(this.contentStream);
	}*/
	
	/*private void updateProfile() throws Exception
	{
		if ( !doc.getFieldValues( Fields.STATE ).contains( docFlags.PROFILE ))
			proxy.updateProfile();
	}*/
	
	/*private void updateXHTML() throws Exception
	{
		boolean requested = requestedState.contains( docFlags.XHTML ) ;
		
		if ( requested || doc.getFieldValues( Fields.STATE ).contains( docFlags.XHTML ))
			proxy.updateXHTML( requested );
			
		//if (!doc.getFieldValues( Fields._REQUESTED _STATE_ ).contains( docFlags.XHTML ) && !doc.getFieldValues( Fields.STATE ).contains( docFlags.XHTML ))
		//	return;
			
		//log.info( "*************** updateXHTML *************** id:"+doc.getFieldValue(Fields.ID) );
			
		//boolean requested = doc.getFieldValues( Fields._REQUESTED _STATE_ ).contains( docFlags.XHTML ) ;
		
		//if (requested)
		//	log.info( "*************** REQUESTED updateXHTML *************** id:"+id );
		
		//new ProviderProxy(doc,req).updateXHTML( requested );	
	}*/

    protected void checkParent() throws Exception
	{
		String parent = (String) doc.getFieldValue( Fields.PARENT );

        Object valueE = doc.getFieldValue(Fields.ENABLED);
        boolean enabled = FieldUtils.parseBool(valueE, true); // value != null ? !Schema.FALSE.equalsIgnoreCase( value.toString() ) : true ;
        boolean hidden = !enabled;

        Collection<Object> acl_parents = doc.getFieldValues( Fields.ACL_PARENTS );

        boolean acl_inherits = false;
        String acl_parent=null;
        Long acl_sequence=null;

        log.debug("parent:{} enabled:{} acl_parents:{}",parent,enabled,acl_parents);
		
		if ( parent != null )
		{
			ContentCacheManager.CacheEntry pdoc = ContentCacheManager.getInstance().getCacheEntry(req,parent);

            if (pdoc==null)
                throw new SolrException(SolrException.ErrorCode.CONFLICT, Schema.NOLOG + "Parent not found for id:"+id ) ;

            List<String> parentIds = ContentCacheManager.getInstance().getParents(req,parent);

            log.trace("parentIds:{}",parentIds);
				
			if ( parentIds == null )
				throw new SolrException(SolrException.ErrorCode.CONFLICT, Schema.NOLOG + "Empty parent chain for id:"+id ) ;
			
			if ( parentIds.contains(id) || id.equals(parent) )
				throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, Schema.NOLOG + "Document is in the parent chain for id:"+id ) ;
			
				
			//Object value = doc.getFieldValue(Fields.ACL_INHERITS);
			//acl_inherits = value != null ? !Schema.FALSE.equalsIgnoreCase( value.toString() ) : true ;

            acl_inherits = FieldUtils.parseBool(doc.getFieldValue(Fields.ACL_INHERITS), false);

            //int dotIdx = parent.lastIndexOf(".");
            //int sepIdx = parent.lastIndexOf("!");

            /* algoritmo per garantire il routing by parent
             *
             * 1) Se il parent non ha "!" il figlio può avere qualunque id e non c'è ottimizzazione in fase di routing
             * 2) Se il parent ha "!" ma non ha "." il figlio deve aggiungere il punto (ereditando la location dal parent)
             * 3) Se il parent ha la location (ha "!" e ".") il figlio deve includere la parte che imposta i livelli (fino all'ultimo "!")
             *
             * In questo modo, in fase di routing, dall'id del parent si imposta il range di ricerca (RealtimeSearchHandler)
             *
             * */

            String parent_division = schema.getDivision(parent);

            log.trace("id:{} parent division:{}",id,parent_division);

            if ( !id.startsWith(parent_division) && (id.contains(".") || (id.contains("!"))) )
                throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, Schema.NOLOG + "Incompatible division (1):"+parent_division+","+ id ) ;

            /*if ( sepIdx>=0 && dotIdx>=0  )
            {

                String pdiv = parent.substring(0,sepIdx+1);

                if ( !id.startsWith(pdiv) )
                    throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, Schema.NOLOG + "Incompatible division:"+id ) ;
            }*/

			if (acl_inherits)
			{
                acl_parent = pdoc.getAclParent(); //(String) pdoc.getFieldValue(Fields.ACL_PARENT);
                acl_sequence = pdoc.getAclSequence(); //(Long) pdoc.getFieldValue(Fields.SEQUENCE);

                if (acl_parent==null)
                    acl_parent = parent;

                if (acl_sequence==null)
                    acl_sequence = pdoc.getSequence();

            }

            if (enabled)
                hidden = pdoc.isHidden();
		}
		

		
		/*if (acl_inherits)
		{
			acl_parent = pdoc.getAclParent(); //(String) pdoc.getFieldValue(Fields.ACL_PARENT);
			acl_sequence = pdoc.getAclSequence(); //(Long) pdoc.getFieldValue(Fields.SEQUENCE);
		}
		else
		{
			acl_parent = null; //id;
			acl_sequence = null; //(Long) doc.getFieldValue(Fields.SEQUENCE); //getCounter( "global-sequence" );
		}*/
		
		doc.setField( Fields.ACL_INHERITS , acl_inherits );
		doc.setField( Fields.ACL_PARENT , acl_parent );
		doc.setField( Fields.ACL_SEQUENCE , acl_sequence );

		Collection<Long> acl_sequences = new LinkedHashSet<>();
		
		if (acl_sequence != null)
			acl_sequences.add(acl_sequence);


        if (acl_parents != null)
		{
			if (schema.canHaveChildren( type ))
				throw new SolrException(SolrException.ErrorCode.CONFLICT, Schema.NOLOG + "Only leaf types con have acl_parents. id:"+id ) ;

			List<Object> ids = new ArrayList<>( acl_parents );
			
			SolrQuery params = new SolrQuery();
			params.set("ticket" , Session.ROOTTICKET );
			params.set( "ids" , StrUtils.join( ids , ',' ) );
			params.setFields( Fields.SEQUENCE, Fields.ACL_SEQUENCE );
			params.set( Params.SHORTCUT , true );
			params.set( "qt" , "/get" );
			params.set( "rows" , 1000 );
			
			SolrDocumentList parents = CoreClient.getInstance().query( params ).getResults();
			
			for ( SolrDocument p : parents )
            {
                if (p.containsKey(Fields.ACL_SEQUENCE))
                    acl_sequence = (long) p.getFieldValue( Fields.ACL_SEQUENCE );
                else
                    acl_sequence = (long) p.getFieldValue( Fields.SEQUENCE );

                acl_sequences.add( acl_sequence );
            }
		}
		
		doc.setField( Fields.ACL_SEQUENCES , acl_sequences );

        doc.setField( Fields.HIDDEN , hidden );

        log.trace("doc:\n{}",doc);
		
		//doc.setField( schema.getRouteField() , schema.get Route( id, false ) );
		
	}
	
	/*public static void alignFields( SolrInputDocument doc ) throws Exception
	{
		String acl_parent = (String) doc.getFieldValue( Fields.ID );
		
		log.info( "*************** alignFields *************** id:"+acl_parent);
		
		if ( doc.containsKey( Fields.PARENT ) && (Boolean) doc.getFieldValue(Fields.ACL_INHERITS) )
		{
			SolrDocument pDoc = Session.
			acl_parent = ContentCacheManager. getInstance().get CacheEntry( (String) doc.getFieldValue( Fields.PARENT ) ).acl_parent ;
		}
		
		doc.setField( Fields.ACL_PARENT , acl_parent );
		doc.setField( Schema. getInstance().getInstance().get RouteField() , Schema. getInstance().get Route(acl_parent) );
		
	}*/
	
	/*private void cleanVersions() throws Exception
	{
		//String id = (String) doc.getFieldValue( Fields.ID );
		
		//log.info( "*************** cleanVersions *************** id:"+id);
		
		//long version = (long) doc.getFieldValue( Fields._VERSION_ );
		//String vs = DateField.formatExternal( new Date(version >>> 20) );
		
		String targetShard = getTargetShard();
		
		if ( !targetShard.equals(this.origShard) ) 
		{
			log.trace("clean old versions because shard is changed for id:"+id + " -shard:"+ targetShard );
			queueDeleteOlds( id, targetShard );
			//doc.setField(Fields._VERSION_ , -1 );
			doc.remove(Fields._VERSION_);
		}
		else if ( Boolean.TRUE.equals(doc.getFieldValue( Fields._DUPLICATES_ )))
		{
			log.debug("clean old versions because duplicates found for id:"+id + " -shard:"+ targetShard );
			queueDeleteOlds( id, targetShard );
		}
	}*/

    protected void cleanChildren() throws Exception
	{
		boolean clean = true;
		
		if (!operation.equals(Operations.CREATE) && schema.canHaveChildren( type ))
		{
            Long acl_sequence = (Long) doc.getFieldValue( Fields.ACL_SEQUENCE );

            if (acl_sequence==null)
            {
                boolean acl_inherits = FieldUtils.parseBool(doc.getFieldValue(Fields.ACL_INHERITS), false);

                if (acl_inherits)
                    throw new SolrException(SolrException.ErrorCode.CONFLICT,"acl_sequence null during clean for inheriting object");
                else
                    acl_sequence = sequence;
            }

            boolean hidden = FieldUtils.parseBool(doc.getFieldValue(Fields.HIDDEN), false) ;

            String q1 = CoreClient.Query.makeClause(Fields.PARENT, id) ;

            if (hidden)
            {
                /* se il padre è stato disabilitato pulisco tutti i figli visibili */

                q1 += CoreClient.Query.makeNotClause(Fields.HIDDEN, true);
            }
            else
            {
                //if (oldDoc==null)
                //    throw new SolrException(SolrException.ErrorCode.CONFLICT,"oldDoc null during clean");

                //String modified_on = FieldUtils.formatDate( (Date) oldDoc.getFieldValue( Fields.MODIFIED_ON ) );

                //modified_on += String.format( "-%sSECONDS", RESTORE_TOLERANCE );

                /* i documenti modificati dopo la data di ultima modifica del padre (al momento dell'eliminazione) sono quelli sincronizzati a cascata
                si sottraggono X secondi per includere eventuali documenti cancellati ricorsivamente dai client */

                q1 += CoreClient.Query.makeClause(Fields.HIDDEN, true);

                //String q11 = CoreClient.Query.makeClause(Fields.ENABLED, true);

                //String q12 = CoreClient.Query.makeRangeClause( Fields.MODIFIED_ON, modified_on, "*" );

                //q1 += String.format( " +( (%s) OR (%s) ) ", q11, q12 );
            }

            /* prendo tutti i documenti ereditanti che non contengono l'acl_sequence del padre */

            String q2 = CoreClient.Query.makeClause(Fields.PARENT, id) ;
            q2 += CoreClient.Query.makeClause( Fields.ACL_INHERITS, true );
            q2 += CoreClient.Query.makeNotClause( Fields.ACL_SEQUENCE, acl_sequence );

            String q3 = CoreClient.Query.makeClause( Fields.ACL_PARENTS, id ) ;
            q3 += CoreClient.Query.makeClause( Fields.ACL_INHERITS, true );
            q3 += CoreClient.Query.makeNotClause( Fields.ACL_SEQUENCES, acl_sequence );

			String q = String.format( " (%s) OR (%s) OR (%s) ", q1, q2, q3 );
			
			int rows = req.getParams().getInt( "maxqueuesize" , schema.getMaxQueueSize() ) - QueueUtils.queuesSize(req) ;
			
			SolrQuery params = new SolrQuery();
			params.set( Params.TICKET , Session.ROOTTICKET );
			params.set( Params.SHORTCUT , true );
            params.setRequestHandler("/queueselect");
			params.setQuery(q);
			params.setSort( Fields.INDEXED_ON , SolrQuery.ORDER.asc );
			params.addField( Fields.ID );
			params.addField( Fields._VERSION_ );
			params.addField( Fields.INDEXED_ON );
			params.setRows( Math.max(0,rows) );
			params.set( Params.PROUTE , id );

            log.trace("params:\n{}",params);
			
			//params.set(ShardParams. _ROUTE_,Schema. getInstance().get Route(id));
			
			//children = Session. select( q  , "fl=id" , "rows=" + Math.max(0,rows) , ShardParams. _ROUTE_+"="+ Schema. getInstance().get Route(id) );
			SolrDocumentList children = CoreClient.getInstance().query( params ).getResults();

            log.trace("children:\n{}",children);
			
			clean = false;
			
			String tracemsg = params.toString();
			
			for ( SolrDocument child : children )
			{
				String childId = (String) child.getFieldValue( Fields.ID );
				QueueUtils.queueClean(req, childId, false);
				tracemsg += "\n" + childId;
			}
			
			//TraceMessage trace = new TraceMessage(tracemsg);
			
			if ( children.size() < Math.min( children.getNumFound() , rows ) )
			{
				String msg = String.format( "queued %s dirty children of %s requested. parent:%s NumFound:%s", children.size() , rows , id, children.getNumFound() );
				log.error( "LESS THEN EXPECTED!!!! " + msg + "\n{}", tracemsg);
			}
			else if ( children.size() < children.getNumFound() )
			{
				if (rows==0)
				{
					String msg = String.format( "QUEUE IS FULL. %s dirty children found. parent:%s", children.getNumFound() ,id );
					log.warn( msg + "\n{}", tracemsg );
				}
				else
				{
					String msg = String.format( "PARTIAL CLEANING. queued %s dirty children of %s found. parent:%s", children.size() , children.getNumFound() ,id );
					log.warn( msg + "\n{}", tracemsg );
				}
			}
			else if ( children.size() > 0 )
			{
				String msg = String.format( "queued %s dirty children and parent:%s", children.size() , id );
				QueueUtils.queueClean(req, id, true);
				log.debug(msg + "\n{}", tracemsg);
			}
			else /* numFound==0 */
			{
				String msg = String.format( "cleaned parent:%s", id );
				log.debug(msg + "\n{}", tracemsg);
				clean = true;
			}
		}
		
		if (!clean)
		{
			ContentManager.doRemove( doc, Fields.STATE, docFlags.CLEAN );
		}
		else	
		{
			doc.addField(Fields.STATE, docFlags.CLEAN );
			ContentManager.doRemove( doc, Fields.ERROR, docFlags.CLEAN );
		}
		
		
			
			/*if ( Session. getRequestInfo().getReq().getParams().getBool("admin-queue",false) ) 
			{
				if ( !"crawl".equals( doc.getFieldValue("[command]") ) )
				{
					log.debug( "there are "+children.getNumFound()+" children to clean for dirty parent:"+id );
					return;
				}
			}*/
			
			
			/*else
			{
				log.debug("Partial cleaning for parent id:"+id , new TraceMessage(message) );
			}*/
			//queueState( id , docFlags.SET_CLEAN );
		
	}
	
	public void run( SolrQueryResponse rsp )
	{
		if ( this.cmd instanceof AddUpdateCommand )
		{
			handleAdd();
			handleAddResponse(rsp);
			return;
		}
		
		throw new java.lang.UnsupportedOperationException(); 
	}
	
	@SuppressWarnings("unchecked")
    protected void handleAddResponse( SolrQueryResponse rsp )
	{
		SolrDocument sdoc = new SolrDocument();
		
		Collection<String> fls = StrUtils.splitSmart( req.getParams().get( "fl" , "id,state" ) , ',' );

        if (fls.contains("*"))
            fls = doc.getFieldNames();

		for ( String field : fls )
		{
			Collection<Object> vals = doc.getFieldValues( field );
			
			if (vals==null || vals.size()==0)
				sdoc.setField( field, null );
			else if (vals.size()== 1)
				sdoc.setField( field, vals.iterator().next() );
			else
				sdoc.setField( field, vals );
		}

        Collection<Object> errors = null;

        if (doc.containsKey(Fields.ERROR))
            errors = new HashSet<>(doc.getFieldValues( Fields.ERROR ));

        if (errors!=null && oldDoc!=null && oldDoc.containsKey(Fields.ERROR))
            errors.removeAll(oldDoc.getFieldValues( Fields.ERROR ));
		
		rsp.add("processAdd",sdoc);	
		
		if (this.newVersion != null)
			sdoc.setField("newVersion" , this.newVersion);
		
		if (errors != null && errors.size()>0)
			rsp.add("processError",errors);

        log.trace("id:{} errors:{} sdoc:\n{}",id,errors,sdoc);
		
		SimpleOrderedMap<Object> header = (SimpleOrderedMap<Object>) rsp.getValues().get("responseHeader");
		header.add("id" , id );

        //lastDoc = doc;

	}

    protected void handleAdd()
	{
		
		//log.info( "*************** ContentManager *************** id:"+doc.getFieldValue(Fields.ID) );
		
		try
		{
			/*if ( doc.getFieldValues( Fields.STATE )==null )
				doc.setField( Fields.STATE , new ArrayList<Object>() );
				
			if ( doc.getFieldValues( Fields._REQUESTED _STATE_ )==null )
				doc.setField( Fields._REQUESTED _STATE_ , new ArrayList<Object>() );	
				
			ContentManager.doRemove( doc, Fields.STATE, docFlags.SYNCED );
			
			if ( doc.getFieldValues( Fields._REQUESTED _STATE_ ).contains( docFlags.SYNCED ))
			{
				doc.addField( Fields._REQUESTED _STATE_, docFlags.CLEAN );
				doc.addField( Fields._REQUESTED _STATE_, docFlags.PROFILE );
				doc.addField( Fields._REQUESTED _STATE_, docFlags.CONTENT );
				doc.addField( Fields._REQUESTED _STATE_, docFlags.XHTML );
			}*/
			
			
			initStream();
			
			log.debug("handleAdd id:{} stream:{} \n{}" , id, this.contentStream, doc );
			
			initAdd();

            //initAliases();
			
			prepareDocument();
			
			//if ( doc.getFieldValues( Fields._REQUESTED _STATE_ ).contains( docFlags.SET_CLEAN ) )
			//	doc.addField(Fields.STATE, docFlags.CLEAN );
			
			//if ( !operation.equals(Operations.SYNC) )
			//	checkStream();
				
			//alignFields(doc);
			
			if ( requestedState.contains( docFlags.NOTEXISTS ))
				checkName();

            if (this.contentStream != null)
            {
                ((ContentStreamBase)this.contentStream).setName( (String) doc.getFieldValue(Fields.NAME));
                ((ContentStreamBase)this.contentStream).setSourceInfo(id);
            }
  		
			/*String aliasName = schema.getAlias( type, Fields.NAME );
			if (aliasName != null)
				doc.setField( aliasName, doc.getFieldValue( Fields.NAME) );*/
				
			if ( requestedState.contains( docFlags.CLEAN ))
				checkParent();
			
			applyAliases();

			runProcessAdd();
			
			applyValidations();
				
			/*if ( !operation.equals(Operations.CREATE))
				cleanVersions();*/
				
			if ( requestedState.contains( docFlags.CLEAN ))
				cleanChildren();

            //if ( requestedState.contains( docFlags.CACHE ))

            Integer rVer = req.getParams().getInt( "restoreVersion" , -1 );
            boolean newVer = req.getParams().getBool( "newVersion" , false );

            if ( rVer>=0 )
            {
                proxy.restoreVersion(rVer,newVer);
            }
            else if ( newVer )
            {
				if ( !doc.getFieldValues( Fields.STATE ).contains( docFlags.CONTENT ) )
				{
					if (this.contentStream != null)
						proxy.write(this.contentStream);
					else /* if ( !doc.getFieldValues( Fields.STATE ).contains( docFlags.CONTENT ) ) */
						proxy.sync();
				} /*else if (this.contentStream != null)
				{
					proxy.writeCache(this.contentStream);
				}*/
				
				this.newVersion =  proxy.createVersion( this.contentStream );
				
				if ( !requestedState.contains( docFlags.CONTENT ) && this.contentStream != null)
				{
					proxy.writeCache(this.contentStream);
				}
			}
			else
			{
                if (this.contentStream != null)
                {
                    proxy.write(this.contentStream);
                }
                else if ( doc.containsKey( Fields.CONTENT_MODIFIED_ON ) && !doc.getFieldValues( Fields.STATE ).contains( docFlags.CONTENT ) )
                {
                    proxy.sync();
                }
				else if (this.contentStream!=null)
				{
					proxy.writeCache(this.contentStream);
				}
			}
			
			if ( requestedState.contains( docFlags.PROFILE ))
			{
                if ( !doc.getFieldValues( Fields.STATE ).contains( docFlags.PROFILE ))
                {
                    doc.addField( Schema.Fields.STATE , Schema.docFlags.PROFILE );
                    ContentManager.doRemove( doc, Schema.Fields.ERROR , Schema.docFlags.PROFILE );
                    //proxy.updateProfile();
                }
			}

            if ( doc.containsKey( Fields.LOCK_TO ) )
            {
                Date lock_to;

                this.proxy = new ProviderProxy(doc,req);

                Object appo =  doc.getFieldValue(Fields.LOCK_TO);

                if (appo instanceof Date)
                    lock_to = (Date) appo;
                else
                    lock_to = FieldUtils.parseDate(appo.toString());

                /*if ( lock_to.after( new Date() ) )
                    proxy.share();
                else
                    proxy.unshare();*/
            }

            /*if ( requestedState.contains(docFlags.SHARED))
            {
                if ( !doc.getFieldValues( Fields.STATE ).contains( docFlags.SHARED ))
                    proxy.share();
            }
            else if ( !doc.getFieldValues( Fields.STATE ).contains( docFlags.SHARED ))
            {
                proxy.unshare();
            }*/

            /* se il documento ha file */
			if ( doc.containsKey( Fields.CONTENT_MODIFIED_ON ) )
			{
                /* se l'update dell'xhtml è richiesto nella richiesta */
				boolean requested = requestedState.contains( docFlags.XHTML ) ;

                //TODO
                // per questioni di performance si potrebbe configurare se aggiornare implicitamente il campo poichè esso richiede
                //la lettura di un file dal filesystem dalla cache xhtml

                /* è richiesto oppure lo aveva già */
				if ( requested || doc.getFieldValues( Fields.STATE ).contains( docFlags.XHTML ))
					proxy.updateXHTML( requested );
                /* l'aggiornamento del campo xhtml è effettuato se è esplicitamente richiesto oppure se è presente in cache */


				Collection<Object> states =  doc.getFieldValues( Fields.STATE );
				Collection<Object> errors =  doc.getFieldValues( Fields.ERROR );

                if ( states.contains( docFlags.PROFILE ) && states.contains( docFlags.CLEAN ) && (states.contains( docFlags.XHTML ) || errors!=null && errors.contains( docFlags.XHTML )) && states.contains( docFlags.CONTENT ) )
                    doc.addField( Fields.STATE , docFlags.SYNCED );
            }
			else
			{
				Collection<Object> states =  doc.getFieldValues( Fields.STATE );
				if ( states.contains( docFlags.PROFILE ) && states.contains( docFlags.CLEAN ) )
					doc.addField( Fields.STATE , docFlags.SYNCED );
			}
			
			ContentManager.doRemove( doc, Fields.ERROR, docFlags.SERVER_ERROR );
            doc.removeField( "error_message" );

            log.info("end workflow:{}",id);
		}
		catch( SolrException solrExc )
		{
            log.error("Error id:{} operation:{}",id,operation , solrExc );
			//String operation = (String) doc.getFieldValue( Fields._OPERATION_ );

            if ( Operations.SYNC.equals(operation) && solrExc.code() == SolrException.ErrorCode.SERVER_ERROR.code )
            {
                doc.addField( Fields.ERROR, docFlags.SERVER_ERROR );
                doc.setField( "error_message" , solrExc.getMessage());
            }
            else if ( Operations.SYNC.equals(operation) && solrExc.code() == SolrException.ErrorCode.CONFLICT.code )
            {
                doc.addField( Fields.ERROR, docFlags.CLEAN );
                doc.setField( "error_message" , solrExc.getMessage());
            }
            else
                throw solrExc;
        }
		catch( Exception e )
		{
            log.error("Error id:{} operation:{}",id,operation , e );

			throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , "Error during workflow:"+doc.getFieldValue(Fields.ID), e ) ;
		}
		
	}


    @SuppressWarnings("unchecked")
	public static void doRemove( SolrInputDocument doc , String field, Object val )
	{
		Collection<Object> vals = doc.getFieldValues(field);
		
		while( vals!= null && vals.remove( val ) )
			doc.setField( field, vals );
	}

    protected void ensureInputDoc()
	{
		ArrayList<String> fields = new ArrayList<>(doc.getFieldNames());
		
		nullFields = new LinkedHashSet<>();
		
		for( String field : fields )
		{
			Object val = doc.getFieldValue(field);
			
			if (val instanceof Map && ((Map)val).containsKey("set") )
			{
				val = ((Map)val).get("set");
				doc.setField(field, val);
			}
			
			if (val!=null && (val.equals("") || val.equals("__NULL__")))
			{
				val = null;
				doc.setField(field, null);
			}
		
			if (val==null)
				nullFields.add(field);
		}

        log.trace("nullFields:{}",nullFields);
	}

    protected void ensureUpdatedDoc()
	{
		Set<String> fields = new LinkedHashSet<>();

        //doc.removeField(Fields.DIVISION);
		
		/* inserisco i campi del nuovo documento per gestire l'operatore set/add/.... */
		fields.addAll( doc.getFieldNames() );
		
		/* inserisco i campi del vecchio documento per prendere il valore */
		fields.addAll( oldDoc.getFieldNames() );

		//TODO rivedere la gestione del null che ora ignora ""

		changedFields = new LinkedHashSet<>();
		
		for( String field : fields )
		{
			boolean changed = false;
			
			Object newValue = doc.getFieldValue(field);
			
			if (nullFields.contains(field))
			{
                log.trace("nullField:{}",field);
				/* annullo il valore */
				//doc.setField(field,null);
				doc.removeField(field);
				changed = oldDoc.containsKey(field);
			}
			else if ( newValue == null )
			{
                log.trace("null:{}",field);
				/* copio il valore ( se il doc contiene null è come assente */
				doc.setField(field, oldDoc.getFieldValue(field) );
			}
			else 
		    {
				Collection<Object> oldValues = oldDoc.getFieldValues(field);
			
				if (oldValues==null)
					oldValues = new LinkedHashSet<>();
				else
					oldValues = new LinkedHashSet<>(oldValues);

                log.trace("oldValues:{}",oldValues);

                if ( newValue instanceof java.util.Map )
                {
                    /* gestisco l'operatore */

                    Object value = oldDoc.getFieldValue(field);

                    Map map = (Map) newValue;

                    log.trace("old value:{} map:{}",value,map);

                    if (map.containsKey("set"))
                    {
                        value = map.get("set") ;
                    }
                    else if (map.containsKey("inc"))
                    {
                        value = map.get("inc") ;

                        if (oldValues.size()>0 && value instanceof Number)
                        {
                            Object oldVal = oldValues.iterator().next();

                            if (oldVal instanceof Number)
                                value = ((Number)oldVal).floatValue() + ((Number) value).floatValue();
                        }
                    }
                    else
                    {
                        for ( Object operator : map.keySet() )
                        {
                            value = map.get(operator) ;

                            if (field.equals( Fields.ACL_EXPLICIT ) )
                                oldValues = DocUtils.applyListOperator("removeacl", value, oldValues) ;

                            value = DocUtils.applyListOperator(operator.toString(), value, oldValues);
                        }
                    }

                    doc.setField( field, value );

                    newValue = value;
                }

                log.trace("new value:",newValue);
				
				if (newValue==null)
                {
					changed = (oldValues.size()>0);
                }
				else
                {
                    Collection<Object> newValues = doc.getFieldValues(field);

                    changed = FieldUtils.diffCollections(newValues, oldValues);

                    /*if (newValues.size() == oldValues.size())
                    {
                        log.trace("comparison old:{} new:{}",oldValues,newValues);

                        Iterator<Object> nv = newValues.iterator();
                        Iterator<Object> ov = oldValues.iterator();

                        while (ov.hasNext())
                        {
                            Object nval = nv.next();
                            Object oval = ov.next();

                            if (nval==null && oval==null)
                                continue;
                            else if (nval==null || oval==null)
                                changed=true;
                            else
                                changed = !nval.toString().equals(oval.toString());

                            if (changed)
                                break;
                        }
                    }
                    else
                    {
                        changed = true;
                    }*/
                    //changed = !ObjectUtils.equals( oldValues , doc.getFieldValues(field) );
                }
			}

            log.trace("field:{} changed:{}",field,changed);
			
			if (changed)
				changedFields.add(field);
		}

        log.trace("changedFields:",changedFields);
		
	}


	
	/*@SuppressWarnings("unchecked")
	private static boolean ensureUpdatedField(String field)
	{
		Object nv = doc.getFieldValue(field);
		Object ov = oldDoc.getFieldValue(field);
		
		if ( oldDoc.getFirstValue(field) instanceof IndexableField )
		{
			SchemaField sf = req.getSchema().getField(field);
			
			if (ov instanceof Collection)
			{
				ArrayList<Object> al = new ArrayList<>();
				
				for ( Object ind : (Collection) ov )
					al.add( sf.getType().toObject( (IndexableField) ind ) );
				
				ov = al;
			}
			else
				ov = sf.getType().toObject( (IndexableField) ov );
			
			oldDoc.setField( field, ov );
		}
		
		if (nv==null)
		{		
			doc.setField(field, ov );
			return false;
		}
		
		
		
		if (nv instanceof java.util.Map)
		{
		
			java.util.Map map = (java.util.Map) nv;
				
			Object[] ops = map.keySet().toArray();

			for (int i=0; i < ops.length; i++)
			{
				String op = ops[i].toString();
				Object operand = map.get(ops[i]);
				Collection<Object> oldVals;
				
				if (oldDoc==null && !op.equals("set"))
					continue;
				
				switch(op)
				{
					case "set":
					
						if (operand==null)
							doc.removeField(field);
						else
							doc.setField(field, operand);
							
						nv = operand;
						break;

					case "add":
						oldVals = oldDoc.getFieldValues(field);
						
						if (oldVals!=null)
						{
							Collection<Object> al;
							
							if (operand instanceof Collection )
								al = new HashSet<Object>((Collection) operand );
							else
								al = Collections.singleton(operand);
							
							if (field.equals( Fields.ACL_EXPLICIT ) )
							{
								for ( Object elem : al )
								{
									String acl = elem.toString().split(":")[0];
									Iterator iter=oldVals.iterator();
									while(iter.hasNext()){
										Object oldElem=iter.next();
										if (  oldElem.toString().indexOf( acl+":" ) == 0 )
											iter.remove();
										
									}

									
								}
							}
							
							for ( Object elem : al )
								if (!oldVals.contains(elem))
									oldVals.add(elem);
						
							doc.setField(field,oldVals);
						}
						else doc.addField(field,operand);
						
						nv = doc.getFieldValues(field);

						break;

					case "inc":
						Integer oldVal = (Integer) oldDoc.getFieldValue(field);
						
						if (oldVal==null)
							oldVal=0;
						
						doc.setField(field,oldVal + ((Integer) operand)  );
						nv = doc.getFieldValue(field);
						
						break;

					case "remove":
						oldVals = oldDoc.getFieldValues(field);

						if (oldVals!=null)
						{
							Collection<Object> al;
							
							if (operand instanceof Collection )
								al = new HashSet<Object>((Collection) operand );
							else
								al = Collections.singleton(operand);
							
							if (field.equals( Fields.ACL_EXPLICIT ) )
							{		
								for ( Object elem : al )
								{
									String acl = elem.toString().split(":")[0];
									Iterator iter=oldVals.iterator();
									while(iter.hasNext()){
										Object oldElem=iter.next();
										if (  oldElem.toString().indexOf( acl+":" ) == 0 )
											iter.remove();
										
									}
									
								}
							}
							else 
								oldVals.removeAll( (Collection) al );
							
							doc.setField(field,oldVals);
						}
						nv = doc.getFieldValues(field);

						break;

					default:
						 throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, "[NOLOG]Unknown operation for the an atomic update, operation ignored: " + op);


				}
			}
		}
		
			
		return !ObjectUtils.equals(nv,ov);
	
	}*/
	

}

