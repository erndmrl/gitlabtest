package testprojectcore.dataprovider;

import com.github.javafaker.Faker;
import com.google.gson.*;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.number.IntegerRandomizer;


import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestBodyGenerator {

    /**
     * Custom serializer for LocalDate
     *
     */
    static class LocalDateAdapter implements JsonSerializer<LocalDate> {

        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
        }
    }

    /**
     * Register
     *
     */
    private static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

   /* public static String generateRequestBody() {
        EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
                .randomize(FieldPredicates.ofType(Integer.class), new IntegerRandomizer(10));
        EasyRandom easyRandom = new EasyRandom();
        FooDto fooDto = new FooDto();
        fooDto = easyRandom.nextObject(FooDto.class);
        Gson gson = getGson();
        String json = gson.toJson(fooDto);
        return json;
    }


    public static String generateRequestBody() {
        EasyRandom easyRandom = new EasyRandom();
        FooDto fooDto = new FooDto();
        fooDto = easyRandom.nextObject(FooDto.class);
        Faker faker = new Faker();
        String id = String.valueOf(faker.number().numberBetween(0, 34));
        fooDto.setId(id);
        Gson gson = getGson();
        String json = gson.toJson(fooDto);
        return json;
    }*/
}
