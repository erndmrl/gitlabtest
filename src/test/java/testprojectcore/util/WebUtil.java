package testprojectcore.util;

import testprojectcore.core.interfaces.Callable;
import org.openqa.selenium.WebElement;

import java.util.List;


/**
 * Class to enable methods to be passed as parameters
 * to other methods
 *
 * @author Eren Demirel
 */
public class WebUtil {

    /**
     * Method to enable easy use of WebElement methods. You can access all methods by
     * giving the method name roughly
     *
     * @param webElements         Web Elements that WebElement methods will be applied on
     * @param nameOfMethodRoughly The name of the WebElement method roughly(no need to be exact), e.g. clear, enable
     * @return
     */
    public static void webElementMethods(List<WebElement> webElements, String nameOfMethodRoughly, Callable callable) {
        for (WebElement element : webElements) {
            callable.execute(nameOfMethodRoughly, element);
        }
    }

}
