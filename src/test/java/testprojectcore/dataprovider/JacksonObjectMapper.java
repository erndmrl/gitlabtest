package testprojectcore.dataprovider;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;


/**
 * @author Eren Demirel
 *
 */
public class JacksonObjectMapper {

    private JacksonObjectMapper(){}

    /**
     * Maps json file to Java object via Jackson object mapper
     *
     * @param clazz        Object that is being mapped to
     * @param filePath     File path of the json file to map from
     * @return t           Instance of object that is being returned from object mapper
     */
    public static <T> T mapJsonFileToObject(Class<T> clazz, String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(filePath);
            T t = mapper.readValue(file, clazz);
            return t;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Maps object to json as string via Jackson object mapper
     *
     * @param o          Object to map to json string
     * @return value     Json as string
     */
    public static String mapObjectToJsonAsString(Object o) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String value = mapper.writeValueAsString(o);
            return value;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Maps Json string to object via Jackson object mapper
     *
     * @param jsonAsString      Json string that is being mapped to object
     * @param clazz             Object that is being mapped to
     * @return t                Mapped object
     */
    public static <T> T mapJsonStringToObject(String jsonAsString, Class<T> clazz) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            T t = mapper.readValue(jsonAsString, clazz);
            return t;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
