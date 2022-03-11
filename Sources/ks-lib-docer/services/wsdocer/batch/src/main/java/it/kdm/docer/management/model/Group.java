package it.kdm.docer.management.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by SL_550 on 10/01/2016.
 */
@XStreamAlias("group")
public class Group {

    @XStreamAsAttribute
    private String rights;

    private String value;

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
