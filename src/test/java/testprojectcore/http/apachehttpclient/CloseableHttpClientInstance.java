package testprojectcore.http.apachehttpclient;

import io.qameta.allure.httpclient.AllureHttpClientRequest;
import io.qameta.allure.httpclient.AllureHttpClientResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author Eren Demirel
 */
public enum CloseableHttpClientInstance {

    INSTANCE;

    public CloseableHttpClient getDefaultClient() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().addInterceptorFirst(new AllureHttpClientRequest())
                .addInterceptorLast(new AllureHttpClientResponse()).build();
        return httpClient;
    }
}
