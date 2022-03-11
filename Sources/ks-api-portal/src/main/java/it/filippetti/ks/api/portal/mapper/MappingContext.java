/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.mapper;

import it.filippetti.ks.api.portal.model.AuthenticationContext;
import it.filippetti.ks.api.portal.model.Fetcher;

/**
 *
 * @author marco.mazzocchetti
 */
public class MappingContext {

    public static final MappingContext EMPTY = new MappingContext(null, Fetcher.EMPTY);
    
    private final AuthenticationContext authenticationContext;
    private final Fetcher fetcher;

    private MappingContext(AuthenticationContext authenticationContext, Fetcher fetcher) {
        this.authenticationContext = authenticationContext;
        this.fetcher = fetcher;
       
    }

    public AuthenticationContext authentication() {
        return authenticationContext;
    }

    public Fetcher fetcher() {
        return fetcher;
    }

    public boolean fetch(String key) {
        return fetcher.has(key);
    }

    public MappingContext of(String fetch) {
        return of(authenticationContext, fetcher.get(fetch));
    }
    
    public static MappingContext of(AuthenticationContext context) {
        return new MappingContext(context, Fetcher.EMPTY);
    }

    public static MappingContext of(Fetcher fetcher) {
        return new MappingContext(null, fetcher);
    }

    public static MappingContext of(AuthenticationContext context, Fetcher fetcher) {
        return new MappingContext(context, fetcher);
    }
}
