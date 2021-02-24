package apicalls.requests.book;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookRequest {

    @JsonProperty("JobId")
    public int jobId;

    @JsonProperty("QuoteId")
    public int quoteId;

    @JsonProperty("SimulateJob")
    public boolean simulateJob;

}
