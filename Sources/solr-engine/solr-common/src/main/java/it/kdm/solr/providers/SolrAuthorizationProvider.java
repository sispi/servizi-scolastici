package it.kdm.solr.providers;

import it.kdm.solr.common.DocUtils;
import it.kdm.solr.core.Session;
import it.kdm.solr.common.FieldUtils;
import it.kdm.solr.interfaces.IUserRoles;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.request.SolrQueryRequest;

import java.util.*;

/**
 * Created by Paolo_2 on 29/04/16.
 */
public class SolrAuthorizationProvider implements IUserRoles {

    protected String name;

    @Override
    public void setConfig( Map<String,Object> config )
    {
        name = (String) config.get("name");
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
    @Override
    public Collection<String> getUserRoles( String userId )
    {
        //String organization = null;
        //String userName = userId.split ("@")[0];

		/*if (userId.indexOf("!")!=-1)
		{
			organization = userId.split("!")[0];
			userName = userId.split("!")[1].split(" @")[0];
		}
		else
		{
			userName = userId.split(" @")[0];
		}*/

        SolrDocument userDoc = DocUtils.getDocument(req, FieldUtils.encodeId(userId) + "@user", "groups");

        Set<String> roles = new LinkedHashSet<>();

        if (userDoc==null)
            return roles;

        Collection<Object> userroles = userDoc.getFieldValues( "groups" ) ;

        if (userroles != null)
        {
            for ( Object role : userroles )
            {
                if ( role.equals("root"))
                    roles.add( Session.ROOTROLE );
				/*else if (organization != null)
					roles.add( organization + "!" + role + "@group" );
				else*/
                roles.add( role.toString().split("@")[0] );
            }
        }

        return roles;
    }

    @Override
    public Map<String,Object> getUserProperties( String userId ) {
        return new HashMap<>(); // DocUtils.getDocument( req, FieldUtils.encodeId(userId)+"@user" );
    }
}
