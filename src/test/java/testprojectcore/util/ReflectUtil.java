package testprojectcore.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


/**
 * @author Eren Demirel
 */
public class ReflectUtil {

    public static <T> int getFieldCount(Class<T> clazz) throws NoSuchFieldException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        int fieldCount = 0;
        Class[] declaredClasses = clazz.getDeclaredClasses();
        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field field : declaredFields) {
            fieldCount++;
        }

        for (Class innerClass : declaredClasses) {
            Field[] innerClassDeclaredFields = innerClass.getDeclaredFields();
            for (Field field : innerClassDeclaredFields) {
                fieldCount++;
            }
        }
        return fieldCount;
    }
}
