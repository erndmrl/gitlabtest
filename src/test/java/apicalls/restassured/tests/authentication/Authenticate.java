package apicalls.restassured.tests.authentication;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import apicalls.requests.authentication.AuthenticationRequest;
import apicalls.responses.authentication.AuthenticationResponse;
import testprojectcore.dataprovider.JacksonObjectMapper;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;


public class Authenticate {


    @Test
    public void shouldAuthenticateSuccesfully() {
        final String bearerToken;
        AuthenticationRequest authenticationRequest = JacksonObjectMapper.mapJsonFileToObject(AuthenticationRequest.class, "src/test/java/restassured/requests/authentication/jsonpayloads/AuthenticationRequest.json");
        RestAssured.baseURI = "https://auth-test.noqu.cloud";
        RestAssured.basePath = "/v2";

        AuthenticationResponse authenticationResponse =
                given().
                contentType(ContentType.JSON).
                body(authenticationRequest).
                when().
                post("/token").
                then().assertThat().statusCode(200).body("isEmpty()", Matchers.is(false)).and().body("$", hasKey("access_token")).
                extract().response().as(AuthenticationResponse.class);
    }

    public String getBearerToken() {
        AuthenticationRequest authenticationRequest = JacksonObjectMapper.mapJsonFileToObject(AuthenticationRequest.class, "src/test/java/apicalls/payloads/authentication/AuthenticationRequest.json");

        RestAssured.baseURI = "https://auth-test.noqu.cloud";
        RestAssured.basePath = "/v2";

        Response response =
                given().
                contentType(ContentType.JSON).
                body(authenticationRequest).
                when().
                post("/token").
                then().assertThat().statusCode(200).body("isEmpty()", Matchers.is(false)).and().body("$", hasKey("access_token")).
                using().extract().response();

        return response.jsonPath().getString("access_token");
    }
}
