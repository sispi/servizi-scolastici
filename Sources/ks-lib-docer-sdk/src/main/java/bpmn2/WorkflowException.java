package bpmn2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WorkflowException extends RuntimeException implements Map<String,Object> {
    final String DETAIL="detail";
    final String CODE="code";
    final String REASON="reason";

    Map<String,Object> exc = new HashMap<>();

    public WorkflowException(String code, String reason, Object detail)
    {
        super(String.format("code:%s reason:%s",code,reason)+ (detail!=null? "\n" + detail.toString():""));
        exc.put(CODE,code);
        exc.put(REASON,reason);
        exc.put(DETAIL,detail);
    }

    public String getCode()
    {
        return (String) exc.get(CODE);
    }

    public String getReason()
    {
        return (String) exc.get(REASON);
    }

    public Object getDetail()
    {
        return exc.get(DETAIL);
    }

    @Override
    public int size() {
        return exc.size();
    }

    @Override
    public boolean isEmpty() {
        return exc.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return exc.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return exc.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return exc.get(key);
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
        return exc.keySet();
    }

    @Override
    public Collection<Object> values() {
        return exc.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return exc.entrySet();
    }
}
