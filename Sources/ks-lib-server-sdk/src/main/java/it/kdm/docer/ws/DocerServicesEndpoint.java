package it.kdm.docer.ws;

//import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.google.common.base.Strings;
import it.kdm.docer.businesslogic.BusinessLogic;
import it.kdm.docer.providers.solr.SolrBaseUtil;
import it.kdm.docer.sdk.EnumACLRights;
import it.kdm.docer.sdk.classes.Version;
import it.kdm.docer.sdk.classes.xsd.HistoryItem;
import it.kdm.docer.sdk.classes.xsd.LockStatus;
import it.kdm.docer.sdk.classes.xsd.SearchItem;
import it.kdm.docer.sdk.classes.xsd.StreamDescriptor;
import it.kdm.docer.sdk.exceptions.DocerException;
import it.kdm.docer.sdk.interfaces.IHistoryItem;
import it.kdm.docer.sdk.interfaces.IKeyValuePair;
import it.kdm.docer.sdk.interfaces.ILockStatus;
import it.kdm.docer.sdk.interfaces.ISearchItem;
import it.kdm.docer.webservices.*;
//import it.kdm.doctoolkit.helper.InputStreamDataSource;
import keysuite.docer.interceptors.Logging;
import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.ws.mime.Attachment;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

@Endpoint
@Logging(group = "DocerServices")
public class DocerServicesEndpoint {

    public static final String HTTP_WEBSERVICES_DOCER_KDM_IT = "http://webservices.docer.kdm.it";
    //private BusinessLogic BLDocer;

    public DocerServicesEndpoint() throws DocerException{
        //BLDocer = new BusinessLogic();
    }

