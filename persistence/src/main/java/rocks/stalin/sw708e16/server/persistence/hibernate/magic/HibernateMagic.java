package rocks.stalin.sw708e16.server.persistence.hibernate.magic;

import org.bson.types.ObjectId;
import org.hibernate.Hibernate;
import org.springframework.util.ReflectionUtils;

import javax.management.ReflectionException;
import java.lang.reflect.Field;

public class HibernateMagic {
    /**
     * Initialize a hibernate proxy property on an object without having access to that field.
     * @param obj The object to initialize the field on
     * @param fieldName The name of the field to initialize
     * @param <T> The return type
     * @return The initialized value. Useful for recursively initializing
     */
    public static <T> T initialize(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        Field field = ReflectionUtils.findField(clazz, fieldName);
        ReflectionUtils.makeAccessible(field);
        Object val = ReflectionUtils.getField(field, obj);
        Hibernate.initialize(val);
        return (T) val;
    }
}
