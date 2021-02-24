package testprojectcore.core;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import testprojectcore.core.interfaces.Callable;

import static testprojectcore.core.DriverManager.driver;
import static testprojectcore.core.DriverManager.timeOutInSeconds;


/**
 * @author Eren Demirel
 */
public class DriverUtils {

    public static Callable callable = new Invoker();

    public static Wait fluentWait;

    public static Select getSelect(WebElement selectWebElement) {
        return new Select(selectWebElement);
    }

    public static Wait getWebDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, timeOutInSeconds);
    }

    public static Actions getActions(WebDriver driver) {
        return new Actions(driver);
    }

    public static JavascriptExecutor getJSExecutor(WebDriver driver) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        return executor;
    }

    public static void waitUntil(ExpectedCondition<?> condition, double timeOutInSeconds, WebDriver driver) {
        (new WebDriverWait(driver, (long) timeOutInSeconds)).until(condition);
    }

    public static void waitUntil(boolean condition, double timeOutInSeconds, WebDriver driver) {
        (new WebDriverWait(driver, (long) timeOutInSeconds)).equals(condition);
    }

    public static boolean webElementoperations(String nameOfOperationRoughly, WebElement webElement) {
        boolean returnValue = false;
        if (nameOfOperationRoughly.contains("clear".toLowerCase()) && webElement.getAttribute("type").equals("text")) {
            webElement.clear();
            returnValue = true;
        }
        if (nameOfOperationRoughly.contains("enable".toLowerCase()) && webElement.getAttribute("type").equals("text")) {
            returnValue = webElement.isEnabled();
        }
        return returnValue;
    }
}
