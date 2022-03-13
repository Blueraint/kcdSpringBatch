package com.kcd.KCDSpringBatch.util;

import com.kcd.KCDSpringBatch.config.ApplicationContextProvider;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;

@UtilityClass
public class BeanUtil {
    public static Object getBean(Class<?> clazz) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(clazz);
    }
}
