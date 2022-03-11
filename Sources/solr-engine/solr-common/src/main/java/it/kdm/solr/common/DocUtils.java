package it.kdm.solr.common;

import com.google.common.base.Strings;
import it.kdm.solr.client.CoreClient;
import it.kdm.solr.client.SolrClient;
import it.kdm.solr.core.Schema;
import it.kdm.solr.core.Schema.Fields;
import it.kdm.solr.core.Session;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.BytesRefBuilder;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.cloud.CloudDescriptor;
import org.apache.solr.cloud.ZkController;
import org.apache.solr.common.*;
import org.apache.solr.common.cloud.ClusterState;
import org.apache.solr.common.cloud.DocCollection;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.RealTimeGetComponent;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.request.SolrRequestInfo;
import org.apache.solr.schema.FieldType;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.RefCounted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Function;


@SuppressWarnings("unchecked")
public class DocUtils {

    private transient static Logger log = LoggerFactory.getLogger(DocUtils.class);

    public static void testFile(String file) throws IOException , SolrServerException
    {
        String coll = CoreClient.getInstance().getDefaultCollection();

        String path = String.format("/configs/%s/%s",coll,file);

        SolrZkClient zkClient = CoreClient.getInstance().getCoreContainer().getZkController().getZkClient();

        String jsonString = Schema.loadJSONFile(zkClient, path, null);

        jsonString = jsonString.replaceAll( "/\\*[^*]+\\*/" , "" );

        NamedList<Object> testNl = FieldUtils.parseJSON(jsonString);

        NamedList<Object> params = (NamedList<Object>) testNl.findRecursive("responseHeader","params");

        List<NamedList<Object>> ndocs = (List<NamedList<Object>>) testNl.findRecursive("response", "docs");

        Map<String,SolrDocument> testdocs = new HashMap<>();

        for( NamedList<Object> ndoc : ndocs )
        {
            Iterator<Map.Entry<String,Object>> idoc = ndoc.iterator();

            SolrDocument doc = new SolrDocument();

            while( idoc.hasNext() )
            {
                Map.Entry<String,Object> entry = idoc.next();
                doc.addField(entry.getKey(), entry.getValue());
            }

            String id = (String) ndoc.remove("id");

            testdocs.put(id, doc);
        }

        ModifiableSolrParams solrParams = new ModifiableSolrParams();

        for (Map.Entry<String, Object> entry : params) {
            solrParams.add(entry.getKey(), entry.getValue().toString());
        }

        QueryRequest request = new QueryRequest(solrParams);

        NamedList<Object> result = SolrClient.getInstance().request(request);

        SolrDocumentList resultdocs = (SolrDocumentList) result.get("response");

        for( SolrDocument resultdoc : resultdocs )
        {
            String id = (String) resultdoc.getFieldValue("id");
            SolrDocument testdoc = testdocs.remove(id);

            if (testdoc==null)
                throw new RuntimeException("not found in test list:"+id);

            for( String key : testdoc.keySet() )
            {
                Collection<Object> testValues = testdoc.getFieldValues(key);
                Collection<Object> resultValues = resultdoc.getFieldValues(key);

                if ( FieldUtils.diffCollections(testValues, resultValues) )
                    throw new RuntimeException( id + " differs on " + key );
            }
        }

        if (testdocs.size()>0)
            throw new RuntimeException( "test list differs" );

    }

