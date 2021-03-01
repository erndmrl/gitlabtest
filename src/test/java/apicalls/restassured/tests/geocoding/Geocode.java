package apicalls.restassured.tests.geocoding;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import testprojectcore.dataprovider.EnvironmentDataProvider;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;

public class Geocode {

    public ResponseBody getLatLong(@Nullable String Address1, @Nullable String Address2, @Nullable String Postcode, @Nullable String City, @Nullable String Country) throws IOException, InterruptedException, URISyntaxException {

        Response response = given().contentType(ContentType.JSON).
                when().
                queryParam("apiKey", EnvironmentDataProvider.TESTDATA.getData("HereLocationAPI_apiKey")).
                queryParam("q", Address1 + ", " + Address2 + ", " + Postcode + ", " + City + ", " + Country).
                get(EnvironmentDataProvider.TESTDATA.getData("HereLocationAPI_GeoCodingAPIURL")).
                then().
                assertThat().statusCode(200).extract().response();

        return response.body();

    }

    public ResponseBody getPlusCode(String Latitude, String Longitude) throws IOException {

        Response response = given().contentType(ContentType.JSON).
                when().
                queryParam("latitude", Latitude).
                queryParam("longitude", Longitude).
                get(EnvironmentDataProvider.TESTDATA.getData("BigDataCloudGeocodingAPI_ReverseGeocodingURL")).
                then().
                assertThat().statusCode(200).extract().response();

        return response.body();

    }
}
