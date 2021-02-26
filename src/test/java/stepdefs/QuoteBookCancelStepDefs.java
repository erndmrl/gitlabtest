package stepdefs;

import apicalls.requests.book.BookRequest;
import apicalls.requests.quote.QuoteRequest;
import apicalls.responses.book.BookResponse;
import apicalls.responses.quote.QuoteResponse;
import apicalls.restassured.tests.authentication.UseBearerToken;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import pageobjects.NDSHomePage;
import testprojectcore.core.DriverManager;
import testprojectcore.dataprovider.EnvironmentDataProvider;
import testprojectcore.dataprovider.JacksonObjectMapper;
import testprojectcore.dataprovider.UseParsers;
import testprojectcore.driverutil.PageObjectFactory;
import testprojectcore.http.apachehttpclient.ApacheHttpClient;
import testprojectcore.http.apachehttpclient.HttpCallBuilder;
import testprojectcore.testcontext.TestContext;
import testprojectcore.util.DateTimeUtil;
import testprojectcore.util.Helper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class QuoteBookCancelStepDefs {

    private TestContext testContext;

    private NDSHomePage ndsHomePage;

    private HttpResponse apacheHttpClientResponse;
    private Response restAssuredResponse;
    private Object clientResponse;

    private String responseBody;

    private QuoteRequest quoteRequest;
    private QuoteResponse quoteResponse;
    private BookResponse bookResponse;

    private List<Integer> quoteIds = new ArrayList<>();
    private int jobId;
    private Date uiDate;


    public QuoteBookCancelStepDefs(TestContext testContext) {
        this.testContext = testContext;
        ndsHomePage = PageObjectFactory.createClass(NDSHomePage.class);
    }


    @Given("Take an authorization token and request to Quote for Single Scheduled Any Other Day Delivery")
    public void takeAnAuthorizationTokenAndRequestToQuote() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/quote/SingleScheduledAnyOtherDayPickup.json");
        for (int i = 0; i < quoteRequest.tasks.size(); i++) {
            if (quoteRequest.tasks.get(i).requestedWindow != null) {
                quoteRequest.tasks.get(i).requestedWindow.from = DateTimeUtil.addDaysToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 1);
                quoteRequest.tasks.get(i).requestedWindow.to = DateTimeUtil.addDaysToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 2);
            }
        }
        String postingString = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(quoteRequest)));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_QuoteURL"), postingString, headers, 35000));
        clientResponse = response;
    }


    @And("Quote Response: Http response status code should be {int}")
    public void quoteHttpResponseStatusCodeShouldBe(int arg0) throws IOException, InterruptedException {
        if (clientResponse instanceof HttpResponse) {
            apacheHttpClientResponse = (HttpResponse) clientResponse;
            responseBody = EntityUtils.toString(apacheHttpClientResponse.getEntity(), StandardCharsets.UTF_8);
            System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + " Response body: " + responseBody);


            assertEquals(201, apacheHttpClientResponse.getStatusLine().getStatusCode());


            quoteResponse = JacksonObjectMapper.mapJsonStringToObject(responseBody, QuoteResponse.class);
            assert quoteResponse != null;

            jobId = quoteResponse.payload.id;
            Logger.getLogger(this.getClass()).info("Job Id: " + jobId);

            for (int l = 0; l < quoteResponse.payload.quotes.size(); l++) {
                quoteIds.add(quoteResponse.payload.quotes.get(l).id);
            }

            uiDate = quoteResponse.payload.processedDates.requestedDeliveryStart;
            if (uiDate == null) {
                uiDate = quoteResponse.payload.processedDates.requestedPickupStart;
            }
            if (uiDate == null) {
                uiDate = quoteResponse.payload.processedDates.expectedJobPickup;
            }
        }


        if (clientResponse instanceof Response) {
            restAssuredResponse = (Response) clientResponse;
            assertEquals(201, restAssuredResponse.statusCode());


            responseBody = restAssuredResponse.getBody().asString();

            quoteResponse = JacksonObjectMapper.mapJsonStringToObject(responseBody, QuoteResponse.class);
            assert quoteResponse != null;

            jobId = quoteResponse.payload.id;
            Logger.getLogger(this.getClass()).info("Job Id: " + jobId);

            for (int l = 0; l < quoteResponse.payload.quotes.size(); l++) {
                quoteIds.add(quoteResponse.payload.quotes.get(l).id);
            }

            uiDate = quoteResponse.payload.processedDates.requestedDeliveryStart;
            if (uiDate == null) {
                uiDate = quoteResponse.payload.processedDates.requestedPickupStart;
            }
            if (uiDate == null) {
                uiDate = quoteResponse.payload.processedDates.expectedJobPickup;
            }
        }
    }

    @And("Quote Response: Check response parameters")
    public void quoteParameterInsideBodyAndParameterInsideBodyShouldBe() throws Exception {


        assertEquals(quoteRequest.tasks.size(), quoteResponse.payload.tasks.size());

        int supportedTransportTypeIdentifiedQuoteObjectNumber = 0;
        boolean supportAnyTransportType = false;
        String[] deliveryProvidersThatSupportAnyTypeOfTransportType = UseParsers.extractAJsonArrayFromJsonFile("src/test/resources/misc/deliveryProviderProperties.json", "DeliveryProvidersThatSupportAnyTypeOfTransportType").split(",", -1);
        for (int i = 0; i < deliveryProvidersThatSupportAnyTypeOfTransportType.length; i++) {
            deliveryProvidersThatSupportAnyTypeOfTransportType[i] = deliveryProvidersThatSupportAnyTypeOfTransportType[i].replace("[", "");
            deliveryProvidersThatSupportAnyTypeOfTransportType[i] = deliveryProvidersThatSupportAnyTypeOfTransportType[i].replace("]", "");
            deliveryProvidersThatSupportAnyTypeOfTransportType[i] = deliveryProvidersThatSupportAnyTypeOfTransportType[i].replace("\"", "");
        }
        boolean isTransportTypeSupported = false;

        for (String deliveryProvider : deliveryProvidersThatSupportAnyTypeOfTransportType) {
            if (quoteRequest.deliveryProvider.get(0).equals(deliveryProvider)) {
                supportAnyTransportType = true;
            }
        }

        if (supportAnyTransportType) {
            assertEquals(1, quoteResponse.payload.quotes.size());
            assertEquals("any", quoteResponse.payload.quotes.get(0).transportTypes.get(0));
            assertEquals("quoted", quoteResponse.payload.quotes.get(0).quoteStatus);
        } else if (!supportAnyTransportType) {
            String[] supportedTransportTypesForSpecificDeliveryProvider = UseParsers.extractAJsonArrayFromJsonFile("src/test/resources/misc/deliveryProviderProperties.json", quoteRequest.deliveryProvider.get(0) + ".availableTransportTypes").split(",", -1);
            for (int i = 0; i < supportedTransportTypesForSpecificDeliveryProvider.length; i++) {
                supportedTransportTypesForSpecificDeliveryProvider[i] = supportedTransportTypesForSpecificDeliveryProvider[i].replace("[", "");
                supportedTransportTypesForSpecificDeliveryProvider[i] = supportedTransportTypesForSpecificDeliveryProvider[i].replace("]", "");
                supportedTransportTypesForSpecificDeliveryProvider[i] = supportedTransportTypesForSpecificDeliveryProvider[i].replace("\"", "");
            }
            for (int t = 0; t < quoteRequest.transportType.size(); t++) {
                for (String supportedTransportType : supportedTransportTypesForSpecificDeliveryProvider) {
                    if (quoteRequest.transportType.get(t).equals(supportedTransportType)) {
                        assertEquals("quoted", quoteResponse.payload.quotes.get(t).quoteStatus);
                        supportedTransportTypeIdentifiedQuoteObjectNumber = t;
                    }
                }
                while (t != supportedTransportTypeIdentifiedQuoteObjectNumber) {
                    assertEquals("error", quoteResponse.payload.quotes.get(t).quoteStatus);
                }
            }
        }


        for (int i = 0; i < quoteRequest.tasks.size(); i++) {
            int finalI = i;


            assertAll("Comparison of Tasks members",

                    () -> {
                        if (quoteRequest.tasks.get(finalI).transportType != null) {
                            assertAll("Tasks.transportType checks",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).transportType.size(), quoteResponse.payload.tasks.get(finalI).transportType.size()),
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).transportType, quoteResponse.payload.tasks.get(finalI).transportType)

                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).tags != null) {
                            assertAll("Tasks.tags checks",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).tags.size(), quoteResponse.payload.tasks.get(finalI).tags.size()),
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).tags, quoteResponse.payload.tasks.get(finalI).tags)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).flags != null) {
                            assertAll("Tasks.flags checks",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).flags.size(), quoteResponse.payload.tasks.get(finalI).flags.size()),
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).flags, quoteResponse.payload.tasks.get(finalI).flags)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).labels != null) {
                            assertAll("Tasks.labels checks",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).labels.size(), quoteResponse.payload.tasks.get(finalI).labels.size()),
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).labels, quoteResponse.payload.tasks.get(finalI).labels)
                            );
                        }
                    },


                    () -> assertEquals(quoteRequest.tasks.get(finalI).type, quoteResponse.payload.tasks.get(finalI).type),


                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.locationType != null) {
                            assertAll("Tasks.location.locationType Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.locationType, quoteResponse.payload.tasks.get(finalI).location.type)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.companyName != null) {
                            assertAll("Tasks.location.address.companyName Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.companyName, quoteResponse.payload.tasks.get(finalI).location.address.companyName)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.address1 != null) {
                            assertAll("Tasks.location.address.address1 Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.address1, quoteResponse.payload.tasks.get(finalI).location.address.address1)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.address2 != null) {
                            assertAll("Tasks.location.address.address2 Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.address2, quoteResponse.payload.tasks.get(finalI).location.address.address2)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.postcode != null) {
                            assertAll("Tasks.location.address.postcode Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.postcode, quoteResponse.payload.tasks.get(finalI).location.address.postcode)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.city != null) {
                            assertAll("Tasks.location.address.city Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.city, quoteResponse.payload.tasks.get(finalI).location.address.city)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.county != null) {
                            assertAll("Tasks.location.address.county Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.county, quoteResponse.payload.tasks.get(finalI).location.address.county)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.country != null) {
                            assertAll("Tasks.location.address.country Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.country, quoteResponse.payload.tasks.get(finalI).location.address.country)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.notes != null) {
                            assertAll("Tasks.location.address.notes Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.notes, quoteResponse.payload.tasks.get(finalI).location.address.notes)
                            );
                        }
                    },


                    () -> {
                        if (quoteRequest.tasks.get(finalI).requestedWindow != null) {
                            if (quoteRequest.tasks.get(finalI).type.equals("pickup") && quoteRequest.tasks.get(finalI).requestedWindow.from != null
                                    && quoteRequest.tasks.get(finalI).requestedWindow.to != null) {
                                assertAll("Comparison of ProcessedDates parameters under payload",
                                        () -> assertEquals(DateUtils.truncate(quoteRequest.tasks.get(finalI).requestedWindow.from, Calendar.SECOND), DateUtils.truncate(quoteResponse.payload.processedDates.requestedPickupStart, Calendar.SECOND)),
                                        () -> assertEquals(DateUtils.truncate(quoteRequest.tasks.get(finalI).requestedWindow.to, Calendar.SECOND), DateUtils.truncate(quoteResponse.payload.processedDates.requestedPickupEnd, Calendar.SECOND))
                                );
                            }
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).requestedWindow != null) {
                            if (quoteRequest.tasks.get(finalI).type.equals("delivery") && quoteRequest.tasks.get(finalI).requestedWindow.from != null
                                    && quoteRequest.tasks.get(finalI).requestedWindow.to != null) {
                                assertAll("Comparison of ProcessedDates parameters under payload",
                                        () -> assertEquals(DateUtils.truncate(quoteRequest.tasks.get(finalI).requestedWindow.from, Calendar.SECOND), quoteResponse.payload.processedDates.requestedDeliveryStart),
                                        () -> assertEquals(DateUtils.truncate(quoteRequest.tasks.get(finalI).requestedWindow.to, Calendar.SECOND), quoteResponse.payload.processedDates.requestedDeliveryEnd)
                                );
                            }
                        }
                    },


                    () -> assertAll("Comparison of the parameters other than Tasks json array members and except Quotes json array",
                            () -> assertEquals(201, quoteResponse.info.status),
                            () -> assertEquals("POST", quoteResponse.info.method),
                            () -> assertEquals(false, quoteResponse.info.error),
                            () -> assertEquals(0, quoteResponse.info.errorMessages.size()),
                            () -> assertEquals(quoteRequest.clientRef, quoteResponse.payload.clientRef),
                            () -> assertEquals("quoted", quoteResponse.payload.status),
                            () -> assertEquals(quoteRequest.callbackUrl, quoteResponse.payload.callbackUrl)
                    )
            );


            if (quoteRequest.tasks.get(finalI).location.persons != null) {
                assertAll("Tasks.location.persons Checks",
                        () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.size(), quoteResponse.payload.tasks.get(finalI).location.persons.size())
                );
                for (int j = 0; j < quoteRequest.tasks.get(finalI).location.persons.size(); j++) {
                    int finalJ = j;
                    assertAll("Comparison of Persons parameters under Location",
                            () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).firstName, quoteResponse.payload.tasks.get(finalI).location.persons.get(finalJ).firstName),
                            () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).lastName, quoteResponse.payload.tasks.get(finalI).location.persons.get(finalJ).lastName),
                            () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).phone, quoteResponse.payload.tasks.get(finalI).location.persons.get(finalJ).phone),
                            () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).email, quoteResponse.payload.tasks.get(finalI).location.persons.get(finalJ).email)
                    );
                    if (quoteRequest.tasks.get(finalI).location.persons.get(finalJ).notes != null) {
                        assertAll("Tasks.location.persons Checks",
                                () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).notes, quoteResponse.payload.tasks.get(finalI).location.persons.get(finalJ).notes)
                        );
                    }
                }
            }

            if (quoteRequest.tasks.get(finalI).items != null) {
                assertAll("Tasks.items Checks",
                        () -> assertEquals(quoteRequest.tasks.get(finalI).items.size(), quoteResponse.payload.tasks.get(finalI).items.size())
                );
                for (int k = 0; k < quoteRequest.tasks.get(finalI).items.size(); k++) {
                    int finalK = k;
                    assertAll("Comparison of Items parameters",
                            () -> assertEquals(quoteRequest.tasks.get(finalI).items.get(finalK).ref, quoteResponse.payload.tasks.get(finalI).items.get(finalK).ref),
                            () -> assertEquals(quoteRequest.tasks.get(finalI).items.get(finalK).pckg.size, quoteResponse.payload.tasks.get(finalI).items.get(finalK).pckg.size)
                    );
                    if (quoteRequest.tasks.get(finalI).items.get(finalK).notes != null) {
                        assertAll("Tasks.items.package.notes Checks",
                                () -> assertEquals(quoteRequest.tasks.get(finalI).items.get(finalK).notes, quoteResponse.payload.tasks.get(finalI).items.get(finalK).notes)
                        );
                    }
                }
            }
        }
    }

    @And("Request to book")
    public void requestToBook() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        BookRequest bookRequest = JacksonObjectMapper.mapJsonFileToObject(BookRequest.class, "src/test/java/apicalls/payloads/book/Book.json");
        bookRequest.jobId = jobId;
        bookRequest.quoteId = quoteIds.get(0);  //soon to be parametrized as 'i' when multiple drop
        String postingString = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(bookRequest)));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_BookURL"), postingString, headers, 20000));
        clientResponse = response;
    }

    @And("Book Response: Http response status code should be {int}")
    public void bookHttpResponseStatusCodeShouldBe(int arg0) throws IOException {
        if (clientResponse instanceof HttpResponse) {
            apacheHttpClientResponse = (HttpResponse) clientResponse;
            responseBody = EntityUtils.toString(apacheHttpClientResponse.getEntity(), StandardCharsets.UTF_8);
            System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + " Response body: " + responseBody);


            assertEquals(201, apacheHttpClientResponse.getStatusLine().getStatusCode());


            quoteResponse = JacksonObjectMapper.mapJsonStringToObject(responseBody, QuoteResponse.class);
            assert quoteResponse != null;

            jobId = quoteResponse.payload.id;
            Logger.getLogger(this.getClass()).info("Job Id: " + jobId);

            for (int l = 0; l < quoteResponse.payload.quotes.size(); l++) {
                quoteIds.add(quoteResponse.payload.quotes.get(l).id);
            }

            uiDate = quoteResponse.payload.processedDates.requestedDeliveryStart;
            if (uiDate == null) {
                uiDate = quoteResponse.payload.processedDates.requestedPickupStart;
            }
            if (uiDate == null) {
                uiDate = quoteResponse.payload.processedDates.expectedJobPickup;
            }
        }


        if (clientResponse instanceof Response) {
            restAssuredResponse = (Response) clientResponse;
            assertEquals(201, restAssuredResponse.statusCode());


            responseBody = restAssuredResponse.getBody().asString();

            quoteResponse = JacksonObjectMapper.mapJsonStringToObject(responseBody, QuoteResponse.class);
            assert quoteResponse != null;

            jobId = quoteResponse.payload.id;
            Logger.getLogger(this.getClass()).info("Job Id: " + jobId);

            for (int l = 0; l < quoteResponse.payload.quotes.size(); l++) {
                quoteIds.add(quoteResponse.payload.quotes.get(l).id);
            }

            uiDate = quoteResponse.payload.processedDates.requestedDeliveryStart;
            if (uiDate == null) {
                uiDate = quoteResponse.payload.processedDates.requestedPickupStart;
            }
            if (uiDate == null) {
                uiDate = quoteResponse.payload.processedDates.expectedJobPickup;
            }
        }
    }


    @And("Log in to the UI with following credentials")
    public void logInToTheUIWithFollowingCredentialsAndVerifyTheQuotedJobIsListedOnTheUI(DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        ndsHomePage.navigateToHomePage();
        ndsHomePage.login(rows.get(1).get(0), rows.get(1).get(1));

    }

    @And("Book Response: Check response parameters")
    public void bookResponseParameterInsideBodyAndParameterInsideBodyShouldBe() throws IOException {
        bookResponse = JacksonObjectMapper.mapJsonStringToObject(responseBody, BookResponse.class);
        assert bookResponse != null;


        assertEquals(quoteRequest.tasks.size(), bookResponse.payload.tasks.size());

        int supportedTransportTypeIdentifiedQuoteObjectNumber = 0;
        boolean supportAnyTransportType = false;
        String[] deliveryProvidersThatSupportAnyTypeOfTransportType = UseParsers.extractAJsonArrayFromJsonFile("src/test/resources/misc/deliveryProviderProperties.json", "DeliveryProvidersThatSupportAnyTypeOfTransportType").split(",", -1);
        for (int i = 0; i < deliveryProvidersThatSupportAnyTypeOfTransportType.length; i++) {
            deliveryProvidersThatSupportAnyTypeOfTransportType[i] = deliveryProvidersThatSupportAnyTypeOfTransportType[i].replace("[", "");
            deliveryProvidersThatSupportAnyTypeOfTransportType[i] = deliveryProvidersThatSupportAnyTypeOfTransportType[i].replace("]", "");
            deliveryProvidersThatSupportAnyTypeOfTransportType[i] = deliveryProvidersThatSupportAnyTypeOfTransportType[i].replace("\"", "");
        }
        boolean isTransportTypeSupported = false;

        for (String deliveryProvider : deliveryProvidersThatSupportAnyTypeOfTransportType) {
            if (quoteRequest.deliveryProvider.get(0).equals(deliveryProvider)) {
                supportAnyTransportType = true;
            }
        }

        if (supportAnyTransportType) {
            assertEquals(1, bookResponse.payload.quotes.size());
            assertEquals("any", bookResponse.payload.quotes.get(0).transportTypes.get(0));
            assertEquals("booked", bookResponse.payload.quotes.get(0).quoteStatus);
        } else if (!supportAnyTransportType) {
            String[] supportedTransportTypesForSpecificDeliveryProvider = UseParsers.extractAJsonArrayFromJsonFile("src/test/resources/misc/deliveryProviderProperties.json", quoteRequest.deliveryProvider.get(0) + ".availableTransportTypes").split(",", -1);
            for (int i = 0; i < supportedTransportTypesForSpecificDeliveryProvider.length; i++) {
                supportedTransportTypesForSpecificDeliveryProvider[i] = supportedTransportTypesForSpecificDeliveryProvider[i].replace("[", "");
                supportedTransportTypesForSpecificDeliveryProvider[i] = supportedTransportTypesForSpecificDeliveryProvider[i].replace("]", "");
                supportedTransportTypesForSpecificDeliveryProvider[i] = supportedTransportTypesForSpecificDeliveryProvider[i].replace("\"", "");
            }
            for (int t = 0; t < quoteRequest.transportType.size(); t++) {
                for (String supportedTransportType : supportedTransportTypesForSpecificDeliveryProvider) {
                    if (quoteRequest.transportType.get(t).equals(supportedTransportType)) {
                        assertEquals("booked", bookResponse.payload.quotes.get(t).quoteStatus);
                        supportedTransportTypeIdentifiedQuoteObjectNumber = t;
                    }
                }
                while (t != supportedTransportTypeIdentifiedQuoteObjectNumber) {
                    assertEquals("error", bookResponse.payload.quotes.get(t).quoteStatus);
                }
            }
        }


        for (int i = 0; i < quoteRequest.tasks.size(); i++) {
            int finalI = i;


            assertAll("Comparison of Tasks members",

                    () -> {
                        if (quoteRequest.tasks.get(finalI).transportType != null) {
                            assertAll("Tasks.transportType checks",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).transportType.size(), bookResponse.payload.tasks.get(finalI).transportType.size()),
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).transportType, bookResponse.payload.tasks.get(finalI).transportType)

                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).tags != null) {
                            assertAll("Tasks.tags checks",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).tags.size(), bookResponse.payload.tasks.get(finalI).tags.size()),
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).tags, bookResponse.payload.tasks.get(finalI).tags)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).flags != null) {
                            assertAll("Tasks.flags checks",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).flags.size(), bookResponse.payload.tasks.get(finalI).flags.size()),
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).flags, bookResponse.payload.tasks.get(finalI).flags)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).labels != null) {
                            assertAll("Tasks.labels checks",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).labels.size(), bookResponse.payload.tasks.get(finalI).labels.size()),
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).labels, bookResponse.payload.tasks.get(finalI).labels)
                            );
                        }
                    },


                    () -> assertEquals(quoteRequest.tasks.get(finalI).type, bookResponse.payload.tasks.get(finalI).type),


                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.locationType != null) {
                            assertAll("Tasks.location.locationType Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.locationType, bookResponse.payload.tasks.get(finalI).location.type)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.companyName != null) {
                            assertAll("Tasks.location.address.companyName Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.companyName, bookResponse.payload.tasks.get(finalI).location.address.companyName)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.address1 != null) {
                            assertAll("Tasks.location.address.address1 Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.address1, bookResponse.payload.tasks.get(finalI).location.address.address1)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.address2 != null) {
                            assertAll("Tasks.location.address.address2 Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.address2, bookResponse.payload.tasks.get(finalI).location.address.address2)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.postcode != null) {
                            assertAll("Tasks.location.address.postcode Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.postcode, bookResponse.payload.tasks.get(finalI).location.address.postcode)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.city != null) {
                            assertAll("Tasks.location.address.city Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.city, bookResponse.payload.tasks.get(finalI).location.address.city)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.county != null) {
                            assertAll("Tasks.location.address.county Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.county, bookResponse.payload.tasks.get(finalI).location.address.county)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.country != null) {
                            assertAll("Tasks.location.address.country Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.country, bookResponse.payload.tasks.get(finalI).location.address.country)
                            );
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).location.address.notes != null) {
                            assertAll("Tasks.location.address.notes Check",
                                    () -> assertEquals(quoteRequest.tasks.get(finalI).location.address.notes, bookResponse.payload.tasks.get(finalI).location.address.notes)
                            );
                        }
                    },


                    () -> {
                        if (quoteRequest.tasks.get(finalI).requestedWindow != null) {
                            if (quoteRequest.tasks.get(finalI).type.equals("pickup") && quoteRequest.tasks.get(finalI).requestedWindow.from != null
                                    && quoteRequest.tasks.get(finalI).requestedWindow.to != null) {
                                assertAll("Comparison of ProcessedDates parameters under payload",
                                        () -> assertEquals(DateUtils.truncate(quoteRequest.tasks.get(finalI).requestedWindow.from, Calendar.SECOND), DateUtils.truncate(bookResponse.payload.processedDates.requestedPickupStart, Calendar.SECOND)),
                                        () -> assertEquals(DateUtils.truncate(quoteRequest.tasks.get(finalI).requestedWindow.to, Calendar.SECOND), DateUtils.truncate(bookResponse.payload.processedDates.requestedPickupEnd, Calendar.SECOND))
                                );
                            }
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).requestedWindow != null) {
                            if (quoteRequest.tasks.get(finalI).type.equals("delivery") && quoteRequest.tasks.get(finalI).requestedWindow.from != null
                                    && quoteRequest.tasks.get(finalI).requestedWindow.to != null) {
                                assertAll("Comparison of ProcessedDates parameters under payload",
                                        () -> assertEquals(DateUtils.truncate(quoteRequest.tasks.get(finalI).requestedWindow.from, Calendar.SECOND), bookResponse.payload.processedDates.requestedDeliveryStart),
                                        () -> assertEquals(DateUtils.truncate(quoteRequest.tasks.get(finalI).requestedWindow.to, Calendar.SECOND), bookResponse.payload.processedDates.requestedDeliveryEnd)
                                );
                            }
                        }
                    },


                    () -> assertAll("Comparison of the parameters other than Tasks json array members and except Quotes json array",
                            () -> assertEquals(201, bookResponse.info.status),
                            () -> assertEquals("POST", bookResponse.info.method),
                            () -> assertEquals(false, bookResponse.info.error),
                            () -> assertEquals(0, bookResponse.info.errorMessages.size()),
                            () -> assertEquals(quoteRequest.clientRef, bookResponse.payload.clientRef),
                            () -> assertEquals("booked", bookResponse.payload.status),
                            () -> assertEquals(quoteRequest.callbackUrl, bookResponse.payload.callbackUrl)
                    )
            );


            if (quoteRequest.tasks.get(finalI).location.persons != null) {
                assertAll("Tasks.location.persons Checks",
                        () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.size(), bookResponse.payload.tasks.get(finalI).location.persons.size())
                );
                for (int j = 0; j < quoteRequest.tasks.get(finalI).location.persons.size(); j++) {
                    int finalJ = j;
                    assertAll("Comparison of Persons parameters under Location",
                            () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).firstName, bookResponse.payload.tasks.get(finalI).location.persons.get(finalJ).firstName),
                            () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).lastName, bookResponse.payload.tasks.get(finalI).location.persons.get(finalJ).lastName),
                            () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).phone, bookResponse.payload.tasks.get(finalI).location.persons.get(finalJ).phone),
                            () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).email, bookResponse.payload.tasks.get(finalI).location.persons.get(finalJ).email)
                    );
                    if (quoteRequest.tasks.get(finalI).location.persons.get(finalJ).notes != null) {
                        assertAll("Tasks.location.notes Check",
                                () -> assertEquals(quoteRequest.tasks.get(finalI).location.persons.get(finalJ).notes, bookResponse.payload.tasks.get(finalI).location.persons.get(finalJ).notes)
                        );
                    }
                }
            }


            if (quoteRequest.tasks.get(finalI).items != null) {
                assertAll("Tasks.items Checks",
                        () -> assertEquals(quoteRequest.tasks.get(finalI).items.size(), bookResponse.payload.tasks.get(finalI).items.size())
                );
                for (int k = 0; k < quoteRequest.tasks.get(finalI).items.size(); k++) {
                    int finalK = k;
                    assertAll("Comparison of Items parameters",
                            () -> assertEquals(quoteRequest.tasks.get(finalI).items.get(finalK).ref, bookResponse.payload.tasks.get(finalI).items.get(finalK).ref),
                            () -> assertEquals(quoteRequest.tasks.get(finalI).items.get(finalK).pckg.size, bookResponse.payload.tasks.get(finalI).items.get(finalK).pckg.size)
                    );
                    if (quoteRequest.tasks.get(finalI).items.get(finalK).notes != null) {
                        assertAll("Tasks.items.package.notes Check",
                                () -> assertEquals(quoteRequest.tasks.get(finalI).items.get(finalK).notes, bookResponse.payload.tasks.get(finalI).items.get(finalK).notes)
                        );
                    }
                }
            }
        }
    }

    @And("Verify the quoted job is listed on the UI")
    public void verifyTheQuotedJobIsListedOnTheUI() throws Exception {
        ndsHomePage.refreshTableData();
        Helper.waitForJavascriptToLoad(15000, 50);
        Helper.waitForJavascriptToLoad(DriverManager.getDriver());
        try {
            assertEquals(String.valueOf(jobId), ndsHomePage.getASpecificJobIdTextFromJobsTable(String.valueOf(jobId)));
        } catch (Exception e) {
            ndsHomePage.goToDateWhereJobSupposedToBeIn(uiDate);
            ndsHomePage.waitUntilTableHeaderIsVisible();
            assertEquals(String.valueOf(jobId), ndsHomePage.getASpecificJobIdTextFromJobsTable(String.valueOf(jobId)));
        }
    }

    @And("Verify the status of the quoted job is {string} on the UI")
    public void verifyTheStatusOfTheQuotedJobIsOnTheUI(String arg0) throws InterruptedException {
        String jobStatus;
        long endWaitTime = System.currentTimeMillis() + 35 * 1000;  //35 seconds
        boolean isConditionMet = false;
        switch (arg0) {
            case "Quoted":
                jobStatus =
                        ndsHomePage.getJobStatusAccordingToRowNumber(ndsHomePage.locateTheRowThatASpecificJobIdIsIn(String.valueOf(jobId)));
                assertEquals("Quoted", jobStatus);
                break;
            case "Booked":
                ndsHomePage.refreshTableData();
                jobStatus =
                        ndsHomePage.getJobStatusAccordingToRowNumber(ndsHomePage.locateTheRowThatASpecificJobIdIsIn(String.valueOf(jobId)));

                while (System.currentTimeMillis() < endWaitTime && !isConditionMet) {
                    isConditionMet = jobStatus.equals("Booked");
                    if (isConditionMet) {
                        break;
                    } else {
                        Thread.sleep(100);
                        jobStatus =
                                ndsHomePage.getJobStatusAccordingToRowNumber(ndsHomePage.locateTheRowThatASpecificJobIdIsIn(String.valueOf(jobId)));
                    }
                }
                assertEquals("Booked", jobStatus);
                break;
            case "Cancelled":
                ndsHomePage.refreshTableData();
                jobStatus =
                        ndsHomePage.getJobStatusAccordingToRowNumber(ndsHomePage.locateTheRowThatASpecificJobIdIsIn(String.valueOf(jobId)));

                while (System.currentTimeMillis() < endWaitTime && !isConditionMet) {
                    isConditionMet = jobStatus.equals("Cancelled");
                    if (isConditionMet) {
                        break;
                    } else {
                        Thread.sleep(500);
                        jobStatus =
                                ndsHomePage.getJobStatusAccordingToRowNumber(ndsHomePage.locateTheRowThatASpecificJobIdIsIn(String.valueOf(jobId)));
                    }
                }
                assertEquals("Cancelled", jobStatus);
                break;
        }
    }

    @And("Request to cancel the job")
    public void requestToCancelTheJob() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.PUT.putUsingJsonAsStringOrEmptyBody(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_CancelBookingBaseURL"), "/v2/job/cancel/" + jobId, null, headers, 10000));
        clientResponse = response;
    }

    @And("Cancel Booking Response: Http response status code should be {int}")
    public void cancelBookingResponseHttpResponseStatusCodeShouldBe(int arg0) throws IOException {
        if (clientResponse instanceof HttpResponse) {
            apacheHttpClientResponse = (HttpResponse) clientResponse;
            responseBody = EntityUtils.toString(apacheHttpClientResponse.getEntity(), StandardCharsets.UTF_8);
            System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + " Response body: " + responseBody);


            assertEquals(200, apacheHttpClientResponse.getStatusLine().getStatusCode());
        }


        if (clientResponse instanceof Response) {
            restAssuredResponse = (Response) clientResponse;

            assertEquals(200, restAssuredResponse.statusCode());
        }
    }

    @And("Take an authorization token and request to Quote for Single ASAP Pickup")
    public void takeAnAuthorizationTokenAndRequestToQuoteForSingleASAPPickup() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/quote/SingleASAPPickup.json");
        String postingString = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(quoteRequest)));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_QuoteURL"), postingString, headers, 10000));
        clientResponse = response;
    }

    @And("Take an authorization token and request to Quote for Single Scheduled Same Day Delivery")
    public void takeAnAuthorizationTokenAndRequestToQuoteForSingleScheduledSameDayDelivery() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/quote/SingleScheduledSameDayDelivery.json");
        for (int i = 0; i < quoteRequest.tasks.size(); i++) {
            if (quoteRequest.tasks.get(i).requestedWindow != null) {
                quoteRequest.tasks.get(i).requestedWindow.from = DateTimeUtil.addHoursToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 1);
                quoteRequest.tasks.get(i).requestedWindow.to = DateTimeUtil.addHoursToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 4);
            }
        }
        String postingString = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(quoteRequest)));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_QuoteURL"), postingString, headers, 10000));
        clientResponse = response;
    }

    @And("Take an authorization token and request to Quote for Single Scheduled Delivery Windows - Same Day")
    public void takeAnAuthorizationTokenAndRequestToQuoteForSingleScheduledDeliveryWindowsSameDay() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/quote/SingleScheduledDeliveryWindowsSameDay.json");
        for (int i = 0; i < quoteRequest.tasks.size(); i++) {
            if (quoteRequest.tasks.get(i).requestedWindow != null) {
                quoteRequest.tasks.get(i).requestedWindow.from = DateTimeUtil.addHoursToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 1);
                quoteRequest.tasks.get(i).requestedWindow.to = DateTimeUtil.addHoursToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 4);
            }
        }
        String postingString = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(quoteRequest)));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_QuoteURL"), postingString, headers, 10000));
        clientResponse = response;
    }

    @And("Take an authorization token and request to Quote for Single Scheduled Delivery Windows - Any Other Day")
    public void takeAnAuthorizationTokenAndRequestToQuoteForSingleScheduledDeliveryWindowsAnyOtherDay() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/quote/SingleScheduledDeliveryWindowsAnyOtherDay.json");
        for (int i = 0; i < quoteRequest.tasks.size(); i++) {
            if (quoteRequest.tasks.get(i).requestedWindow != null) {
                quoteRequest.tasks.get(i).requestedWindow.from = DateTimeUtil.addDaysToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 1);
                quoteRequest.tasks.get(i).requestedWindow.to = DateTimeUtil.addDaysToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 2);
            }
        }
        String postingString = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(quoteRequest)));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_QuoteURL"), postingString, headers, 10000));
        clientResponse = response;
    }


    @And("Take an authorization token and request to Book for Single Drop - Immediate Booking, Quote & Book - ASAP")
    public void takeAnAuthorizationTokenAndRequestToBookForSingleDropImmediateBookingQuoteBookASAP() throws Exception {
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/book/ImmediateBookingQuoteBookAsap.json");
        RestAssured.baseURI = "https://api2-test.noqu.delivery";
        RestAssured.basePath = "/v2";

        Response response =
                given().
                        contentType(ContentType.JSON).
                        header("Authorization", UseBearerToken.INSTANCE.getBearerToken()).
                        body(quoteRequest).
                        when().
                        log().body().
                        post("/nds/immediateBooking").
                        then().
                        log().body().
                        extract().response();

        clientResponse = response;
    }

    @And("Take an authorization token and request to Book for Single Drop - Immediate Booking, Quote & Book - Scheduled today")
    public void takeAnAuthorizationTokenAndRequestToBookForSingleDropImmediateBookingQuoteBookScheduledToday() throws Exception {
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/book/ImmediateBookingQuoteBookScheduledToday.json");
        for (int i = 0; i < quoteRequest.tasks.size(); i++) {
            if (quoteRequest.tasks.get(i).requestedWindow != null) {
                quoteRequest.tasks.get(i).requestedWindow.to = DateTimeUtil.addHoursToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 1);
                quoteRequest.tasks.get(i).requestedWindow.from = DateTimeUtil.addHoursToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 4);
            }
        }
        RestAssured.baseURI = "https://api2-test.noqu.delivery";
        RestAssured.basePath = "/v2";

        Response response =
                given().
                        contentType(ContentType.JSON).
                        header("Authorization", UseBearerToken.INSTANCE.getBearerToken()).
                        body(quoteRequest).
                        when().
                        log().body().
                        post("/nds/immediateBooking").
                        then().
                        log().body().
                        extract().response();

        clientResponse = response;
    }

    @And("Take an authorization token and request to Book for Single Drop - Immediate Booking, Quote & Book - Scheduled Any Other Day")
    public void takeAnAuthorizationTokenAndRequestToBookForSingleDropImmediateBookingQuoteBookScheduledAnyOtherDay() throws Exception {
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/book/ImmediateBookingQuoteBookScheduledAnyOtherDay.json");
        for (int i = 0; i < quoteRequest.tasks.size(); i++) {
            if (quoteRequest.tasks.get(i).requestedWindow != null) {
                quoteRequest.tasks.get(i).requestedWindow.from = DateTimeUtil.addDaysToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 1);
                quoteRequest.tasks.get(i).requestedWindow.to = DateTimeUtil.addDaysToDate(DateTimeUtil.getCurrentLocalTimeAccordingToZoneIdAndPattern("Europe/London", "yyyy-MM-dd'T'HH:mm:ss"), 2);
            }
        }
        RestAssured.baseURI = "https://api2-test.noqu.delivery";
        RestAssured.basePath = "/v2";

        Response response =
                given().
                        contentType(ContentType.JSON).
                        header("Authorization", UseBearerToken.INSTANCE.getBearerToken()).
                        body(quoteRequest).
                        when().
                        log().body().
                        post("/nds/immediateBooking").
                        then().
                        log().body().
                        extract().response();

        clientResponse = response;
    }

    @Given("Take an authorization token and request to Quote for Single Scheduled Same Day Pickup")
    public void takeAnAuthorizationTokenAndRequestToQuoteForSingleScheduledSameDayPickup() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/quote/SingleScheduledSameDayPickup.json");
        String postingString = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(quoteRequest)));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_QuoteURL"), postingString, headers, 10000));
        clientResponse = response;
    }

    @Given("Take an authorization token and request to Quote for Single Scheduled Any Other Day Pickup")
    public void takeAnAuthorizationTokenAndRequestToQuoteForSingleScheduledAnyOtherDayPickup() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        quoteRequest = JacksonObjectMapper.mapJsonFileToObject(QuoteRequest.class, "src/test/java/apicalls/payloads/quote/SingleScheduledAnyOtherDayPickup.json");
        String postingString = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(quoteRequest)));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_QuoteURL"), postingString, headers, 10000));
        clientResponse = response;
    }
}