    public static boolean validateRange( Collection<Object> vals , String range )
    {
        if (vals==null)
            return true;

        String min = range.split(" TO ")[0].trim();
        String max = range.split(" TO ")[1].trim();

        if (min.equals("*"))
            min =null;

        if (max.equals("*"))
            max = null;

        log.trace("min:{} max:{} vals:{}",min,max,vals);

        for( Object val : vals )
        {
            try
            {
                if (val==null)
                {
                    return false;
                }
                else if (val instanceof Date)
                {
                    Date date = (Date) val;

                    if (min!=null && date.compareTo( FieldUtils.parseDate(min) ) == -1 )
                        return false;

                    if (max!=null && date.compareTo( FieldUtils.parseDate(min) ) == 1 )
                        return false;
                }
                else if (val instanceof Number )
                {
                    Double num = ((Number)val).doubleValue();

                    if (min!=null && num.compareTo( NumberFormat.getInstance().parse(min).doubleValue() ) == -1 )
                        return false;

                    if (max!=null && num.compareTo( NumberFormat.getInstance().parse(max).doubleValue() ) == 1 )
                        return false;
                }
                else
                {
                    if (min!=null && val.toString().compareToIgnoreCase(min) == -1)
                        return false;
                    if (max!=null && val.toString().compareToIgnoreCase(max) == 1)
                        return false;
                }
            }
            catch(Exception e )
            {
                log.warn("range exc",e);
                return false;
            }
        }
        return true;
    }

    private static SolrDocumentList select(Object q)
    {
        try
        {
            if (SolrRequestInfo.getRequestInfo()!=null)
            {
                SolrQuery params = new SolrQuery();
                params.set( Schema.Params.TICKET , Session.ROOTTICKET );
                params.set( Schema.Params.SHORTCUT , true );
                params.setRequestHandler("/select");
                params.setQuery((String)q);
                params.setRows( 1000 );
                SolrDocumentList result = CoreClient.getInstance().query( params ).getResults();
                return result;
            }
            else
                throw new SolrException(SolrException.ErrorCode.INVALID_STATE,"request not found");
        }
        catch( Exception e )
        {
            throw new SolrException(SolrException.ErrorCode.SERVER_ERROR,e);
        }
    }

    private static SolrDocument getOrNull( Object id  )
    {
        try
        {
            if (SolrRequestInfo.getRequestInfo()!=null)
            {
                SolrQuery params = new SolrQuery();
                params.set( "ticket" , Session.ROOTTICKET );
                params.set( "shortcut" , true );
                params.set( Fields.ID , (String) id );
                params.setFields( "*" );

                try
                {
                    return CoreClient.getInstance().get( params );
                }
                catch( SolrServerException e )
                {
                    throw new RuntimeException(e);
                }
            }
            else
                throw new SolrException(SolrException.ErrorCode.INVALID_STATE,"request not found");
        }
        catch (SolrException se )
        {
            if (se.code() == SolrException.ErrorCode.NOT_FOUND.code )
                return null;
            throw se;
        }
    }

    public static Object eval(String operation,final SolrInputDocument doc, String expr) {

        Class cls = null;
        try {
            cls = Class.forName( "it.kdm.solr.common.ExpressionBindings" );
        } catch (ClassNotFoundException e) {
            throw new SolrException(SolrException.ErrorCode.INVALID_STATE, "ExpressionBindings not available");
        }

        try
        {
            Method method = cls.getMethod("eval",Map.class, String.class);

            Map<String,Object> map = new Map<String,Object>() {

                private Map<String,Object> toMap()
                {
                    Map m = new HashMap<String,Object>();
                    for( Object key : this.keySet())
                        m.put(key,this.get(key));
                    return m;
                }

                @Override
                public Object get(Object key) {
                    SolrInputField field = doc.get(key);
                    if (field==null)
                        return null;
                    if (field.getValue() instanceof Collection)
                        return field.getValues();
                    else
                        return field.getFirstValue();
                }
                @Override
                public Object put(String key, Object value) {
                    Object old = this.get( (String) key );
                    doc.setField( (String) key, value );
                    return old;
                }
                @Override
                public void putAll(Map m) {
                    for( Object key : m.keySet() )
                        this.put((String) key,m.get(key));
                }
                @Override
                public int size() { return doc.size(); }
                @Override
                public boolean isEmpty() { return doc.isEmpty(); }
                @Override
                public boolean containsKey(Object key) { return doc.containsKey(key);}
                @Override
                public boolean containsValue(Object value) {return toMap().containsValue(value); }
                @Override
                public Object remove(Object key) {return doc.remove(key);}
                @Override
                public void clear() { doc.clear(); }
                @Override
                public Set keySet() { return new HashSet(doc.getFieldNames()); }
                @Override
                public Collection values() { return toMap().values(); }
                @Override
                public Set<Entry<String, Object>> entrySet() { return toMap().entrySet(); }
                @Override
                public boolean equals(Object o) {return toMap().equals(o);}
                @Override
                public int hashCode() {return toMap().hashCode();}

                @Override
                public String toString() { return doc.toString(); };
            };

            Map<String,Object> context = new HashMap<>();

            context.put("logger", DocUtils.log );
            context.put("document" , map );
            context.put("$", (Function<Object,SolrDocument>) DocUtils::getOrNull );
            context.put("get", (Function<Object,SolrDocument>) DocUtils::getOrNull );
            context.put("select", (Function<Object,SolrDocumentList>) DocUtils::select );
            context.put("NULL","__NULL__");
            context.put("operation",operation);
            context.put("NOW", FieldUtils.formatDate(new Date()) );

            if (SolrRequestInfo.getRequestInfo()!=null)
            {
                SolrQueryRequest req = SolrRequestInfo.getRequestInfo().getReq();
                context.put("user", Session.get(req).getId());
                context.put("req", req);
                context.put("rsp", SolrRequestInfo.getRequestInfo().getRsp());
                context.put("NOW", FieldUtils.formatDate(SolrRequestInfo.getRequestInfo().getNOW()) );
            }





            Object result = method.invoke(null, context,expr );

            if (result==null)
                return null;

            if (result instanceof java.util.Date)
                result = FieldUtils.formatDate((java.util.Date) result);

            return result.toString();
        }
        catch( Exception e )
        {
            log.error("Could not run expression",e);
            throw new RuntimeException(e);
        }
    }

