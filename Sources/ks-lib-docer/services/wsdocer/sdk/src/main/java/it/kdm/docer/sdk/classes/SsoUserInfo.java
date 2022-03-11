package it.kdm.docer.sdk.classes;

import it.kdm.docer.sdk.interfaces.ISsoUserInfo;

import java.util.Map;

/**
 * Created by Łukasz Kwasek on 11/02/15.
 */
public class SsoUserInfo implements ISsoUserInfo {

    private String userID;
    private String ticket;
    private String groups;
    private Map<String, Object> extraData;

    @Override
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String getTicket() {
        return ticket;
    }

    @Override
    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public void setExtraData(Map<String, Object> extraData) {
        this.extraData = extraData;
    }
}
