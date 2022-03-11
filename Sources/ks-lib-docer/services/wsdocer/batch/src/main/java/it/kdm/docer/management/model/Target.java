package it.kdm.docer.management.model;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("target")
public class Target  implements Serializable {

	@XStreamImplicit(itemFieldName="groups")
	private List<Groups> groups;
	
	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}

	@Override
    public String toString() {
        return "ClassPojo [groups = "+groups+"]";
    }
	
}
