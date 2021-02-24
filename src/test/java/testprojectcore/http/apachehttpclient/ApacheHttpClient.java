package testprojectcore.http.apachehttpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * @author Eren Demirel
 */
public class ApacheHttpClient {

    public static HttpResponse sendRequest(HttpUriRequest httpUriRequest) throws IOException {
        CloseableHttpClient client;
        client = CloseableHttpClientInstance.INSTANCE.getDefaultClient();
        HttpResponse httpResponse = client.execute(httpUriRequest);
        return httpResponse;
    }
}
