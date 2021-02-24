package testprojectcore.core;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import testprojectcore.dataprovider.TestConfigProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


/**
 * @author Eren Demirel
 *
 */
public class DriverManager {

    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    String browser = null;

    String grid = null;

    static long timeOutInSeconds = 10;

    private static String WEB_REMOTE_URL;
    private static String ANDROID_REMOTE_URL;
    private static String IOS_REMOTE_URL;


    public DriverManager() {
        try {
            WEB_REMOTE_URL = TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("remoteUrl");
            ANDROID_REMOTE_URL = TestConfigProvider.ANDROIDTESTPROPERTIES.getAllPropertyValuePairs().get("remoteUrl");
            IOS_REMOTE_URL = TestConfigProvider.IOSTESTPROPERTIES.getAllPropertyValuePairs().get("remoteUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static synchronized void closeDriver() {
        getDriver().close();
    }

    public static synchronized void quitDriver() {
        getDriver().quit();
    }

    public static synchronized WebDriver getDriver() {
        return driver.get();
    }


    public void initDriver() throws Exception {

        try {
            browser = System.getProperty("browser").toLowerCase();
        } catch (Exception e) {
            Logger.getLogger(this.getClass()).warn("Browser parameter was not passed in run command, setting the value from the config file...");
            browser = TestConfigProvider.TESTCONFIGURATION.getPropertyValue("browser");
        } finally {
            if (browser == null) {
                Logger.getLogger(this.getClass()).warn("Couldn't set browser parameter from the property file either(it was null), setting the default browser...");
                browser = TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("defaultBrowser");
            }
            if (browser.equals("")) {
                Logger.getLogger(this.getClass()).warn("Couldn't set browser parameter from the property file either(it was empty), setting the default browser...");
                browser = TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("defaultBrowser");
            }
        }
        grid = System.getProperty("grid");
        boolean remote = false;
        if (grid != null) {
            remote = Boolean.parseBoolean(grid);
        }
        if (remote) {
            try {
                URL webRemoteUrl = new URL(WEB_REMOTE_URL);
                URL androidRemoteUrl = new URL(ANDROID_REMOTE_URL);
                URL iosRemoteUrl = new URL(IOS_REMOTE_URL);
//                String gridIP = System.getProperty("gridIP");

                if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("web")) {
                    switch (browser) {
                        case "firefox":
                            driver.set(new RemoteWebDriver(webRemoteUrl, OptionsAndCapabilitiesManager.setAndReturnFirefoxCaps()));
                            break;
                        case "chrome":
                            driver.set(new RemoteWebDriver(webRemoteUrl, OptionsAndCapabilitiesManager.setAndReturnChromeCaps()));
                            break;
                    }
                }
                if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("android")) {
                    driver.set(new RemoteWebDriver(androidRemoteUrl, OptionsAndCapabilitiesManager.setAndReturnAppiumDesiredCaps()));
                }
                if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("ios")) {
                    driver.set(new RemoteWebDriver(iosRemoteUrl, OptionsAndCapabilitiesManager.setAndReturnAppiumDesiredCaps()));
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("web")) {
                    switch (browser) {
                        case "firefox":
                            System.setProperty("webdriver.gecko.driver", TestConfigProvider.TESTCONFIGURATION.getPropertyValue("driverPathFirefoxForWindows"));
                            driver.set(new FirefoxDriver(OptionsAndCapabilitiesManager.setAndReturnFirefoxCaps()));
                            break;
                        case "chrome":
                            System.setProperty("webdriver.chrome.driver", TestConfigProvider.TESTCONFIGURATION.getPropertyValue("driverPathChromeForWindows"));
                            driver.set(new ChromeDriver(OptionsAndCapabilitiesManager.setAndReturnChromeCaps()));
                            break;
                    }
                }
                if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("android")) {
                    ANDROID_REMOTE_URL = TestConfigProvider.ANDROIDTESTPROPERTIES.getAllPropertyValuePairs().get("remoteUrl");
                    URL androidRemoteUrl = new URL(ANDROID_REMOTE_URL);
                    driver.set(new RemoteWebDriver(androidRemoteUrl, OptionsAndCapabilitiesManager.setAndReturnAppiumDesiredCaps()));
                }
                if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("ios")) {
                    IOS_REMOTE_URL = TestConfigProvider.IOSTESTPROPERTIES.getAllPropertyValuePairs().get("remoteUrl");
                    URL iosRemoteUrl = new URL(IOS_REMOTE_URL);
                    driver.set(new RemoteWebDriver(iosRemoteUrl, OptionsAndCapabilitiesManager.setAndReturnAppiumDesiredCaps()));
                }
            } else if (System.getProperty("os.name").toLowerCase().contains("nix") || System.getProperty("os.name").toLowerCase().contains("nux") || System.getProperty("os.name").toLowerCase().indexOf("aix") > 0) {
                if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("web")) {
                    switch (browser) {
                        case "firefox":
                            System.setProperty("webdriver.gecko.driver", TestConfigProvider.TESTCONFIGURATION.getPropertyValue("driverPathFirefoxForLinux"));
                            driver.set(new FirefoxDriver(OptionsAndCapabilitiesManager.setAndReturnFirefoxCaps()));
                            break;
                        case "chrome":
                            System.setProperty("webdriver.chrome.driver", TestConfigProvider.TESTCONFIGURATION.getPropertyValue("driverPathChromeForLinux"));
                            driver.set(new ChromeDriver(OptionsAndCapabilitiesManager.setAndReturnChromeCaps()));
                            break;
                    }
                }
                if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("android")) {
                    ANDROID_REMOTE_URL = TestConfigProvider.ANDROIDTESTPROPERTIES.getAllPropertyValuePairs().get("remoteUrl");
                    URL androidRemoteUrl = new URL(ANDROID_REMOTE_URL);
                    driver.set(new RemoteWebDriver(androidRemoteUrl, OptionsAndCapabilitiesManager.setAndReturnAppiumDesiredCaps()));
                }
                if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("ios")) {
                    IOS_REMOTE_URL = TestConfigProvider.IOSTESTPROPERTIES.getAllPropertyValuePairs().get("remoteUrl");
                    URL iosRemoteUrl = new URL(IOS_REMOTE_URL);
                    driver.set(new RemoteWebDriver(iosRemoteUrl, OptionsAndCapabilitiesManager.setAndReturnAppiumDesiredCaps()));
                }
            }
        }
        if ((TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("startMaximized").equals("yes") ||
                TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("startMaximized").equals("true")) &&
                !(TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("android") ||
                        TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("ios"))) {
            getDriver().manage().window().maximize();
        }
        getDriver().manage().timeouts().implicitlyWait(Integer.parseInt(TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("implicitWaitDuration")), TimeUnit.SECONDS);
    }
}


