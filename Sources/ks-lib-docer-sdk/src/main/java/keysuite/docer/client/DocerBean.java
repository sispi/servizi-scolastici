package keysuite.docer.client;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiModelProperty;

import java.io.IOException;
import java.util.*;

public abstract class DocerBean {

    static final int documento_mask = 32767-32;
    static final int anagrafica_mask = 1+4+32+64+1024+2048+8192+16384;
    static final int fascicolo_mask = 1+4+32+64+1024+8192+16384;
    static final int folder_mask = 1+4+32+64+1024+2048+8192+16384;
    static final int group_mask = 1+4+32+64+1024+2048+8192+16384;
    static final int titolario_mask = 1+4+32+64+1024+8192+16384;
    static final int user_mask = 1+4+64+1024+2048+8192+16384;

    enum docRight {
        read(1),
        history(1),
        contentRead(2),
        aclRead(4),
        versionRead(8),
        versionList(16),
        create(32),
        write(64),
        disable(64),
        lock(128),
        contentWrite(256),
        versionWrite(512),
        rename(1024),
        move(2048),
        unlock(4096),
        destroy(8192),
        aclWrite(16384),
        sync(32768);

        int bitmask;

        docRight(int bitmask) {
            this.bitmask = bitmask;
        }

        public int getBitmask(){
            return bitmask;
        }

        static Collection<String> getRights(Integer bitmask){
            Collection<String> rights = new ArrayList<>();
            for ( docRight right : docRight.values() ){
                if ( (right.bitmask & bitmask) > 0)
                    rights.add(right.name());
            }
            return rights;
        }
    }

    Set<String> nullValues = new HashSet<>();

    public void addNullField(String field){
        nullValues.add(field);
    }

    public void removeNullField(String field){
        nullValues.remove(field);
    }

    public Set<String> getNullFields(){
        return nullValues;
    }

    public DocerBean(){
        type = this.getClass().getSimpleName().toLowerCase();
    };

    @Override
    public String toString(){
        return this.getType()+"("+this.getDocerId()+")";
    }

    @ApiModelProperty(hidden = true)
    //@JsonIgnore
    public abstract String getDocerId();

    @ApiModelProperty(hidden = true)
    @JsonProperty("id")
    protected String id;

    public String getSolrId(){
        return id; //ClientUtils.getSolrId(getCodEnte(),getCodAoo(),getId(),getType());
    }

    @ApiModelProperty(hidden = true)
    public abstract String getName();

    @ApiModelProperty(hidden = true)
    public String getDisplayName(){
        if (Strings.isNullOrEmpty(getName()) || getName().equals(getDocerId()))
            return getDocerId();
        else
            return String.format("%s (%s)",getName(), getDocerId());
    }

    public String getType(){
        return this.type;
    };

    public static final String MV_SEP = ",";

    public static class ArrayJsonDeserializer extends StdDeserializer<String[]> {

        public ArrayJsonDeserializer() {
            this(null);
        }

        public ArrayJsonDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String[] deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {

            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            JsonNode node = mapper.readTree(jp);

            String[] arr = new String[node.size()];

            for ( int i=0; i<arr.length; i++ ){
                arr[i] = node.get(i).asText();
            }
            return arr;
        }
    }

    @ApiModelProperty(hidden = true)
    protected Map<String, Object> properties = new HashMap<>();

    public String getCodAoo() {
        return codAoo;
    }

    public void setCodAoo(String codAoo) {
        this.codAoo = codAoo;
    }

    public String getCodEnte() {
        return codEnte;
    }