    protected BusinessLogic newBLDocer(){
        try{
            return new BusinessLogic();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "login")
    @ResponsePayload
    public LoginResponse login(@RequestPayload Login request) throws DocerException {

        String ticket = newBLDocer().login(
                request.getCodiceEnte().getValue(),
                request.getUserId().getValue(),
                request.getPassword().getValue() );

        LoginResponse response = WSTransformer.wsFactory.createLoginResponse();
        response.setReturn(WSTransformer.wsFactory.createLoginResponseReturn(ticket));

        return response;
    }

    private InputStream getAttachment(JAXBElement<DataHandler> je) throws IOException {
        SaajSoapMessage message = DualProtocolSaajSoapMessageFactory.soapMessage.get();

        InputStream is = null;

        if (message.getAttachments().hasNext()){
            Attachment att = message.getAttachments().next();
            is = att.getInputStream();
        }

        if (is==null){
            if (je!=null){
                DataHandler dh = je.getValue();
                if (dh!=null){
                    is = dh.getInputStream();
                }
            }
        }

        return is;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "createDocument")
    @ResponsePayload
    public CreateDocumentResponse createDocument(@RequestPayload CreateDocument request) throws DocerException {



        CreateDocumentResponse response = WSTransformer.wsFactory.createCreateDocumentResponse();

        Map<String,String> metadata = WSTransformer.toMap(request.getMetadata(),false);

        try {

            InputStream is = getAttachment(request.getFile());

            String docnum = newBLDocer().createDocument( request.getToken().getValue() ,metadata,is);

            response.setReturn(WSTransformer.wsFactory.createCreateDocumentResponseReturn(docnum));

            return response;

        } catch (Exception e) {
            throw new DocerException(e);
        }
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "downloadDocument")
    @ResponsePayload
    public DownloadDocumentResponse downloadDocument(@RequestPayload DownloadDocument request) throws DocerException {

        DownloadDocumentResponse response = WSTransformer.wsFactory.createDownloadDocumentResponse();

        InputStream stream = newBLDocer().streamDocument(request.getToken().getValue(),request.getDocId().getValue());
        InputStreamDataSource ds = new InputStreamDataSource(stream) {
            @Override
            public String getContentType() {
                return "application/octet-stream";
            }
        };
        javax.activation.DataHandler handler = new DataHandler(ds);


        StreamDescriptor sd = new StreamDescriptor();
        try {
            Map<String,Object> extra = SolrBaseUtil.extraFields.get();
            if (extra!=null && extra.containsKey("streamSize")){
                Number length = (Number) extra.get("streamSize") ;
                sd.setByteSize( length.longValue() );
            } else {
                byte[] bytes = IOUtils.toByteArray(stream);
                handler = new DataHandler(new ByteArrayDataSource(bytes));
                sd.setByteSize(  new Long(bytes.length) );
            }
        } catch (IOException e) {
            sd.setByteSize(0L);
        }

        sd.setHandler(WSTransformer.sdkfFactory.createStreamDescriptorHandler(handler));

        JAXBElement<StreamDescriptor> jsd = WSTransformer.wsFactory.createDownloadDocumentResponseReturn(sd);
        response.setReturn(jsd);

        DualProtocolSaajSoapMessageFactory.forceJaxb.set(true);

        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getUserRightsAnagrafiche")
    @ResponsePayload
    public GetUserRightsAnagraficheResponse getUserRightsAnagrafiche(@RequestPayload GetUserRightsAnagrafiche request) throws DocerException {

        EnumACLRights result = newBLDocer().getUserRightsAnagrafiche(
                request.getToken().getValue(),
                request.getType().getValue(),
                WSTransformer.toMap(request.getId(),false),
                request.getUserId().getValue() );

        GetUserRightsAnagraficheResponse response = WSTransformer.wsFactory.createGetUserRightsAnagraficheResponse();
        response.setReturn(result.getCode());
        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "createFascicolo")
    @ResponsePayload
    public CreateFascicoloResponse createFascicolo(@RequestPayload CreateFascicolo request) throws DocerException {

        newBLDocer().createFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloInfo(),false));

        CreateFascicoloResponse response = WSTransformer.wsFactory.createCreateFascicoloResponse();
        response.setReturn(true);
        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getProfileDocument")
    @ResponsePayload
    public GetProfileDocumentResponse getProfileDocument(@RequestPayload GetProfileDocument request) throws DocerException {

        Map<String,String> map = newBLDocer().getProfileDocument(
                request.getToken().getValue(),
                request.getDocId().getValue());

        GetProfileDocumentResponse response = WSTransformer.wsFactory.createGetProfileDocumentResponse();
        response.getReturn().addAll(WSTransformer.toPairs(map));
        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "fascicolaDocumento")
    @ResponsePayload
    public FascicolaDocumentoResponse fascicolaDocumento(@RequestPayload FascicolaDocumento request) throws DocerException {

        newBLDocer().fascicolaDocumento(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                WSTransformer.toMap(request.getMetadata(),false));

        FascicolaDocumentoResponse response = WSTransformer.wsFactory.createFascicolaDocumentoResponse();
        response.setReturn(true);
        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateFascicolo")
    @ResponsePayload
    public UpdateFascicoloResponse updateFascicolo(@RequestPayload UpdateFascicolo request) throws DocerException {

        newBLDocer().updateFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloId(),false),
                WSTransformer.toMap(request.getFascicoloInfo(),false));

        UpdateFascicoloResponse response = WSTransformer.wsFactory.createUpdateFascicoloResponse();
        response.setReturn(true);
        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getUserRights")
    @ResponsePayload
    public GetUserRightsResponse updateFascicolo(@RequestPayload GetUserRights request) throws DocerException {

        EnumACLRights result = newBLDocer().getUserRights(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                request.getUserId().getValue());

        GetUserRightsResponse response = WSTransformer.wsFactory.createGetUserRightsResponse();
        response.setReturn(result.getCode());
        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getACLDocument")
    @ResponsePayload
    public GetACLDocumentResponse getACLDocument(@RequestPayload GetACLDocument request) throws DocerException {

        Map<String,EnumACLRights> result = newBLDocer().getACLDocument(
                request.getToken().getValue(),
                request.getDocId().getValue());

        GetACLDocumentResponse response = WSTransformer.wsFactory.createGetACLDocumentResponse();
        response.getReturn().addAll(WSTransformer.toArray2(result));
        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getRelatedDocuments")
    @ResponsePayload
    public GetRelatedDocumentsResponse getRelatedDocuments(@RequestPayload GetRelatedDocuments request) throws DocerException {

        List<String> result = newBLDocer().getRelatedDocuments(
                request.getToken().getValue(),
                request.getDocId().getValue());

        GetRelatedDocumentsResponse response = WSTransformer.wsFactory.createGetRelatedDocumentsResponse();
        response.getReturn().addAll(result);
        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "registraDocumento")
    @ResponsePayload
    public RegistraDocumentoResponse registraDocumento(@RequestPayload RegistraDocumento request) throws DocerException {

        newBLDocer().registraDocumento(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                WSTransformer.toMap(request.getMetadata(),false));

        RegistraDocumentoResponse response = WSTransformer.wsFactory.createRegistraDocumentoResponse();
        response.setReturn(true);
        return response;
    }

    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "protocollaDocumento")
    @ResponsePayload
    public ProtocollaDocumentoResponse protocollaDocumento(@RequestPayload ProtocollaDocumento request) throws DocerException {

        newBLDocer().protocollaDocumento(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                WSTransformer.toMap(request.getMetadata(),false));

        ProtocollaDocumentoResponse response = WSTransformer.wsFactory.createProtocollaDocumentoResponse();
        response.setReturn(true);
        return response;
    }

    /** documento **/

    //searchDocuments
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "searchDocuments")
    @ResponsePayload
    public SearchDocumentsResponse searchDocuments(@RequestPayload SearchDocuments request) throws DocerException {

        List<ISearchItem> items = newBLDocer().searchDocuments(
                request.getToken().getValue(),
                WSTransformer.toMap3(request.getSearchCriteria()),
                request.getKeywords(),
                request.getMaxRows(),
                WSTransformer.toMap4(request.getOrderby()));

        SearchDocumentsResponse response = new SearchDocumentsResponse();
        response.getReturn().addAll(WSTransformer.toSearchItemArray(items));
        return response;
    }

    //downloadVersion
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "downloadVersion")
    @ResponsePayload
    public DownloadVersionResponse downloadVersion(@RequestPayload DownloadVersion request) throws DocerException {

        DownloadVersionResponse response = WSTransformer.wsFactory.createDownloadVersionResponse();

        InputStream stream = newBLDocer().streamVersion(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                request.getVersionNumber().getValue());
        InputStreamDataSource ds = new InputStreamDataSource(stream) {
            @Override
            public String getContentType() {
                return "application/octet-stream";
            }
        };
        javax.activation.DataHandler handler = new DataHandler(ds);

        StreamDescriptor sd = new StreamDescriptor();
        try {
            Map<String,Object> extra = SolrBaseUtil.extraFields.get();
            if (extra!=null && extra.containsKey("streamSize")){
                Number length = (Number) extra.get("streamSize") ;
                sd.setByteSize( length.longValue() );
            } else {
                byte[] bytes = IOUtils.toByteArray(stream);
                handler = new DataHandler(new ByteArrayDataSource(bytes));
                sd.setByteSize(  new Long(bytes.length) );
            }
        } catch (IOException e) {
            sd.setByteSize(0L);
        }
        sd.setHandler(WSTransformer.sdkfFactory.createStreamDescriptorHandler(handler));

        JAXBElement<StreamDescriptor> jsd = WSTransformer.wsFactory.createDownloadDocumentResponseReturn(sd);
        response.setReturn(jsd);

        DualProtocolSaajSoapMessageFactory.forceJaxb.set(true);

        return response;
    }

