package testprojectcore.core;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import testprojectcore.dataprovider.TestConfigProvider;

import java.util.Map;
import java.util.logging.Level;


/**
 * @author Eren Demirel
 */
class OptionsAndCapabilitiesManager {

    public static DesiredCapabilities setAndReturnChromeCaps() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-popup-blocking");
        chromeOptions.addArguments("--disable-notifications");
        //Below 4 options are a workaround for Chromedriver bug. See https://bugs.chromium.org/p/chromedriver/issues/detail?id=2473
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");
        if (TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("performanceLogs").equals("enabled") ||
                TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("performanceLogs").equals("true")) {
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
            chromeOptions.setCapability("goog:loggingPrefs", logPrefs);
        }
        return capabilities.merge(chromeOptions);
    }

    public static DesiredCapabilities setAndReturnFirefoxCaps() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setCapability("marionette", true);
        firefoxOptions.addPreference("permissions.default.desktop-notification", 0);
        if (TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("performanceLogs").equals("enabled") ||
                TestConfigProvider.WEBTESTPROPERTIES.getPropertyValue("performanceLogs").equals("true")) {
            firefoxOptions.setLogLevel(FirefoxDriverLogLevel.TRACE);
        }
        return capabilities.merge(firefoxOptions);
    }


    public static DesiredCapabilities setAndReturnAppiumDesiredCaps() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("android")) {
            for (Map.Entry<String, String> mapElement : TestConfigProvider.ANDROIDTESTPROPERTIES.getAllPropertyValuePairs().entrySet()) {
                capabilities.setCapability(mapElement.getKey(), mapElement.getValue());
            }
        } else if (TestConfigProvider.TESTCONFIGURATION.getPropertyValue("platform").equals("ios")) {
            for (Map.Entry<String, String> mapElement : TestConfigProvider.IOSTESTPROPERTIES.getAllPropertyValuePairs().entrySet()) {
                capabilities.setCapability(mapElement.getKey(), mapElement.getValue());
            }
        }
        return capabilities;
    }
}