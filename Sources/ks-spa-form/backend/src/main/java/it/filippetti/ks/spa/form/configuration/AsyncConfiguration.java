/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.spa.form.configuration;

import it.filippetti.ks.spa.form.exception.AsyncExceptionHandler;
import java.util.concurrent.Executor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer, SchedulingConfigurer {

    @Value("${async.executor.corePoolSize:10}")
    private int executorCorePoolSize;
    
    @Value("${async.executor.maxPoolSize:1000}")    
    private int executorMaxPoolSize;        
    
    @Value("${async.executor.queueCapacity:0}")    
    private int executorqueueCapacity;        
    
    @Value("${async.scheduler.poolSize:10}")            
    private int schedulerPoolSize;

    @Autowired
    private AsyncExceptionHandler exceptionHandler;
    
    @Autowired
    private ThreadPoolTaskExecutor executor;
        
    @Autowired
    private ThreadPoolTaskScheduler scheduler;
    
    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("executor-");
        executor.setCorePoolSize(executorCorePoolSize <= 0 ? Integer.MAX_VALUE : executorCorePoolSize);
        executor.setMaxPoolSize(executorMaxPoolSize <= 0 ? Integer.MAX_VALUE : executorMaxPoolSize);
        executor.setQueueCapacity(executorqueueCapacity <= 0 ? Integer.MAX_VALUE : executorqueueCapacity);
        executor.setRejectedExecutionHandler(exceptionHandler);
        executor.initialize();
        return executor;
    }
    
    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("scheduler-");
        scheduler.setPoolSize(schedulerPoolSize <= 0 ? Integer.MAX_VALUE : schedulerPoolSize);
        scheduler.setErrorHandler(exceptionHandler);
        scheduler.setRejectedExecutionHandler(exceptionHandler);
        scheduler.initialize();
        return scheduler;
    }
    
    @Override
    public Executor getAsyncExecutor() {
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(scheduler);
    }
}

