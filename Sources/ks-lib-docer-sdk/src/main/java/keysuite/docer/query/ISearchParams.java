package keysuite.docer.query;

import java.util.Map;
import java.util.Set;

public interface ISearchParams {
    String[] get(String param);
    String getFirst(String param);
    ISearchParams add(String param, String... values);
    ISearchParams set(String param, String... values);
    ISearchParams set(String param, int value);
    ISearchParams set(String param, boolean value);
    Set<String> keySet();
    Set<Map.Entry<String,String[]>> entrySet();
}
