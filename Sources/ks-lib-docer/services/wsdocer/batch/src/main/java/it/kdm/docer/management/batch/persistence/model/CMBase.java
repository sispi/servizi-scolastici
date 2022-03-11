package it.kdm.docer.management.batch.persistence.model;

/**
 * Created with IntelliJ IDEA.
 * User: Vaio
 * Date: 03/11/15
 * Time: 10.17
 * To change this template use File | Settings | File Templates.
 */
public class CMBase {
    private String codEnte;
    private String codAoo;
    private String aclCurrent;
    private String aclModified;
    private String rules;
    private Integer inError;

    public String getCodEnte() {
        return codEnte;
    }

    public void setCodEnte(String codEnte) {
        this.codEnte = codEnte;
    }

    public String getCodAoo() {
        return codAoo;
    }

    public void setCodAoo(String codAoo) {
        this.codAoo = codAoo;
    }

    public String getAclCurrent() {
        return aclCurrent;
    }

    public void setAclCurrent(String aclCurrent) {
        this.aclCurrent = aclCurrent;
    }

    public String getAclModified() {
        return aclModified;
    }

    public void setAclModified(String aclModified) {
        this.aclModified = aclModified;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public Integer getInError() {
        return inError;
    }

    public void setInError(Integer inError) {
        this.inError = inError;
    }
}
