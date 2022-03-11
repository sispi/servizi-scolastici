package it.kdm.docer.commons.docerservice.provider;

import it.kdm.docer.commons.docerservice.LoginMode;

/**
 * Created by Łukasz Kwasek on 21/01/15.
 */
public class AbstractStandardProvider extends AbstractProvider {

    public AbstractStandardProvider() {
        super(LoginMode.STANDARD);
    }
}
