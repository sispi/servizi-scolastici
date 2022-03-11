package it.kdm.docer.providers.solr6.bl.documenti;

import com.google.common.base.Strings;
import it.kdm.docer.providers.solr6.SolrBaseUtil;
import it.kdm.docer.providers.solr6.bl.Fields;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.EnumSearchOrder;
import it.kdm.docer.sdk.classes.LockStatus;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.ILockStatus;
import it.kdm.docer.sdk.interfaces.ILoggedUserInfo;
import it.kdm.solr.client.SolrClient;
import it.kdm.utils.DataTable;
import it.kdm.utils.exceptions.DataTableException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.ContentStreamBase;
import org.apache.solr.common.util.NamedList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by microchip on 04/05/15.
 */
public class DocumentoBL extends SolrBaseUtil {

    static Logger log = org.slf4j.LoggerFactory.getLogger(DocumentoBL.class.getName());

    public DocumentoBL() throws DocerException{
        super();
    }

    public void removeFromFolderDocuments(ILoggedUserInfo currentUser, String folderId, List<String> documents) throws DocerException {
        Map<String,List<String>> criteria = new HashMap<String, List<String>>();
        criteria.put(Fields.Documento.DOCNUM, documents);
        criteria.put(Fields.Documento.PARENT_FOLDER_ID, Collections.singletonList(folderId));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.SOLR_ID);
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);

        SolrDocumentList users = solrSearch(currentUser, TYPE_DOCUMENTO, criteria, null, returnProps, -1, null);

        for (Iterator iterator = users.iterator(); iterator.hasNext();) {
            Map rec = (Map) iterator.next();
            String solrId = rec.get(Fields.Documento.SOLR_ID).toString();
            String ente = rec.get(Fields.Documento.COD_ENTE).toString();
            String aoo = rec.get(Fields.Documento.COD_AOO).toString();

            //update document
            SolrInputDocument metadata = new SolrInputDocument();
            metadata.setField(Fields.Documento.SOLR_ID, solrId);
            metadata.setField(Fields.Documento.PARENT_FOLDER_ID, Collections.singletonMap("set",null));
            metadata.setField(Fields.Documento.COD_ENTE, ente);
            metadata.setField(Fields.Documento.COD_AOO, aoo);

            solrUpdate(currentUser, solrId, metadata);
        }
    }

    public void addToFolderDocuments(ILoggedUserInfo currentUser, String folderId, List<String> documents) throws DocerException {
        Map<String,List<String>> criteria = new HashMap<String, List<String>>();
        criteria.put(Fields.Documento.DOCNUM, documents);

        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.SOLR_ID);
        returnProps.add(Fields.Documento.PARENT_FOLDER_ID);

        SolrDocumentList users = solrSearch(currentUser, TYPE_DOCUMENTO, criteria, null, returnProps, -1, null);

        for (Iterator iterator = users.iterator(); iterator.hasNext();) {
            SolrDocument rec = (SolrDocument) iterator.next();
            String solrId = rec.getFieldValue(Fields.Documento.SOLR_ID).toString();
            String parentId = (String)rec.getFieldValue(Fields.Documento.PARENT_FOLDER_ID);

            if (folderId.equalsIgnoreCase(parentId))
                continue;

            //update document
            SolrInputDocument metadata = new SolrInputDocument();
            metadata.setField(Fields.Documento.SOLR_ID, solrId);
            metadata.setField(Fields.Documento.PARENT_FOLDER_ID, folderId);

            solrUpdate(currentUser, solrId, metadata);
        }
    }

    public void addNewAdvancedVersion(ILoggedUserInfo currentUser, String docIdLastVersion, String docIdNewVersion) throws DocerException {
        Collection<Object> related = new ArrayList<Object>();

        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docIdLastVersion, Fields.Documento.SYSTEM_ID, returnProps);
        String ente = (String)solrDoc.get(Fields.Documento.COD_ENTE);
        String aoo = (String)solrDoc.get(Fields.Documento.COD_AOO);

        Map<String, String> metaDati = new HashMap<String, String>();
        metaDati.put(Fields.Documento.COD_ENTE,ente);
        metaDati.put(Fields.Documento.COD_AOO,aoo);

        String docSolrId = getSolrId(docIdLastVersion, metaDati);

        returnProps = new ArrayList<String>();
        returnProps.add(Fields.Base.SOLR_ID);
        returnProps.add(Fields.Related.RELATED);

        String solrId = null;

        try {
            solrDoc = getSolrObjectByFieldId(currentUser, TYPE_VERSIONS, docSolrId, Fields.Related.RELATED, returnProps);
            related = solrDoc.getFieldValues(Fields.Related.RELATED);
            solrId = (String)solrDoc.get(Fields.Base.SOLR_ID);
        } catch (DocerException de) {
            if (de.getErrNumber()!=404) {
                throw de;
            }
        }

        if (related==null)
            related = new ArrayList<Object>();

        //aggiunge se non esiste il last
        if (!related.contains(docSolrId)) {
            related.add(docSolrId);
        }

        docSolrId = getSolrId(docIdNewVersion, metaDati);

        //aggiunge se non esiste il new
        if (!related.contains(docSolrId)) {
            related.add(docSolrId);
        }

        //update solr object
        SolrInputDocument metadata = new SolrInputDocument();
        metadata.setField(Fields.Related.RELATED, related);
        metadata.setField(Fields.Related.COD_ENTE, ente);
        metadata.setField(Fields.Related.COD_AOO, aoo);

        if (solrId!=null) {
            solrUpdate(currentUser, solrId, metadata);
        } else {
            solrInsert(currentUser, TYPE_VERSIONS,metadata);
        }
    }

    public List<String> getAdvancedVersions(ILoggedUserInfo currentUser, String docId) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String ente = (String)solrDoc.get(Fields.Documento.COD_ENTE);
        String aoo = (String)solrDoc.get(Fields.Documento.COD_AOO);

        Map<String, String> metaDati = new HashMap<String, String>();
        metaDati.put(Fields.Documento.COD_ENTE,ente);
        metaDati.put(Fields.Documento.COD_AOO,aoo);

        String docSolrId = getSolrId(docId, metaDati);

        returnProps = new ArrayList<String>();
        returnProps.add(Fields.Related.RELATED);

        Collection<Object> related = new ArrayList<Object>();
        try {
            solrDoc = getSolrObjectByFieldId(currentUser, TYPE_VERSIONS, docSolrId, Fields.Related.RELATED, returnProps);
            related = solrDoc.getFieldValues(Fields.Related.RELATED);
            related.remove(docSolrId);
            related = removeDeleteDocs(currentUser, related);
        } catch (DocerException de) {
            if (de.getErrNumber()!=404) {
                throw de;
            }
        }

        return convertRelatedListToDocer(related);
    }

    private Collection<Object> removeDeleteDocs(ILoggedUserInfo currentUser, Collection<Object> related) throws DocerException {
        SolrDocumentList docs = filterDeleteDocs(currentUser,related);

        Collection<Object> list = new ArrayList<Object>();
        for (Iterator iterator = docs.iterator(); iterator.hasNext();) {
            Map rec = (Map) iterator.next();
            list.add(rec.get(Fields.Documento.DOCNUM).toString());
        }

        return list;
    }


    public List<String> getFolderDocuments(ILoggedUserInfo currentUser, String folderId) throws DocerException {
        Map<String,List<String>> criteria = new HashMap<String, List<String>>();
        criteria.put(Fields.Documento.PARENT_FOLDER_ID, Collections.singletonList(folderId));

        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.DOCNUM);

        SolrDocumentList docs = solrSearch(currentUser, TYPE_DOCUMENTO, criteria, null, returnProps, -1, null);

        List<String> list = new ArrayList<String>();

        for (Iterator iterator = docs.iterator(); iterator.hasNext();) {
            Map rec = (Map) iterator.next();
            list.add(rec.get(Fields.Documento.DOCNUM).toString());
        }

        return list;

    }

    public List<String> getVersions(ILoggedUserInfo currentUser, String docId) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");
        returnProps.add(Fields.Documento.VERSIONS);

        SolrDocument doc = getSolrObjectByFieldId(currentUser,TYPE_DOCUMENTO,docId, Fields.Documento.SYSTEM_ID, returnProps);
        Collection<Object> versions = doc.getFieldValues(Fields.Documento.VERSIONS);

        return convertSolrCollectionToMap(versions);
    }

    public String replaceLastVersion(ILoggedUserInfo currentUser, String docId, InputStream documentFile) throws DocerException {
        return manageVersion(currentUser,docId,documentFile,false);
    }

    public String addNewVersion(ILoggedUserInfo currentUser, String docId, InputStream documentFile) throws DocerException {
        return manageVersion(currentUser,docId,documentFile,true);
    }

    private String manageVersion(ILoggedUserInfo currentUser, String docId, InputStream documentFile, Boolean isNewVersion) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String ente = (String)solrDoc.getFieldValue(Fields.Documento.COD_ENTE);
        String aoo = (String)solrDoc.getFieldValue(Fields.Documento.COD_AOO);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

