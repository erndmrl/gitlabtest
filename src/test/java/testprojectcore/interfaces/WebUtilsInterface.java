package testprojectcore.interfaces;

import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * @author Eren Demirel
 *
 */
public interface WebUtilsInterface {

    void fillInInputField(WebElement webElement, String valueToEnter);

    List<String> getAttributeValuesAccordingToATag(List<WebElement> elementsAccordingToTagName, String attributeToGetValueOf);
}
