package pageobjects;

import testprojectcore.core.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import testprojectcore.core.DriverManager;
import testprojectcore.util.HTTPUtil;

public class MiscPO extends Driver {


    public MiscPO(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void somethingAppium(){
        driver.manage().logs();
    }
}
