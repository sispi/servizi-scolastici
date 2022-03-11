package it.kdm.solr.core;

import com.google.common.base.Strings;
import it.kdm.solr.client.CoreClient;
import it.kdm.solr.common.DocUtils;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.interfaces.IRepository;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.IndexableField;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ProviderProxy {

    public static final String FILE_DAT = "file.dat";
    public static Logger log = LoggerFactory.getLogger(ProviderProxy.class);
	
	//private IRepository provider = null;
	
	//protected SolrDocument sdoc;
	protected SolrInputDocument idoc;
	private Schema schema = null;
	SolrQueryRequest req = null;
	Session session = null;
	
	public ProviderProxy( SolrDocument doc , SolrQueryRequest req )
	{
		this.req = req;
		
		this.schema = Schema.get(req);
		this.session = Session.get(req);

        DocUtils.convertIndexableFields( req, doc, Schema.Fields.ID, Schema.Fields.NAME, Schema.Fields.STATE, Schema.Fields.CONTENT_VERSIONS, Schema.Fields.EDIT_LINK, Schema.Fields.CONTENT_ID , Schema.Fields.CONTENT_MODIFIED_ON , Schema.Fields.CREATED_ON );

        //sdoc = doc;
        idoc = DocUtils.toSolrInputDocument(doc);
	}
	
	public ProviderProxy( SolrInputDocument doc , SolrQueryRequest req )
	{
		this.req = req;
		
		this.schema = Schema.get(req);
		this.session = Session.get(req);
		
		idoc = doc;
		//sdoc = DocUtils.toSolrDocument(doc);
	}
	
	private String id()
	{
		return (String) idoc.getFieldValue( Schema.Fields.ID );
	}
	
	/*private String division()
	{
		return (String) idoc.getFieldValue( Schema.Fields.DIVISION );
	}*/
	
	/*private String content_id()
	{
		return (String) idoc.getFieldValue( Fields.CONTENT_ID );
	}
	
	private String provider_id()
	{
		if (content_id()==null)
			return null;
	
		int idx = content_id().indexOf( "://" );
			
		if (idx != -1)
			return content_id().substring(idx+3);
		else
			return content_id();
	}*/
	
	/*private String provider()
	{
		if (content_id()==null)
			return null;
	
		int idx = content_id().indexOf( "://" );
			
		if (idx != -1)
			return content_id().substring(0,idx);
		else
			return null;
	}*/
	
	private Date content_modified_on()
	{
		return (Date) idoc.getFieldValue( Schema.Fields.CONTENT_MODIFIED_ON );
	}
	
	/*private String firstVersion()
	{
		Object d = idoc.getFieldValue( Fields.CREATED_ON );
		
		if (d!=null)
		{
			Long ts = ( (Date) idoc.getFieldValue( Fields.CREATED_ON ) ).getTime();
			return "" + ( ts / 1000L );
		}
		else
			return null;
	}*/
	
	private IRepository getRepository( String provider )
	{
		if (provider != null)
			provider = provider + "://";
		return schema.getRepository( req, provider );
	}
	
	private IRepository getDefaultRepository()
	{
		return getRepository( null );
	}
	
	/*private IRepository getRepository()
	{
		return schema.getRepository( req, content_id() );
	}*/
	
	private static String cachepath( String id , Date content_modified_on )
	{


        //String ISO = org.apache.solr.schema.DateRangeField.formatExternal( content_modified_on );

        String ISO = FieldUtils.formatDate(content_modified_on);

	    String slashed = ISO.substring(0,13).replaceAll("[^\\d]","/") ;
		
		String filename = id + ISO.substring(13, ISO.length() -1 ).replaceAll("[^\\d]","\\-") ;
		
		return slashed + "/" + filename;
	}
	
	public File getCacheFile()
	{
		return getCacheFile( schema, id() , content_modified_on() );
	}
	
	public static File getCacheFile( Schema schema , String id, Date content_modified_on )
	{
		if (content_modified_on==null || id==null)
			return null;
		
		String cache = schema.getFileCache();
		
		if (cache==null)
			return null;
		
		/*String path = new File(".").getAbsolutePath() + "/" + cache + "/" + cachepath(id,content_modified_on)+".dat";

        path = FilenameUtils.normalize(path);
        return new File(path);*/

        return FieldUtils.resolvePath(cache + "/" + cachepath(id, content_modified_on) + ".dat");

    }

    public File getXHTMLFile()
    {
        if (content_modified_on()==null)
            return null;
		
/*String path = new File(".").getAbsolutePath() + "/" + schema.getXHTMLCache() + "/" + cachepath( id() , content_modified_on() )+".xhtml";

		path = FilenameUtils.normalize(path);
		return new File(path);*/

        return FieldUtils.resolvePath(schema.getXHTMLCache() + "/" + cachepath(id(), content_modified_on()) + ".xhtml");
    }
	
	/*public java.net.URL getCacheURL( String version_id )
	{
		Session session = Session. get();
		String collection = session.get Collection().getName();
		String ticket = session.ticket;
		
		
		
		org.apache.solr.common.cloud.ZkCoreNodeProps coreNodeProps = new org.apache.solr.common.cloud.ZkCoreNodeProps(Session.getSlice().getLeader());
		
		String baseUrl = coreNodeProps.getBaseUrl().replaceAll("\\/$","");
		
		String url = String.format( "%s/%s/get?wt=raw&fl=id,content_id,content_modified_on&id=%s" , baseUrl , collection , id() );
		
		if (version_id!=null)
			url += "&version="+version_id;
		
		try
		{
			return new java.net.URL(url);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}*/
	
	
	
	public Map<String,Object> getProfile()
	{
		Map<String,Object> hdoc = new HashMap<>();
			
		for ( org.apache.solr.common.SolrInputField sif : idoc.values()) {
			
			Object val = sif.getValue();
			String field = sif.getName();
			
			if (field.equals(Schema.Fields.FULLTEXT))
				continue;
				
			/*if (field.equals(Fields.CONTENT_ID))
			{
				hdoc.put( field, provider_id() ) ;
				continue;
			}*/
			
			org.apache.solr.schema.SchemaField sf = req.getCore().getLatestSchema().getFieldOrNull(field);
			
			if ( val instanceof java.util.Collection )
				val = ((java.util.Collection) val).toArray();
				
			if ( val instanceof Object[] && sf != null && !sf.multiValued() )
			{
				Object[] objs = (Object[]) val;
				
				if (objs.length == 0)
					val = null;
				else	
					val = objs[0];
			}
			
			if ( val instanceof java.util.Date )
                val = FieldUtils.formatDate((java.util.Date) val);
				//val = org.apache.solr.schema.DateField.formatExternal( (java.util.Date) val ) ;
			
			if ( val instanceof IndexableField )
				val = ((IndexableField)val).stringValue() ;
				
			hdoc.put( field, val );
		}
		
		return hdoc;
	}



    public void updateProfile()
	{
        String id = id();

		log.debug( "updateProfile id:{}" , id );
		
		try
		{
			String JSON = org.noggit.JSONUtil.toJSON( getProfile() );
				
			ContentStreamBase.StringStream stream = new ContentStreamBase.StringStream( JSON );
			stream.setName( id + ".json" );
            stream.setSourceInfo(id);
			
			IRepository repo = getDefaultRepository();
			
			String version = getVersion(-1);
			
			if (repoIsChanged())
			{
				ContentStreamBase.StringStream dummy = new ContentStreamBase.StringStream( "\n" );
				dummy.setName( id );
                dummy.setSourceInfo(id);
				
				version = repo.create( dummy );
				
				Properties props = FieldUtils.getVersionProperties(version);
				
				idoc.setField( Schema.Fields.CONTENT_ID, repo.getName()+ PROV_SEP + props.get(Schema.Fields.ID) );
			}
			
			repo.write( version , false, stream );

            log.trace("id:{} version:{} json:{}",id,version,JSON);
			
			idoc.addField( Schema.Fields.STATE , Schema.docFlags.PROFILE );
			ContentManager.doRemove( idoc, Schema.Fields.ERROR , Schema.docFlags.PROFILE );
		}
		catch(Exception e)
		{
            log.error("id:{}", id, e) ;

			idoc.addField( Schema.Fields.ERROR , Schema.docFlags.PROFILE );
			//log.error( "Problem updating profile content id:{}" , id() , e);
			throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , e );
		}
	}


	
	/* scrive in cache senza sincronizzare il provider */
	public void writeCache( ContentStream stream ) { 
	
		log.debug( "write cache id:{}", id() );
		
		//Session session = Session. get();
		
		idoc.setField(Schema.Fields.CONTENT_MODIFIED_ON, new java.util.Date() );
		idoc.setField(Schema.Fields.CONTENT_MODIFIED_BY, session.getId() );
        idoc.setField(Schema.Fields.CONTENT_MODIFIED_DN_BY, session.getDisplayName() );

        //String path = Schema. getInstance().getFileCache() + "/" + getCachePath();
		
		File file = getCacheFile();
		boolean moved = false;
		try
		{
			if ( idoc.containsKey( Schema.Fields._STREAM_FILE_ ) )
			{
				String sourcePath = (String) idoc.getFieldValue(Schema.Fields._STREAM_FILE_);
			
				File source = new File( sourcePath );
				
				if (source.exists())
				{
					try
					{
						stream.getStream().close();
						
						File cacheRoot = new File( schema.getFileCache());
						
						if ( FileUtils.directoryContains(cacheRoot,source) )
							FileUtils.moveFile( source, file );
						else
							FileUtils.copyFile( source, file );
							
						moved = true;
					}
					catch(Exception e )
					{
						log.error("error moving file" , e );
					}
				}

                log.trace("source:{} moved:{}",sourcePath,moved);
			}
			
			if (!moved)
				FileUtils.copyInputStreamToFile( stream.getStream() , file);
			
			idoc.setField(Schema.Fields.CONTENT_SIZE, file.length() );
            idoc.setField(Schema.Fields.CONTENT_TYPE, getStreamType(stream) );
            idoc.addField( Schema.Fields.STATE , Schema.docFlags.CACHE );
			ContentManager.doRemove( idoc, Schema.Fields.STATE, Schema.docFlags.CONTENT );
			ContentManager.doRemove( idoc, Schema.Fields.ERROR , Schema.docFlags.CACHE );
			
			//return ;//file.toString();
		}
		catch ( Exception e )
		{
			idoc.addField( Schema.Fields.ERROR , Schema.docFlags.CACHE );
			//log.error( "Problem writing cache content id:{}" , id() , e);
			throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , "Problem writing cache content id" , e );
		}
	}
	
	/*private String lastVersion()
	{
		Collection<Object> actualVersions = idoc.getFieldValues( Fields.CONTENT_VERSIONS );
		
		if (actualVersions!=null && actualVersions.size()>0)
			return new ArrayList<Object>(actualVersions).get( actualVersions.size()-1 ).toString();
		else
			return null;
	}*/
	
	public boolean repoIsChanged()
	{
		/*Collection<Object> actualVersions = idoc.getFieldValues( Fields.CONTENT_VERSIONS );
		
		if (actualVersions==null || actualVersions.size()==0)
			return false;
			
		String last = actualVersions.get( actualVersions.size()-1 );*/
		
		String prov = getProvider(-1);
		
		if ( prov == null )
			return true;
		//String prov = content_id().split(":")[0];
		
		IRepository repo = getDefaultRepository(); 
		
		String prov2 = repo.getName();

		return !prov.equals(prov2);
	}
	
	private int getStreamSize( ContentStream stream )
	{
		try
		{
			if (stream.getSize() != null)
				return stream.getSize().intValue();
			else
				return stream.getStream().available() ;
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}		
	
	public void sync()
	{
        log.info("sync id:{}",id() );

		File file = getCacheFile();
		
		if (file==null || !file.exists())
			throw new SolrException(SolrException.ErrorCode.CONFLICT, "Nothing to sync from cache" ) ;
		
		ContentStream stream = new ContentStreamBase.FileStream(file);
		
		writeToProvider(stream);
	}

    final static MimetypesFileTypeMap ct_map = new MimetypesFileTypeMap();

    public static String getStreamType(ContentStream stream)
    {
        String name = stream.getName();
        String ct = null;

        if (name!=null)
            ct = ct_map.getContentType(name);

        if (ct==null)
            return "application/octet-stream";
        else
            return ct;
    }

    public void write( ContentStream stream )
    {
        log.info("write id:{} stream:{}",id(),stream);

        String editlink = idoc.containsKey(Schema.Fields.EDIT_LINK) ? (String) idoc.get(Schema.Fields.EDIT_LINK).getFirstValue() : null;

        if (!Strings.isNullOrEmpty(editlink))
            throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "Document is shared by edit link" );

        idoc.setField(Schema.Fields.CONTENT_MODIFIED_ON, new java.util.Date() );
        idoc.setField(Schema.Fields.CONTENT_MODIFIED_BY, session.getId() );
        idoc.setField(Schema.Fields.CONTENT_MODIFIED_DN_BY, session.getDisplayName() );
        idoc.setField(Schema.Fields.CONTENT_SIZE, getStreamSize(stream) );
        idoc.setField(Schema.Fields.CONTENT_TYPE, getStreamType(stream) );

        writeToProvider(stream);
    }
	
	static final String PROV_SEP = "://";
	
	/* sincronizza il provider a partire dalla cache  */
	private void writeToProvider( ContentStream stream )
	{
		log.debug( "write content to provider id:{} stream:{}", id(),stream );
		
		//String content_id = content_id();
		
		try
		{
			//File file = new File( Schema. getInstance().getFileCache() + "/" + getCachePath() );
			
			/* il sync è dalla cache*/
			/*if (touch)
			{
				idoc.setField(Fields.CONTENT_MODIFIED_ON, new java.util.Date() );
				idoc.setField(Fields.CONTENT_MODIFIED_BY, session.getId() );
				idoc.setField(Fields.CONTENT_SIZE, getStreamSize(stream) );
			}*/
			
			//boolean result = false;
			
			/* se il file è null vuol dire che questo oggetto non ha file associato */
			//if (stream!=null)
			//{
				IRepository repo = getDefaultRepository();
				
				//((ContentStreamBase)stream).set Name(id());
                ((ContentStreamBase)stream).setSourceInfo(id());

                if (Strings.isNullOrEmpty(stream.getName()))
                    ((ContentStreamBase)stream).setName(FILE_DAT);
				
				String lastVersion;

                Properties metaData = getMetaData();
				
				/* se content_id è null vuol dire che il documento non è mai stato sincronizzata, va quindi creata la prima versione */
				if ( repoIsChanged() )
				{
					//repo = schema.getRepository(req,null);
					
					//((ContentStreamBase)stream).set Name(id());
					
					//String newId =  repo.create( stream );
					
					lastVersion = repo.create( stream );
					
					Properties props = FieldUtils.getVersionProperties(lastVersion);

                    String cid = repo.getName()+ PROV_SEP + props.get(Schema.Fields.ID);

                    log.debug("lastVersion:{} cid:{}",lastVersion,cid);
					
					idoc.setField( Schema.Fields.CONTENT_ID, cid );

                    metaData.put(Schema.Fields.CREATED_ON, new Date());
                    metaData.put(Schema.Fields.CREATED_BY, session.getId());
                    metaData.put(Schema.Fields.CREATED_DN_BY, session.getDisplayName());


                    //Collection<String> provVersions = repo.listVersions( provider_id() );
				
					//lastVersion = content_id() + repo.lastVersion(); //doVersionString( provVersions.iterator().next() , stream );
					
					/* quando si crea la prima versione si scrive anche il profilo */
					//idoc.addField( Fields.STATE , docFlags.PROFILE );
					ContentManager.doRemove( idoc, Schema.Fields.ERROR , Schema.docFlags.PROFILE );


				}
				else /* va sostituita la versione corrente */
				{
					//repo = getRepository();
					//content_id = repo.getName()+"://"+ repo.write( provider_id() , stream );
					
					//((ContentStreamBase)stream).set Name(id());
					
					lastVersion = repo.write( getVersion(-1) , false, stream ) ;

                    log.debug("lastVersion:{}",lastVersion);
				}
				
				//String lastVersion = content_id() + "?" + repo.lastVersion(provider_id());
				
				Collection<Object> actualVersions = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS );
				
				List<Object> newVersions = new ArrayList<>();
				
				if (actualVersions!=null)
				{
					newVersions.addAll(actualVersions);


                    /*** Gestione custom version properties

                     String versionStr = (String) newVersions.get(newVersions.size() -1);

                     Properties previousProps = FieldUtils.getVersionProperties(versionStr);

                     Properties newProps = FieldUtils.getVersionProperties(lastVersion);

                     boolean flag = false;

                     for( String key : previousProps.stringPropertyNames() )
                     {
                     if (key.startsWith(META_VERSION_PFX))
                     {
                     newProps.setProperty(key,previousProps.getProperty(key));
                     flag = true;
                     }
                     }

                     if (flag)
                     lastVersion = FieldUtils.doVersionString(newProps);

                     fine gestione custom version properties ***/

                    newVersions.set( newVersions.size() -1 , repo.getName()+ PROV_SEP + lastVersion );
				}
				else
				{
					newVersions.add( repo.getName() + PROV_SEP + lastVersion);
				}

                log.debug("actualVersion:{} newVersions:{}",actualVersions,newVersions);
				
				idoc.setField(Schema.Fields.CONTENT_VERSIONS, newVersions );
				
			/*	result = true;
			}
			else
			{
				log.warn("missing cache file {}",content_id);
			}*/

            setMetaData(metaData);
			
			idoc.addField( Schema.Fields.STATE , Schema.docFlags.CONTENT );
			
			ContentManager.doRemove( idoc, Schema.Fields.STATE , Schema.docFlags.XHTML );
			ContentManager.doRemove( idoc, Schema.Fields.ERROR , Schema.docFlags.CONTENT );
			
			//return result;
		}
		catch (Exception e)
		{
            log.error("error id:{}",id(),e);

			idoc.addField( Schema.Fields.ERROR , Schema.docFlags.CONTENT );
			
			//if (content_id==null)
			//	idoc.addField( Fields.ERROR , docFlags.PROFILE );
			//log.error( "Problem writing to provider content id:{}" , id() , e);
			throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , "Problem writing to provider content", e );
		}
	}
	
	/* creazione di una versione sul provider */
	public int createVersion( ContentStream stream )
	{
		log.debug( "createVersion id:{}", id() );

		String editlink = idoc.containsKey(Schema.Fields.EDIT_LINK) ? (String) idoc.get(Schema.Fields.EDIT_LINK).getFirstValue() : null;

		if (!Strings.isNullOrEmpty(editlink))
			throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "Document is shared by edit link" );


		try
		{
			Collection<Object> states =  idoc.getFieldValues( Schema.Fields.STATE );
			if (states == null || !states.contains( Schema.docFlags.CONTENT ))
				throw new SolrException(SolrException.ErrorCode.CONFLICT, "Not possible to create version if content is out of sync" ) ;

            //List<Object> versions = new ArrayList<>( idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS ) );

            Properties metaData = getMetaData();

            if (idoc.containsKey(Schema.Fields.CONTENT_MODIFIED_ON))
                metaData.put(Schema.Fields.MODIFIED_ON, idoc.get(Schema.Fields.CONTENT_MODIFIED_ON).getFirstValue());

            if (idoc.containsKey(Schema.Fields.CONTENT_MODIFIED_BY))
                metaData.put(Schema.Fields.MODIFIED_BY, idoc.get(Schema.Fields.CONTENT_MODIFIED_BY).getFirstValue());

            if (idoc.containsKey(Schema.Fields.CONTENT_MODIFIED_DN_BY))
                metaData.put(Schema.Fields.MODIFIED_DN_BY, idoc.get(Schema.Fields.CONTENT_MODIFIED_DN_BY).getFirstValue());

            if (idoc.containsKey(Schema.Fields.CONTENT_SIZE))
                metaData.put(Schema.Fields.CONTENT_SIZE, idoc.get(Schema.Fields.CONTENT_SIZE).getFirstValue());

            if (idoc.containsKey(Schema.Fields.CONTENT_TYPE))
                metaData.put(Schema.Fields.CONTENT_TYPE, idoc.get(Schema.Fields.CONTENT_TYPE).getFirstValue());

            if (idoc.containsKey(Schema.Fields.CONTENT_COMMENT))
                metaData.put(Schema.Fields.CONTENT_COMMENT, idoc.get(Schema.Fields.CONTENT_COMMENT).getFirstValue());

            for( String field : idoc.getFieldNames())
            {
                if (field.startsWith("content__"))
                    metaData.put(field, idoc.get(field).getFirstValue());
            }

            setMetaData(metaData);

            log.debug("updated current metadata:{}",metaData);

			/* è una nuova versione con nuovo file */
			if (stream != null)
			{
                log.trace("stream:{}",stream);

				idoc.setField(Schema.Fields.CONTENT_MODIFIED_ON, new java.util.Date() );
				idoc.setField(Schema.Fields.CONTENT_MODIFIED_BY, session.getId() );
                idoc.setField(Schema.Fields.CONTENT_MODIFIED_DN_BY, session.getDisplayName() );
                idoc.setField(Schema.Fields.CONTENT_SIZE, getStreamSize(stream) );
                idoc.setField(Schema.Fields.CONTENT_TYPE, getStreamType(stream) );
			}
			else /* è una nuova versione copia della precedente */
			{
				List<Object> versions = new ArrayList<>( idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS ) );

                log.trace("versions:{}",versions);
		
				if (versions==null || versions.size() == 0)
				{
					throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "No version to copy" );
				}
				else
				{
					stream = downloadVersion( versions.size() -1 + FIRSTVERSION );
				}
			}
			
			IRepository repo = getDefaultRepository();
			
			((ContentStreamBase)stream).setSourceInfo(id());

            if (Strings.isNullOrEmpty(stream.getName()))
                ((ContentStreamBase)stream).setName(FILE_DAT);

            String newVersion;
			
			if ( repoIsChanged() )
			{
				//repo = schema.getRepository(req,null);
				
				//String provider_id = repo. create( getProfile() , stream );
				
				//((ContentStreamBase)stream).set Name(id());

                newVersion = repo.create( stream );

                Properties props = FieldUtils.getVersionProperties(newVersion);

                String cid = repo.getName()+ PROV_SEP + props.getProperty(Schema.Fields.ID );

                log.debug("lastVersion:{} cid:{}",newVersion,cid);
				
				idoc.setField( Schema.Fields.CONTENT_ID, cid );
				
				//Collection<String> provVersions = repo.listVersions( provider_id() );
			
				//version_id = repo.lastVersion() ; //provVersions.iterator().next() ;
				
				/* quando si crea la prima versione si scrive anche il profilo */
				//idoc.addField( Fields.STATE , docFlags.PROFILE );
				ContentManager.doRemove( idoc, Schema.Fields.ERROR , Schema.docFlags.PROFILE );
			}
			else
			{			
				//repo = getRepository();
				
				//((ContentStreamBase)stream).set Name(id());

                newVersion = repo.write( getVersion(-1), true, stream ) ;

                log.debug("lastVersion:{}",newVersion);
			}
			
			//String version_id = content_id() + "?" + repo.lastVersion(provider_id()) ;

            String cv = repo.getName()+ PROV_SEP + newVersion;

            log.debug("content versions:{}",cv);

            idoc.addField(Schema.Fields.CONTENT_VERSIONS, cv );
            idoc.removeField(Schema.Fields.CONTENT_COMMENT);

            metaData = getMetaData(); /* ora c'è un'altra versione */
            metaData.put(Schema.Fields.CREATED_ON, new Date());
            metaData.put(Schema.Fields.CREATED_BY, session.getId());
            metaData.put(Schema.Fields.CREATED_DN_BY, session.getDisplayName());
            setMetaData(metaData);

            log.debug("created new metadata:{}", metaData);
				
			idoc.addField( Schema.Fields.STATE , Schema.docFlags.CONTENT );
			
			ContentManager.doRemove( idoc, Schema.Fields.STATE, Schema.docFlags.XHTML );
			ContentManager.doRemove( idoc, Schema.Fields.ERROR , Schema.docFlags.CONTENT );
			
			idoc.setField(Schema.Fields.CONTENT_SIZE, getStreamSize( stream ) );

            idoc.setField(Schema.Fields.CONTENT_TYPE, getStreamType(stream) );
			
			return idoc.getFieldValues(Schema.Fields.CONTENT_VERSIONS).size();
		}
		catch( Exception e)
		{
            log.error("id:{}",id(),e);

			idoc.addField( Schema.Fields.ERROR , Schema.docFlags.CONTENT );
			//log.error( "Problem creating version id:{}" , id() , e);
			throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , "Problem creating version" , e );
		}
	}

    public void	restoreVersion( int version, boolean newVersion )
    {
        String version_id = getVersion(version);

        log.debug("version:{} version_id:{}",version,version_id);

        if (version==-1)
            version = lastVersion();

        if (version == lastVersion())
        {
            log.warn("done nothing to restore last version", version, version_id);
            return;
        }

        String repo = getProvider(version);

        ContentStream cs = getRepository(repo).downloadVersion(version_id);

        Properties ps = getMetaData();

        for( String p : ps.stringPropertyNames() )
        {
            if (p.startsWith("content__"))
                idoc.setField(p, ps.getProperty(p));
        }

        if (newVersion)
            this.createVersion(cs);
        else
            this.writeToProvider(cs);

    }
	
	public void	deleteVersion( int version )
	{
        String version_id = getVersion(version);

        log.debug("version:{} version_id:{}",version,version_id);

        if (version==-1)
            version = lastVersion();

        if (version == FIRSTVERSION && lastVersion() == FIRSTVERSION )
            throw new SolrException(SolrException.ErrorCode.CONFLICT, "Not possibile to delete the unique version" );

        String repo = getProvider(version);

        getRepository(repo).deleteVersion( version_id );
	}
	
	public void	delete()
	{
        log.debug("delete id:{}",id());

		if ( !repoIsChanged() )
		{
			getDefaultRepository().delete( getVersion(-1) );
		}
	}

    public Properties getMetaData()
    {
        String v = getVersion(-1);

        int idx = 0;

        if (v!=null)
            idx = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS ).size();

        Collection c = idoc.getFieldValues( FieldUtils.getMetaDataField(idx) );

        if (c!=null && c.size()>0)
            return FieldUtils.getVersionProperties((String) c.iterator().next());
        else
            return new Properties();
    }

    private void setMetaData(Properties props)
    {
        String v = getVersion(-1);

        int idx = 0;

        if (v!=null)
            idx = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS ).size();

        String vStr = FieldUtils.doVersionString(props);

        idoc.setField( FieldUtils.getMetaDataField(idx) , vStr);
    }

    public int lastVersion()
    {
        Collection<Object> vers = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS );

        if (vers==null || vers.size()==0)
            return 0;
        else
            return vers.size()-1+FIRSTVERSION;
    }
	
	private String getVersion( int idx )
	{
		Collection<Object> vers = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS );

        if (vers==null || vers.size()==0)
			return null;
		
		List<Object> versions = new ArrayList<>( vers );
		
		String version;
		if (idx==-1)
			version = versions.get( versions.size()-1 ).toString() ;
		else
			version = versions.get( idx-FIRSTVERSION ).toString() ;

        String editlink = (String) idoc.getFieldValue( Schema.Fields.EDIT_LINK );
        Collection<Object> states = idoc.getFieldValues( Schema.Fields.STATE );

        if (editlink!=null)
        {
            editlink = FieldUtils.decrypt(editlink, schema.getSecret());

            version += "&" + FieldUtils.encodeUrlPart("sharedlink", editlink);
        }

        if (states.contains(Schema.docFlags.SHARED))
            version += "&" + FieldUtils.encodeUrlPart("shared", "true");

        return version.split(PROV_SEP)[1];
	}
	
	private String getProvider( int idx )
	{
		Collection<Object> vers = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS );
		
		if (vers==null || vers.size()==0)
			return null;
		
		List<Object> versions = new ArrayList<>( vers );
		
		String version;
		if (idx==-1)
			version = versions.get( versions.size()-1 ).toString() ;
		else
			version = versions.get( idx-FIRSTVERSION ).toString() ;
		
		return version.split(PROV_SEP)[0];
	}
	
	/*public Map<String,Object> read() { 
	
		return getRepository().read( provider_id() );
	}*/
	
	public ContentStream downloadLastVersion() {
		
		File file = getCacheFile();

        String editlink = idoc.containsKey(Schema.Fields.EDIT_LINK) ? (String) idoc.get(Schema.Fields.EDIT_LINK).getFirstValue() : null;

        ContentStream stream;
		
		if ( Strings.isNullOrEmpty(editlink) && file != null && file.exists() )
		{
			stream = new ContentStreamBase.FileStream(file);

            log.debug("id:{} stream from cache:{}",id(),file);
		}
		else
		{
			Collection<Object> actualVersions = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS );
			
			if(actualVersions==null || actualVersions.size()==0)
				throw new SolrException(SolrException.ErrorCode.CONFLICT, "No cache and no last version" );

            stream = null;

            int idx = actualVersions.size()-1+FIRSTVERSION;

            /* dobbiamo trovare la prima versione non cancellata */
            while (stream==null && idx>= FIRSTVERSION )
            {
                stream = downloadVersion( actualVersions.size()-1+FIRSTVERSION );
                idx--;
            }

            log.debug("id:{} stream from last version:{}",id(),stream);
			
			//Properties prop = getVersionProperties( lastVersion );
			
			//stream = getRepository( prop.getProperty("provider") ).downloadVersion( prop.getProperty("provider_id") , prop.getProperty("version_id") );
			
			if (file!=null)
			{
				try
				{
					FileUtils.copyInputStreamToFile( stream.getStream() , file );
				}
                catch(FileNotFoundException fnf)
                {
                    log.error( "Non è stato trovare il file {}" , file.toString() );
                    throw new SolrException( SolrException.ErrorCode.NOT_FOUND ,  fnf );
                }
				catch(Exception e)
				{
					log.error( "Non è stato possibile salvare il file in cache {}" , file.toString() );
                    throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , "Problem writing cache content id" , e );
				}
			}
		}

        String name = (String) idoc.getFieldValue(Schema.Fields.NAME);

        if (name==null)
            name = FILE_DAT;

        ((ContentStreamBase)stream).setName(name);
        ((ContentStreamBase)stream).setSourceInfo(id());
		
		return stream;
	}

    /*public void unshare() {

        //int rights = DocUtils.getUserRights( req, sdoc , Schema.Rights.updateACL );

        //if (rights==0)
        //    throw new SolrException( SolrException.ErrorCode.FORBIDDEN , "Current user can't unshare"  );

        String editlink = idoc.containsKey(Schema.Fields.EDIT_LINK) ? (String) idoc.get(Schema.Fields.EDIT_LINK).getFirstValue() : null;

        if (Strings.isNullOrEmpty(editlink))
        {
            ContentManager.doRemove(idoc, Schema.Fields.STATE , Schema.docFlags.SHARED);
            return;
        }

        Collection<Object> actualVersions = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS );

        if(actualVersions==null || actualVersions.size()==0)
            throw new SolrException(SolrException.ErrorCode.CONFLICT, "No version to unshare" );

        int version = actualVersions.size()-1+FIRSTVERSION ;

        getRepository( getProvider(version) ).unshare(getVersion(version));

        idoc.removeField(Schema.Fields.EDIT_LINK);
        //sdoc.removeFields(Schema.Fields.EDIT_LINK);

        ContentManager.doRemove( idoc, Schema.Fields.STATE , Schema.docFlags.SHARED );
    }

    public void share() {

        String editlink = idoc.containsKey(Schema.Fields.EDIT_LINK) ? (String) idoc.get(Schema.Fields.EDIT_LINK).getFirstValue() : null;

        if (!Strings.isNullOrEmpty(editlink))
        {
            idoc.addField( Schema.Fields.STATE , Schema.docFlags.SHARED );
            return;
        }

        Collection<Object> actualVersions = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS );

        if(actualVersions==null || actualVersions.size()==0)
            throw new SolrException(SolrException.ErrorCode.CONFLICT, "No version to share" );

        int version = actualVersions.size()-1+FIRSTVERSION ;

        editlink = getRepository( getProvider(version) ).share(getVersion(version));

        editlink = FieldUtils.encrypt(editlink, schema.getSecret());

        idoc.setField(Schema.Fields.EDIT_LINK, editlink );
        //sdoc.setField(Schema.Fields.EDIT_LINK, editlink);

        idoc.addField( Schema.Fields.STATE , Schema.docFlags.SHARED );

        //return;
    }*/
	
	//final public static String LASTVERSION = "last";
	
	public ContentStream downloadVersion( int version ) { 
		
		ContentStream stream = getRepository( getProvider(version) ).downloadVersion( getVersion(version) );

        String name = (String) idoc.getFieldValue(Schema.Fields.NAME);

        if (name==null)
            name = FILE_DAT;

        ((ContentStreamBase)stream).setName(name);
        ((ContentStreamBase)stream).setSourceInfo(id());

        return stream;
		
	
		
		
		
		
		/*if (version_id!=null && version_id.equals(LASTVERSION))
			version_id = ""+versions.size();
		
		if (version_id!=null && version_id.equals(FIRSTVERSION) && versions.size()==1)
			version_id = null;
		
		if (version_id!=null)
		{
			int idx = Integer.parseInt(version_id);
			if ( versions.size() != 1 || idx != 1 )
			{
				version_id = versions.get( idx-1 ).toString();
				
				Properties prop = getVersionProperties(version_id);
		
				return getRepository( prop.getProperty("provider") ).downloadVersion( prop.getProperty("provider_id") , prop.getProperty("version_id") );
				
				
			}
		}
	
		
	
		File file = getCacheFile();
		
		try
		{
			if (file==null || !file.exists() )
			{
				version_id = versions.get( versions.size()-1 ).toString();
				
				ContentStream stream;
				
				Properties prop = getVersionProperties(version_id);
		
				stream = getRepository( prop.getProperty("provider") ).downloadVersion( prop.getProperty("provider_id") , prop.getProperty("version_id") );
				
				
				
				if (file!=null)
					FileUtils.copyInputStreamToFile( stream.getStream() , file );
				else
					return stream;
			}
			return new ContentStreamBase.FileStream(file);
		}
		catch( Exception e )
		{
			throw new RuntimeException(e);
		}*/
	}

    final public static int FIRSTVERSION = 1;

	public Collection<Integer> listVersions() 
	{
		if ( content_modified_on() == null )
			return null;
		
		
		
		//IRepository repo = getRepository();
				
		//Collection<String> provVersions = repo.listVersions( provider_id() );
		
		Collection<Object> vers = idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS );
		
		if (vers==null || vers.size()==0)
			return Collections.singletonList(FIRSTVERSION);
		
		List<Integer> versions = new ArrayList<>();
		
		for( Object ver : vers )
			versions.add( versions.size()+FIRSTVERSION );

        log.trace("versions:{}",versions);
		
		return versions;
		
		/*if ( content_modified_on() == null )
			return null;
		
		if ( content_id()==null )
		{
			String fv = firstVersion();
			if (fv!=null)
				return Collections.singletonList(firstVersion());
			else
				return null;
		}
		
		List<String> versions = new ArrayList<>( getRepository().listVersions( provider_id() ) );
		
		versions.set( versions.size()-1 , firstVersion() );
		
		return versions;*/
	}
	

	
	
	
	@SuppressWarnings("unchecked")
	public void updateXHTML( boolean requested ) 
	{
		try
		{
			log.debug( "updateXHTML id:{} requested:{}", id(), requested );
			
			List<Object> versions = new ArrayList<>( idoc.getFieldValues( Schema.Fields.CONTENT_VERSIONS ) );
			
			File file = getXHTMLFile();
			
			if (file == null || versions.size() == 0)
				throw new SolrException(SolrException.ErrorCode.CONFLICT, "No version to index" );
			
			idoc.removeField(Schema.Fields.FULLTEXT);
			idoc.removeField(Schema.Fields.FULLTEXT_SIZE) ;
			idoc.removeField(Schema.Fields.FULLTEXT_ON);
			
			ContentManager.doRemove( idoc, Schema.Fields.STATE, Schema.docFlags.XHTML );

            /* se non c'è in cache e non è esplicitamente richiesto */
			if (!file.exists() && !requested)
				return;
			
			String contentText;
			
			if (file.exists())
			{
				contentText = org.apache.commons.io.FileUtils.readFileToString( file , StandardCharsets.UTF_8.name() );

                log.debug("file exists:{}", file);
			}
			else /* l'estrazione avviene se è esplicitamente richiesto e non c'è in cache */
			{
				ContentStreamUpdateRequest request = new ContentStreamUpdateRequest("/extract");
				request.setParam( Schema.Params.TICKET , Session.ROOTTICKET );
				request.getParams().set( Schema.Params.SHORTCUT , true );
				
				ContentStream stream = downloadVersion( versions.size() -1 + FIRSTVERSION );
				request.addContentStream( stream );
				
				NamedList nl = CoreClient.getInstance().request(request);
				
				contentText = (String) nl.get( null );
				
				if (contentText==null)
					contentText = (String) nl.get( stream.getName() );
				
				log.debug("tika response id:{} size:{} file:{}\n{}", id(), contentText.length() , file, contentText );
				
				org.apache.commons.io.FileUtils.writeStringToFile( file , contentText , StandardCharsets.UTF_8.name() );
			}
			
			idoc.setField(Schema.Fields.FULLTEXT , contentText);
			idoc.setField(Schema.Fields.FULLTEXT_SIZE, contentText.length() ) ;
			idoc.setField(Schema.Fields.FULLTEXT_ON, new Date() );
			
			idoc.addField(Schema.Fields.STATE, Schema.docFlags.XHTML );
			ContentManager.doRemove( idoc, Schema.Fields.ERROR, Schema.docFlags.XHTML );
			
				/*for (int i=0; i< nl.size() && contentText == null ; i++)
				{
					String key = nl.getName(i);
					
					if (key==null)
						continue;
					
					int idx = key.indexOf("_metadata");
					
					if (idx >0)
						contentText = (String) nl.get( key.substring(0,idx) );
				}
				
				if (contentField == null)
					throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Invalid response from tika" );
				
				
				
				if (contentText == null && contentField.equals("null"))
					contentText = (String) nl.get( null );
				
				if (contentText == null)
					contentText = "";*/
				
			
			
		}
		catch( Exception e )
		{
			
			//if (cause!=null && cause.getClass().getName().indexOf("TikaException") != -1 )
			if (e instanceof org.apache.tika.exception.TikaException || e.getMessage().contains("TikaException"))
			{
				log.warn( "Tika can't extract full text id:{} ",id(),e);
				idoc.addField(Schema.Fields.ERROR, Schema.docFlags.XHTML );
                idoc.setField("error_message", e.getMessage());
			}
			else
            {
                log.error("id:{}",id(),e);
				throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , "Problem extracting content:"+id(), e ) ;
            }
			//log.error( "Problem extracting content id:{}" , id() , e);
			//throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , e );
		}
		
		//return null;
		//return doc.getFieldValues(Fields.STATE);
	}
	
	
	
}