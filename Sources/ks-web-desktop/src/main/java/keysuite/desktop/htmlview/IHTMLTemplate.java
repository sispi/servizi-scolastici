package keysuite.desktop.htmlview;

import java.util.Map;

public interface IHTMLTemplate {
    String getHead();
    String getTitle();
    String getAsbolutePath();
    String getLanguage();
    Map<String,String> getBodyAttributes();
    Map<String,String> getServerAttributes();
}
