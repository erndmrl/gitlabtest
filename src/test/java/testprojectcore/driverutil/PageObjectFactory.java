package testprojectcore.driverutil;

import testprojectcore.core.Driver;
import testprojectcore.core.DriverManager;
import org.openqa.selenium.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 *
 * @author Eren Demirel
 *
 */
public class PageObjectFactory {


//    protected static FooPage fooPage;       //You can also register page objects here

    /**
     * Object creator method using generics to be used in step definition classes to initialize page objects
     *
     * @param clazz Reference to class to create object of
     * @return object created
     */
    public static <T extends Driver> T createClass(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor(WebDriver.class);
            return constructor.newInstance(DriverManager.getDriver());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
