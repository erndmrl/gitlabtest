package testprojectcore.util;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import testprojectcore.core.DriverManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class ReportsUtil {

    public static void takeScreenShot(String screenShotName) {
        try {
            TakesScreenshot scrShot = ((TakesScreenshot) DriverManager.getDriver());
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            InputStream targetStream = new FileInputStream(SrcFile);
            Allure.attachment(screenShotName, targetStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
