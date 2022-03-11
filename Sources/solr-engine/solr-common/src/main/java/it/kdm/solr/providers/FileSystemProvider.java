package it.kdm.solr.providers;

import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.interfaces.IRepository;
import org.apache.commons.io.FileUtils;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.request.SolrQueryRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;


public class FileSystemProvider implements IRepository {

    //public static final String DEF_EXT = ".dat";
    public static Logger log = LoggerFactory.getLogger(FileSystemProvider.class);
	
	protected String Store; 
	//protected String fssecret;
	//protected Boolean autologin;
	protected String name;
    protected String Temp;

    //protected Collection<String> hostfilter = null;
	
	@Override
	public void setConfig( Map<String,Object> config )
	{
		Store = (String) config.get("store");

        if (Store.endsWith("/"))
            Store = Store.substring(0,Store.length()-1);

        Temp = Store+"/temp";

        //fssecret = (String) config.get("secret");
		//autologin = (Boolean) config.get("autologin");
		name = (String) config.get("name");


        //hostfilter = (Collection<String>) config.get("hostfilter");
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
	@SuppressWarnings("unchecked")
	public Map<String,Object> readProfile( String content_id ) { 
	
		try
		{
			File file = getJSONFile(content_id);
		
			String json = FileUtils.readFileToString(file, " UTF-8" );
			
			Map<String,Object> doc = (Map<String,Object>) ObjectBuilder.fromJSON(json);
		
			return doc; 
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}*/
	
	protected String newStorePath( String id )
	{
		if (id.endsWith(".json") || id.endsWith(".xml") )
		{
			int idx = id.lastIndexOf(".");
			id = id.substring(0,idx);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH/"); 
		
		id = id.replaceAll( "[\\/\\\\\\:\\*\\?\\\"\\<\\>\\|]" , "_" );
		
		id = id.replaceAll( "[!\\.]+" , "/" );
		
		int idx = id.lastIndexOf("/") ;
		
		//String path = id.substring(0,idx+1) + sdf.format(new Date()) + id.substring(idx+1) ;
		
		return id.substring(0,idx+1) + sdf.format(new Date()) + id.substring(idx+1) ;
	}
	
	/*@Override
	public void updateProfile( Map<String,Object> doc )
	{
		try
		{
			String content_id = (String) doc.get( Fields.CONTENT_ID );
		
			String JSON = org.noggit.JSONUtil.toJSON( (java.util.Map<String,Object>) doc );
				
			File jsonFile = getJSONFile(content_id);
			
			FileUtils.writeStringToFile(jsonFile, JSON , " UTF-8" );
		}
		catch(Exception e )
		{
			throw new RuntimeException(e);
		}
	}*/
	
	
	
	protected File getVersionFile( String version_id )
	{
		Properties pairs = FieldUtils.getVersionProperties(version_id);
		
		/*int idx0 = version_id.indexOf( "path=" );
		int idx1 = version_id.indexOf( "&" , idx0 );
		if (idx1==-1)
			idx1 = version_id.length();
		
		String path = version_id.substring( idx0+5 , idx1 );*/
		
		String path = pairs.getProperty("path");
		String content_id = pairs.getProperty("id");
		
		if ( path.indexOf("/") != 0 )
			path = content_id + "/" + path;
		
		return new File( Store + "/" + path );
	}
	
	@Override
	public String create( ContentStream stream ) {
	
		try
		{
			String name = stream.getName();
            String id = stream.getSourceInfo();
			
			//File file;
			
			//String ext = name.replaceAll(".*@.*(\\.[^\\.]+)","$1");
			
			//String content_id = newStorePath( name );
			String version;
			String path;
			
			if (name.endsWith(".json") || name.endsWith(".xml"))
			{
				//name = name.substring( 0,name.length()-ext.length() );
				//content_id = newStorePath( name );
				version="profile";
				//path = name+ext;
				path = name;
			}
			else
			{
				//content_id =  newStorePath(name);
				//Long ts = (System.currentTimeMillis() / 1000L)-1;

                version = ""+(System.currentTimeMillis()-1);

                path = version + "." + name.substring(name.lastIndexOf(".")+1) ;
				//file = getVersion( content_id , String.format( verFormat , ts , ".dat" ) );
			}
			
			String versionString = FieldUtils.doVersionString(newStorePath(id), version, path);
			
			File file = getVersionFile( versionString );
			
			//file = getVersion( content_id, "path="+name+ext );

            safeSave( stream, file );
			
			return versionString;
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e);
		}
	}

    public void safeSave( ContentStream stream , File file ) throws IOException
    {
        file.getParentFile().mkdirs();

        // path del file specificato in querystring con stream.file
        if (stream instanceof ContentStreamBase.FileStream )
        {
            File sourceFile=null;
            try
            {
                Field m = ContentStreamBase.FileStream.class.getDeclaredField("file");
                m.setAccessible(true);
                sourceFile = (File) m.get(stream);

                //se source è dentro lo store temp
                if (sourceFile.getPath().startsWith(Temp+"/") && file.getPath().startsWith(Store+"/"))
                {
                    Files.move(sourceFile.toPath(), file.toPath() , StandardCopyOption.ATOMIC_MOVE );
                    log.info("direct filesystem moved source:{} dest:{}",sourceFile,file);
                    return;
                }
                /*else
                {
                    Files.copy(sourceFile.toPath(), file.toPath() , StandardCopyOption.ATOMIC_MOVE );
                    log.info("direct filesystem copied source:{} dest:{}",sourceFile,file);
                }*/

            }
            catch(AtomicMoveNotSupportedException e)
            {
                log.warn("stream move not support atomic_move source:{} dest:{}", sourceFile, file,e);
            }
            catch(Exception e)
            {
                log.error("Could not directly copy stream:{}",stream);
            }
        }


        // copia lo stream http in un file temporaneo e poi lo sposta in modo atomico

        File tempFile = new File(file.getParent(), UUID.randomUUID().toString() ); // Files.createTempFile(UUID.randomUUID().toString(), (String) null);

        Path tempPath = tempFile.toPath();

        Files.copy( stream.getStream() , tempPath , StandardCopyOption.REPLACE_EXISTING );

        try
        {
            Files.move(tempPath, file.toPath() , StandardCopyOption.ATOMIC_MOVE , StandardCopyOption.REPLACE_EXISTING );
            log.info("stream moved from temp source:{} dest:{}",tempPath,file);
            return;
        }
        catch(Exception e)
        {
            if (!FileUtils.deleteQuietly( tempFile ))
                log.warn("could not delete temp file:{}", tempPath);

            log.error("stream move failed source:{} dest:{}", tempPath, file,e);
            throw e;
        }

        /*File backUp = null;
        try
        {
            if (file.exists())
            {
                backUp = new File(file.getParent(), "file.bak");
                Files.move( file.toPath(), backUp.toPath() , StandardCopyOption.REPLACE_EXISTING , StandardCopyOption.ATOMIC_MOVE);

                log.info("backed up existing file:{}",backUp);
            }

            Files.copy(stream.getStream(), file.toPath());
            //Files.copy( tempPath , file.toPath() );

            log.info("stream copied from stream to dest:{}", file);
        }
        catch(Exception e)
        {
            log.error("stream copy source to {}", file,e);
            if (backUp!=null)
            {
                Files.move(backUp.toPath(),file.toPath(), StandardCopyOption.ATOMIC_MOVE);
                log.warn("backup restored:{}", file);
            }
            throw e;
        }*/






    }
	
	/*private String doVersionString( String path , File file )
	{
		return "path="+path;
	}*/
	
	/* write e createVersion ritornano il versionId */
	@Override
	public String write( String version_id , boolean newVersion, ContentStream stream ) { 
	
		try
		{
			/*Collection<String> versions = listVersions(content_id);
			
			if (versions==null || versions.size()==0)
				return createVersion( content_id , stream );
			
			String lastVersion = versions.iterator().next();*/
			
			//String lv = lastVersion(content_id);
			
			//String name = stream.getName();
			
			//String ext = name.replaceAll(".*@.*(\\.[^\\.]+)","$1");
			
			Properties pairs = FieldUtils.getVersionProperties(version_id);
			
			String content_id = pairs.getProperty("id");
			
			File file = getVersionFile( version_id );
			String name = stream.getName();
			
			String path = file.getName();
			String version = pairs.getProperty("version");

            if (name.endsWith(".json") || name.endsWith(".xml"))
			{
				version = "profile";
				path = name;
			}
            else if (newVersion)
            {
                version = ""+(System.currentTimeMillis()-1);
                path = version + "." + name.substring(name.lastIndexOf(".")+1) ;
            }
			
			file = new File( file.getParent() , path );
			
			//File file;
			/*if (ext!="")
			{
				file = getVersion( "id="+content_id+"&path="+name );
			}
			else
			{
				file = getVersion( version_id );
			}*/

            safeSave( stream, file );
			
			return FieldUtils.doVersionString(content_id, version, path);
			//return lv;
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public ContentStream downloadVersion( String version_id ) { 
		
		File file = getVersionFile( version_id );

        return new ContentStreamBase.FileStream( file );
	}
	
	@Override
	public void	deleteVersion( String version_id ) { 
		
		try
		{
			File file = getVersionFile( version_id );
			file.delete();
		}
		catch( Exception e )
		{
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void	delete( String version_id ) { 
		
		try
		{
			File file = getVersionFile(version_id).getParentFile();
			FileUtils.deleteDirectory(file);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

    @Override
    public String share(String version_id) {
        return null;
    }

    @Override
    public void unshare(String version_id) {
    }
	
	/*private static Map<String, Collection<String> > groups;
	
	static 
	{
		groups = new HashMap<String, Collection<String> >();
		
		groups.put( "amoresano" 	, 	Arrays.asList( "admins_aoo_40", "gruppo1" , "gruppo2", "gruppo3" ) );
		groups.put( "nina" 	, 	Arrays.asList( "gruppo1"  ) );
		groups.put( "dandy" 	, 	Arrays.asList( "gruppo2", "gruppo3" ) );
		groups.put( "king" 	, 	Arrays.asList( "root" ) );
	}*/
	

}