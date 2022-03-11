package keysuite.docer.client;

public interface IActor {
    String getDocerId();
    String getName();
    String getType();
    String getPrefix();
    String getEmail();
    String getLanguage();

    boolean isAdmin();

    /*default String getDisplayName(){
        if (Strings.isNullOrEmpty(getName()) || getName().equals(getId()))
            return getId();
        else
            return String.format("%s (%s)",getName(),getId());
    }*/
    default boolean isAOO(){
        return "aoo".equals(getType());
    }

    default boolean isEnte(){
        return "ente".equals(getType());
    }

    default boolean isGroup(){
        return "group".equals(getType());
    }

    default boolean isStruttura(){
        return isGroup() && ((Group)this).isStruttura();
    }

    /*default String getPrefixedId() {
        if (getPrefix()!=null)
            return getPrefix()+getDocerId();
        else
            return getDocerId();
    }*/
}
