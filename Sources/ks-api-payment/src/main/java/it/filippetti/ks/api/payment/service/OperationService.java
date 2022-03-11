/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.payment.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.filippetti.ks.api.payment.configuration.ApplicationProperties;
import it.filippetti.ks.api.payment.exception.ApplicationException;
import it.filippetti.ks.api.payment.exception.OperationException;
import it.filippetti.ks.api.payment.model.Operation;
import it.filippetti.ks.api.payment.model.OperationResult;
import it.filippetti.ks.api.payment.repository.OperationResultRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author marco.mazzocchetti
 */
@Service
public class OperationService implements ApplicationListener<ApplicationReadyEvent> {
    
    private static final Logger log = LoggerFactory.getLogger(OperationService.class);
    
    @Autowired
    private ApplicationProperties applicationProperties;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private OperationResultRepository operationResultRepository;
    
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;
    
    private LoadingCache<Operation, Object> cache;
    
    private ScheduledFuture cleaner;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        startup();
    }
    
    public void startup() {
        cache = CacheBuilder.newBuilder()
            .initialCapacity(100)
            .maximumSize(1000)
            .concurrencyLevel(8)
            .expireAfterWrite(
                applicationProperties.operationExpireAfter(), 
                TimeUnit.MINUTES)
            .build(new CacheLoader<Operation, Object>() {
                @Override
                public Object load(Operation operation) throws Exception {
                   return OperationService.this.load(operation);
                }
            });   
        
        log.info("Starting operation cache cleaner");
        cleaner = taskScheduler.scheduleWithFixedDelay(() -> {
                clean();
            }, 
            Duration.of(applicationProperties.operationCleanerDelay(), ChronoUnit.MINUTES));        
    }
    
    @PreDestroy
    public void shutdown() {
        log.info("Shutting down operation cache cleaner");
        cleaner.cancel(false);
    }

    /**
     * 
     * @param <T>
     * @param id
     * @param callable
     * @param type
     * @return
     * @throws ApplicationException 
     */
    public <T> T execute(String id, Callable<T> callable, Class<T> type) 
        throws ApplicationException {
        
        return execute(new Operation<T>(id, callable, type));
    }
    
    /**
     * 
     * @param <T>
     * @param operation
     * @return
     * @throws ApplicationException 
     */
    public <T> T execute(Operation<T> operation) 
        throws ApplicationException {

        try {
            if (operation.isCacheable()) {
                try {
                    return (T) cache.get(operation);
                } catch (ExecutionException e) {
                   throw e.getCause();
                }
            } else {
                return operation.execute();
            }
        } catch (Throwable e) {
            if (e instanceof ApplicationException) {
                throw (ApplicationException) e;
            } else {
                throw new OperationException(e);
            }
        }
    }
    
    /**
     * 
     * @param <T>
     * @param operation
     * @return
     * @throws Exception 
     */
    private <T> T load(Operation<T> operation) throws Exception {
        
        OperationResult result;
        T value;
        
        if (!operation.isCacheable()) {
            throw new IllegalArgumentException();
        }
        
        result = operationResultRepository.findById(operation.id()).orElse(null);
        if (result != null) {
            value = result.getValue().as(operation.type());
        } else {
            value = operation.execute();
            operationResultRepository.save(new OperationResult(operation.id(), value));
        }
        return value;
    }    
    
    /**
     * 
     */
    private void clean() {
        
        int c;
        
        // start transaction
        TransactionStatus tx = transactionManager.getTransaction(
             new DefaultTransactionDefinition());
        try {
            c = operationResultRepository.clean(
                new Date(Instant
                    .now()
                    .minus(
                        applicationProperties.operationExpireAfter(), 
                        ChronoUnit.MINUTES)
                    .toEpochMilli()));
            if (c > 0) {
                log.info(String.format("Deleted %d operation cache entries", c));
            }
        } catch (Throwable t) {
            // rollback
            transactionManager.rollback(tx);
            throw t;
        }
        // commit
        transactionManager.commit(tx);
    }
}
