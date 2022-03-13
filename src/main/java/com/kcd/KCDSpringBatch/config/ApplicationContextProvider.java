package com.kcd.KCDSpringBatch.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    // 인터페이스 구현부
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.applicationContext = ctx;
    }
    // 이 메서드를 통해 ApplicationContext(Spring IoC Container) 접근
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}