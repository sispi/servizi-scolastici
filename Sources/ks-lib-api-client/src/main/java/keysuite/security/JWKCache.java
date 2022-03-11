package keysuite.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JWKCache extends HashMap<String, Map> implements Serializable {

    private static JWKCache cache;


    public static JWKCache getInstance(){
        if(cache == null){
            cache = new JWKCache();
        }
        return cache;
    }

    private JWKCache(){
        super();
    }

    public Map getConfiguration(String key){
        if(this.get(key) == null){
            this.put(key, new HashMap());
        }
        return this.get(key);
    }


}
