package it.kdm.docer.core.authentication.bean;

/**
 * Created by Łukasz Kwasek on 26/11/14.
 */
public class WSTicketAuthInfo {
    private String serviceUrl, userName, password, ente, application,token;

    public WSTicketAuthInfo(String serviceUrl, String userName, String password, String ente, String application,String token) {
        this.serviceUrl = serviceUrl;
        this.userName = userName;
        this.password = password;
        this.ente = ente;
        this.application = application;
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WSTicketAuthInfo that = (WSTicketAuthInfo) o;

        if (application != null ? !application.equals(that.application) : that.application != null) return false;
        if (ente != null ? !ente.equals(that.ente) : that.ente != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (serviceUrl != null ? !serviceUrl.equals(that.serviceUrl) : that.serviceUrl != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = serviceUrl != null ? serviceUrl.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (ente != null ? ente.hashCode() : 0);
        result = 31 * result + (application != null ? application.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnte() {
        return ente;
    }

    public void setEnte(String ente) {
        this.ente = ente;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
