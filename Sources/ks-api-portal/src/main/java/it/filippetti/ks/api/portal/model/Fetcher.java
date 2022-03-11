/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.model;

import it.filippetti.ks.api.portal.exception.ValidationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author marco.mazzocchetti
 */
public class Fetcher {
    
    public static final Fetcher EMPTY = new Fetcher();
    
    private static final Map<Class<?>, Map<String, Fetchable>> fetchables = new HashMap<>();

    private Map<String, Fetcher> map;

    private Fetcher() {
        map = new HashMap<>();
    }

    public Set<String> keys() {
        return Collections.unmodifiableSet(map.keySet());
    }    
    
    public boolean has(String name) {
        return map.containsKey(name);
    }

    public Fetcher get(String name) {
        return map.get(name);
    }

    private void put(String name, Fetcher fetch) {
        map.put(name, fetch);
    }

    @Override
    public String toString() {
        return map
            .entrySet()
            .stream()
            .map(e -> String.format("%s{%s}", 
                e.getKey(), 
                e.getValue().toString()))
            .collect(Collectors.joining(", "));
    }
    
    /**
     * 
     * @param type
     * @param fetch
     * @return
     * @throws ValidationException 
     */
    public static Fetcher of(Class<?> type, String fetch) throws ValidationException {
        
        Fetcher fetcher, nestedFetcher;
        String property;
        int i, j;
        char c;
        
        if (fetch == null && type.getAnnotation(Fetchables.class) != null) {
            fetch = type.getAnnotation(Fetchables.class).defaultFetch();
        }
        if (StringUtils.isBlank(fetch)) {
            return Fetcher.EMPTY;
        }
        if (fetch.equals("*") && type.getAnnotation(Fetchables.class) != null) {
            fetch = Arrays
                .stream(type.getAnnotation(Fetchables.class).value())
                .map(f -> f.property())
                .collect(Collectors.joining(","));
        }
        fetch += ",";
        fetcher = new Fetcher();
        nestedFetcher = Fetcher.EMPTY;
        property = "";
        i = j = 0;
        while (i < fetch.length()) {
            c = fetch.charAt(i);
            if (c != ',' && c != '(' && c != ')') {
                property += c;
            }
            else {
                if (c == ')' || 
                    c == '(' && ((j = indexOfRParen(fetch, i)) == -1 || property.startsWith("@")) || 
                    StringUtils.isBlank(property)) {
                    throw new ValidationException(
                        "fetch: invalid format");
                } 
                if (!(fetchables(type).containsKey(property) || property.matches("@.+"))) {
                    throw new ValidationException(String.format(
                        "fetch: invalid property '%s', allowed values are %s", 
                        property,
                        fetchables(type).keySet().stream().sorted().collect(Collectors.toList())));
                }
                if (c == ',') {
                    fetcher.put(property, nestedFetcher);
                    nestedFetcher = Fetcher.EMPTY;
                    property = "";
                }
                if (c == '(') {
                    nestedFetcher = of(
                        fetchables(type).get(property).type(),
                        fetch.substring(i + 1, j));
                    i = j;
                } 
            } 
            i++;
        }
        return fetcher;
    }    
    
    /**
     * 
     * @param e
     * @param s
     * @return 
     */
    private static int indexOfRParen(String e, int s) {
        
        int d, i, l;
        char c;
        
        d = 1;
        i = s + 1;
        l = e.length();
        while (d != 0 && i < l) {
            c = e.charAt(i++);
            if (c == '(') {
                d++;
            } else if (c == ')') {
                d--;
            }
        }
        return d == 0 ? i - 1 : -1;
    }
    
    /**
     * 
     * @param type
     * @return 
     */
    private static Map<String, Fetchable> fetchables(Class<?> type) {
        
        Map<String, Fetchable> typeFetchables;
        
        typeFetchables = fetchables.get(type);
        if (typeFetchables == null) {
            typeFetchables = new HashMap<>();
            if (type.getAnnotation(Fetchables.class) != null) {
                for (Fetchable fetchable : type.getAnnotation(Fetchables.class).value()) {
                    typeFetchables.put(fetchable.property(), fetchable);
                }
            }
            fetchables.put(type, typeFetchables);
        }
        return typeFetchables;
    }    
}