    public static boolean validateRegex( Collection<Object> vals , String regex )
	{
		if (vals==null)
			return true;

        log.trace("regex:{} vals:{}",regex,vals);

		for( Object val : vals )
		{
			if (val==null || !val.toString().matches(regex))
				return false;
		}
		return true;
	}

    public static boolean validateSize( int size , String sizeStr )
	{
		int min;
		int max;

		int idx = sizeStr.indexOf(',');

		if (idx==-1)
		{
			min = Integer.parseInt(sizeStr);
			max = min;
		}
		else
		{
			min = Integer.parseInt(sizeStr.substring(0,idx));
			max = Integer.parseInt(sizeStr.substring(idx+1));
		}

        log.trace("size:{} min:{} max:{}",size,min,max);

		return min<=size && size<=max;
	}

    @SuppressWarnings("unchecked")
    public static Collection<Object> applyListOperator(String op, Object newValue , Collection<Object> oldValues )
    {
        log.debug("op:{} newValue:{} oldValues:{}",op,newValue,oldValues);

        HashSet<Object> newValues = new LinkedHashSet<>();
                
        if (newValue instanceof Collection)
            newValues.addAll( (Collection) newValue);
        else if (newValue != null)
            newValues.add(newValue);
        
        switch(op)
        {
            case "add":
                
                oldValues.addAll(newValues);			
                break;

            case "remove":
                oldValues.removeAll(newValues);
                break;
                
            case "removeacl":
            case "removeregex":
                Iterator<Object> iterator = oldValues.iterator();
                while( iterator.hasNext() )
                {
                    String str = iterator.next().toString();
                    for( Object val : newValues )
                    {
                        String regex = val.toString();
                        
                        if ( op.equals("removeacl") &&  str.startsWith(regex.split(":")[0]+":") || op.equals("removeregex") && str.matches(regex) )
                        {
                            iterator.remove();
                            break;
                        }
                    }
                }
                break;

            default:
                log.warn("Unknown operation for the an atomic update, operation ignored: " + op);
                break;

        }

        log.debug("newValues:{} oldValues:{}",newValues,oldValues);

        return oldValues;
    }

    public static interface Expirable
	{
		boolean isExpired();
		Object getObject();
	}

    /*public static Object getFirstValue( Collection<Object> values )
    {
        Object firstValue = null;

        if (values.size() > 0 ) {
            firstValue = values.iterator().next();
        }

        return firstValue;
    }*/

