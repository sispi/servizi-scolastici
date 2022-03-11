package it.kdm.docer.management.model;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("groups")
public class Groups  implements Serializable {

	@XStreamImplicit(itemFieldName="group")
	private List<Group> group;

    public List<Group> getGroup () {
        return group;
    }

    public void setGroup (List<Group> group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "ClassPojo [group = "+group+"]";
    }
    
}
