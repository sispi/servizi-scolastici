package keysuite.docer.client;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import keysuite.cache.ClientCache;

public class Group extends Actor {

    public Group(){
        super();
    }

    public Group(String groupId){
        this();
        setGroupId(groupId);
    }

    public static final String TYPE = "group";
    public static final String TYPE_AOO = "aoo";
    public static final String TYPE_ENTE = "ente";

    @JsonProperty("GROUP_ID")
    String groupId;

    @JsonProperty("prefix")
    String prefix = "";

    public String getPrefixedGroupId() {
        return ClientCache.suffix(this.getPrefix()) + this.getGroupId();
    }

    public boolean isAdmin(){

        /*if (groupId==null)
            return false;

        if ("admins".equals(groupId))
            return true;

        Group ente = ClientCache.getInstance().getEnte(this.getCodEnte());

        if (ente!=null && groupId.equals(ente.otherFields().get("ADMIN_GROUP_ID")))
            return true;

        Group aoo = ClientCache.getInstance().getEnte(this.getCodAoo());

        if (aoo!=null && groupId.equals(aoo.otherFields().get("ADMIN_GROUP_ID")))
            return true;*/

        return false;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getParentGroupId() {
        return parentGroupId;
    }

    public void setParentGroupId(String parentGroupId) {
        this.parentGroupId = parentGroupId;
    }

    public boolean isStruttura() {
        return struttura;
    }

    public void setStruttura(boolean struttura) {
        this.struttura = struttura;
    }

    @JsonProperty("GROUP_NAME")
    String groupName;

    public String getEmail() {
        return email;
    }

    @Override
    public String getLanguage() {
        return "it";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("EMAIL_ADDRESS")
    @JsonAlias("GROUP_MAIL")
    String email;

    @JsonProperty("PARENT_GROUP_ID")
    String parentGroupId;

    @JsonProperty("GRUPPO_STRUTTURA")
    boolean struttura;

    @Override
    public String getDocerId() {
        return groupId;
    }

    @Override
    public String getName() {
        return groupName;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected Integer getRightsMask() {
        return group_mask;
    }
}
