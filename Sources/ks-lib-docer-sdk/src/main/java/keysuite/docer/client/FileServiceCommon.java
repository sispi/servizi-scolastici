package keysuite.docer.client;

import com.google.common.base.Strings;
import keysuite.SessionUtils;
import keysuite.desktop.exceptions.KSExceptionBadRequest;
import keysuite.desktop.exceptions.KSExceptionForbidden;
import keysuite.desktop.exceptions.KSExceptionNotFound;
import keysuite.desktop.exceptions.KSRuntimeException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ContentDisposition;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static keysuite.docer.client.ClientUtils.throwKSException;

public class FileServiceCommon {

    public final static String DEFAULT = "upload";
    public final static String PUBLIC = "public";

    static ThreadLocal<String> threadFolderRef = new ThreadLocal<>();

    public static void setThreadFolderRef(String string){
        threadFolderRef.set(string);
    }

    public static String getThreadFolderRef() { return threadFolderRef.get(); };

    String aoo;
    String username;
    public FileServiceCommon(String aoo, String username){
        this.aoo = aoo;
        this.username = username;
    }

    public static void setStorePath(String store, String path){
        System.setProperty("tempfiles."+store,path);
    }

    public static void setFilesRoot(String path){
        System.setProperty("tempfiles", path);
    }

    public static String getFilesRoot(){
        String root = System.getProperty("tempfiles", ClientUtils.getConfigHome()+"/files");
        return root;
    }

    public static String getStorePath(String store){
        String path = System.getProperty("tempfiles."+store , getFilesRoot() + "/"+store );
        return path;
    }

    public static boolean canReadStore(String store){
        try {
            return new File(getStorePath(getStore(store))).canRead();
        } catch (SecurityException se) {
            return false;
        }
    }

    public static boolean canWriteStore(String store){
        try {
            if (store==null)
                store = DEFAULT;
            return new File(getStorePath(getStore(store))).canWrite();
        } catch (SecurityException se) {
            return false;
        }
    }

    public static Map<String,String> getAllStorePaths(){
        Map<String,String> list = new LinkedHashMap<>();
        list.put("tempfiles",getFilesRoot());
        for( String key : System.getProperties().stringPropertyNames() ){
            if (key.startsWith("tempfiles.")){
                list.put(key,System.getProperty(key));
            }
        }
        return list;
    }

    public static String getStore(String file_service_path){
        if (Strings.isNullOrEmpty(file_service_path))
            file_service_path = DEFAULT;

        file_service_path = encodeFileURL(file_service_path);

        if (file_service_path.contains("$")){
            return file_service_path.split("\\$")[0];
        } else {
            return file_service_path;
        }
    }

    public String getFileAuth(){
        return (aoo+","+username).toLowerCase();
    }

    public String getFileId(String file_service_path){
        if (Strings.isNullOrEmpty(file_service_path))
            file_service_path = DEFAULT;
        else
            file_service_path = encodeFileURL(file_service_path);

        String fileId;
        String auth = "";
        if (aoo!=null && username!=null){
            auth = getFileAuth()+"@";
        }

        String store_group;
        String filename;

        if (file_service_path.contains(".")){
            if (file_service_path.contains("$")){
                store_group = file_service_path.substring(0,file_service_path.lastIndexOf("$"));
                filename = file_service_path.substring(file_service_path.lastIndexOf("$")+1);
            } else {
                store_group = DEFAULT;
                filename = file_service_path;
            }
        } else {
            store_group = file_service_path;
            filename = null;
        }

        if (store_group.equals(PUBLIC) || store_group.startsWith(PUBLIC+"$"))
            auth = "";

        if (store_group.contains("$")){
            if (filename==null)
                filename = auth+ UUID.randomUUID().toString()+".tmp";
            fileId = store_group + "$" + filename;
        } else {
            if (filename==null){
                fileId = store_group + "$" + auth+UUID.randomUUID().toString()+".tmp";
            } else {
                fileId = store_group + "$" + auth+UUID.randomUUID().toString() + "$" + filename;
            }
        }

        return fileId;
    }

