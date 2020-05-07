package reflection;

import java.lang.reflect.Field;
import java.util.Map;

public class ReflectionUtil {

    /**
     * map对象转换成Object
     * @param clazz 要转换的类型的class
     * @param map map对象
     * @param <T> 要转换的类型
     * @return 返回转换后的对象
     */
    public static <T> T mapToObject(Class<T> clazz, Map<String, ?> map) {
        T instance = null;
        try {
            instance = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                Object value = map.get(field.getName());
                if (value != null) {
                    if (value.getClass().isAssignableFrom(field.getType())) {
                        field.set(instance, value);
                    } else if (value instanceof Map) {
                        field.set(instance, mapToObject(field.getType(), (Map<String, ?>) value));
                    } else {
                        if (field.getType().isPrimitive()) {
                            field.set(instance, value);
                        } else {
                            field.set(instance, field.getType().cast(value));
                        }
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return instance;
    }

}
