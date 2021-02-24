package testprojectcore.dataprovider;

import org.apache.log4j.Logger;


/**
 * @author Eren Demirel
 */
enum ConfigFilePathProvider {

    INSTANCE;

    private String configFilePath;
    Boolean warningLogged = false;

    public String getConfigFilePath() {
        try {
            configFilePath = System.getProperty("configfilepath").toLowerCase();
        } catch (Exception e) {
            if (!warningLogged) {
                Logger.getLogger(EnvVariableProvider.class).warn("Config file path parameter was not passed in run command, reading from the default config file under " + "'src/test/resources/config/config.txt'...");
            }
            configFilePath = "src/test/resources/config/config.txt";
            warningLogged = true;
        } finally {
            if (configFilePath == null || configFilePath.equals("")) {
                Logger.getLogger(EnvVariableProvider.class).warn("Could not set config file path parameter. It is either null or empty.");
            }
        }
        return configFilePath;
    }
}
