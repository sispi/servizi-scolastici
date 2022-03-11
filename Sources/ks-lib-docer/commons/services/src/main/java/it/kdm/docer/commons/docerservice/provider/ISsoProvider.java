package it.kdm.docer.commons.docerservice.provider;

public interface ISsoProvider extends IStandardProvider {
    public String loginSSO(String saml, String ente) throws Exception;
}