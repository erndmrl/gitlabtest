package testprojectcore.dataprovider;

import java.io.IOException;

/**
 * @author Eren Demirel
 */
public class EnvironmentDataProvider {

    public static class ENVIRONMENTVARIABLE {
        public static String getEnvironmentVariable() throws IOException {
            return EnvVariableProvider.INSTANCE.getEnv();
        }
    }

    public static class DATABASE {
        public static String getPropertyValue(String propertyName) throws Exception {
            return UseParsers.getAValueFromPropertyFile("src/test/resources/propertyfiles/db.properties", propertyName);
        }
    }

    public static class APPLICATION {
        public static String getPropertyValue(String propertyName) throws Exception {
            return UseParsers.getAValueFromPropertyFile("src/test/resources/propertyfiles/application.properties", propertyName);
        }
    }

    public static class TESTDATA {
        public static String getData(String key) throws Exception {
            return UseParsers.extractAValueFromJsonFile("src/test/resources/testdata/testdata.json", key);
        }
    }
}
