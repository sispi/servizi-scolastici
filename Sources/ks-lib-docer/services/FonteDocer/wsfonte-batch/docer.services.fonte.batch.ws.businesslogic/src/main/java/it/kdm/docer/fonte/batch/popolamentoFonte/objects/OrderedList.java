package it.kdm.docer.fonte.batch.popolamentoFonte.objects;

import java.util.HashMap;
import java.util.Map;

public class OrderedList<V> {

    private Map<Integer, V> orderedList = new HashMap<Integer, V>();
    
    public void add(V v){
        orderedList.put(orderedList.size(), v);
    }
    
    public V get(Integer position){
        return orderedList.get(position);
    }
    
    @Override
    public String toString(){
        return orderedList.values().toString();
    }
    
    public boolean contains(String value){
        return orderedList.containsValue(value);
    }
    
    public Integer size(){
        return orderedList.size();
    }
}
