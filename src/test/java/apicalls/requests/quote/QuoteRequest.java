package apicalls.requests.quote;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuoteRequest {

    @JsonProperty("TransportType")
    public List<String> transportType;
    @JsonProperty("CallbackUrl")
    public String callbackUrl;
    @JsonProperty("Notes")
    public String notes;
    @JsonProperty("ClientRef")
    public String clientRef;
    @JsonProperty("Tags")
    public List<String> tags;
    @JsonProperty("DeliveryProvider")
    public List<String> deliveryProvider;
    @JsonProperty("Flags")
    public List<String> flags;
    @JsonProperty("Labels")
    public List<Object> labels;
    @JsonProperty("Tasks")
    public List<Task> tasks;
    @JsonProperty("SimulateJob")
    public boolean simulateJob;

    public List<String> getTransportType() {
        if (transportType == null)
            return new ArrayList<String>();
        else
            return transportType;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RequestedWindow {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("From")
        public Date from;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("To")
        public Date to;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Person {
        @JsonProperty("FirstName")
        public String firstName;
        @JsonProperty("LastName")
        public String lastName;
        @JsonProperty("Phone")
        public String phone;
        @JsonProperty("Email")
        public String email;
        @JsonProperty("Notes")
        public String notes;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Location {
        @JsonProperty("LocationType")
        public String locationType;
        @JsonProperty("Address")
        public Address address;
        @JsonProperty("Persons")
        public List<Person> persons;
        @JsonProperty("Keep")
        public boolean keep;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Package {
        @JsonProperty("Size")
        public String size;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Item {
        @JsonProperty("Ref")
        public String ref;
        @JsonProperty("Package")
        public Package pckg;
        @JsonProperty("Notes")
        public String notes;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Task {
        @JsonProperty("Type")
        public String type;
        @JsonProperty("RequestedWindow")
        public RequestedWindow requestedWindow;
        @JsonProperty("TransportType")
        public List<String> transportType;
        @JsonProperty("Tags")
        public List<String> tags;
        @JsonProperty("DeliveryProvider")
        public List<String> deliveryProvider;
        @JsonProperty("Flags")
        public List<String> flags;
        @JsonProperty("Notes")
        public String notes;
        @JsonProperty("Location")
        public Location location;
        @JsonProperty("Items")
        public List<Item> items;
        @JsonProperty("Labels")
        public List<Object> labels;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Address {
        @JsonProperty("CompanyName")
        public String companyName;
        @JsonProperty("Address1")
        public String address1;
        @JsonProperty("Address2")
        public String address2;
        @JsonProperty("Postcode")
        public String postcode;
        @JsonProperty("City")
        public String city;
        @JsonProperty("County")
        public String county;
        @JsonProperty("Country")
        public String country;
        @JsonProperty("Notes")
        public String notes;
    }
}