    public static String getStorePath(){
        return getStorePath(DEFAULT);
    }

    public static String encodeFileURL(String path){
        if (path==null)
            return null;
        else
            return path.replace("/","$").replace("%2f","$").replace("%2F","$");
    }

    public static String decodeFileURL(String fileId){
        if (fileId==null)
            return null;
        else
            return fileId.replace("$","/").replace("%2f","/").replace("%2F","/");
    }

    public NamedInputStream openURL(URL url, String bearer) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        try {
            InputStream is = null;
            String name = null;
            String _url = url.toString();

            if ("file".equals(url.getProtocol())) {
                try {
                    is = new FileInputStream(url.getFile());
                    name = FilenameUtils.getName(url.toString());
                } catch (FileNotFoundException e) {
                    throw new KSExceptionNotFound(e);
                }
            } else if (_url.contains("/files/")) {

                File file = getFile(_url.substring(_url.indexOf("/files/") + 7));

                if (file.exists()) {
                    is = new FileInputStream(file);
                    name = file.getName();
                }
            }

            if (is == null) {
                if (_url.contains("docer-local-url")){
                    url = new URL(_url.replace("http://docer-local-url",DocerBean.baseUrl.get()));
                }
                return FileServiceCommon.openGenericURL(url, bearer);
            }

            return NamedInputStream.getNamedInputStream(is, name);
        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    private static NamedInputStream openGenericURL(URL url, String bearer) {
        try {

            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

            if (bearer!=null)
                urlConn.setRequestProperty("Authorization", bearer );

            InputStream is = urlConn.getInputStream();
            String name = null;

            String cd = urlConn.getHeaderField("Content-Disposition");
            if (!Strings.isNullOrEmpty(cd)){
                ContentDisposition cdh = ContentDisposition.parse(cd);
                name = cdh.getFilename();
            }

            if (Strings.isNullOrEmpty(name)){
                String n = url.toString().replace("/","$");
                int idx = n.lastIndexOf("$");
                if (idx>0)
                    name = n.substring(idx+1);
                else
                    name = "file.bin";
            }

            return NamedInputStream.getNamedInputStream(is,name);

        } catch (IOException e) {
            throw new KSRuntimeException(e);
        }
    }

    public String getFilename(String fileId){
        fileId = decodeFileURL(fileId);
        String filename = fileId.substring(fileId.lastIndexOf("/")+1);
        return filename;
    }

    public File getFile(String fileId) throws KSExceptionForbidden {
        fileId = decodeFileURL(fileId);

        int idx = fileId.lastIndexOf("/");
        String store_group,filename;
        if (idx>=0){
            store_group = fileId.substring(0,idx);
            filename = fileId.substring(idx+1);
        } else {
            store_group = DEFAULT;
            filename = fileId;
        }

        if (filename.contains("@")){
            if (!filename.split("@")[0].equalsIgnoreCase( getFileAuth()))
                throw new KSExceptionForbidden("unauthorized access to file:"+fileId);
        }

        File temp = new File(getBasePath(store_group),filename);

        return temp;
    }

    public File getBasePath(String store_group) throws KSExceptionForbidden {

        String store = getStore(store_group);
        String base = getStorePath(store); //env.getProperty("tempfiles."+store , new File(Utils.getConfigHome() , "files" + "/"+store ).toString());

        if (store_group.contains("/")){
            String group = store_group.substring(store.length()+1);

            if (group.contains("@")){
                if (!group.split("@")[0].equalsIgnoreCase(getFileAuth()))
                    throw new KSExceptionForbidden("unauthorized access to group:"+group);
            }

            return new File(base, group);
        }

        return new File(base);
    }

    public String create(NamedInputStream file, String groupId) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        if (groupId == null)
            groupId = DEFAULT;

        String folderRef;
        if (SessionUtils.getRequest()!=null) {
            folderRef = SessionUtils.getRequest().getHeader("thread-folder-ref");
        } else {
            folderRef = threadFolderRef.get();
        }
        if (folderRef!=null)
            groupId += "/" + folderRef;

        String fileName = file.getName();
        if (fileName!=null){
            //if (groupId==null)
            //    groupId = DEFAULT;
            groupId+="/"+UUID.randomUUID()+"/"+fileName;
        }

        String fileId = getFileId(groupId);

        File f = getFile(fileId);

        if (f.exists())
            throw new KSExceptionBadRequest("il file "+fileId.replace("$","/")+" già esiste");

        try {
            FileUtils.copyInputStreamToFile(file.getStream(),f);
            return fileId;
        } catch (IOException e) {
            throw new KSExceptionBadRequest(e);
        }
    }

