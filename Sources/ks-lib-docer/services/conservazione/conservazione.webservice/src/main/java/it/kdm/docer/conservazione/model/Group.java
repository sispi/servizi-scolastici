package it.kdm.docer.conservazione.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("group")
public class Group
{
	@XStreamAsAttribute
	private String name;

    @XStreamImplicit(itemFieldName = "section")
    private List<Section> section = new ArrayList<Section>();

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Section> getSection() {
		return section;
	}

	public void setSection(List<Section> section) {
		this.section = section;
	}

	@Override
    public String toString()
    {
        return "ClassPojo [name = "+name+", section = "+section+"]";
    }
}
			
			