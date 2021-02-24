package pageobjects;

import testprojectcore.core.Driver;
import testprojectcore.core.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

/**
 * @author Eren Demirel
 */
public class ExamplePage extends Driver {

    /* @FindBy annotation can be used to locate Web Elements*/
    @FindBy(tagName = "a")
    List<WebElement> links;

    @FindBy(id = "js-link-box-fr")
    WebElement frenchWikiLink;


    public ExamplePage(WebDriver driver) {        //Initalize Web Elements using Selenium PageFactory
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void navigateToHomePage() {
        driver.get("https://www.wikipedia.org/");
    }

    public boolean verifyLanguageOptionsExist() {
        return links.size() > 0;
    }

    public void clickWikiInFrench() {
        frenchWikiLink.click();
    }
}
