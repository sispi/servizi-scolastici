/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.authentication;

import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author marco.mazzocchetti
 */
public class Authority implements GrantedAuthority {
    
    public enum Type {
        TENANT, ORGANIZATION, ROLE;
    }
    
    private Type type;
    private String value;
    private String authority;
    
    private Authority(Type type, String value) throws IllegalArgumentException {
        this.type = type;
        this.value = value;
        this.authority = String.format("%s_%s", type.name(), value);
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
    
    @Override
    public String getAuthority() {
        return authority;
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.authority);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Authority other = (Authority) obj;
        if (!Objects.equals(this.authority, other.authority)) {
            return false;
        }
        return true;
    }

    protected static Authority tenant(String value) {
        return new Authority(Type.TENANT, value);
    }

    protected static Authority organization(String value) {
        return new Authority(Type.ORGANIZATION, value);
    }

    protected static Authority role(String value) {
        return new Authority(Type.ROLE, value);
    }    
}
