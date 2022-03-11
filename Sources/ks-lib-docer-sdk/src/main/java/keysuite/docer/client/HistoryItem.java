package keysuite.docer.client;

import java.util.Date;

public class HistoryItem {

    String timestamp;
    String username;
    String type;

    public HistoryItem(){

    }

    public HistoryItem(String type,String history){
        this.type = type;

        int idx = history.indexOf("@");

        username = history.substring(0,idx);

        int idx2 = history.lastIndexOf(":");

        timestamp = history.substring(idx+1,idx2);

        if (timestamp.matches("\\d+")){
            timestamp = ClientUtils.dateFormat.format(new Date(Long.parseLong(timestamp)));
        }
        info = history.substring(idx2+1);
    }

    public String getType() {
        return type;
    }

    String info;

    public String getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getInfo() {
        return info;
    }

}
