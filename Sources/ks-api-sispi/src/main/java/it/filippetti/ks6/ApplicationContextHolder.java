/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.filippetti.ks6;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * @author mazzocchetti
 */
@Component
public class ApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.applicationContext = applicationContext;
	}

	public static boolean isApplicationContextInitialized() {
		return applicationContext != null;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static <T> T getBean(Class<T> type) {
		if (isApplicationContextInitialized()) {
			return getApplicationContext().getBean(type);
		} else {
			throw new IllegalStateException();
		}
	}

	public static <T> T getBean(Class<T> type, String name) {
		if (isApplicationContextInitialized()) {
			return getApplicationContext().getBean(name, type);
		} else {
			throw new IllegalStateException();
		}
	}

	public static <T> T initializeBean(T bean) {
		return initializeBean(bean, bean.getClass().getSimpleName());
	}

	public static <T> T initializeBean(T bean, String name) {

		AutowireCapableBeanFactory beanFactory;

		if (isApplicationContextInitialized()) {
			beanFactory = getApplicationContext().getAutowireCapableBeanFactory();
			beanFactory.autowireBean(bean);
			beanFactory.initializeBean(bean, name);
		} else {
			throw new IllegalStateException();
		}
		return bean;
	}

	public static <T> T getAndInitializeBean(Class<T> type) {
		return initializeBean(getBean(type));
	}

	public static <T> T getAndInitializeBean(Class<T> type, String name) {
		return initializeBean(getBean(type, name), name);
	}

	public static <T> T autowireObject(T object) {

		AutowireCapableBeanFactory beanFactory;

		if (isApplicationContextInitialized()) {
			beanFactory = getApplicationContext().getAutowireCapableBeanFactory();
			beanFactory.autowireBean(object);
		} else {
			throw new IllegalStateException();
		}
		return object;
	}
}
