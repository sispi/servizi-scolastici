package keysuite.docer.client;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import keysuite.cache.ClientCache;

import java.util.*;

public class User extends Actor {

    public static final String TYPE = "user";

    public User(){
        super();
    }

    public User(String username){
        this();
        setUserName(username);
    }

    @JsonProperty("USER_ID")
    String userName;

    @JsonProperty("FULL_NAME")
    String fullName;

    @JsonProperty("EMAIL_ADDRESS")
    @JsonAlias("USER_MAIL")
    String email;

    @JsonProperty("prefix")
    String prefix = "";

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("USER_PASSWORD")
    String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getGroups() {
        return groups;
    }

    /*@JsonIgnore
    public List<String> getDocerGroups(){
        return Arrays.asList(this.getGroups() ).stream().map( c-> c.split("@")[0] ).collect(Collectors.toList());
    }*/

    @JsonIgnore
    public String getLanguage(){
        return "it";
    }

    @JsonIgnore
    public String getCountry(){
        return "IT";
    }

    public void setGroups(String[] groups) {
        if (groups!=null) {
            for (int i = 0; i < groups.length; i++) {
                groups[i] = groups[i].split("@")[0];
            }
            Set<String> set = new HashSet<>(Arrays.asList(groups));
            groups = set.toArray(new String[0]);
        }
        this.groups = groups;
    }

    public void addGroups(String... groups ) {
        if (groups==null || groups.length==0)
            return;
        if (this.groups==null){
            setGroups(groups);
        } else {
            Set<String> set = new HashSet<>(Arrays.asList(this.groups));
            for( String g : groups )
                set.add(g);
            setGroups(set.toArray(new String[0]));
        }
    }

    public void removeGroups(String... groups ) {
        if (groups==null || groups.length==0 || this.groups==null)
            return;

        Set<String> set = new HashSet<>(Arrays.asList(this.groups));
        for( String g : groups )
            set.remove(g);
        setGroups(set.toArray(new String[0]));
    }

    public String getPrefixedUsername() {
        return ClientCache.suffix(this.getPrefix()) + this.getUserName();
    }

    public boolean isAdmin(){
        if (groups==null)
            return false;

        List<String> lgroups = Arrays.asList(groups);

        if (lgroups.contains("admins"))
            return true;

        Group ente = ClientCache.getInstance().getEnte(this.getCodEnte());

        if (ente!=null){
            String[] acls = ente.getAcls();
            if (acls!=null){
                for( String acl : acls ){
                    String right = acl.split(":")[1];
                    if (right.equals("fullAccess")){
                        String grp = acl.split("@")[0];
                        if (lgroups.contains(grp))
                            return true;
                    }
                }
            }
        }

        Group aoo = ClientCache.getInstance().getAOO(this.getCodAoo());

        if (aoo!=null){
            String[] acls = aoo.getAcls();
            if (acls!=null){
                for( String acl : acls ){
                    String right = acl.split(":")[1];
                    if (right.equals("fullAccess")){
                        String grp = acl.split("@")[0];
                        if (lgroups.contains(grp))
                            return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isGuest() {
        return this.getCodAoo() != null && "guest".equals(this.getUserName());
    }

    public boolean hasGroup(String... groups){
        if (isAdmin())
            return true;

        if (this.groups==null || this.groups.length==0)
            return false;

        List<String> lgroups = Arrays.asList(this.groups);

        for( String group : groups){
            if (Strings.isNullOrEmpty(group))
                continue;
            String[] _groups = group.split(",");
            for(String g : _groups) {

                if (g.contains("*")){
                    g = g.replaceAll("\\.?\\*",".*");
                    for( String grp : this.groups ){
                        if (grp.matches(g))
                            return true;
                    }
                } else {
                    if (lgroups.contains(g))
                        return true;
                }
            }
        }

        return false;
    }

    String[] groups;

    @Override
    public String getDocerId() {
        return userName;
    }

    @Override
    public String getName() {
        return fullName;
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
        return user_mask;
    }

    public final static String opt_prefix = "EXTRA_OPTION_";

    public void setOptions(Map<String,Object> options) {
        Set<String> toRemove = new HashSet<>();
        for(String key: otherFields().keySet()){
            if (key.startsWith(opt_prefix)) {
                toRemove.add(key);
            }
        }
        if (options!=null) {
            for (String key : options.keySet()) {
                setOtherField(opt_prefix + key, options.get(key));
                toRemove.remove(opt_prefix + key);
            }
        }
        for(String key: toRemove)
            otherFields().remove(key);
    }

    public Map<String,Object> getOptions() {

        Map<String,Object> options = new LinkedHashMap<>();
        for(String key: otherFields().keySet()){
            if (key.startsWith(opt_prefix)) {
                String opt_key = key.substring(opt_prefix.length());
                options.put(opt_key, otherFields().get(key));
            }
        }
        return options;
    }
}