    public String createGroup(String store) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        if (store == null)
            store = DEFAULT;

        String groupId = store + "$" + getFileAuth() + "@" + UUID.randomUUID().toString();
        return groupId;
    }

    public NamedInputStream get(String fileId, String range) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {
        Integer offset = 0;
        Integer length = null;

        if (range!=null) {
            if (range.startsWith("bytes="))
                range = range.substring(6);
            String[] parts = range.split("-");

            offset = Integer.parseInt(parts[0]);

            if (parts.length > 1)
                length = Integer.parseInt(parts[1]) - offset + 1;
        }

        try {

            File f = getFile(fileId);

            if (!f.exists())
                throw new KSExceptionNotFound();

            InputStream stream = FileUtils.openInputStream(f);

            if (offset>0 || length!=null){

                long aval = stream.available()-offset;

                if (offset>0)
                    stream.skip(offset);

                if (length!=null && length!=aval){
                    byte[] bytes = new byte[length];
                    int read = IOUtils.read(stream,bytes,0,length);
                    stream.close();
                    assert(read==length);

                    stream = new ByteArrayInputStream(bytes);
                }
            }

            return NamedInputStream.getNamedInputStream(stream,f.getName());

        } catch (Exception e) {
            throw throwKSException(e);
        }
    }

    public void update(InputStream file,String fileId,String range) throws KSExceptionNotFound, KSExceptionBadRequest, KSExceptionForbidden {

        String[] parts = range.split("-");

        Integer offset = Integer.parseInt(parts[0]);
        Integer length = null;
        if (parts.length>1)
            length = Integer.parseInt(parts[1])-offset+1;

        try {

            InputStream is = file;

            if (offset>0){

                if (length!=null && is.available()!=length)
                    throw new KSExceptionBadRequest("range size must be equal to stream size");

                File oldFile = getFile(fileId);

                if (!oldFile.exists())
                    throw new KSExceptionNotFound();

                if (oldFile.length()!=offset)
                    throw new KSExceptionBadRequest("range start must be equal to file size");

                FileOutputStream fr = new FileOutputStream(oldFile, true);
                byte[] strToBytes = IOUtils.toByteArray(is);
                fr.write(strToBytes);
                fr.close();

            } else {
                FileUtils.copyInputStreamToFile(is,getFile(fileId));
            }


        } catch (IOException e) {
            throw new KSExceptionBadRequest(e);
        }
    }

    private void delete(File file){
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            try {
                FileUtils.forceDeleteOnExit(file);
            } catch (IOException e2) {
                throw new KSRuntimeException(e2);
            }
        }
    }

    public void delete(String fileId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        File f = getFile(fileId);
        if (!f.exists())
            throw new KSExceptionNotFound();
        delete(f);
    }

    public void deleteGroup(String groupId) throws KSExceptionNotFound, KSExceptionForbidden, KSExceptionBadRequest {
        File file = getBasePath(groupId);
        if (file.exists()) {
            delete(getBasePath(groupId));
            return;
        }
    }
}
