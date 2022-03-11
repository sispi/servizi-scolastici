package keysuite.docer.client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Folder extends DocerBean {

    public static final String TYPE = "folder";

    public Folder(){
        super();
    }

    public Folder(String folderId){
        this();
        setFolderId(folderId);
    }

    @Override
    public String getDocerId() {
        return getFolderId();
    }

    @Override
    public String getName() {
        return getDescrizione();
    }


    @JsonProperty("FOLDER_ID")
    String folderId;

    @JsonProperty("PARENT_FOLDER_ID")
    String parentFolderId;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @JsonProperty("FOLDER_NAME")
    String descrizione;

    @Override
    protected Integer getRightsMask() {
        return folder_mask;
    }

}
