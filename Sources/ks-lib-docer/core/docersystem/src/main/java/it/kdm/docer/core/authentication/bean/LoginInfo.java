package it.kdm.docer.core.authentication.bean;


import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Łukasz Kwasek on 26/11/14.
 */
public class LoginInfo {
    private String user, pass, ente, application;

    private String ticket;

    private Map<String, String> additionalTokenKeys = new HashMap<String, String>();

    public LoginInfo() {

    }

    public LoginInfo(String user, String pass, String ente, String application) {
        this.user = user;
        this.pass = pass;
        this.ente = ente;
        this.application = application;
    }

    public String getLoginInfoData() {
        String data = "%s;%s;%s;%s";
        data = String.format(data,this.getUser(),this.getPass(),this.getEnte(),this.getApplication());

        String tokens = "";
        for (Map.Entry<String, String> entry : getTokenKeys()) {
            tokens+=entry.getKey()+":"+entry.getValue()+";";
        }

        String outData = data + "#" + tokens + "#" + this.getTicket();

        return outData;
    }

    public void setLoginInfoData(String data) {
        String[] parts = data.split("#");
        String[] props = parts[0].split(";");
        this.setUser(props[0]);
        this.setPass(props[1]);
        this.setEnte(props[2]);
        this.setApplication(props[3]);

        if (!parts[2].equals("null"))
            this.setTicket(parts[2]);

        if (!Strings.isNullOrEmpty(parts[1])) {
            String[] items = parts[1].split(";");
            for (String item : items) {
                String[] keyValue = item.split(":");
                this.addTokenKey(keyValue[0], keyValue[1]);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginInfo userInfo = (LoginInfo) o;

        if (application != null ? !application.equals(userInfo.application) : userInfo.application != null)
            return false;
        if (ente != null ? !ente.equals(userInfo.ente) : userInfo.ente != null) return false;
        if (pass != null ? !pass.equals(userInfo.pass) : userInfo.pass != null) return false;
        if (user != null ? !user.equals(userInfo.user) : userInfo.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (pass != null ? pass.hashCode() : 0);
        result = 31 * result + (ente != null ? ente.hashCode() : 0);
        result = 31 * result + (application != null ? application.hashCode() : 0);
        return result;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public void addTokenKey(String key, String value) {
        additionalTokenKeys.put(key, value);
    }

    public String getTokenKey(String key) {
        return additionalTokenKeys.get(key);
    }

    public Set<Map.Entry<String, String>> getTokenKeys() {
        return additionalTokenKeys.entrySet();
    }
}
