package it.kdm.docer.management.model;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("batch")
public class Batch implements Serializable {

    @XStreamAsAttribute
    private String ente;

    @XStreamAsAttribute
    private String aoo;

    @XStreamImplicit(itemFieldName="rule")
    private List<Rule> rules;

    public String getEnte() {
        return ente;
    }

    public void setEnte(String ente) {
        this.ente = ente;
    }

    public String getAoo() {
        return aoo;
    }

    public void setAoo(String aoo) {
        this.aoo = aoo;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {
        return "ClassPojo [rules = "+rules+"]";
    }
}
