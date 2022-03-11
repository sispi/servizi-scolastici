/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.exception;

import java.lang.reflect.Method;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 *
 * @author marco.mazzocchetti
 */
@Component
public class AsyncExceptionHandler
    implements AsyncUncaughtExceptionHandler, ErrorHandler, RejectedExecutionHandler {

    private static final Logger log = LoggerFactory.getLogger(AsyncExceptionHandler.class);

    /**
     * Async task errors
     * 
     * @param exception
     * @param method
     * @param args 
     */
    @Override
    public void handleUncaughtException(Throwable exception, Method method, Object... arguments) {
        log.error("Async task error", exception);
    }
    
    /**
     * Scheduled task errors
     * 
     * @param exception 
     */
    @Override
    public void handleError(Throwable exception) {
        log.error("Scheduled task error", exception);
    }

    /**
     * Async/Scheduled rejected
     * 
     * @param runnable
     * @param executor 
     */
    @Override
    public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
        log.error("Async/Scheduled task {} was rejected", runnable.getClass().getName());
    }    
}
