package it.kdm.docer.fascicolazione;


import it.kdm.docer.commons.docerservice.provider.ISsoProvider;

import java.util.UUID;

/**
 * Created by Łukasz Kwasek on 23/12/14.
 */
public class SsoTestProvider extends Provider implements ISsoProvider {

    public String loginSSO(String saml, String ente) {
        return UUID.randomUUID().toString();
    }
}
