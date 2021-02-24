package stepdefs;

import apicalls.requests.book.BookRequest;
import apicalls.requests.quote.QuoteRequest;
import apicalls.responses.book.BookResponse;
import apicalls.responses.quote.QuoteResponse;
import apicalls.restassured.tests.authentication.UseBearerToken;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import pageobjects.NDSHomePage;
import testprojectcore.core.DriverManager;
import testprojectcore.dataprovider.EnvironmentDataProvider;
import testprojectcore.dataprovider.JacksonObjectMapper;
import testprojectcore.driverutil.PageObjectFactory;
import testprojectcore.http.apachehttpclient.ApacheHttpClient;
import testprojectcore.http.apachehttpclient.HttpCallBuilder;
import testprojectcore.testcontext.TestContext;
import testprojectcore.util.Helper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParametrizedQuoteStepDefs {

    private TestContext testContext;

    private NDSHomePage ndsHomePage;

    private HttpResponse apacheHttpClientResponse;

    private String responseBody;

    private QuoteRequest quoteRequest;
    private QuoteResponse quoteResponse;
    private BookResponse bookResponse;

    private List<Integer> quoteIds = new ArrayList<>();
    private int jobId;
    private Date uiDate;


    public ParametrizedQuoteStepDefs(TestContext testContext) {
        this.testContext = testContext;
    }


    @Given("Take an authorization token, change request parameters as stated below using JsonPath then request to Quote")
    public void takeAnAuthorizationTokenChangeRequestParametersAsStatedBelowUsingJsonPathThenRequestToQuote(DataTable dataTable) throws Exception {
        final String FILE_PATH = "src/test/java/apicalls/payloads/quote/Quote.json";
        String filePath = FILE_PATH.replace('/', File.separatorChar);
        String action = "";
        String thingToRemoveOrChange = "";
        String newValue = "";

        String postingString = new String(Files.readAllBytes(Paths.get(filePath)));
        DocumentContext requestJsonPathDoc = JsonPath.parse(postingString);

        List<Map<String, String>> changeList = dataTable.asMaps();
        for (Map<String, String> row : changeList) {
            action = row.get("Action");
            thingToRemoveOrChange = row.get("Parameter to Remove or Value to Change");
            newValue = row.get("New Value");

            if (action.equalsIgnoreCase("remove")) {
                requestJsonPathDoc.delete(thingToRemoveOrChange);
            }

            if (action.equalsIgnoreCase("change")) {
                requestJsonPathDoc.set(thingToRemoveOrChange, newValue);
            }
        }

        String updatedPostingString = requestJsonPathDoc.jsonString();

        QuoteRequest quoteRequest = (JacksonObjectMapper.mapJsonStringToObject(updatedPostingString, QuoteRequest.class));
//        String stringReadyToPost = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(quoteRequest)));

        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));

        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_QuoteURL"), updatedPostingString, headers, 10000));
        apacheHttpClientResponse = response;
    }

    @And("\\(P)Quote Response: Http response status code should be {int}")
    public void pQuoteResponseHttpResponseStatusCodeShouldBe(int arg0) throws IOException {
        responseBody = EntityUtils.toString(apacheHttpClientResponse.getEntity(), StandardCharsets.UTF_8);
        System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + " Response body: " + responseBody);

        assertEquals(201, apacheHttpClientResponse.getStatusLine().getStatusCode());

        quoteResponse = JacksonObjectMapper.mapJsonStringToObject(responseBody, QuoteResponse.class);
        assert quoteResponse != null;

        jobId = quoteResponse.payload.id;

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

    @And("\\(P)Quote Response: Check response parameters")
    public void pQuoteResponseCheckResponseParameters() {


        assertAll("Size comparison of Tasks json array",
                () -> assertEquals(quoteRequest.tasks.size(), quoteResponse.payload.tasks.size())
        );

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
                                        () -> assertEquals(quoteRequest.tasks.get(finalI).requestedWindow.from, quoteResponse.payload.processedDates.requestedPickupStart),
                                        () -> assertEquals(quoteRequest.tasks.get(finalI).requestedWindow.to, quoteResponse.payload.processedDates.requestedPickupEnd)
                                );
                            }
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).requestedWindow != null) {
                            if (quoteRequest.tasks.get(finalI).type.equals("delivery") && quoteRequest.tasks.get(finalI).requestedWindow.from != null
                                    && quoteRequest.tasks.get(finalI).requestedWindow.to != null) {
                                assertAll("Comparison of ProcessedDates parameters under payload",
                                        () -> assertEquals(quoteRequest.tasks.get(finalI).requestedWindow.from, quoteResponse.payload.processedDates.requestedDeliveryStart),
                                        () -> assertEquals(quoteRequest.tasks.get(finalI).requestedWindow.to, quoteResponse.payload.processedDates.requestedDeliveryEnd)
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

    @And("\\(P)Log in to the UI with following credentials")
    public void pLogInToTheUIWithFollowingCredentials(DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);
        ndsHomePage.navigateToHomePage();
        ndsHomePage.login(rows.get(1).get(0), rows.get(1).get(1));
    }

    @And("\\(P)Verify the quoted job is listed on the UI")
    public void pVerifyTheQuotedJobIsListedOnTheUI() throws Exception {
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

    @And("\\(P)Verify the status of the quoted job is {string} on the UI")
    public void pVerifyTheStatusOfTheQuotedJobIsOnTheUI(String arg0) throws InterruptedException {
        String jobStatus =
                ndsHomePage.getJobStatusAccordingToRowNumber(ndsHomePage.locateTheRowThatASpecificJobIdIsIn(String.valueOf(jobId)));
        assertEquals("Quoted", jobStatus);
    }

    @And("\\(P)Request to book")
    public void pRequestToBook() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        BookRequest bookRequest = JacksonObjectMapper.mapJsonFileToObject(BookRequest.class, "src/test/java/apicalls/payloads/book/Book.json");
        bookRequest.jobId = jobId;
        bookRequest.quoteId = quoteIds.get(0);  //soon to be parametrized as 'i' when multiple drop
        String postingString = EntityUtils.toString(new StringEntity(JacksonObjectMapper.mapObjectToJsonAsString(bookRequest)));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.POST.postUsingJsonAsString(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_BookURL"), postingString, headers, 10000));
        apacheHttpClientResponse = response;
    }


    @And("\\(P)Book Response: Http response status code should be {int}")
    public void pBookResponseHttpResponseStatusCodeShouldBe(int arg0) throws IOException {
        responseBody = EntityUtils.toString(apacheHttpClientResponse.getEntity(), StandardCharsets.UTF_8);
        System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + " Response body: " + responseBody);
        assertEquals(201, apacheHttpClientResponse.getStatusLine().getStatusCode());
    }

    @And("\\(P)Book Response: Check response parameters")
    public void pBookResponseCheckResponseParameters() {
        bookResponse = JacksonObjectMapper.mapJsonStringToObject(responseBody, BookResponse.class);
        assert bookResponse != null;


        assertAll("Size comparison of Tasks json array",
                () -> assertEquals(quoteRequest.tasks.size(), bookResponse.payload.tasks.size())
        );

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
                                        () -> assertEquals(quoteRequest.tasks.get(finalI).requestedWindow.from, bookResponse.payload.processedDates.requestedPickupStart),
                                        () -> assertEquals(quoteRequest.tasks.get(finalI).requestedWindow.to, bookResponse.payload.processedDates.requestedPickupEnd)
                                );
                            }
                        }
                    },

                    () -> {
                        if (quoteRequest.tasks.get(finalI).requestedWindow != null) {
                            if (quoteRequest.tasks.get(finalI).type.equals("delivery") && quoteRequest.tasks.get(finalI).requestedWindow.from != null
                                    && quoteRequest.tasks.get(finalI).requestedWindow.to != null) {
                                assertAll("Comparison of ProcessedDates parameters under payload",
                                        () -> assertEquals(quoteRequest.tasks.get(finalI).requestedWindow.from, bookResponse.payload.processedDates.requestedDeliveryStart),
                                        () -> assertEquals(quoteRequest.tasks.get(finalI).requestedWindow.to, bookResponse.payload.processedDates.requestedDeliveryEnd)
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

    @And("\\(P)Request to cancel the job")
    public void pRequestToCancelTheJob() throws Exception {
        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Authorization", UseBearerToken.INSTANCE.getBearerToken()));
        HttpResponse response =
                ApacheHttpClient.sendRequest(
                        HttpCallBuilder.PUT.putUsingJsonAsStringOrEmptyBody(EnvironmentDataProvider.APPLICATION.getPropertyValue("noquapiV2_CancelBookingBaseURL"), "/v2/job/cancel/" + jobId, null, headers, 10000));
        apacheHttpClientResponse = response;
    }

    @And("\\(P)Cancel Booking Response: Http response status code should be {int}")
    public void pCancelBookingResponseHttpResponseStatusCodeShouldBe(int arg0) throws IOException {
        responseBody = EntityUtils.toString(apacheHttpClientResponse.getEntity(), StandardCharsets.UTF_8);
        System.out.println("[Thread id: " + Thread.currentThread().getId() + "] " + " Response body: " + responseBody);
        assertEquals(200, apacheHttpClientResponse.getStatusLine().getStatusCode());
    }

}
