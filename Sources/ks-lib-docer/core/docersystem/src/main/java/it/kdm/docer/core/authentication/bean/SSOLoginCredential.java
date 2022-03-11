package it.kdm.docer.core.authentication.bean;

/**
 * Created by Łukasz Kwasek on 14/01/15.
 */
public class SSOLoginCredential {

    private String originalSaml;
    private String saml;
    private String ticket;
    private String realUser;
    private String groups;

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getOriginalSaml() {
        return originalSaml;
    }

    public void setOriginalSaml(String originalSaml) {
        this.originalSaml = originalSaml;
    }

    public String getSaml() {
        return saml;
    }

    public void setSaml(String saml) {
        this.saml = saml;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void setRealUser(String realUser) {
        this.realUser = realUser;
    }

    public String getRealUser() {
        return realUser;
    }
}
