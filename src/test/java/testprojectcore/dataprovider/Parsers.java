package testprojectcore.dataprovider;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Enum implementation of strategy design pattern for parsers used in the test framework
 *
 * @author Eren Demirel
 */
enum Parsers {

    ExtractAValueFromJsonFile {

        JsonObject gsonJsonObject;
        Gson gson = new Gson();
        BufferedReader bufferedReader;
        private String dataGroup;

        @Override
        String execute(String filePath, String requestedData) throws IOException {
            dataGroup = EnvVariableProvider.INSTANCE.getEnv();
            filePath = filePath.replace('/', File.separatorChar);
            try {
                bufferedReader = new BufferedReader(new FileReader(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            gsonJsonObject = gson.fromJson(bufferedReader, JsonObject.class);
            return gsonJsonObject.getAsJsonObject(dataGroup).get(requestedData).getAsString();
        }

        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            return null;
        }
    },

    GetAPropertyFromConfigTxtFile {

        Properties properties;
        BufferedReader reader;

        @Override
        String execute(String txtPropertyFilePath, String requestedData) throws IOException {
            txtPropertyFilePath = txtPropertyFilePath.replace('/', File.separatorChar);
            String propertyValue;
            try {
                reader = new BufferedReader(new FileReader(txtPropertyFilePath));
                properties = new Properties();
                try {
                    properties.load(reader);
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IOException("Error while loading the property file " + "'" + txtPropertyFilePath);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("config.txt not found at " + txtPropertyFilePath);
            }
            propertyValue = properties.getProperty(requestedData).toLowerCase();
            if (propertyValue != null) return propertyValue;
            else
                throw new RuntimeException("Can not read property" + "'" + propertyValue + "'" + "from the configuration file");
        }

        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            return null;
        }
    },

    GetAPropertyFromPropertyFiles {

        String propertyGroup;{
            try {
                propertyGroup = EnvVariableProvider.INSTANCE.getEnv();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Properties properties;
        FileReader reader;

        @Override
        String execute(String propertyFilePath, String propertyName) throws IOException {
            properties = new Properties();
            propertyFilePath = propertyFilePath.replace('/', File.separatorChar);
            reader = new FileReader(propertyFilePath);
            String propertyValue = null;
            try {
                properties.load(reader);
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Property file not found at " + propertyFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Error while loading the property file " + propertyFilePath);
            }
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                if (key.toString().startsWith(propertyGroup + ".")) {
                    propertyValue = properties.getProperty(propertyGroup + '.' + propertyName).toLowerCase();
                } else if (key.toString().equals(propertyName)) {
                    if (!propertyFilePath.toLowerCase().contains("webconfig") && !propertyFilePath.toLowerCase().contains("androidconfig") && !propertyFilePath.toLowerCase().contains("iosconfig")) {
                        Logger.getLogger(this.getClass()).warn("No such environment property: " + "'" + propertyGroup + "." + propertyName + "'" + " in " + propertyFilePath + ". Setting the variable using " + "'" + propertyName + "' property(No environment). " + "Please check the file later");
                    }
                    propertyValue = properties.getProperty(propertyName).toLowerCase();
                }
            }
            if (propertyValue == null) {
                Logger.getLogger(this.getClass()).error("Couldn't find " + "'" + propertyGroup + "." + propertyName + "'" + " or " + "'" + propertyName + "'" + " in " + propertyFilePath);
                throw new IOException();
            }
            return propertyValue;
        }

        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            return null;
        }
    },

    GetAllPropertiesFromPropertyFiles {

        LinkedHashMap<String, String> propertiesInOrder = new LinkedHashMap<String, String>();

        String propertyGroup;{
            try {
                propertyGroup = EnvVariableProvider.INSTANCE.getEnv();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Properties properties;
        FileReader reader;


        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            properties = new Properties();
            propertyFilePath = propertyFilePath.replace('/', File.separatorChar);
            reader = new FileReader(propertyFilePath);
            try {
                properties.load(reader);
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Property file not found at " + propertyFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Error while loading the property file " + propertyFilePath);
            }
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                if (key.toString().startsWith(propertyGroup + ".")) {
                    propertiesInOrder.put(key.toString(), properties.getProperty(propertyGroup + '.' + key.toString()));
                }
            }
            //else return all properties after the loop
            if (!(propertiesInOrder.size() > 0) && keys != null) {
                if (!propertyFilePath.toLowerCase().contains("webconfig") && !propertyFilePath.toLowerCase().contains("androidconfig") && !propertyFilePath.toLowerCase().contains("iosconfig")) {
                    Logger.getLogger(this.getClass()).warn("No environment properties in: " + propertyFilePath + ". Returning all properties. Please check the file later");
                }
                for (Object key : keys) {
                    propertiesInOrder.put(key.toString(), properties.getProperty(key.toString()));
                }
            }
            if (keys.toString() == null) {
                Logger.getLogger(this.getClass()).error("No properties defined in" + propertyFilePath);
                throw new IOException();
            }
            return propertiesInOrder;
        }

        @Override
        String execute(String fileName, String requestedData) throws IOException {
            return null;
        }
    },

    ExtractJsonObjectFromFile {

        JSONObject orgJsonObject;

        @Override
        String execute(String filePathFromContentRoot, String requestedJsonObject) throws IOException {
            File jsonFile = new File(filePathFromContentRoot);
            JSONObject jsonObject = null;
            if (jsonFile.exists()) {
                InputStream is = new FileInputStream(filePathFromContentRoot);
                String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
                orgJsonObject = new JSONObject(jsonTxt);
                jsonObject = orgJsonObject.getJSONObject(requestedJsonObject);
            }
            if (jsonObject != null) {
                return jsonObject.toString();
            } else return null;
        }

        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            return null;
        }
    },

    ExtractAValueFromYamlFile {

        Yaml yaml;

        @Override
       /* Note that the file should be under Resources and if the file is inside a package, provide the name of the
        package also. e.g. "testyamlfiles/testfile.yaml" */
        String execute(String filePathFromSourceRoot, String key) throws IOException {
            yaml = new Yaml();
            InputStream inputStream = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream(filePathFromSourceRoot);
            Map<String, Object> obj = yaml.load(inputStream);
            return obj.get(key).toString();
        }

        @Override
        LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException {
            return null;
        }
    };


    abstract String execute(String fileName, String requestedData) throws IOException;

    abstract LinkedHashMap<String, String> execute(String propertyFilePath) throws IOException;

}