    /**
     * @param d SolrDocument to convert
     * @return a SolrInputDocument with the same fields and values as the
     *   SolrDocument.  All boosts are 1.0f
     */
    public static SolrInputDocument toSolrInputDocument( SolrDocument d )
    {
        SolrInputDocument doc = new SolrInputDocument();
        for( String name : d.getFieldNames() ) {
            doc.addField( name, d.getFieldValue(name), 1.0f );
        }
        return doc;
    }

    /**
     * @param d SolrInputDocument to convert
     * @return a SolrDocument with the same fields and values as the SolrInputDocument
     */
    public static SolrDocument toSolrDocument(SolrInputDocument d) {
        SolrDocument doc = new SolrDocument();
        for (SolrInputField field : d) {
            doc.setField(field.getName(), field.getValue());
        }
        if (d.getChildDocuments() != null) {
            for (SolrInputDocument in : d.getChildDocuments()) {
                doc.addChildDocument(toSolrDocument(in));
            }

        }
        return doc;
    }


    @SuppressWarnings("unchecked")
	public static Object convertIndexableField( SolrQueryRequest req, SolrDocument doc, String field)
	{
        Object firstValue = doc.getFirstValue(field);

        if ( firstValue instanceof IndexableField )
        {
            Object fv = doc.getFieldValue(field);

            SchemaField sf = req.getSchema().getField(field);

            if (fv instanceof Collection)
            {
                ArrayList<Object> al = new ArrayList<>();

                for ( Object ind : (Collection) fv )
                    al.add( sf.getType().toObject( (IndexableField) ind ) );

                firstValue = al.get(0);
                doc.setField( field, al );
            }
            else
            {
                firstValue = sf.getType().toObject( (IndexableField) fv );
                doc.setField( field, firstValue );
            }
        }

        return firstValue;
	}
	
	@SuppressWarnings("unchecked")
	public static void convertIndexableFields( SolrQueryRequest req, SolrDocument doc, String... fields)
	{
		for ( String field : fields )
		{
            convertIndexableField( req, doc, field );
		}
	}
	
	/*public static BytesRef[] bytesRefs( SolrQueryRequest req, String fname, SolrDocumentList docs )
	{
		Collection<BytesRef> bytesRefs = new ArrayList<>();
		
		FieldType ft = req.getSchema().getFieldTypeNoEx(fname);
		
		for ( SolrDocument doc : docs ) {
			
			BytesRef term = new BytesRef();
			
			Object val = doc.getFieldValue( Fields.ID );
			
			if (ft != null) {
				ft.readableToIndexed(val.toString() , term);
			} else {
				term.copyChars(val.toString() );
			}
			
			bytesRefs.add( term );
		}
		
		return bytesRefs.toArray( new BytesRef[ bytesRefs.size() ] );
	}*/
	
	/*public static BytesRef[] bytesRefs( SolrQueryRequest req, String fname, NamedList<Object> nl )
	{
		Collection<BytesRef> bytesRefs = new ArrayList<>();
		
		FieldType ft = req.getSchema().getFieldTypeNoEx(fname);
		
		for ( Map.Entry<String,Object> entry : nl ) {
			
			BytesRef term = new BytesRef();
			
			if (ft != null) {
				ft.readableToIndexed( entry.getKey() , term);
			} else {
				term.copyChars( entry.getKey() );
			}
			
			bytesRefs.add( term );
		}
		
		return bytesRefs.toArray( new BytesRef[ bytesRefs.size() ] );
	}*/

    public static BytesRef[] bytesRefs( java.lang.String... terms )
    {
        BytesRef[] bytesRefs = new BytesRef[terms.length];
        int i=0;
        for ( String val : terms ) {
            BytesRef term = new BytesRef(val);
            bytesRefs[i++] = term;
        }
        return bytesRefs;
    }

