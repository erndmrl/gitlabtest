package testprojectcore.dataprovider;

import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * @author Eren Demirel
 */
enum EnvVariableProvider {

    INSTANCE;

    private String env;
    Boolean warningLogged = false;


    public String getEnv() throws IOException {
        try {
            env = System.getProperty("env").toLowerCase();
        } catch (Exception e) {
            if (!warningLogged) {
                Logger.getLogger(EnvVariableProvider.class).warn("Environment parameter was not passed in run command, setting the value from the config file...");
            }
            env = UseParsers.getAValueFromTxtPropertyFile("C:\\Users\\dmrle\\IdeaProjects\\test-automation\\src\\test\\resources\\config\\config.txt", "env");
            warningLogged = true;
        } finally {
            if (env == null || env.equals("")) {
                Logger.getLogger(EnvVariableProvider.class).warn("Could not set environment parameter. It is either null or empty.");
            }
        }
        return env;
    }
}