    //addNewVersion
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "addNewVersion")
    @ResponsePayload
    public AddNewVersionResponse addNewVersion(@RequestPayload AddNewVersion request) throws DocerException {

        try {
            InputStream is = getAttachment(request.getFile());
            String version = newBLDocer().addNewVersion( request.getToken().getValue() ,request.getDocId().getValue(),is);
            return WSTransformer.getReturn(AddNewVersionResponse.class,version);

        } catch (Exception e) {
            throw new DocerException(e);
        }
    }

    //replaceLastVersion
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "replaceLastVersion")
    @ResponsePayload
    public ReplaceLastVersionResponse replaceLastVersion(@RequestPayload ReplaceLastVersion request) throws DocerException {

        try {
            InputStream is = getAttachment(request.getFile());
            newBLDocer().replaceLastVersion( request.getToken().getValue() ,request.getDocId().getValue(),is);
            return WSTransformer.getReturn(ReplaceLastVersionResponse.class,true);

        } catch (Exception e) {
            throw new DocerException(e);
        }
    }

    //getVersionEstesa
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getVersionEstesa")
    @ResponsePayload
    public GetVersionEstesaResponse getVersionEstesa(@RequestPayload GetVersionEstesa request) throws DocerException {

        GetVersionEstesaResponse getVersionEstesaResponse = new GetVersionEstesaResponse();

        Version[] vers = newBLDocer().getVersionEstesa( request.getToken().getValue() ,request.getDocId().getValue());
        for( Version v : vers ){
            it.kdm.docer.sdk.classes.xsd.Version v2 = new it.kdm.docer.sdk.classes.xsd.Version();
            v2.getMetadata().addAll( WSTransformer.toPairs(v.getMetadata()));
            getVersionEstesaResponse.getReturn().add(v2);
        }

        return getVersionEstesaResponse;
    }

    //addRelated
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "addRelated")
    @ResponsePayload
    public AddRelatedResponse addRelated(@RequestPayload AddRelated request) throws DocerException {

        newBLDocer().addRelated(request.getToken().getValue(), request.getDocId().getValue(), request.getRelated());
        return WSTransformer.getReturn(AddRelatedResponse.class,true);
    }

    //removeRelated
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "removeRelated")
    @ResponsePayload
    public RemoveRelatedResponse removeRelated(@RequestPayload RemoveRelated request) throws DocerException {

        newBLDocer().removeRelated(request.getToken().getValue(), request.getDocId().getValue(), request.getRelated());
        return WSTransformer.getReturn(RemoveRelatedResponse.class,true);
    }

    //getVersions
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getVersions")
    @ResponsePayload
    public GetVersionsResponse getVersions(@RequestPayload GetVersions request) throws DocerException {

        List<String> vers = newBLDocer().getVersions(request.getToken().getValue(), request.getDocId().getValue());

        GetVersionsResponse response = new GetVersionsResponse();
        response.getReturn().addAll(vers);
        return response;
    }

    //getLockStatus
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getLockStatus")
    @ResponsePayload
    public GetLockStatusResponse getLockStatus(@RequestPayload GetLockStatus request) throws DocerException {

        ILockStatus istatus = newBLDocer().getLockStatus(request.getToken().getValue(), request.getDocId().getValue());
        LockStatus status = WSTransformer.sdkfFactory.createLockStatus();
        status.setUserId(WSTransformer.sdkfFactory.createLockStatusUserId(istatus.getUserId()));
        status.setFullName(WSTransformer.sdkfFactory.createLockStatusFullName(istatus.getFullName()));
        status.setLocked(istatus.getLocked());

        return WSTransformer.getReturn(GetLockStatusResponse.class,status);
    }

    //updateProfileDocument
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateProfileDocument")
    @ResponsePayload
    public UpdateProfileDocumentResponse updateProfileDocument(@RequestPayload UpdateProfileDocument request) throws DocerException {

        newBLDocer().updateProfileDocument(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                WSTransformer.toMap(request.getMetadata(),false));

        return WSTransformer.getReturn(UpdateProfileDocumentResponse.class,true);
    }

    //setACLDocument
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "setACLDocument")
    @ResponsePayload
    public SetACLDocumentResponse setACLDocument(@RequestPayload SetACLDocument request) throws DocerException {

        newBLDocer().setACLDocument(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                WSTransformer.toMap2(request.getAcls()));

        return WSTransformer.getReturn(SetACLDocumentResponse.class,true);
    }

    //changeACLDocumento
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "changeACLDocumento")
    @ResponsePayload
    public ChangeACLDocumentoResponse changeACLDocumento(@RequestPayload ChangeACLDocumento request) throws DocerException {

        String[] toRemove = new String[request.getAclToRemove().size()];
        for( int i=0; i<toRemove.length; i++  ){
            toRemove[i] = WSTransformer.addPrefix(request.getAclToRemove().get(i));
        }

        Map<String, EnumACLRights> acls = newBLDocer().updateACLDocumento(
                request.getToken().getValue(),
                request.getDocnum().getValue(),
                WSTransformer.toKeyValuePairsArray(request.getAclToAdd(),true),
                toRemove);

        ChangeACLDocumentoResponse soap = new ChangeACLDocumentoResponse();
        soap.getReturn().addAll(WSTransformer.toArray2(acls));

        return soap;
    }

    //addNewAdvancedVersion
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "addNewAdvancedVersion")
    @ResponsePayload
    public AddNewAdvancedVersionResponse addNewAdvancedVersion(@RequestPayload AddNewAdvancedVersion request) throws DocerException {

        newBLDocer().addNewAdvancedVersion(
                request.getToken().getValue(),
                request.getDocIdLastVersion().getValue(),
                request.getDocIdNewVersion().getValue());

        return WSTransformer.getReturn(AddNewAdvancedVersionResponse.class,true);
    }

    //getAdvancedVersions
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getAdvancedVersions")
    @ResponsePayload
    public GetAdvancedVersionsResponse getAdvancedVersions(@RequestPayload GetAdvancedVersions request) throws DocerException {

        List<String> vers = newBLDocer().getAdvancedVersions(
                request.getToken().getValue(),
                request.getDocId().getValue());

        GetAdvancedVersionsResponse response = new GetAdvancedVersionsResponse();
        response.getReturn().addAll(vers);
        return response;
    }

    //addOldAdvancedVersion
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "addOldAdvancedVersion")
    @ResponsePayload
    public AddOldAdvancedVersionResponse addOldAdvancedVersion(@RequestPayload AddOldAdvancedVersion request) throws DocerException {

        newBLDocer().addOldAdvancedVersion(
                request.getToken().getValue(),
                request.getDocIdLastVersion().getValue(),
                request.getDocIdLastVersion().getValue(),
                request.getVersion());

        return WSTransformer.getReturn(AddOldAdvancedVersionResponse.class,true);
    }

    //classificaDocumento
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "classificaDocumento")
    @ResponsePayload
    public ClassificaDocumentoResponse classificaDocumento(@RequestPayload ClassificaDocumento request) throws DocerException {

        newBLDocer().classificaDocumento(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                WSTransformer.toMap(request.getMetadata(),false));

        return WSTransformer.getReturn(ClassificaDocumentoResponse.class,true);
    }

    //removeRiferimentiDocuments
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "removeRiferimentiDocuments")
    @ResponsePayload
    public RemoveRiferimentiDocumentsResponse removeRiferimentiDocuments(@RequestPayload RemoveRiferimentiDocuments request) throws DocerException {

        newBLDocer().removeRiferimentiDocuments(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                request.getRiferimenti());

        return WSTransformer.getReturn(RemoveRiferimentiDocumentsResponse.class,true);
    }

    //addRiferimentiDocuments
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "addRiferimentiDocuments")
    @ResponsePayload
    public AddRiferimentiDocumentsResponse addRiferimentiDocuments(@RequestPayload AddRiferimentiDocuments request) throws DocerException {

        newBLDocer().addRiferimentiDocuments(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                request.getRiferimenti());

        return WSTransformer.getReturn(AddRiferimentiDocumentsResponse.class,true);
    }

    //getRiferimentiDocuments
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getRiferimentiDocuments")
    @ResponsePayload
    public GetRiferimentiDocumentsResponse getRiferimentiDocuments(@RequestPayload GetRiferimentiDocuments request) throws DocerException {

        List<String> refs = newBLDocer().getRiferimentiDocuments(
                request.getToken().getValue(),
                request.getDocId().getValue());

        GetRiferimentiDocumentsResponse response = new GetRiferimentiDocumentsResponse();
        response.getReturn().addAll(refs);
        return response;
    }

    //pubblicaDocumento
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "pubblicaDocumento")
    @ResponsePayload
    public PubblicaDocumentoResponse pubblicaDocumento(@RequestPayload PubblicaDocumento request) throws DocerException {

        newBLDocer().pubblicaDocumento(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                WSTransformer.toMap(request.getMetadata(),false));

        return WSTransformer.getReturn(PubblicaDocumentoResponse.class,true);
    }

    //deleteDocument
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "deleteDocument")
    @ResponsePayload
    public DeleteDocumentResponse deleteDocument(@RequestPayload DeleteDocument request) throws DocerException {

        newBLDocer().deleteDocument(
                request.getToken().getValue(),
                request.getDocId().getValue());

        return WSTransformer.getReturn(DeleteDocumentResponse.class,true);
    }

    //getDocumentTypesByAOO
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getDocumentTypesByAOO")
    @ResponsePayload
    public GetDocumentTypesByAOOResponse getDocumentTypesByAOO(@RequestPayload GetDocumentTypesByAOO request) throws DocerException {

        List<IKeyValuePair> pairs = newBLDocer().getDocumentTypes(
                request.getToken().getValue(),
                request.getCodiceEnte().getValue(),
                request.getCodiceAOO().getValue());

        GetDocumentTypesByAOOResponse getDocumentTypesByAOOResponse = new GetDocumentTypesByAOOResponse();
        getDocumentTypesByAOOResponse.getReturn().addAll(WSTransformer.toPairs(pairs.toArray(new IKeyValuePair[0])));

        return getDocumentTypesByAOOResponse;
    }

    //getDocumentTypes
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getDocumentTypes")
    @ResponsePayload
    public GetDocumentTypesResponse getDocumentTypes(@RequestPayload GetDocumentTypes request) throws DocerException {

        List<IKeyValuePair> pairs = newBLDocer().getDocumentTypes(
                request.getToken().getValue());

        GetDocumentTypesResponse getDocumentTypesResponse = new GetDocumentTypesResponse();
        getDocumentTypesResponse.getReturn().addAll(WSTransformer.toPairs(pairs.toArray(new IKeyValuePair[0])));

        return getDocumentTypesResponse;
    }

    //unlockDocument
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "unlockDocument")
    @ResponsePayload
    public UnlockDocumentResponse unlockDocument(@RequestPayload UnlockDocument request) throws DocerException {
        newBLDocer().unlockDocument(request.getToken().getValue(),request.getDocId().getValue());
        return WSTransformer.getReturn(UnlockDocumentResponse.class,true);
    }

    //lockDocument
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "lockDocument")
    @ResponsePayload
    public LockDocumentResponse lockDocument(@RequestPayload LockDocument request) throws DocerException {
        newBLDocer().lockDocument(request.getToken().getValue(),request.getDocId().getValue());
        return WSTransformer.getReturn(LockDocumentResponse.class,true);
    }

    //archiviaDocumento
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "archiviaDocumento")
    @ResponsePayload
    public ArchiviaDocumentoResponse archiviaDocumento(@RequestPayload ArchiviaDocumento request) throws DocerException {
        newBLDocer().archiviaDocumento(
                request.getToken().getValue(),
                request.getDocId().getValue(),
                WSTransformer.toMap(request.getMetadata(),false));
        return WSTransformer.getReturn(ArchiviaDocumentoResponse.class,true);
    }

    //getHistory
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getHistory")
    @ResponsePayload
    public GetHistoryResponse getHistory(@RequestPayload GetHistory request) throws DocerException {
        List<IHistoryItem> history = newBLDocer().getHistory(
                request.getToken().getValue(),
                request.getDocId().getValue());

        List<HistoryItem> historyItems = new ArrayList<>();

        for( IHistoryItem ihi : history ){
            HistoryItem item = new HistoryItem();

            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(ihi.getDate().getTime());
            XMLGregorianCalendar impl = new XMLGregorianCalendarImpl(gc);

            item.setUser( WSTransformer.sdkfFactory.createHistoryItemUser(ihi.getUser()) );
            item.setDate( WSTransformer.sdkfFactory.createHistoryItemDate(impl) );
            item.setDescription( WSTransformer.sdkfFactory.createHistoryItemDescription(ihi.getDescription()) );

            historyItems.add(item);
        }

        GetHistoryResponse response = new GetHistoryResponse();
        response.getReturn().addAll(historyItems);
        return response;
    }

    /** fascicoli **/

    //getFascicolo
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getFascicolo")
    @ResponsePayload
    public GetFascicoloResponse getFascicolo(@RequestPayload GetFascicolo request) throws DocerException {

        Map<String,String> map = newBLDocer().getFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloId(),false));

        GetFascicoloResponse getFascicoloResponse = new GetFascicoloResponse();
        getFascicoloResponse.getReturn().addAll(WSTransformer.toPairs(map));
        return getFascicoloResponse;
    }

    //setACLFascicolo
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "setACLFascicolo")
    @ResponsePayload
    public SetACLFascicoloResponse setACLFascicolo(@RequestPayload SetACLFascicolo request) throws DocerException {

        newBLDocer().setACLFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloId(),false),
                WSTransformer.toMap2(request.getAcls()));

        return WSTransformer.getReturn(SetACLFascicoloResponse.class,true);
    }

    //getACLFascicolo
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getACLFascicolo")
    @ResponsePayload
    public GetACLFascicoloResponse setACLFascicolo(@RequestPayload GetACLFascicolo request) throws DocerException {

        Map<String,EnumACLRights> alcs = newBLDocer().getACLFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloId(),false));

        GetACLFascicoloResponse getACLFascicoloResponse = new GetACLFascicoloResponse();
        getACLFascicoloResponse.getReturn().addAll(WSTransformer.toArray2(alcs));
        return getACLFascicoloResponse;
    }

    //changeACLFascicolo
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "changeACLFascicolo")
    @ResponsePayload
    public ChangeACLFascicoloResponse changeACLFascicolo(@RequestPayload ChangeACLFascicolo request) throws DocerException {

        String[] toRemove = new String[request.getAclToRemove().size()];
        for( int i=0; i<toRemove.length; i++  ){
            toRemove[i] = WSTransformer.addPrefix(request.getAclToRemove().get(i));
        }
        Map<String,EnumACLRights> alcs = newBLDocer().updateACLFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloId(),false),
                WSTransformer.toKeyValuePairsArray(request.getAclToAdd(),true),
                toRemove);

        ChangeACLFascicoloResponse response = new ChangeACLFascicoloResponse();
        response.getReturn().addAll(WSTransformer.toArray2(alcs));
        return response;
    }

    //searchDocumentiFascicolo
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "searchDocumentiFascicolo")
    @ResponsePayload
    public SearchDocumentiFascicoloResponse searchDocumentiFascicolo(@RequestPayload SearchDocumentiFascicolo request) throws DocerException {

        List<ISearchItem> items = newBLDocer().searchDocumentiFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloId(),false),
                request.getMaxRows());

        SearchDocumentiFascicoloResponse response = new SearchDocumentiFascicoloResponse();
        response.getReturn().addAll(WSTransformer.toSearchItemArray(items));
        return response;
    }

    //removeRiferimentiFascicolo
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "removeRiferimentiFascicolo")
    @ResponsePayload
    public RemoveRiferimentiFascicoloResponse removeRiferimentiFascicolo(@RequestPayload RemoveRiferimentiFascicolo request) throws DocerException {

        List<SearchItem> items = request.getRiferimenti();

        newBLDocer().removeRiferimentiFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloId(),false),
                WSTransformer.toList1(items.toArray(new SearchItem[0])));

        return WSTransformer.getReturn(RemoveRiferimentiFascicoloResponse.class,true);
    }

    //getRiferimentiFascicolo
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getRiferimentiFascicolo")
    @ResponsePayload
    public GetRiferimentiFascicoloResponse getRiferimentiFascicolo(@RequestPayload GetRiferimentiFascicolo request) throws DocerException {

        List<ISearchItem> items = newBLDocer().getRiferimentiFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloId(),false));

        GetRiferimentiFascicoloResponse response = new GetRiferimentiFascicoloResponse();
        response.getReturn().addAll(WSTransformer.toSearchItemArray(items));
        return response;
    }

    //addRiferimentiFascicolo
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "addRiferimentiFascicolo")
    @ResponsePayload
    public AddRiferimentiFascicoloResponse addRiferimentiFascicolo(@RequestPayload AddRiferimentiFascicolo request) throws DocerException {

        List<SearchItem> items0 = request.getRiferimenti();

        newBLDocer().addRiferimentiFascicolo(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFascicoloId(),false),
                WSTransformer.toList1(items0.toArray(new SearchItem[0])));

        return WSTransformer.getReturn(AddRiferimentiFascicoloResponse.class,true);
    }

    /** anagrafiche **/

    //searchAnagrafiche
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "searchAnagrafiche")
    @ResponsePayload
    public SearchAnagraficheResponse searchAnagrafiche(@RequestPayload SearchAnagrafiche request) throws DocerException {

        List<ISearchItem> items = newBLDocer().searchAnagrafiche(
                request.getToken().getValue(),
                request.getType().getValue(),
                WSTransformer.toMap3(request.getSearchCriteria()));

        SearchAnagraficheResponse response = new SearchAnagraficheResponse();
        response.getReturn().addAll(WSTransformer.toSearchItemArray(items));
        return response;
    }

    //searchAnagraficheEstesa
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "searchAnagraficheEstesa")
    @ResponsePayload
    public SearchAnagraficheEstesaResponse searchAnagraficheEstesa(@RequestPayload SearchAnagraficheEstesa request) throws DocerException {

        List<ISearchItem> items = newBLDocer().searchAnagraficheEstesa(
                request.getToken().getValue(),
                request.getType().getValue(),
                WSTransformer.toMap3(request.getSearchCriteria()),
                request.getMaxRows(),
                WSTransformer.toMap4(request.getOrderby()));

        SearchAnagraficheEstesaResponse response = new SearchAnagraficheEstesaResponse();
        response.getReturn().addAll(WSTransformer.toSearchItemArray(items));
        return response;
    }

    //getAnagraficaCustom
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getAnagraficaCustom")
    @ResponsePayload
    public GetAnagraficaCustomResponse getAnagraficaCustom(@RequestPayload GetAnagraficaCustom request) throws DocerException {

        Map<String,String> map = newBLDocer().getAnagraficaCustom(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getCustomId(),false));

        GetAnagraficaCustomResponse response = new GetAnagraficaCustomResponse();
        response.getReturn().addAll(WSTransformer.toPairs(map));
        return response;
    }

    //createAnagraficaCustom
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "createAnagraficaCustom")
    @ResponsePayload
    public CreateAnagraficaCustomResponse createAnagraficaCustom(@RequestPayload CreateAnagraficaCustom request) throws DocerException {

        newBLDocer().createAnagraficaCustom(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getCustomInfo(),false));

        return WSTransformer.getReturn(CreateAnagraficaCustomResponse.class,true);
    }

    //updateAnagraficaCustom
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateAnagraficaCustom")
    @ResponsePayload
    public UpdateAnagraficaCustomResponse updateAnagraficaCustom(@RequestPayload UpdateAnagraficaCustom request) throws DocerException {

        newBLDocer().updateAnagraficaCustom(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getCustomId(),false),
                WSTransformer.toMap(request.getCustomInfo(),false));

        return WSTransformer.getReturn(UpdateAnagraficaCustomResponse.class,true);
    }

    /** titolari **/

    //getTitolario
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getTitolario")
    @ResponsePayload
    public GetTitolarioResponse getTitolario(@RequestPayload GetTitolario request) throws DocerException {

        Map<String,String> map = newBLDocer().getTitolario(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getTitolarioId(),false));

        GetTitolarioResponse response = new GetTitolarioResponse();
        response.getReturn().addAll(WSTransformer.toPairs(map));
        return response;
    }

    //getACLTitolario
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getACLTitolario")
    @ResponsePayload
    public GetACLTitolarioResponse getACLTitolario(@RequestPayload GetACLTitolario request) throws DocerException {

        Map<String,EnumACLRights> map = newBLDocer().getACLTitolario(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getTitolarioId(),false));

        GetACLTitolarioResponse response = new GetACLTitolarioResponse();
        response.getReturn().addAll(WSTransformer.toArray2(map));
        return response;
    }

    //updateTitolario
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateTitolario")
    @ResponsePayload
    public UpdateTitolarioResponse updateTitolario(@RequestPayload UpdateTitolario request) throws DocerException {

        newBLDocer().updateTitolario(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getTitolarioId(),false),
                WSTransformer.toMap(request.getTitolarioInfo(),false));

        return WSTransformer.getReturn(UpdateTitolarioResponse.class,true);
    }

    //createTitolario
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "createTitolario")
    @ResponsePayload
    public CreateTitolarioResponse createTitolario(@RequestPayload CreateTitolario request) throws DocerException {

        newBLDocer().createTitolario(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getTitolarioInfo(),false));

        return WSTransformer.getReturn(CreateTitolarioResponse.class,true);
    }

    //setACLTitolario
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "setACLTitolario")
    @ResponsePayload
    public SetACLTitolarioResponse setACLTitolario(@RequestPayload SetACLTitolario request) throws DocerException {

        newBLDocer().setACLTitolario(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getTitolarioId(),false),
                WSTransformer.toMap2(request.getAcls()));

        return WSTransformer.getReturn(SetACLTitolarioResponse.class,true);
    }

    //changeACLTitolario
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "changeACLTitolario")
    @ResponsePayload
    public ChangeACLTitolarioResponse changeACLTitolario(@RequestPayload ChangeACLTitolario request) throws DocerException {

        String[] toRemove = new String[request.getAclToRemove().size()];
        for( int i=0; i<toRemove.length; i++  ){
            toRemove[i] = WSTransformer.addPrefix(request.getAclToRemove().get(i));
        }

        Map<String,EnumACLRights> acls = newBLDocer().updateACLTitolari(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getTitolarioId(),false),
                WSTransformer.toKeyValuePairsArray(request.getAclToAdd(),true),
                toRemove);

        ChangeACLTitolarioResponse response = new ChangeACLTitolarioResponse();
        response.getReturn().addAll(WSTransformer.toArray2(acls));
        return response;
    }

    /** utenti e gruppi **/

    //getUser
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getUser")
    @ResponsePayload
    public GetUserResponse getUser(@RequestPayload GetUser request) throws DocerException {

        Map<String,String> map = newBLDocer().getUser(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getUserId().getValue()) );

        GetUserResponse response = new GetUserResponse();
        response.getReturn().addAll(WSTransformer.toPairs(map));
        return response;
    }

    //searchUsers
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "searchUsers")
    @ResponsePayload
    public SearchUsersResponse searchUsers(@RequestPayload SearchUsers request) throws DocerException {

        List<ISearchItem> items = newBLDocer().searchUsers(
                request.getToken().getValue(),
                WSTransformer.toMap3(request.getSearchCriteria()));

        SearchUsersResponse response = new SearchUsersResponse();
        response.getReturn().addAll(WSTransformer.toSearchItemArray(items));
        return response;
    }

    //getGroup
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getGroup")
    @ResponsePayload
    public GetGroupResponse getGroup(@RequestPayload GetGroup request) throws DocerException {

        Map<String,String> map = newBLDocer().getGroup(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getGroupId().getValue()) );

        GetGroupResponse response = new GetGroupResponse();
        response.getReturn().addAll(WSTransformer.toPairs(map));
        return response;
    }

    //searchGroups
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "searchGroups")
    @ResponsePayload
    public SearchGroupsResponse searchGroups(@RequestPayload SearchGroups request) throws DocerException {

        List<ISearchItem> items = newBLDocer().searchGroups(
                request.getToken().getValue(),
                WSTransformer.toMap3(request.getSearchCriteria()));

        SearchGroupsResponse response = new SearchGroupsResponse();
        response.getReturn().addAll(WSTransformer.toSearchItemArray(items));
        return response;
    }

    //getUsersOfGroup
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getUsersOfGroup")
    @ResponsePayload
    public GetUsersOfGroupResponse getUsersOfGroup(@RequestPayload GetUsersOfGroup request) throws DocerException {

        List<String> list = newBLDocer().getUsersOfGroup(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getGroupId().getValue()) );

        GetUsersOfGroupResponse response = new GetUsersOfGroupResponse();

        for( String el : list )
            response.getReturn().add(WSTransformer.removePrefix(el));

        return response;
    }

    //getGroupsOfUser
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getGroupsOfUser")
    @ResponsePayload
    public GetGroupsOfUserResponse getGroupsOfUser(@RequestPayload GetGroupsOfUser request) throws DocerException {

        List<String> list = newBLDocer().getGroupsOfUser(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getUserId().getValue()) );

        GetGroupsOfUserResponse response = new GetGroupsOfUserResponse();

        for( String el : list )
            response.getReturn().add(WSTransformer.removePrefix(el));

        return response;
    }

    //updateUser
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateUser")
    @ResponsePayload
    public UpdateUserResponse updateUser(@RequestPayload UpdateUser request) throws DocerException {

        newBLDocer().updateUser(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getUserId().getValue()),
                WSTransformer.toMap(request.getUserInfo(),false));

        return WSTransformer.getReturn(UpdateUserResponse.class,true);
    }

    //createUser
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "createUser")
    @ResponsePayload
    public CreateUserResponse createUser(@RequestPayload CreateUser request) throws DocerException {

        newBLDocer().createUser(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getUserInfo(),false));

        return WSTransformer.getReturn(CreateUserResponse.class,true);
    }

    //createGroup
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "createGroup")
    @ResponsePayload
    public CreateGroupResponse createGroup(@RequestPayload CreateGroup request) throws DocerException {

        newBLDocer().createGroup(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getGroupInfo(),false));

        return WSTransformer.getReturn(CreateGroupResponse.class,true);
    }

    //updateGroup
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateGroup")
    @ResponsePayload
    public UpdateGroupResponse updateGroup(@RequestPayload UpdateGroup request) throws DocerException {

        newBLDocer().updateGroup(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getGroupId().getValue()),
                WSTransformer.toMap(request.getGroupInfo(),false));

        return WSTransformer.getReturn(UpdateGroupResponse.class,true);
    }

    //setGroupsOfUser
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "setGroupsOfUser")
    @ResponsePayload
    public SetGroupsOfUserResponse setGroupsOfUser(@RequestPayload SetGroupsOfUser request) throws DocerException {

        List<String> list = request.getGroups();
        list = WSTransformer.removeNulls(list);
        for( int i=0; i<list.size(); i++)
            list.set(i, WSTransformer.addPrefix(list.get(i)));

        newBLDocer().setGroupsOfUser(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getUserId().getValue()),
                list);

        return WSTransformer.getReturn(SetGroupsOfUserResponse.class,true);
    }

    //updateGroupsOfUser
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateGroupsOfUser")
    @ResponsePayload
    public UpdateGroupsOfUserResponse updateGroupsOfUser(@RequestPayload UpdateGroupsOfUser request) throws DocerException {

        List<String> listAdd = request.getGroupsToAdd();
        listAdd = WSTransformer.removeNulls(listAdd);
        for( int i=0; i<listAdd.size(); i++)
            listAdd.set(i, WSTransformer.addPrefix(listAdd.get(i)));

        List<String> listRemove = request.getGroupsToRemove();
        listRemove = WSTransformer.removeNulls(listRemove);
        for( int i=0; i<listRemove.size(); i++)
            listRemove.set(i, WSTransformer.addPrefix(listRemove.get(i)));

        newBLDocer().updateGroupsOfUser(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getUserId().getValue()),
                listAdd,listRemove);

        return WSTransformer.getReturn(UpdateGroupsOfUserResponse.class,true);
    }

    //setUsersOfGroup
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "setUsersOfGroup")
    @ResponsePayload
    public SetUsersOfGroupResponse setUsersOfGroup(@RequestPayload SetUsersOfGroup request) throws DocerException {

        List<String> list = request.getUsers();
        list = WSTransformer.removeNulls(list);
        for( int i=0; i<list.size(); i++)
            list.set(i, WSTransformer.addPrefix(list.get(i)));

        newBLDocer().setUsersOfGroup(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getGroupId().getValue()),
                list);

        return WSTransformer.getReturn(SetUsersOfGroupResponse.class,true);
    }

    //updateUsersOfGroup
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateUsersOfGroup")
    @ResponsePayload
    public UpdateUsersOfGroupResponse updateUsersOfGroup(@RequestPayload UpdateUsersOfGroup request) throws DocerException {

        List<String> listAdd = request.getUsersToAdd();
        listAdd = WSTransformer.removeNulls(listAdd);

        for (int i = 0; i < listAdd.size(); i++) {
            listAdd.set(i, WSTransformer.addPrefix(listAdd.get(i)));
        }

        List<String> listRemove = request.getUsersToRemove();
        listRemove = WSTransformer.removeNulls(listRemove);

        for (int i = 0; i < listRemove.size(); i++) {
            listRemove.set(i, WSTransformer.addPrefix(listRemove.get(i)));
        }

        newBLDocer().updateUsersOfGroup(
                request.getToken().getValue(),
                WSTransformer.addPrefix(request.getGroupId().getValue()),
                listAdd,listRemove);

        return WSTransformer.getReturn(UpdateUsersOfGroupResponse.class,true);
    }

    /** folder **/

    //createFolder
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "createFolder")
    @ResponsePayload
    public CreateFolderResponse createFolder(@RequestPayload CreateFolder request) throws DocerException {

        String id = newBLDocer().createFolder(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getFolderInfo(),false));

        return WSTransformer.getReturn(CreateFolderResponse.class,id);
    }

    //getACLFolder
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getACLFolder")
    @ResponsePayload
    public GetACLFolderResponse getACLFolder(@RequestPayload GetACLFolder request) throws DocerException {

        Map<String,EnumACLRights> map = newBLDocer().getACLFolder(
                request.getToken().getValue(),
                request.getFolderId().getValue());

        GetACLFolderResponse response = new GetACLFolderResponse();
        response.getReturn().addAll(WSTransformer.toArray2(map));
        return response;
    }

    //changeACLFolder
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "changeACLFolder")
    @ResponsePayload
    public ChangeACLFolderResponse changeACLFolder(@RequestPayload ChangeACLFolder request) throws DocerException {

        String[] toRemove = new String[request.getAclToRemove().size()];
        for( int i=0; i<toRemove.length; i++  ){
            toRemove[i] = WSTransformer.addPrefix(request.getAclToRemove().get(i));
        }

        Map<String,EnumACLRights> acls = newBLDocer().updateACLFolder(
                request.getToken().getValue(),
                request.getFolderId().getValue(),
                WSTransformer.toKeyValuePairsArray(request.getAclToAdd(),true),
                toRemove);

        ChangeACLFolderResponse response = new ChangeACLFolderResponse();
        response.getReturn().addAll(WSTransformer.toArray2(acls));
        return response;
    }

    //getUserRightsFolder
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getUserRightsFolder")
    @ResponsePayload
    public GetUserRightsFolderResponse getUserRightsFolder(@RequestPayload GetUserRightsFolder request) throws DocerException {

        EnumACLRights acl = newBLDocer().getUserRightsFolder(
                request.getToken().getValue(),
                request.getFolderId().getValue(),
                request.getUserId().getValue());

        return WSTransformer.getReturn(GetUserRightsFolderResponse.class,acl.getCode());
    }

    //deleteFolder
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "deleteFolder")
    @ResponsePayload
    public DeleteFolderResponse deleteFolder(@RequestPayload DeleteFolder request) throws DocerException {

        newBLDocer().deleteFolder(
                request.getToken().getValue(),
                request.getFolderId().getValue());

        return WSTransformer.getReturn(DeleteFolderResponse.class,true);
    }

    //searchFolders
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "searchFolders")
    @ResponsePayload
    public SearchFoldersResponse searchFolders(@RequestPayload SearchFolders request) throws DocerException {

        List<ISearchItem> items = newBLDocer().searchFolders(
                request.getToken().getValue(),
                WSTransformer.toMap3(request.getSearchCriteria()),
                request.getMaxRows(),
                WSTransformer.toMap4(request.getOrderby()));

        SearchFoldersResponse response = new SearchFoldersResponse();
        response.getReturn().addAll(WSTransformer.toSearchItemArray(items));
        return response;
    }

    //updateFolder
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateFolder")
    @ResponsePayload
    public UpdateFolderResponse updateFolder(@RequestPayload UpdateFolder request) throws DocerException {

        newBLDocer().updateFolder(
                request.getToken().getValue(),
                request.getFolderId().getValue(),
                WSTransformer.toMap(request.getFolderInfo(),false));

        return WSTransformer.getReturn(UpdateFolderResponse.class,true);
    }

    //setACLFolder
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "setACLFolder")
    @ResponsePayload
    public SetACLFolderResponse setACLFolder(@RequestPayload SetACLFolder request) throws DocerException {

        newBLDocer().setACLFolder(
                request.getToken().getValue(),
                request.getFolderId().getValue(),
                WSTransformer.toMap2(request.getAcls()));

        return WSTransformer.getReturn(SetACLFolderResponse.class,true);
    }

    //addToFolderDocuments
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "addToFolderDocuments")
    @ResponsePayload
    public AddToFolderDocumentsResponse addToFolderDocuments(@RequestPayload AddToFolderDocuments request) throws DocerException {

        newBLDocer().addToFolderDocuments(
                request.getToken().getValue(),
                request.getFolderId().getValue(),
                request.getDocument());

        return WSTransformer.getReturn(AddToFolderDocumentsResponse.class,true);
    }

    //getFolderDocuments
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getFolderDocuments")
    @ResponsePayload
    public GetFolderDocumentsResponse getFolderDocuments(@RequestPayload GetFolderDocuments request) throws DocerException {

        List<String> docs = newBLDocer().getFolderDocuments(
                request.getToken().getValue(),
                request.getFolderId().getValue());

        GetFolderDocumentsResponse response = new GetFolderDocumentsResponse();
        response.getReturn().addAll(docs);
        return response;
    }

    //removeFromFolderDocuments
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "removeFromFolderDocuments")
    @ResponsePayload
    public RemoveFromFolderDocumentsResponse removeFromFolderDocuments(@RequestPayload RemoveFromFolderDocuments request) throws DocerException {

        newBLDocer().removeFromFolderDocuments(
                request.getToken().getValue(),
                request.getFolderId().getValue(),
                request.getDocument());

        return WSTransformer.getReturn(RemoveFromFolderDocumentsResponse.class,true);
    }

    /** enti ed aoo **/

    //getEnte
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getEnte")
    @ResponsePayload
    public GetEnteResponse getEnte(@RequestPayload GetEnte request) throws DocerException {

        Map<String,String> map = newBLDocer().getEnte(
                request.getToken().getValue(),
                request.getCodiceEnte().getValue());

        GetEnteResponse response = new GetEnteResponse();
        response.getReturn().addAll(WSTransformer.toPairs(map));
        return response;
    }

    //getAOO
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "getAOO")
    @ResponsePayload
    public GetAOOResponse getAOO(@RequestPayload GetAOO request) throws DocerException {

        Map<String,String> map = newBLDocer().getAOO(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getAooId(),false));

        GetAOOResponse response = new GetAOOResponse();
        response.getReturn().addAll(WSTransformer.toPairs(map));
        return response;
    }

    //updateEnte
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateEnte")
    @ResponsePayload
    public UpdateEnteResponse updateEnte(@RequestPayload UpdateEnte request) throws DocerException {

        newBLDocer().updateEnte(
                request.getToken().getValue(),
                request.getCodiceEnte().getValue(),
                WSTransformer.toMap(request.getEnteInfo(),false));

        return WSTransformer.getReturn(UpdateEnteResponse.class,true);
    }

    //updateAOO
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "updateAOO")
    @ResponsePayload
    public UpdateAOOResponse updateAOO(@RequestPayload UpdateAOO request) throws DocerException {

        newBLDocer().updateAOO(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getAooId(),false),
                WSTransformer.toMap(request.getAooInfo(),false));

        return WSTransformer.getReturn(UpdateAOOResponse.class,true);
    }

    //createAOO
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "createAOO")
    @ResponsePayload
    public CreateAOOResponse createAOO(@RequestPayload CreateAOO request) throws DocerException {

        newBLDocer().createAOO(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getAooInfo(),false));

        return WSTransformer.getReturn(CreateAOOResponse.class,true);
    }

    //createEnte
    @PayloadRoot(namespace = HTTP_WEBSERVICES_DOCER_KDM_IT, localPart = "createEnte")
    @ResponsePayload
    public CreateEnteResponse createEnte(@RequestPayload CreateEnte request) throws DocerException {

        newBLDocer().createEnte(
                request.getToken().getValue(),
                WSTransformer.toMap(request.getEnteInfo(),false));

        return WSTransformer.getReturn(CreateEnteResponse.class,true);
    }

}
