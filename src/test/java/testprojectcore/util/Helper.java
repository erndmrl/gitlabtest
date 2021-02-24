package testprojectcore.util;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.json.XML;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import testprojectcore.core.DriverManager;
import testprojectcore.interfaces.HelperInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Eren Demirel
 */
public class Helper implements HelperInterface {


    @Override
    public boolean isImageDisplayed(WebElement imageToTest) {
        return (Boolean) ((JavascriptExecutor) DriverManager.getDriver()).executeScript("return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0", imageToTest);
    }

    public static void waitForJavascriptToLoad(int maxWaitMillis, int pollDelimiterInMillis) throws InterruptedException {
        double startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() < startTime + maxWaitMillis) {
            String prevState = DriverManager.getDriver().getPageSource();
            Thread.sleep(pollDelimiterInMillis);
            if (prevState.equals(DriverManager.getDriver().getPageSource())) {
                return;
            }
        }
    }

    public static void clickBlankSpace() {
        DriverManager.getDriver().findElement(By.xpath("//html")).click();
    }

    public static int checkAllCheckboxes(List<WebElement> checkboxes) {
        for (WebElement checkbox : checkboxes) {
            checkbox.click();
        }
        return checkboxes.size();
    }

    @Override
    public void selectFromDrowpdownByALocator(WebElement webElement, String visibleText) {
        Select select = new Select(webElement);
        select.selectByVisibleText(visibleText);
    }

    public static List<String> getAttributeValuesAccordingToATag(List<WebElement> elementsAccordingToTagName, String attributeToGetValueOf) {
        List<String> attributeValues = new ArrayList<>();
        for (WebElement element : elementsAccordingToTagName) {
            attributeValues.add(element.getAttribute(attributeToGetValueOf));
        }
        return attributeValues;
    }

    public static WebElement getVisibleElement(List<WebElement> elements, WebDriver driver) {
        //   WebDriverWait wait = new WebDriverWait(driver, 15);
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                return element;
            }
        }
        throw new RuntimeException(elements.toString() + " are not visible!");
    }

    public static boolean waitForJavascriptToLoad(WebDriver driver) {

        WebDriverWait wait = new WebDriverWait(driver, 15);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver12 -> {
            try {
                return ((Long) js.executeScript("return jQuery.active") == 0);
            } catch (Exception e) {
                return true;
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver1 -> js.executeScript("return document.readyState")
                .toString().equals("complete");

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }


    public static void waitUntilElementIgnoringStale(WebDriver driver, WebElement element, int timeout) {
        new WebDriverWait(driver, timeout)
                .ignoring(StaleElementReferenceException.class)
                .until(driver1 -> element.isDisplayed());
    }

    public static String getTextIgnoringStale(WebElement element, int tryCount) {
        int i = 0;
        while (i < tryCount) {
            try {
                return element.getText();
            } catch (StaleElementReferenceException e) {
                System.out.println("Could not get text");
            }
            i++;
        }
        return null;
    }

    @Override
    public void clickElementByALocator(WebDriver driver, String text) {
        List<WebElement> elements = driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]"));
        this.getVisibleElement(elements, driver).click();
    }

    public static String errorMessageUnderInputField(String inputFieldName) {
        WebElement element = DriverManager.getDriver().findElement(By.cssSelector("*[name='" + inputFieldName + "'] + .errorMessage"));
        return element.getText();
    }

    public static String convertJsonToXml(JSONObject json, String root) {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-15\"?>\n<" + root + ">" + XML.toString(json) + "</" + root + ">";
    }
}

