package com.github.utils;

import groovy.lang.GroovyClassLoader;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GroovyContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static final GroovyClassLoader CLASS_LOADER = new GroovyClassLoader();

    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) {
        applicationContext = context;
    }

    public static void autowireBean(String beanName, String script) {
        Class<?> clazz = CLASS_LOADER.parseClass(script);
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        autowireCapableBeanFactory.applyBeanPostProcessorsAfterInitialization(beanDefinition, beanName);

        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry) autowireCapableBeanFactory;
        if (beanRegistry.containsBeanDefinition(beanName)) {
            beanRegistry.removeBeanDefinition(beanName);
        }
        beanRegistry.registerBeanDefinition(beanName, beanDefinition);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}


