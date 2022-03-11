package it.kdm.docer.conservazione.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("ente")
public class Ente
{
	@XStreamImplicit(itemFieldName = "ente")
    private List<Aoo> aoo = new ArrayList<Aoo>();

    @XStreamAsAttribute
    private String name;

    public List<Aoo> getAoo() {
		return aoo;
	}

	public void setAoo(List<Aoo> aoo) {
		this.aoo = aoo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public String toString()
    {
        return "ClassPojo [aoo = "+aoo+", name = "+name+"]";
    }
}
			
			