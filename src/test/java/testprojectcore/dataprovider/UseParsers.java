package testprojectcore.dataprovider;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Enum implementation of strategy design pattern for parsers used in the test framework
 *
 * @author Eren Demirel
 */
public class UseParsers {

    //While getting the all properties, the order is important
    public LinkedHashMap<String, String> perform(Parsers parsers, String x) throws IOException {
        return parsers.execute(x);
    }

    public String perform(Parsers parsers, String x, String y) throws IOException {
        return parsers.execute(x,y);
    }


    public static String extractAValueFromJsonFile(String fileName, String key) throws IOException {
        UseParsers useParsers = new UseParsers();
        return useParsers.perform(Parsers.ExtractAValueFromJsonFile, fileName, key);
    }

    public static String extractAJsonObjectFromJsonFile(String filePathFromContentRoot, String requestedJsonObject) throws IOException {
        UseParsers useParsers = new UseParsers();
        return useParsers.perform(Parsers.ExtractJsonObjectFromFile, filePathFromContentRoot, requestedJsonObject);
    }

    public static String extractAJsonArrayFromJsonFile(String filePathFromContentRoot, String requestedJsonArray) throws IOException {
        UseParsers useParsers = new UseParsers();
        return useParsers.perform(Parsers.ExtractJsonArrayFromFile, filePathFromContentRoot, requestedJsonArray);
    }

    public static String getAValueFromTxtPropertyFile(String fileName, String key) throws IOException {
        UseParsers useParsers = new UseParsers();
        return useParsers.perform(Parsers.GetAPropertyFromConfigTxtFile, fileName, key);
    }

    public static String getAValueFromPropertyFile(String fileName, String key) throws IOException {
        UseParsers useParsers = new UseParsers();
        return useParsers.perform(Parsers.GetAPropertyFromPropertyFiles, fileName, key);
    }

    public static String extractAValueFromYamlFile(String filePathFromSourceRoot, String key) throws IOException {
        UseParsers useParsers = new UseParsers();
        return useParsers.perform(Parsers.ExtractAValueFromYamlFile, filePathFromSourceRoot, key);
    }

    public static LinkedHashMap<String, String> getAllValuesFromPropertyFile(String fileName) throws IOException {
        UseParsers useParsers = new UseParsers();
        return useParsers.perform(Parsers.GetAllPropertiesFromPropertyFiles, fileName);
    }
}