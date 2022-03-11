package it.kdm.docer.management.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 05/11/15
 * Time: 11.24
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("rule")
public class Rule {

    @XStreamAsAttribute
    private String uniqueKey;

    private String action;

    private Source source;

    private Target target;

    @XStreamImplicit(itemFieldName="query_fascicolo")
    private List<String> query_fascicolo;

    @XStreamImplicit(itemFieldName="query_titolario")
    private List<String> query_titolario;

    @XStreamImplicit(itemFieldName="query_documento")
    private List<String> query_documento;

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public List<String> getQuery_fascicolo() {
        return query_fascicolo;
    }

    public void setQuery_fascicolo(List<String> query_fascicolo) {
        this.query_fascicolo = query_fascicolo;
    }

    public List<String> getQuery_titolario() {
        return query_titolario;
    }

    public void setQuery_titolario(List<String> query_titolario) {
        this.query_titolario = query_titolario;
    }

    public List<String> getQuery_documento() {
        return query_documento;
    }

    public void setQuery_documento(List<String> query_documento) {
        this.query_documento = query_documento;
    }

    @Override
    public String toString() {
        return "ClassPojo [source = "+source+", query_fascicolo = "+query_fascicolo+", query_titolario = "+query_titolario+", action = "+action+", target = "+target+", query_documento = "+query_documento+"]";
    }

}
