package it.kdm.docer.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by Lorenzo Lucherini
 * Date: 9/10/13
 * Time: 5:45 PM
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext ctx = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static ApplicationContext getCtx() {
        if (ctx == null) {
            throw new IllegalStateException("ApplicationContext not yet initialized");
        }

        return ctx;
    }
}