//        metadata.addField(Fields.Documento.SOLR_ID,solrId);
        metadata.addField(Fields.Documento.COD_ENTE,ente);
        metadata.addField(Fields.Documento.COD_AOO,aoo);
        metadata.addField(Fields.Documento.DOCNUM,docId);

        Map<String,String> params = new HashMap<String, String>();

        params.put("newVersion",isNewVersion.toString());

        NamedList<?> nameValue = solrUpdate(currentUser, TYPE_DOCUMENTO, metadata, documentFile,params);
        String version = null;

        if (((SolrDocument)nameValue.get("processAdd")).get("newVersion")!=null) {
            version = ((SolrDocument) nameValue.get("processAdd")).get("newVersion").toString();
        }

        return version;
    }

    public byte[] downloadLastVersion(ILoggedUserInfo currentUser, String docId, String destinationFilePath, long maxFileLength) throws DocerException {
        return downloadVersion(currentUser,docId,"last",destinationFilePath,maxFileLength);
    }

    public byte[] downloadVersion(ILoggedUserInfo currentUser, String docId, String versionNumber, String destinationFilePath, long maxFileLength) throws DocerException {
        try {

            ContentStreamBase stream = downloadFile(currentUser,docId,versionNumber);

            if (stream.getSize() >= maxFileLength) {
                //scrittura su file system
                FileUtils.copyInputStreamToFile(stream.getStream(), new File(destinationFilePath));
                return null;

            } else {
                //torna byte[]
                return IOUtils.toByteArray(stream.getStream());
            }
        } catch (IOException ioe) {
            throw new DocerException(ioe);
        }

    }

    private ContentStreamBase downloadFile(ILoggedUserInfo currentUser, String docId, String version) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.SOLR_ID);
        returnProps.add(Fields.Documento.CONTENT_SIZE);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String solrId = (String)solrDoc.getFieldValue(Fields.Documento.SOLR_ID);
        Integer size = (Integer)solrDoc.getFieldValue(Fields.Documento.CONTENT_SIZE);

        ContentStreamBase stream = getSolrStream(currentUser,solrId,version);
        stream.setSize(size.longValue());

        return stream;
    }

    public EnumACLRights getEffectiveRights(ILoggedUserInfo currentUser, String docId, String userid) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add("*");
        returnProps.add(Fields.Documento.EFFECTIVE_RIGHTS);

        Map<String, List<String>> criteria = new HashMap<String, List<String>>();
        criteria.put(Fields.Documento.SYSTEM_ID, Collections.singletonList(docId));

        Map<String,String> params = new HashMap<String, String>();
        params.put("userId", userid);

        SolrDocumentList result = solrSearch(currentUser, TYPE_DOCUMENTO, criteria, new ArrayList<String>(), returnProps,1,null,params,null);

        if (result.size()>0) {
            SolrDocument rec = result.get(0);
            String rights = (String)rec.getFirstValue(Fields.Documento.EFFECTIVE_RIGHTS);

            return convertSolrACLRights(rights);
        }

        throw new DocerException("Impossibile recuperare gli EffectiveRights. Documento non trovato.");
    }

    public String createDocument(ILoggedUserInfo currentUser, String documentType, Map<String, String> profileInfo, InputStream documentFile) throws DocerException {

        if (profileInfo.containsKey(Fields.Documento.DOC_URL)) {
            if (documentFile!=null)
                throw new DocerException("File non ammesso per un documento di tipo url");

            String fileContent = profileInfo.get(Fields.Documento.DOC_URL);

            documentFile = new ByteArrayInputStream( fileContent.getBytes() );
        }
        //sovrascrive il TYPE_ID per essere sicuro che sia impostato come metadato del documento
        profileInfo.put(Fields.Documento.DOCTYPE, documentType);

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        metadata = buildExtraInfo(metadata, profileInfo);

        NamedList<?> nameValue = solrInsert(currentUser, TYPE_DOCUMENTO, metadata, documentFile);
        String solrId = (String)((SolrDocument)nameValue.get("processAdd")).get("id");
        String docnum = extractIdItemFromSolrId(solrId);

        return docnum;
    }

    public List<String> getRiferimentiDocuments(ILoggedUserInfo currentUser, String docId) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.SOLR_ID);
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);
        returnProps.add(Fields.Documento.RIFERIMENTI);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String solrId = (String)solrDoc.get(Fields.Documento.SOLR_ID);
        String ente = (String)solrDoc.get(Fields.Documento.COD_ENTE);
        String aoo = (String)solrDoc.get(Fields.Documento.COD_AOO);

        Collection<Object> riferimenti = solrDoc.getFieldValues(Fields.Documento.RIFERIMENTI);

        List<String> out = new ArrayList<String>();
        if (riferimenti!=null)
            out.addAll(convertRelatedListToDocer(riferimenti));

        //recupera i riferimenti degli altri doc
        String q = "";
        q += SolrClient.Query.makeClause(Fields.Documento.RIFERIMENTI, solrId);

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add("sid");

        SolrDocumentList rifList = solrSearchByQuery(currentUser, q, returnProperties, -1, null);

        for (SolrDocument r : rifList) {
            if (!out.contains((String)r.getFieldValue("sid")))
                out.add((String)r.getFieldValue("sid"));
        }

        return out;
    }

    public void addRiferimentiDocuments(ILoggedUserInfo currentUser, String docId, List<String> items) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);
        returnProps.add(Fields.Documento.RIFERIMENTI);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String ente = (String)solrDoc.get(Fields.Documento.COD_ENTE);
        String aoo = (String)solrDoc.get(Fields.Documento.COD_AOO);
        Collection<Object> riferimenti = solrDoc.getFieldValues(Fields.Documento.RIFERIMENTI);

        Map<String, String> metaDati = new HashMap<String, String>();
        metaDati.put(Fields.Documento.COD_ENTE,ente);
        metaDati.put(Fields.Documento.COD_AOO,aoo);

        String docSolrId = getSolrId(docId, metaDati);

        if (riferimenti==null)
            riferimenti = new ArrayList<Object>();

        for (String rif : items) {
            String rifSolrId = getSolrId(rif.toString(), metaDati);

            if (!riferimenti.contains(rifSolrId)) {
                riferimenti.add(rifSolrId);
            }
        }

        //update solr object
        SolrInputDocument metadata = new SolrInputDocument();
        metadata.setField(Fields.Documento.RIFERIMENTI, riferimenti);

        solrUpdate(currentUser, docSolrId, metadata);
    }

    public void removeRiferimentiDocuments(ILoggedUserInfo currentUser, String docId, List<String> items) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);
        returnProps.add(Fields.Documento.RIFERIMENTI);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String ente = (String)solrDoc.get(Fields.Documento.COD_ENTE);
        String aoo = (String)solrDoc.get(Fields.Documento.COD_AOO);
        Collection<Object> riferimenti = solrDoc.getFieldValues(Fields.Documento.RIFERIMENTI);

        Map<String, String> metaDati = new HashMap<String, String>();
        metaDati.put(Fields.Documento.COD_ENTE,ente);
        metaDati.put(Fields.Documento.COD_AOO,aoo);

        String docSolrId = getSolrId(docId, metaDati);

        if (riferimenti==null || riferimenti.size()==0)
            return;

        for (String rif : items) {
            String rifSolrId = getSolrId(rif.toString(), metaDati);

            if (riferimenti.contains(rifSolrId)) {
                riferimenti.remove(rifSolrId);
            }
        }

        //update solr object
        SolrInputDocument metadata = new SolrInputDocument();
        if (riferimenti.size()==0)
            metadata.setField(Fields.Documento.RIFERIMENTI, Collections.singletonMap("set",null));
        else
            metadata.setField(Fields.Documento.RIFERIMENTI, riferimenti);

        solrUpdate(currentUser, docSolrId, metadata);

        //cicla la lista per eliminare eventualmente i riferimenti in entrata
