package testprojectcore.interfaces;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author Eren Demirel
 *
 */
public interface HelperInterface {

    public boolean isImageDisplayed(WebElement imageToTest);

    public void selectFromDrowpdownByALocator(WebElement webElement, String visibleText);

    public void clickElementByALocator(WebDriver driver, String text);
}
