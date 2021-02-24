package testprojectcore.stepdefs;

import org.junit.jupiter.api.Order;
import testprojectcore.core.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import testprojectcore.dataprovider.TestConfigProvider;

import java.io.ByteArrayInputStream;


/**
 * @author Eren Demirel
 */
public class BaseStepDefs {
    @Before("@browser")
    @Order(Integer.MIN_VALUE)
    public void initDriverAndBrowsers() throws Exception {       //Here, initialize the driver before each step and close the browser after each step, these operations are done via cucumber tags therefore no need for inheritance and composition
        DriverManager driverManager = new DriverManager();
        driverManager.initDriver();
    }

    @After("@browser")
    public void tearDown(Scenario scenario) throws Exception {       //Take screenshot and add the attachment to the report if failed
        if (scenario.isFailed()) {
            if (scenario.getSourceTagNames().contains("@browser")) {
                Allure.addAttachment("Screenshot: ",
                        new ByteArrayInputStream(((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES)));
            }
        }
        if ((TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("android") ||
                TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("ios"))) {
            DriverManager.quitDriver();
        } else
            DriverManager.closeDriver();
    }
}
