package rocks.stalin.sw708e16.server.persistence.spring.datainserter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Component
public class InserterBean {
    @Autowired
    ApplicationContext context;

    public void insert() {
        Map<String, Object> beans = context.getBeansWithAnnotation(DevelopmentData.class);
        for(Map.Entry<String, Object> entry : beans.entrySet()) {
            Object bean = entry.getValue();
            Method method = ReflectionUtils.findMethod(bean.getClass(), "insert");
            try {
                method.invoke(bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
