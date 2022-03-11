package keysuite.docer.server;

import com.google.common.base.Strings;
import it.kdm.orchestratore.session.Session;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.docer.client.DocerBean;
import keysuite.docer.client.FileServiceCommon;
import keysuite.docer.client.IFiles;
import keysuite.docer.client.NamedInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static keysuite.docer.client.ClientUtils.throwKSException;

@Component
public class FileService implements IFiles {

    @Autowired
    protected Environment env;

    //public final static String DEFAULT_STORE = "default";
    public final static String DEFAULT = "upload";

    /* la prima parte del groupid è lo store */
    @Override
    public String create(InputStream file, String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        FileServiceCommon fsCommon = new FileServiceCommon(Session.getUserInfo().getCodAoo(),Session.getUserInfo().getUsername());
        return fsCommon.create(NamedInputStream.getNamedInputStream(file,null),groupId);
    }

    @Override
    public String createGroup(String store) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        FileServiceCommon fsCommon = new FileServiceCommon(Session.getUserInfo().getCodAoo(),Session.getUserInfo().getUsername());
        return fsCommon.createGroup(store);
    }

    @Override
    public InputStream get(String fileId, String range) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        FileServiceCommon fsCommon = new FileServiceCommon(Session.getUserInfo().getCodAoo(),Session.getUserInfo().getUsername());
        return fsCommon.get(fileId,range).getStream();
    }

    @Override
    public void update(InputStream file,String fileId,String range) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        FileServiceCommon fsCommon = new FileServiceCommon(Session.getUserInfo().getCodAoo(),Session.getUserInfo().getUsername());
        fsCommon.update(file,fileId,range);
    }

    @Override
    public void delete(String fileId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        FileServiceCommon fsCommon = new FileServiceCommon(Session.getUserInfo().getCodAoo(),Session.getUserInfo().getUsername());
        fsCommon.delete(fileId);
    }

    @Override
    public void deleteGroup(String groupId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        FileServiceCommon fsCommon = new FileServiceCommon(Session.getUserInfo().getCodAoo(),Session.getUserInfo().getUsername());
        fsCommon.deleteGroup(groupId);
    }

    public URL createURL(InputStream stream) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return createURL(stream,null);
    }

    public URL createURL(NamedInputStream stream) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        return createURL(stream.getStream(),stream.getName());
    }

    public URL createURL(InputStream stream, String path) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        String fileId = create(stream,path);
        try {
            return new URL(DocerBean.baseUrl.get()+"/files/"+fileId);
        } catch (MalformedURLException e) {
            throw throwKSException(e);
        }
    }

    public NamedInputStream openURL(URL url) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        FileServiceCommon fsCommon = new FileServiceCommon(Session.getUserInfo().getCodAoo(),Session.getUserInfo().getUsername());
        String jwtToken = Session.getRequest().getHeader("Authorization");
        if (Strings.isNullOrEmpty(jwtToken))
            jwtToken = (String) Session.getRequest().getAttribute("authorization");
        return fsCommon.openURL(url,jwtToken);
    }

    public String getFilename(String fileId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        FileServiceCommon fsCommon = new FileServiceCommon(Session.getUserInfo().getCodAoo(),Session.getUserInfo().getUsername());
        return fsCommon.getFile(fileId).getName();
    }
}
