package it.kdm.docer.commons.sso;

import it.cefriel.icar.inf3.web.beans.AuthenticationSessionBean;

/**
 * Created by Łukasz Kwasek on 14/01/15.
 */
public class AuthenticationSessionBeanFactory {
    public static AuthenticationSessionBean create(String userId){
        AuthenticationSessionBean bean = new AuthenticationSessionBean();
        bean.setUserID(userId);
        return bean;
    }
}
