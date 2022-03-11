package it.kdm.solr.interfaces;

import java.util.Collection;
import java.util.Map;

public interface IUserRoles extends IProvider {
	public Collection<String> getUserRoles( String userId );
    public Map<String,Object> getUserProperties( String userId );
}
