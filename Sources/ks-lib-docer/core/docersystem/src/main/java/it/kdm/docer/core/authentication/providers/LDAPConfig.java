package it.kdm.docer.core.authentication.providers;

import java.util.HashMap;

/**
 * Created by Lorenzo Lucherini on 2/12/15.
 */
public class LDAPConfig {
    private String hostname;
    private int port;
    private String prefix;
    private String dnUserFormat;

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getHostname() {
        return hostname;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDnUserFormat() {
        return dnUserFormat;
    }

    public void setDnUserFormat(String dnUserFormat) {
        this.dnUserFormat = dnUserFormat;
    }
}