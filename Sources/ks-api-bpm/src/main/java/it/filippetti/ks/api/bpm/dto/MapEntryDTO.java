/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.bpm.dto;

/**
 * 
 * @author marco.mazzocchetti
 * @param <K>
 * @param <V> 
 */
public class MapEntryDTO<K, V> {
    
    private K key;
    private V value;

    public MapEntryDTO(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
