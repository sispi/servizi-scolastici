package keysuite.docer.server;


import it.kdm.orchestratore.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public abstract class BaseService implements EnvironmentAware {

    protected final static Logger logger = LoggerFactory.getLogger(BaseService.class);

    BaseService(){}

    @Autowired
    protected Environment env;

    protected boolean isPUT(){
        if (Session.getRequest() != null && Session.getResponse() != null){
            return Session.getRequest().getMethod().toUpperCase().equals("PUT");
        } else {
            return false;
        }
    }
}
