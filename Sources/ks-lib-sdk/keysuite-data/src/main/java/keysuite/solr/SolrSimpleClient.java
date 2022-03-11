package keysuite.solr;

import it.kdm.orchestratore.session.UserInfo;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import java.io.IOException;
import java.security.KeyException;
import java.util.*;

public class SolrSimpleClient {

    public static final String ZK_HOST = "zkHost";

    private static CloudSolrClient solrClient = null;

    public SolrSimpleClient() {
    }

    public CloudSolrClient getServer() {
        try {

            if (solrClient==null){
                synchronized (ZK_HOST) {
                    if (solrClient==null) {
                        String zkHost = System.getProperty(ZK_HOST);
                        CloudSolrClient.Builder builder = new CloudSolrClient.Builder((List) null).withZkHost(Arrays.asList(zkHost.split(",")));
                        solrClient = builder.build();
                        String collection = solrClient.getZkStateReader().getClusterState().getCollectionsMap().keySet().iterator().next();
                        solrClient.setDefaultCollection(collection);
                    }
                }
            }

            return solrClient;

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String fqSession(String token){
        String ente = UserInfo.extractOptionalTokenKey(token, "ente","*");
        String aoo = UserInfo.extractOptionalTokenKey(token, "aoo","*");
        String q = String.format( "(+COD_ENTE:%s +COD_AOO:%s) type:(aoo ente user group)",ente,aoo);
        return q;
    }

    public QueryResponse select(String token, SolrParams params) throws SolrServerException {
        ModifiableSolrParams solrParams = new ModifiableSolrParams(params);
        solrParams.set("ticket", UserInfo.extractOptionalTokenKey(token, "uid",null));
        solrParams.add("fq",fqSession(token));

        QueryResponse qr = null;
        try {
            qr = this.getServer().query(solrParams, SolrRequest.METHOD.POST);
        } catch (IOException e) {
            throw new SolrServerException(e);
        }
        return qr;
    }

    public QueryResponse select(SolrParams params) throws SolrServerException {
        try {
            return this.getServer().query(params, SolrRequest.METHOD.POST);
        } catch (IOException e) {
            throw new SolrServerException(e);
        }
    }

    private Map<String,Object> solrUpdateRequest(String userId, String handler, String id, Map<String,Object> doc, Map<String,Object> params, Map<String,Object> ... otherDocs ) {

        if (userId==null) {
            userId = "admin";
        }

        if (id!=null && doc!=null)
            doc.put("id",id);

        UpdateRequest req;
        if (handler.equals("/dummy") || handler.equals("/delete") || handler.equals("/deleteByQuery"))
            req = new UpdateRequest("/update");
        else
            req = new UpdateRequest(handler);

        if (params!=null){
            ModifiableSolrParams mparams = new ModifiableSolrParams();
            for( String key : params.keySet()){
                Object v = params.get(key);

                if (v instanceof String[])
                    mparams.add(key, (String[]) v);
                else
                    mparams.add(key, v.toString());
            }
            req.setParams(mparams);
        }

        req.setParam("ticket",userId);

        if (handler.equals("/dummy")) {

        } else if (handler.equals("/deleteByQuery")) {
            req.deleteByQuery(id);
        } else if (handler.equals("/delete")){
            req.deleteById(id);
        } else {

            if (doc!=null){
            SolrInputDocument idoc = new SolrInputDocument();

            for ( String key : doc.keySet() ){
                idoc.addField(key,doc.get(key));
            }

            req.add(idoc);
        }

            if (otherDocs != null && otherDocs.length>0){
                for( int i=0; i<otherDocs.length; i++){
                    SolrInputDocument idoc2 = new SolrInputDocument();
                    for ( String key : otherDocs[i].keySet() ){
                        idoc2.addField(key,otherDocs[i].get(key));
                    }
                    req.add(idoc2);
                }
            }
        }

        NamedList<?> result = null;
        try {
            result = getServer().request(req);
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        NamedList<?> header =(NamedList<?>) result.get("responseHeader");

        if (header==null || header.get("status")==null)
            throw new RuntimeException("invalid solr response");

        int status = ((Number) header.get("status")).intValue();

        if (status == 0 ){
            Map res = (Map<String,Object>) result.get("processAdd");
            if(res==null){
                return new HashMap<>();
            }
            return res;
        } else {
            NamedList<?> error =(NamedList<?>) result.get("error");

            String msg = (String) error.get("msg");

            if (msg==null)
                msg = "errore sconosciuto";

            throw new SolrException(SolrException.ErrorCode.getErrorCode(status),msg);
        }
    }

    public void delete(String userId, String id){
        solrUpdateRequest(userId,"/delete",id,null,null);
    }

    public void delete(String id){
        solrUpdateRequest(null,"/delete",id,null,null);
    }

    public void deleteByQuery(String query){
        solrUpdateRequest(null,"/deleteByQuery",query,null,null);
    }

    public void commit(){
        Map params = new HashMap();
        params.put("commit","true");
        params.put("waitSearcher","true");

        //params.put("softCommit","true");
        solrUpdateRequest(null,"/dummy",null,null,params);
    }

    public void update(Map<String,Object> doc){
        solrUpdateRequest(null,"/update",null,doc,null);
    }

    public void multiUpdate(String userId, Map<String,Object> params, Map<String,Object> ... docs){
        solrUpdateRequest(userId,"/update",null,null,params, docs);
    }

    public void multiUpdate(Map<String,Object> params, Map<String,Object> ... docs){
        solrUpdateRequest(null,"/update",null,null,params, docs);
    }

    public void update(String id, Map<String,Object> doc){
        solrUpdateRequest(null,"/update",id,doc,null);
    }

    public void update(String userId, String id, Map<String,Object> doc){
        solrUpdateRequest(userId,"/update",id,doc,null);
    }

    public void update(String userId, String id, Map<String,Object> doc, Map<String,Object> params){
        solrUpdateRequest(userId,"/update",id,doc,params);
    }

    public String insert(Map<String,Object> doc){
        return (String) solrUpdateRequest(null,"/create",null,doc,null).get("id");
    }

    public String insert(String id, Map<String,Object> doc){
        return (String) solrUpdateRequest(null,"/create",id,doc,null).get("id");
    }

    public String insert(String userId, String id, Map<String,Object> doc){
        return (String) solrUpdateRequest(userId,"/create",id,doc,null).get("id");
    }

    public String insert(String userId, String id, Map<String,Object> doc, Map<String,Object> params){
        return (String) solrUpdateRequest(userId,"/create",id,doc,params).get("id");
    }

    public String insert_or_update(Map<String,Object> doc) {
        return insert_or_update(null,null,doc,null);
    }

    public String insert_or_update(String id, Map<String,Object> doc) {
        return insert_or_update(null,id,doc,null);
    }

    public String insert_or_update(String userId, String id, Map<String,Object> doc) {
        return insert_or_update(userId,id,doc,null);
    }

    public String insert_or_update(String userId, String id, Map<String,Object> doc, Map<String,Object> params){

        if (id==null)
            id = (String) doc.get("id");

        if (id==null)
            throw new RuntimeException("id must be specified");

        try{
            SolrDocument d = get(userId,id,"id");

            update(userId,id,doc,params);
            return id;
        } catch(SolrException se){
            if (se.code() == SolrException.ErrorCode.NOT_FOUND.code)
                return insert(userId,id,doc,params);
            else
                throw se;
        }
    }

    public SolrDocument get(String id){
        return get(null,id,null);
    }

    public SolrDocument get(String userId,String id, String... fl){

        if (userId==null) {
            userId = "admin";
        }

        ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("id",id);
        solrParams.set("ticket", userId);
        solrParams.set( CommonParams.QT, "/get");
        solrParams.set("fl", fl);

        QueryResponse rsp = null;
        try {
            rsp = getServer().query(solrParams);
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SolrDocument doc = (SolrDocument) rsp.getResponse().get("doc");

        if (doc==null)
            throw new SolrException(SolrException.ErrorCode.NOT_FOUND,id +" not found");

        return doc;
    }

    public SolrDocument getByHandler(String handler,String userId,String id, String... fl){

        if (userId==null) {
            userId = "admin";
        }

        ModifiableSolrParams solrParams = new ModifiableSolrParams();
        solrParams.set("id",id);
        solrParams.set("ticket", userId);
        solrParams.set( CommonParams.QT, handler);
        solrParams.set("fl", fl);

        QueryResponse rsp = null;
        try {
            rsp = getServer().query(solrParams);
        } catch (SolrServerException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SolrDocument doc = (SolrDocument) rsp.getResponse().get("doc");

        if (doc==null)
            throw new SolrException(SolrException.ErrorCode.NOT_FOUND,id +" not found");

        return doc;
    }
}
