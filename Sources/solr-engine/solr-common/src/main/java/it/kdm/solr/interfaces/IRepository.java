package it.kdm.solr.interfaces;

import org.apache.solr.common.util.ContentStream;

public interface IRepository extends IProvider {

	public ContentStream downloadVersion( String version_id );
	
	public String create( ContentStream stream );
	
	public String write( String version_id , boolean newVersion, ContentStream stream );
	
	public void	deleteVersion( String version_id );
	
	public void	delete( String version_id );

    public String share(String version_id);

    public void unshare(String version_id);
	
}