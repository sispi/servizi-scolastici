/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.model;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 *
 * @author marco.mazzocchetti
 */
public class Operation<T> {

    private String id;
    private Callable<T> callable;
    private Class<T> type;

    public Operation(Callable<T> callable, Class<T> type) {
        this(null, callable, type);
    }

    public Operation(String id, Callable<T> callable, Class<T> type) {
        this.id = id;
        this.callable = callable;
        this.type = type;
    }

    public boolean isCacheable() {
        return id != null;
    }

    public String id() {
        return id;
    }

    public T execute() throws Exception {
        return callable.call();
    }

    public Class<T> type() {
        return type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Operation<?> other = (Operation<?>) obj;
        return Objects.equals(this.id, other.id);
    }
}
