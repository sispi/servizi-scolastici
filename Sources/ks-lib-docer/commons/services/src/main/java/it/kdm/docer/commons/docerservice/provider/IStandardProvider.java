package it.kdm.docer.commons.docerservice.provider;

public interface IStandardProvider extends IProvider {
    public String login(String user, String pass, String ente) throws Exception;
}