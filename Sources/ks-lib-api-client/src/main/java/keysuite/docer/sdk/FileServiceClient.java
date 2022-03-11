package keysuite.docer.sdk;

import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.FileServiceCommon;
import keysuite.docer.client.IFiles;
import keysuite.docer.client.NamedInputStream;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static keysuite.docer.sdk.APIClient.*;

public class FileServiceClient implements IFiles {

    final static String collectionPath = "/files";
    final static String itemPath = collectionPath + "/{fileId}";

    public URL getFileURL(String fileId){
        try {
            return new URL(this.client.apiUri+"files/"+fileId);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    APIClient client;
    FileServiceCommon fsCommon;

    public FileServiceClient(APIClient client){
        this.client = client;
        this.fsCommon = client.getFileServiceCommon();
    }

    public String create(String groupId, NamedInputStream file) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String[]  ids = createMulti(groupId, new NamedInputStream[] {file});
        return ids[0];
    }

    @Override
    public String create(InputStream file, String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return create(groupId,NamedInputStream.getNamedInputStream(file,null));
    }

    public String[] createMulti(NamedInputStream... files) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return createMulti(null,files);
    }

    public String create(NamedInputStream file) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return create(null,file);
    }

    public String create(InputStream file) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return create(file,null);
    }

    public URL createURL(NamedInputStream file, String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String fileId = create(FileServiceCommon.encodeFileURL(groupId), file);
        return this.getFileURL(fileId);
    }

    public URL createURL(NamedInputStream file) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return createURL(file,null);
    }

    @Override
    public String createGroup(String store) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        if (fsCommon.canWriteStore(store)){
            return fsCommon.createGroup(store);
        } else {
            return client.execute(collectionPath+"/createGroup",store);
        }
    }

    /* master */
    public String[] createMulti(String groupId, NamedInputStream[] files) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        List<String> ids = new ArrayList<>();
        for( NamedInputStream file : files ){
            String fileId;
            if (fsCommon.canWriteStore(groupId)){
                fileId = fsCommon.create(file,groupId);
            } else {
                fileId = client.execute(collectionPath,file.getStream(),file.getName(),FileServiceCommon.encodeFileURL(groupId));
            }
            ids.add(fileId);
        }
        return ids.toArray(new String[0]);
    }

    @Override
    public void update(InputStream file, String fileId, String Range) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        if (fsCommon.canWriteStore(fileId)){
            fsCommon.update(file,fileId,Range);
        } else {
            client.execute(itemPath+PATCH,Range,file,FileServiceCommon.encodeFileURL(fileId));
        }
    }

    @Override
    public InputStream get(String fileId, String Range) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        NamedInputStream named = getNamed(fileId,Range);
        return named.getStream();
    }

    public NamedInputStream getNamed(String fileId, String Range) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        if (fsCommon.canReadStore(fileId)){
            return fsCommon.get(fileId,Range);
        } else {
            String name = fsCommon.getFilename(fileId);
            InputStream stream = client.execute(itemPath+GET,Range,null,FileServiceCommon.encodeFileURL(fileId));
            return NamedInputStream.getNamedInputStream(stream,name);
        }
    }

    @Override
    public void delete(String fileId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        if (fsCommon.canWriteStore(fileId)){
            fsCommon.delete(fileId);
        } else {
            client.execute(itemPath+DELETE,FileServiceCommon.encodeFileURL(fileId));
        }
    }

    @Override
    public void deleteGroup(String groupId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {

        if (fsCommon.canWriteStore(groupId)){
            fsCommon.deleteGroup(groupId);
        } else {
            client.execute(collectionPath+"/deleteGroup/{groupId}",FileServiceCommon.encodeFileURL(groupId));
        }
    }

    public NamedInputStream openURL(URL url) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return fsCommon.openURL(url, client.bearer.bearer());
    }

    public NamedInputStream openFile(String fileId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        return openURL(getFileURL(fileId));
    }
}
