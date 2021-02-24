package testprojectcore.testcontext;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpResponse;

import java.util.HashMap;

/**
 * This approach using dependency injection of type contructor injection with Picocontainer is thread-safe
 * as discussed here by a former Cucumber developer: https://stackoverflow.com/a/34531404
 *
 * However, when same step definition is used by the scenarios in different feature files, store the references and
 * variables in one of the hash maps below, key always being the current thread id(Thread.currentThread().getId()) to
 * achieve thread safety
 */
public class TestContext {


    public Response restassuredResponse;
    public ValidatableResponse restassuredResponseJson;
    public RequestSpecification restassuredRequest;
    public HttpResponse apachehttpclientresponse;
    public HashMap<Long, HttpResponse> apacheHttpClientResponse = new HashMap<>();
    public String strInContext;

}
