package testprojectcore.dataprovider;

import java.util.LinkedHashMap;

public class TestConfigProvider {

    public static class CONFIGFILEPATH {
        public static String getConfigFilePath() {
            return ConfigFilePathProvider.INSTANCE.getConfigFilePath();
        }
    }

    public static class TESTCONFIGURATION {
        public static String getPropertyValue(String propertyName) throws Exception {
            return UseParsers.getAValueFromTxtPropertyFile(ConfigFilePathProvider.INSTANCE.getConfigFilePath(), propertyName);
        }
    }

    public static class WEBTESTPROPERTIES {
        public static String getPropertyValue(String propertyName) throws Exception {
            return UseParsers.getAValueFromPropertyFile("src/test/resources/propertyfiles/webconfig.properties", propertyName);
        }
    }

    public static class ANDROIDTESTPROPERTIES {
        public static LinkedHashMap<String, String> getAllPropertyValuePairs() throws Exception {
            return UseParsers.getAllValuesFromPropertyFile("src/test/resources/propertyfiles/androidconfig.properties");
        }
    }

    public static class IOSTESTPROPERTIES {
        public static LinkedHashMap<String, String> getAllPropertyValuePairs() throws Exception {
            return UseParsers.getAllValuesFromPropertyFile("src/test/resources/propertyfiles/iosconfig.properties");
        }
    }
}
