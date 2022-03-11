package keysuite.desktop.components;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UrlRewriter {

    private Map<String,String> rules = new LinkedHashMap<>();
    private List<String> keys = new ArrayList<>();

    public void clear(){
        rules.clear();
        keys.clear();
    }

    public Map<String,String> getRules(){
        return rules;
    }

    public List<String> getKeys(){
        return keys;
    }

    public void addRule(String regex, String replace){
        keys.add(regex);
        if (!regex.startsWith("http"))
            regex = "https?\\://[^/]+" + regex;
        /*if (regex.endsWith("?...")){
            regex = regex.replace("?...","\\??(?<querystring>.*)");
        }*/
        if (replace.contains("...")){
            if (!replace.startsWith("forward:")) {
                replace = replace.replace("...", "${querystring}");
            } else {
                replace = replace.replace("...", "");
            }
            regex += "\\??(?<querystring>.*)";
        }
        regex = regex.replaceAll("\\{([^}]+)}","(?<$1>[^/?]+)");
        rules.put(regex,replace);
    }

    public String rewrite(String url){
        for( String regex : rules.keySet() ){
            if (url.matches(regex))
                return url.replaceFirst(regex,rules.get(regex));
        }
        return null;
    }
}
