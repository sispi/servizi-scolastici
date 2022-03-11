package bpmn2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author marco.mazzocchetti
 */
public class WorkflowException extends RuntimeException implements Map<String,Object> {
    
    private static final String DETAIL_KEY = "detail";
    private static final String CODE_KEY = "code";
    private static final String REASON_KEY = "reason";

    private String message;
    private Map<String,Object> data;

    public WorkflowException(String code, String reason, Object detail)
    {        
        super();
     
        message = String.format(
            "%s (%s): %s%s", 
            getClass().getName(), 
            code, 
            reason, 
            (detail != null ? "\n" + detail.toString() : ""));
        
        data = new HashMap<>();
        data.put(CODE_KEY, code);
        data.put(REASON_KEY, reason);
        data.put(DETAIL_KEY, detail);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return (String) data.get(CODE_KEY);
    }

    public String getReason() {
        return (String) data.get(REASON_KEY);
    }

    public Object getDetail() {
        return data.get(DETAIL_KEY);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return data.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return data.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        return data.keySet();
    }

    @Override
    public Collection<Object> values() {
        return data.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return data.entrySet();
    }
}