    public void setCodEnte(String codEnte) {
        this.codEnte = codEnte;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("CREATOR")
    private String creator;

    @ApiModelProperty(hidden = true)
    @JsonProperty("CREATION_DATE")
    private String creation_date;

    @ApiModelProperty(hidden = true)
    @JsonProperty("MODIFIER")
    private String modifier;

    @ApiModelProperty(hidden = true)
    @JsonProperty("CREATED")
    private String created;

    @ApiModelProperty(hidden = true)
    @JsonProperty("MODIFIED")
    private String modified;

    @ApiModelProperty(hidden = true)
    @JsonProperty("COD_AOO")
    private String codAoo;

    @ApiModelProperty(hidden = true)
    @JsonProperty("COD_ENTE")
    private String codEnte;

    public void setType(String type) {
        this.type = type;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty
    protected String type;



    public String[] getAcls() {
        return acls;
    }

    public void setAcls(String... acls) {
        this.acls = acls;
    }

    public void addAcls(String... acls ) {
        if (acls==null || acls.length==0)
            return;
        if (this.acls==null){
            setAcls(acls);
        } else {
            Set<String> set = new HashSet<>(Arrays.asList(this.acls));
            for( String g : acls )
                set.add(g);
            setAcls(set.toArray(new String[0]));
        }
    }

    public void removeAcls(String... acls ) {
        if (acls==null || acls.length==0 || this.acls==null)
            return;

        Set<String> set = new HashSet<>(Arrays.asList(this.acls));
        for( String g : acls )
            set.remove(g);
        setAcls(set.toArray(new String[0]));
    }

    @JsonProperty("acl_explicit")
    @JsonDeserialize(using = ArrayJsonDeserializer.class )
    @ApiModelProperty(example = "[\"everyone@group:readOnly\"]")
    private String[] acls;

    protected Collection<String> permissions;
    protected Map<String,Collection<String>> inherited;

    abstract protected Integer getRightsMask();

    @ApiModelProperty(hidden = true)
    @JsonProperty( value = "permissions")
    public void setPermissions(Object permissions){
        if (permissions instanceof Integer){
            this.permissions = docRight.getRights( ((Integer) permissions) & getRightsMask());
        } else {
            this.permissions = (Collection<String>) permissions;
        }
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty( value = "permissions")
    public Collection<String> getPermissions(){
        return permissions;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty( value = "inherited")
    public void setInherited(Object inherited){
        if (inherited instanceof Map){
            this.inherited = (Map) inherited;
        } else {
            this.inherited = null;
        }
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty( value = "inherited")
    public Map<String,Collection<String>> getInherited(){
        return inherited;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("history_mv")
    @JsonDeserialize(using = ArrayJsonDeserializer.class )
    private String[] history;

    public String[] getHistory() {
        return history;
    }

    @JsonIgnore
    public HistoryItem[] getHistoryItems(){
        String[] history = getHistory();
        if (history==null){
            history = new String[0];
        }
        HistoryItem[] items = new HistoryItem[history.length+1];
        items[0] = new HistoryItem("CREATED",String.format("%s@%s:",getCreator(),getCreated()));
        for ( int i=0; i<history.length; i++){
            items[i+1] = new HistoryItem("UPDATE",history[i]);
        }
        return items;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("ENABLED")
    private boolean enabled = true;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isInherits() {
        return inherits;
    }

    public void setInherits(boolean inherits) {
        this.inherits = inherits;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("INHERITS_ACL")
    boolean inherits = false;

    @ApiModelProperty(hidden = true)
    private boolean deleted = false;

    // Capture all other fields that Jackson do not match other members
    @JsonAnyGetter
    @ApiModelProperty(hidden = true)
    public Map<String, Object> otherFields() {
        return properties;
    }

    //public final static List<String> extraFields = Arrays.asList(new String[] {} );

    /*public final static boolean isExtraField(String name){
        return extraFields.contains(name) || name.startsWith("extra_") || name.startsWith("EXTRA_");
    }*/

    @JsonAnySetter
    @ApiModelProperty(hidden = true)
    public void setOtherField(String name, Object value) {

        if (name.equals("enabled") && Boolean.FALSE.equals(value))
            deleted = true;

        if (!name.matches(".*[a-z].*"))
            properties.put(name, value);
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @ApiModelProperty(hidden = true)
    @JsonProperty("guid")
    private String guid;

    public final static ThreadLocal<String> baseUrl = new ThreadLocal<>();
    public final static String localUrl = "http://docer-local-url";

    public <T extends DocerBean> T copy(){
        try {
            return (T) ClientUtils.OM.readValue(ClientUtils.OM.writeValueAsString(this), this.getClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
