package it.kdm.docer.commons.docerservice;

/**
 * Created by Łukasz Kwasek on 20/01/15.
 */
public class ProviderInfo {
    private String ente;
    private String aoo;
    private LoginMode mode;
    private String defaultUser;
    private String defaultPassword;
    private boolean isMd5;
    private String registro;

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

    public String getDefaultUser() {
        return defaultUser;
    }

    public void setDefaultUser(String defaultUser) {
        this.defaultUser = defaultUser;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(String defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public boolean isMd5() {
        return isMd5;
    }

    public void setMd5(boolean isMd5) {
        this.isMd5 = isMd5;
    }

    public LoginMode getMode() {
        return mode;
    }

    public void setMode(LoginMode mode) {
        this.mode = mode;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}
