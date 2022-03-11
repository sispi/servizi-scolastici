/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks.api.portal.configuration;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.QueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.listener.logging.SLF4JSlowQueryListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author marco.mazzocchetti
 */
@Configuration
public class DataSourceProxyConfiguration implements BeanPostProcessor {


    /**
     * Instead of directly returning a less specific datasource bean
     * (e.g.: HikariDataSource -> DataSource), return a proxy object.
     * See following links for why:
     * https://stackoverflow.com/questions/44237787/how-to-use-user-defined-database-proxy-in-datajpatest
     * https://gitter.im/spring-projects/spring-boot?at=5983602d2723db8d5e70a904
     * http://blog.arnoldgalovics.com/2017/06/26/configuring-a-datasource-proxy-in-spring-boot/
     * @param bean
     * @param beanName
     * @return 
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        
        ProxyFactory proxyFactory;
        
        if (bean instanceof DataSource && !(bean instanceof ProxyDataSource) && beanName.equals("dataSource")) {
            proxyFactory = new ProxyFactory(bean);
            proxyFactory.setProxyTargetClass(true);
            proxyFactory.addAdvice(new ProxyDataSourceInterceptor((DataSource) bean, beanName));
            return proxyFactory.getProxy();
        }
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }
    
    /**
     * 
     */
    private static class ProxyDataSourceInterceptor implements MethodInterceptor {

        private DataSource dataSource;
        
        public ProxyDataSourceInterceptor(DataSource dataSource, String beanName) {

            QueryLogEntryCreator logEntryCreator = new DefaultQueryLogEntryCreator() {
                @Override
                protected String formatQuery(String query) {
                    return FormatStyle.BASIC.getFormatter().format(query);
                }
            };
            
            SLF4JQueryLoggingListener queryLogger = new SLF4JQueryLoggingListener();
            queryLogger.setLogger("QueryLogger");
            queryLogger.setQueryLogEntryCreator(logEntryCreator);
            queryLogger.setWriteDataSourceName(false);
            queryLogger.setLogLevel(SLF4JLogLevel.INFO);
            
            SLF4JSlowQueryListener slowQueryLogger = new SLF4JSlowQueryListener();
            slowQueryLogger.setLogger("SlowQueryLogger");
            slowQueryLogger.setQueryLogEntryCreator(logEntryCreator);
            slowQueryLogger.setWriteDataSourceName(false);
            slowQueryLogger.setLogLevel(SLF4JLogLevel.WARN);
            slowQueryLogger.setThresholdTimeUnit(TimeUnit.MILLISECONDS);
            slowQueryLogger.setThreshold(1_000);

            this.dataSource = ProxyDataSourceBuilder.create(dataSource)
                .name(beanName)
                .multiline()
                .listener(queryLogger)
                .listener(slowQueryLogger)
                .build();
        }

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            
            Method method;
            
            method = ReflectionUtils.findMethod(
                dataSource.getClass(), invocation.getMethod().getName());
            if (method != null) {
                return method.invoke(dataSource, invocation.getArguments());
            }
            return invocation.proceed();
        }
    }
}
