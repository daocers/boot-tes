package co.bugu.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author daocers
 * @Date 2018/11/21:15:10
 * @Description:
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware{
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static <T> T getClass(Class<T> clazz){
        return context.getBean(clazz);
    }

    public static <T> T getClass(Class<T> clazz, String name){
        return context.getBean(name, clazz);
    }
}