	public static BytesRef[] bytesRefs( SolrQueryRequest req, String fname, Collection<String> vals )
	{
		FieldType ft = null;
        if (req!=null)
            ft = req.getSchema().getFieldTypeNoEx(fname);

        BytesRef[] bytesRefs = new BytesRef[vals.size()];
        int i=0;

        if (ft!=null)
        {
            for ( String val : vals ) {
                BytesRefBuilder term = new BytesRefBuilder();
                ft.readableToIndexed(val, term);
                bytesRefs[i++] = term.toBytesRef();
            }
        }
        else
        {
            for ( String val : vals ) {
                BytesRef term = new BytesRef(val);
                bytesRefs[i++] = term;
            }
        }
		
		return bytesRefs;
	}



    public static SolrDocument getDocument( SolrQueryRequest req, String id, String... fields )
	{
		ZkController zkController = req.getCore().getCoreContainer().getZkController();
		CloudDescriptor cloudDescriptor = req.getCore().getCoreDescriptor().getCloudDescriptor();
		String collection = cloudDescriptor.getCollectionName();
		ClusterState clusterState = zkController.getClusterState();
		DocCollection coll = clusterState.getCollection(collection);
		
		String remoteShard = Schema.getRouter().getTargetSlice( id , null , null, null , coll ).getName();
		String localShard = cloudDescriptor.getShardId();
		
		if (fields==null || fields.length==0)
			fields = Fields.COMMON_FIELDS;
		
		//CoreContainer coreContainer = req.getCore().getCoreDescriptor().getCoreContainer();
		
		//TODO NTH ottimizzare trovando l'eventuale core remote incluso nel coreContainer
		
		SolrCore core = null;
		
		if (remoteShard.equals(localShard))
			core = req.getCore();
		
		if (core!=null)
			return getCoreDocument(req,core,id,fields);
		else
			return getRemoteDocument(remoteShard,id,fields);
	}
	
	private static SolrDocument getRemoteDocument( String shard , String id , String[] fields )
	{
		SolrQuery params = new SolrQuery();
		params.set( "ticket" , Session.ROOTTICKET );
		params.set( "shortcut" , true );
		params.set( "shards" , shard );
		params.set( Fields.ID , (String) id );
		//params.set( ShardParams._ROUTE_ , (String) doc.getFieldValue( schema.getRouteField() ) );
		params.setFields( fields );
		
		try
		{		
			return CoreClient.getInstance().get( params );
		}
		catch( Exception e )
		{
			throw new RuntimeException(e);
		}
	}
	
	private static SolrDocument getCoreDocument( SolrQueryRequest req, SolrCore core , String id , String[] fields ) {
	
		SchemaField idField = core.getLatestSchema().getUniqueKeyField();
		FieldType fieldType = idField.getType();
		BytesRefBuilder idBytes = new BytesRefBuilder();
		fieldType.readableToIndexed(id, idBytes);
		
		SolrInputDocument sid = RealTimeGetComponent.getInputDocumentFromTlog(core,idBytes.toBytesRef(),null,null,true);
		
		if (sid==null)
		{
			RefCounted<SolrIndexSearcher> searcherHolder = null;
			
			try {
				searcherHolder = core.getRealtimeSearcher();
				
				SolrIndexSearcher searcher = searcherHolder.get();
				
				int docid = searcher.getFirstMatch(new Term(idField.getName(), idBytes));
				   
				if (docid < 0) 
					throw new SolrException(SolrException.ErrorCode.NOT_FOUND, id+" not found" );
				
				Document doc = searcher.doc(docid);
				
				sid = new SolrInputDocument();
				
				//for( IndexableField f : doc.getFields() ) {
				//  sid.addField( f.name() , f);
				
				for( int i=0; i<fields.length; i++)
				{
					SchemaField sf = req.getSchema().getField(fields[i]);
					
					IndexableField[] ifs = doc.getFields(fields[i]);

                    for (IndexableField anIf : ifs) {
                        Object v = sf.getType().toObject(anIf);
                        sid.addField(fields[i], v);
                    }
				}
				
				if (!sid.containsKey(Fields.ID))
					sid.addField( Fields.ID , doc.get(Fields.ID)  );
			}
			catch( IOException ioe )
			{
				throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, ioe );
			}
			finally
			{
				if (searcherHolder != null) {
					searcherHolder.decref();
				}
			}
		}
		
