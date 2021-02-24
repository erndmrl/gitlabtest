package testprojectcore.http.apachehttpclient;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author Eren Demirel
 */
public class HttpCallBuilder {

    public static class PUT {

        public static HttpUriRequest putUsingJsonAsStringOrEmptyBody(String uri, String pathParam, @Nullable String jsonAsString, @Nullable List<NameValuePair> headers, int socketTimeoutMs) throws IOException, URISyntaxException {

            URIBuilder uriWithPathParam = new URIBuilder(uri);
            uriWithPathParam.setPath("/" + pathParam);

            System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + "URL with pathparam: " + uriWithPathParam.toString());

            RequestBuilder requestBuilder =
                    RequestBuilder.create("PUT").setUri(uriWithPathParam.toString());
            if (!(jsonAsString == null)) {
                StringEntity updateBody = new StringEntity(jsonAsString);
                requestBuilder.setEntity(updateBody);
                String requestBody = EntityUtils.toString(requestBuilder.getEntity(), StandardCharsets.UTF_8);
                System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + "Request body: " + requestBody);
            }
            addHeaders(requestBuilder, headers);
            setTimeout(requestBuilder, socketTimeoutMs);
            return requestBuilder.build();
        }
    }

    public static class POST {

        public static HttpUriRequest postUsingJsonAsString(String uri, String jsonAsString, @Nullable List<NameValuePair> headers, int socketTimeoutMs) throws IOException, URISyntaxException {

            RequestBuilder requestBuilder =
                    RequestBuilder.create("POST").setUri(new URI(uri));

            StringEntity postBody = new StringEntity(jsonAsString);
            requestBuilder.setEntity(postBody);
            addHeaders(requestBuilder, headers);
            setTimeout(requestBuilder, socketTimeoutMs);
            String requestBody = EntityUtils.toString(requestBuilder.getEntity(), StandardCharsets.UTF_8);
            System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + "Headers: "+ Arrays.toString(requestBuilder.build().getAllHeaders()));
            System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + "Request body: " + requestBody);
            return requestBuilder.build();
        }

        public static HttpUriRequest postMultiPartForm(String uri, Map<String, ContentBody> multiPartFormData, @Nullable List<NameValuePair> headers, int socketTimeoutMs) throws URISyntaxException {

            RequestBuilder requestBuilder =
                    RequestBuilder.create("POST").setUri(new URI(uri));
            requestBuilder.setEntity(addMultiPartData(requestBuilder, multiPartFormData));
            addHeaders(requestBuilder, headers);
            setTimeout(requestBuilder, socketTimeoutMs);
            return requestBuilder.build();
        }

        public static HttpUriRequest postFormData(String uri, List<NameValuePair> parameters, @Nullable List<NameValuePair> headers, int socketTimeoutMs) throws URISyntaxException {

            RequestBuilder requestBuilder =
                    RequestBuilder.create("POST").setUri(new URI(uri));
            addFormParameters(requestBuilder, parameters);
            addHeaders(requestBuilder, headers);
            setTimeout(requestBuilder, socketTimeoutMs);
            return requestBuilder.build();
        }
    }

    private static void setTimeout(RequestBuilder requestBuilder, int socketTimeoutMs) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeoutMs).build();
        requestBuilder.setConfig(requestConfig);
    }

    private static void addFormParameters(RequestBuilder requestBuilder, List<NameValuePair> parameters) {

        for (NameValuePair parameter : parameters) {
            requestBuilder.addParameter(parameter);
        }
    }

    private static void addHeaders(RequestBuilder requestBuilder, @Nullable List<NameValuePair> headers) {

        for (NameValuePair header : headers) {
            requestBuilder.addHeader(header.getName(), header.getValue());
        }
    }

    private static HttpEntity addMultiPartData(RequestBuilder requestBuilder, Map<String, ContentBody> nameDataPairs) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        for (Map.Entry<String, ContentBody> entry : nameDataPairs.entrySet()) {
            builder.addPart(entry.getKey(), entry.getValue());
        }

        HttpEntity entity = builder.build();
        return entity;
    }

}
