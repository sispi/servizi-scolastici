package it.kdm.docer.sdk.interfaces;

import java.util.Map;

/**
 * Created by Łukasz Kwasek on 11/02/15.
 */
public interface ISsoUserInfo {

    public String getUserID();

    public String getTicket();

    public String getGroups();

    public Map<String, Object> getExtraData();

}
