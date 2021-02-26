package testprojectcore.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


/**
 * @author Eren Demirel
 */
public class ReflectUtil {


    /**
     * Get field count for both parent class and inner classes. Note that you have to use field.setAccessible(true);
     * in case of private variables
     *
     * @author Eren Demirel
     *
     * @param clazz Name of the outer class
     * @return fieldCount
     */
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