		//SolrDocument doc = DocUtils.toSolrDocument(sid);

		//ContentManager.ensureFields( req, null, doc, Fields.ID  );
		//ContentManager.ensureFields( req, null, doc, fields  );
		
		return DocUtils.toSolrDocument(sid);
	}
	
	/*static boolean checkRights( SolrInputDocument doc , Integer mask ) 
	{
		if (doc==null)
			return checkRights( (SolrDocument) null , mask );
		else
			return checkRights( ClientUtils.toSolrDocument(doc) , mask );
	}*/
	
	public static int getUserRights( SolrQueryRequest req, SolrDocument doc , int mask , String userId ) throws SolrException
	{
		Session session = Session.createUserSession(req, userId);
		
		return getUserRights(req,doc,mask,session);
	}
	
	public static int getUserRights( SolrQueryRequest req, SolrDocument doc , int mask  ) throws SolrException
	{
		return getUserRights(req,doc,mask,Session.get(req));
	}
	
	private static int getUserRights( SolrQueryRequest req, SolrDocument doc , int mask , Session session ) throws SolrException
	{
		String sessionId = session.getId();
		
		Schema schema = Schema.get(req);
		
		int rights = session.getGlobalAcl();
		
		if ( (rights & mask) == mask )
			return mask;
			
		if ( doc == null || mask == 0 )
			return 0;

        String id = (String) convertIndexableField( req, doc, Fields.ID );
			
		String type = id.split("@")[1];
		
		String created_by = (String) convertIndexableField( req, doc, Fields.CREATED_BY );
		
		if ( sessionId!= null && sessionId.equalsIgnoreCase(created_by) )
			rights |= schema.getProfileRight( type, Fields.CREATED_BY );
		
		//if ( session.id.equals(created_by) && (Schema. getInstance().getProfileRight( type, Fields.CREATED_BY ) & mask ) == mask)
		//	return true;
			
		String modified_by = (String) convertIndexableField(req, doc, Fields.MODIFIED_BY);

        Object appo = convertIndexableField(req, doc, Fields.LOCK_TO);

        if (!(appo instanceof Date))
            throw new RuntimeException("Invalid LOCK_TO type");

        Date lock_to = (Date) appo;

		//assert(lock_to!=null);
		assert(created_by!=null);
		assert(modified_by!=null);
		
		Collection<String> roles = session.getRoles();
		
		if ( lock_to.after( new Date() ) && !modified_by.equalsIgnoreCase( sessionId ) )
		{
			rights &= Schema.Rights.readonlymask;
			mask &= Schema.Rights.readonlymask;
		}
		
		rights |= internalGetRights( roles, req, schema, doc, mask, rights );
		
		/* è una modifica, il lock è valido e l'utente corrente non è l'ultimo modificatore */
		/*if ( lock_to.after( new Date() ) && !modified_by.equals( sessionId ) )
			rights = (rights & Schema.Rights.readonlymask) | internalGetRights( roles, req, schema, doc, mask & Schema.Rights.readonlymask , rights );
		else*/
		
		
		/*if (!res && throwException)
			throw new SolrException(SolrException.ErrorCode.FORBIDDEN, "[NOLOG]Operation not allowed: "+mask );
		return res;*/
		return rights & mask;
	}

    public static String getSharedLink(SolrQueryRequest req, SolrDocument doc)
    {
        int rights = DocUtils.getUserRights(req, doc, Schema.Rights.writeContent);

        if (rights==0)
            return null;

        String editlink = (String) doc.get(Schema.Fields.EDIT_LINK);

        if (!Strings.isNullOrEmpty(editlink))
        {
            Schema schema = Schema.get(req);
            return FieldUtils.decrypt(editlink, schema.getSecret()) ;
        }
        else
            return null;
    }
	
	/*private static boolean checkRightsNoExc( String id, String route , Integer mask ) 
	{
		SolrDocument doc = getDocument( id , Fields.COMMON_FIELDS );
		
		try
		{
			if (doc==null)
				doc = Solr Client.getInstance().get( id , route , Fields.COMMON_FIELDS );
		}
		catch(Exception e )
		{
			throw new RuntimeException(e);
		}
		
		return checkRightsNoExc( doc, mask );
	}*/
	
	/*private static boolean checkRightsNoExc( SolrDocument doc , Integer mask ) 
	{
		Session session = Session. get();
		
		if (mask==null)
			return false;
		
		if ( (session.globalacl & mask) == mask )
			return true;
			
		if ( doc == null )
			return false;
			
		String type = ((String) doc.getFieldValue(Fields.ID)).split(" @")[1];
		
		String created_by = (String) doc.getFieldValue( Fields.CREATED_BY );
		
		if ( session.id.equals(created_by) && (Schema. getInstance().getProfileRight( type, Fields.CREATED_BY ) & mask ) == mask)
			return true;
			
		String modified_by = (String) doc.getFieldValue( Fields.MODIFIED_BY );
		
		Date lock_to = (Date) doc.getFieldValue( Fields.LOCK_TO );
		
		
		if ( mask > Schema.Rights.readonlymask && lock_to!=null && lock_to.after( new Date() ) && !modified_by.equals( session.id ) )
			return false;
		else
			return internalCheckRightsNoExc( doc, mask );
	}*/
	
	private static int internalGetRights( Collection<String> roles, SolrQueryRequest req, Schema schema, SolrDocument doc , int mask , int rights ) throws SolrException 
	{	
		
		/*
		
		// All all unique patient IDs in TermsFilter
		 TermsFilter termsFilter = new TermsFilter();
		 Term term;

		 for(String pid : patientIds){
			 term = new Term("patient_ID", pid); // field that's unique (name) to patient and holds patientID
			 termsFilter.addTerm(term);
		 }

		 // get all patients whose ID is in TermsFilter
		 DocList patientsList = null;        
		 patientsList = searcher.getDocList(new MatchAllDocsQuery(), searcher.convertFilter(termsFilter), null, 0, 1000);
 
		*/
		
		if ( (rights & mask) == mask  )
			return mask;
		
		String type = ((String) doc.getFieldValue(Fields.ID)).split("@")[1];

        convertIndexableField(req,doc,Fields.ACL_EXPLICIT);
		
		Collection<Object> acl_explicit =  doc.getFieldValues( Fields.ACL_EXPLICIT );
		
		if (acl_explicit != null)
		{
			for ( Object exp : acl_explicit )
			{
				String [] parts = exp.toString().split(":");
				if (parts.length<2)
					continue;
					
				String actor = parts[0].toLowerCase();
				
				if ( (mask == Schema.Rights.retrieve) && roles.contains( actor ))
					return Schema.Rights.retrieve;
				
				Integer right = schema.getProfileRight( type, parts[1] );
				
				if ( roles.contains( actor ) )
					rights |= right;
				
				if ( (rights & mask) == mask  )
					return mask;
				
				//if ( ((right & mask) == mask) && session.roles.contains( actor ) )
				//	return true;
			}
		}

        boolean inherits = FieldUtils.parseBool(convertIndexableField(req, doc, Fields.ACL_INHERITS), false);

        if ( inherits )
		{
            convertIndexableFields(req,doc,Fields.ACL_PARENTS);

			Collection<Object> aclParentIds0 = doc.getFieldValues( Fields.ACL_PARENTS );
			
			Collection<Object> aclParentIds = new ArrayList<>();
			
			if (mask == Schema.Rights.retrieve) 
				aclParentIds.add( convertIndexableField( req,doc, Fields.ACL_PARENT ) );
			else
				aclParentIds.add( convertIndexableField(req, doc, Fields.PARENT) );
			
			if (aclParentIds0!=null)
				aclParentIds.addAll(aclParentIds0);
			
			//Collection<SolrDocument> docs = new ArrayList<>();
			
			for( Object id : aclParentIds )
			{	
				/*SolrDocument parentDoc = getDocument( req, (String) id , Fields.COMMON_FIELDS );
				
				if (parentDoc==null)
				{
					SolrQuery params = new SolrQuery();
					params.set( "ticket" , Session.ROOTTICKET );
					params.set( "shortcut" , true );
					params.set( Fields.ID , (String) id );
					//params.set( ShardParams._ROUTE_ , (String) doc.getFieldValue( schema.getRouteField() ) );
					params.setFields( Fields.COMMON_FIELDS );
					
					try
					{
						parentDoc = CoreClient.getInstance().get( params );
					}
					catch( Exception e )
					{
						throw new SolrException( SolrException.ErrorCode.SERVER_ERROR , e );
					}
				}*/

                SolrDocument parentDoc;
                try
                {
                    parentDoc = getDocument( req, (String) id  );
                }
                catch( SolrException se )
                {
                    if (se.code() == SolrException.ErrorCode.NOT_FOUND.code)
                        continue;
                    else
                        throw se;
                }

				rights |= internalGetRights( roles, req, schema, parentDoc, mask , rights );

				if ( (rights & mask) == mask  )
					return mask;
			}
		}
		
		return rights & mask;
	}
	
	/*static void checkParentRights( String type , String parentId )
	{
		if (parentId != null)
		{
			Integer createRight = Schema. getInstance().getCreateRight( parentId.split(" @")[1], type );
			SolrDocument pDoc = Solr Client.getInstance().get( parentId , null, Schema.Fields.COMMON_FIELDS);
			checkRights( pDoc , createRight , true );
		}
		else
			checkRights( null , -1 , true );
	}*/
	
	@SuppressWarnings("unchecked")
	public static Map<String,Integer> getAcl( SolrQueryRequest req, SolrDocument doc ) 
	{
		Schema schema = Schema.get(req);
		
		String id = (String) doc.getFieldValue( Fields.ID );
		Boolean inherits = FieldUtils.parseBool(doc.getFieldValue(Fields.ACL_INHERITS), false);
		String parent = (String) doc.getFieldValue( Fields.PARENT );
		//String acl_parent = (String) doc.getFieldValue( Fields.ACL_PARENT );
		
		String type = id.split("@")[1] ;
		String created_by = (String) doc.getFieldValue( Fields.CREATED_BY );
		int creatorRights = schema.getProfileRight( type, Fields.CREATED_BY );
		
		Map<String,Integer> acl;
		
		//CoreClient client = CoreClient.getInstance();
		
		//TODO NTH si potrebbero richiamare implicitamente i campi necessari (anche per [versions] ed altri augmenter dipendenti)
		
		if (inherits)
		{
			
			/*SolrDocument pDoc = getDocument( req, parent, Fields.COMMON_FIELDS );
			
			try
			{
				if (pDoc==null)
				{
					SolrQuery params = new SolrQuery();
					params.set( "ticket" , Session.ROOTTICKET );
					params.set( Fields.ID , parent );
					//params.set( ShardParams._ROUTE_ , acl_parent );
					params.setFields( Fields.COMMON_FIELDS );
					
					pDoc = client.get( params );
				}
			}
			catch(Exception e )
			{
				throw new RuntimeException(e);
			}*/
			SolrDocument pDoc = getDocument( req, parent );
			
			acl = getAcl( req, pDoc );
		}
		else
			acl = new HashMap<>();
			
		if (acl.containsKey(created_by))
			creatorRights = creatorRights | acl.get(created_by) ;
		
		acl.put( created_by , creatorRights );
			
		Collection<Object> acl_explicit = doc.getFieldValues( Fields.ACL_EXPLICIT );
		
		if (acl_explicit != null)
		{
			for ( Object exp : acl_explicit )
			{
				String[] parts = exp.toString().split(":");
				
				String actor = parts[0];
                //actor = parts[0].toLowerCase();
				
				Integer right = schema.getProfileRight( type, parts[1] );
				
				if (acl.containsKey(actor))
					right = right | acl.get(actor);
				
				acl.put(actor,right);
			}
		}
		
		return acl;
	}
	
}