//        List<String> newList = new ArrayList<String>();
//        for (String i : items)
//            newList.add(getSolrId(i, metaDati));

        String q = "";
        q += SolrClient.Query.makeClause(Fields.Documento.DOCNUM, items.toArray());
        q += SolrClient.Query.makeClause(Fields.Documento.RIFERIMENTI, docSolrId);

        List<String> returnProperties = new ArrayList<String>();
        returnProperties.add(Fields.Base.SOLR_ID);
        returnProperties.add("sid");
        returnProperties.add(Fields.Documento.RIFERIMENTI);

        SolrDocumentList rifList = solrSearchByQuery(currentUser, q, returnProperties, -1, null);
        for (SolrDocument d : rifList) {
            String rifSolrId = (String)d.getFieldValue(Fields.Documento.SOLR_ID);
            Collection<Object> rifs = d.getFieldValues(Fields.Documento.RIFERIMENTI);
            rifs.remove(docSolrId);

            SolrInputDocument metarif = new SolrInputDocument();
            if (rifs.size()==0)
                metarif.setField(Fields.Documento.RIFERIMENTI, Collections.singletonMap("set",null));
            else
                metarif.addField(Fields.Documento.RIFERIMENTI, rifs.toArray());


            solrUpdate(currentUser, rifSolrId, metarif);
        }
    }

    public void addRelatedDocuments(ILoggedUserInfo currentUser, String docId, List<String> items) throws DocerException {
        Collection<Object> related = new ArrayList<Object>();

        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String ente = (String)solrDoc.get(Fields.Documento.COD_ENTE);
        String aoo = (String)solrDoc.get(Fields.Documento.COD_AOO);

        Map<String, String> metaDati = new HashMap<String, String>();
        metaDati.put(Fields.Documento.COD_ENTE,ente);
        metaDati.put(Fields.Documento.COD_AOO,aoo);


        returnProps = new ArrayList<String>();
        returnProps.add(Fields.Base.SOLR_ID);
        returnProps.add(Fields.Related.RELATED);

        //aggiunge se stesso
        items.add(docId);

        List<String> solrIds = new ArrayList<String>();

        for (String item : items)
            solrIds.add(getSolrId(item, metaDati));
        //String docSolrId = getSolrId(docId, metaDati);

        String solrId = null;

        try {
            solrDoc = getSolrObjectByFieldId(currentUser, TYPE_RELATED, solrIds, Fields.Related.RELATED, returnProps);
            related = solrDoc.getFieldValues(Fields.Related.RELATED);
            solrId = (String)solrDoc.get(Fields.Base.SOLR_ID);
        } catch (DocerException de) {
            if (de.getErrNumber()!=404) {
                throw de;
            }
        }

        if (related==null)
            related = new ArrayList<Object>();

        for (String rifSolrId : solrIds) {
            //String rifSolrId = getSolrId(rel, metaDati);

            if (!related.contains(rifSolrId)) {
                related.add(rifSolrId);
            }
        }

        //update solr object
        SolrInputDocument metadata = new SolrInputDocument();
        metadata.setField(Fields.Related.RELATED, related);
        metadata.setField(Fields.Related.COD_ENTE, ente);
        metadata.setField(Fields.Related.COD_AOO, aoo);

        if (solrId!=null) {
            solrUpdate(currentUser, solrId, metadata);
        } else {
            solrInsert(currentUser, TYPE_RELATED,metadata);
        }
    }

    public void removeRelatedDocuments(ILoggedUserInfo currentUser, String docId, List<String> items) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String ente = (String)solrDoc.get(Fields.Documento.COD_ENTE);
        String aoo = (String)solrDoc.get(Fields.Documento.COD_AOO);

        Map<String, String> metaDati = new HashMap<String, String>();
        metaDati.put(Fields.Documento.COD_ENTE,ente);
        metaDati.put(Fields.Documento.COD_AOO,aoo);

        List<String> solrIds = new ArrayList<String>();

        //aggiunge se stesso
        solrIds.add(getSolrId(docId, metaDati));

        for (String item : items)
            solrIds.add(getSolrId(item, metaDati));

        //String docSolrId = getSolrId(docId, metaDati);

        returnProps = new ArrayList<String>();
        returnProps.add(Fields.Base.SOLR_ID);
        returnProps.add(Fields.Related.RELATED);

        try {
            solrDoc = getSolrObjectByFieldId(currentUser, TYPE_RELATED, solrIds, Fields.Related.RELATED, returnProps);
        } catch (DocerException de) {
            if (de.getErrNumber()==404)
                return;
        }

        String solrId = (String)solrDoc.get(Fields.Base.SOLR_ID);
        Collection<Object> related = solrDoc.getFieldValues(Fields.Related.RELATED);

        if (related==null || related.size()==0)
            return;

        for (String rel : items) {
            String rifSolrId = getSolrId(rel, metaDati);

            if (related.contains(rifSolrId)) {
                related.remove(rifSolrId);
            }
        }

        //update solr object
        SolrInputDocument metadata = new SolrInputDocument();
        //gestione rimozione tutti gli elementi
        if (related.size()==0) {
            metadata.setField(Fields.Related.RELATED, Collections.singletonMap("set",null));
        } else {
            metadata.setField(Fields.Related.RELATED, related);
        }

        metadata.setField(Fields.Related.COD_ENTE, ente);
        metadata.setField(Fields.Related.COD_AOO, aoo);


        solrUpdate(currentUser, solrId, metadata);
    }

    public List<String> getRelatedDocuments(ILoggedUserInfo currentUser, String docId) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Documento.COD_ENTE);
        returnProps.add(Fields.Documento.COD_AOO);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String ente = (String)solrDoc.get(Fields.Documento.COD_ENTE);
        String aoo = (String)solrDoc.get(Fields.Documento.COD_AOO);

        Map<String, String> metaDati = new HashMap<String, String>();
        metaDati.put(Fields.Documento.COD_ENTE,ente);
        metaDati.put(Fields.Documento.COD_AOO,aoo);

        String docSolrId = getSolrId(docId, metaDati);

        returnProps = new ArrayList<String>();
        returnProps.add(Fields.Related.RELATED);

        Collection<Object> related = new ArrayList<Object>();

        try {
            solrDoc = getSolrObjectByFieldId(currentUser, TYPE_RELATED, docSolrId, Fields.Related.RELATED, returnProps);
            related = solrDoc.getFieldValues(Fields.Related.RELATED);
            related.remove(docSolrId);
            related = removeDeleteDocs(currentUser, related);
        } catch (DocerException de) {
            if (de.getErrNumber()!=404)
                throw de;
        }

        return convertRelatedListToDocer(related);
    }

    public ILockStatus isCheckedOutDocument(ILoggedUserInfo currentUser, String docId) throws DocerException {
        List<String> returnProps = new ArrayList<String>();
        returnProps.add(Fields.Base.SOLR_ID);
        returnProps.add(Fields.Documento.LOCK);
        returnProps.add(Fields.Documento.LOCKED_BY);

        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String solrId = (String)solrDoc.get(Fields.Documento.SOLR_ID);
        String lockedBy = (String)solrDoc.get(Fields.Documento.LOCKED_BY);
        Date lockTo = (Date)solrDoc.get(Fields.Documento.LOCK);

        boolean isLocked = lockTo.after(new Date());

        ILockStatus status = new LockStatus();
        status.setLocked(isLocked);

        if (isLocked) {
            status.setUserId(lockedBy);
            status.setFullName(lockedBy);
        }

        return status;
    }

    public void unlockDocument(ILoggedUserInfo currentUser, String docId) throws DocerException {
        List<String> returnProps = Collections.singletonList(Fields.Base.SOLR_ID);
        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String solrId = (String)solrDoc.get(Fields.Base.SOLR_ID);

        SolrInputDocument metadata = new SolrInputDocument();
        metadata.setField(Fields.Documento.LOCK, new Date());

        solrUpdate(currentUser, solrId, metadata);
    }

    public void lockDocument(ILoggedUserInfo currentUser, String docId) throws DocerException {
        List<String> returnProps = Collections.singletonList(Fields.Base.SOLR_ID);
        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String solrId = (String)solrDoc.get(Fields.Base.SOLR_ID);

        SolrInputDocument metadata = new SolrInputDocument();
        Date date = new Date(System.currentTimeMillis()+DEFAULT_LOCK_DURATION);
        metadata.setField(Fields.Documento.LOCK, date);

        solrUpdate(currentUser, solrId, metadata);
    }

    public Map<String, EnumACLRights> getACLDocument(ILoggedUserInfo currentUser, String docId) throws DocerException {
        SolrDocument doc = getSolrObjectByFieldId(currentUser,TYPE_DOCUMENTO,docId, Fields.Documento.SYSTEM_ID);
        String solrId = (String)doc.get(Fields.Documento.SOLR_ID);
        Collection<Object> aclExplicit = doc.getFieldValues(Fields.Documento.ACL);

        return convertSolrACLToMap(aclExplicit);
    }

    public void setACLDocument(ILoggedUserInfo currentUser, String docId, Map<String, EnumACLRights> acls) throws DocerException {
        SolrDocument doc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID);
        String solrId = (String)doc.get(Fields.Documento.SOLR_ID);

        List<String> solrAcl = convertMapToSolrACL(currentUser,acls);
        SolrInputDocument metadata = new SolrInputDocument();

        for (String acl : solrAcl)
            metadata.addField(Fields.Documento.ACL, acl);

        if (solrAcl.size()==0)
            metadata.setField(Fields.Documento.ACL, Collections.singletonMap("set",null));

        solrUpdate(currentUser, solrId, metadata);
    }

    public NamedList<?> deleteDocument(ILoggedUserInfo currentUser, String docId) throws DocerException {
        List<String> returnProps = Collections.singletonList(Fields.Base.SOLR_ID);
        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String solrId = (String)solrDoc.get(Fields.Base.SOLR_ID);

        return solrDeleteById(currentUser, solrId);
    }

    public NamedList<?> updateProfileDocument(ILoggedUserInfo currentUser, String docId, Map<String, String> metaDati) throws DocerException {

        List<String> returnProps = Collections.singletonList(Fields.Base.SOLR_ID);
        SolrDocument solrDoc = getSolrObjectByFieldId(currentUser, TYPE_DOCUMENTO, docId, Fields.Documento.SYSTEM_ID, returnProps);
        String solrId = (String)solrDoc.get(Fields.Base.SOLR_ID);

//        String solrId = getSolrId(docId, metaDati);

//        if (!metaDati.containsKey(Fields.Documento.COD_ENTE))
//            throw new DocerException("Impossibie eseguire la modifica del profilo del documento. Codice Ente non impostato.");
//
//        if (!metaDati.containsKey(Fields.Documento.COD_AOO))
//            throw new DocerException("Impossibie eseguire la modifica del profilo del documento. Codice AOO non impostato.");

        //TODO: controllo se documento bloccato (locked)
        //TODO: rimozione dei metadata per compatibilità docarea???

        //convertire info in metadata
        SolrInputDocument metadata = new SolrInputDocument();

        metadata = buildExtraInfo(metadata, metaDati);

        return solrUpdate(currentUser, solrId, metadata);
    }

    public DataTable<String> search(ILoggedUserInfo currentUser, Map<String, List<String>> searchCriteria, List<String> keywords, List<String> returnProperties, int maxResults, Map<String, EnumSearchOrder> orderBy) throws DocerException {
//        log.debug("----------------------------------------------------------------------------------------------");
//        log.debug("METODO: searchDocument");
//        log.debug("----------------------------------------------------------------------------------------------");

        //FIX: docunum duplicati
        if (!searchCriteria.containsKey(Fields.Ente.COD_ENTE)) {
            String ente = currentUser.getCodiceEnte();

            if (Strings.isNullOrEmpty(ente))
                ente = "";

            searchCriteria.put(Fields.Ente.COD_ENTE, Collections.singletonList(ente));
        }

        SolrDocumentList results = solrSearch(currentUser, TYPE_DOCUMENTO, searchCriteria, keywords, returnProperties, maxResults, orderBy);

        try {
            //converte solr results in DataTable<String>
            DataTable<String> dataTable = this.convertSolrToDataTable(results,returnProperties);

            return dataTable;

        } catch (DataTableException e) {
            throw new DocerException(e);
        }
    }


}
