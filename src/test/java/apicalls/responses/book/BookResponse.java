package apicalls.responses.book;

import apicalls.responses.quote.QuoteResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class BookResponse {

    public Info info;
    public Payload payload;


    public static class ProcessedDates {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("RequestedPickupStart")
        public Date requestedPickupStart;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("RequestedPickupEnd")
        public Date requestedPickupEnd;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("RequestedDeliveryStart")
        public Date requestedDeliveryStart;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("RequestedDeliveryEnd")
        public Date requestedDeliveryEnd;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("ExpectedJobPickup")
        public Date expectedJobPickup;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("ExpectedJobComplete")
        public Date expectedJobComplete;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("ActualJobPickup")
        public Date actualJobPickup;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("ActualJobComplete")
        public Date actualJobComplete;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("RequestedTimeStart")
        public Date requestedTimeStart;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("RequestedTimeEnd")
        public Date requestedTimeEnd;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("ExpectedTimeComplete")
        public Date expectedTimeComplete;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT")
        @JsonProperty("ActualTimeComplete")
        public Date actualTimeComplete;
    }

    public static class Quote {
        @JsonProperty("Id")
        public int id;
        @JsonProperty("JobId")
        public int jobId;
        @JsonProperty("ClientId")
        public int clientId;
        @JsonProperty("EntityId")
        public int entityId;
        @JsonProperty("TransportTypes")
        public List<String> transportTypes;
        @JsonProperty("QuoteStatus")
        public String quoteStatus;
        @JsonProperty("DeliveryProviderId")
        public int deliveryProviderId;
        @JsonProperty("DeliveryProviderCurrency")
        public String deliveryProviderCurrency;
        @JsonProperty("DeliveryProviderMeasurement")
        public String deliveryProviderMeasurement;
        @JsonProperty("ProviderQuoteId")
        public String providerQuoteId;
        @JsonProperty("ProviderJobId")
        public String providerJobId;
        @JsonProperty("Distance")
        public double distance;
        @JsonProperty("FinalTotalAmount")
        public int finalTotalAmount;
        @JsonProperty("CancellationFee")
        public int cancellationFee;
        @JsonProperty("ExpireAt")
        public Date expireAt;
        @JsonProperty("BookedTime")
        public Date bookedTime;
        @JsonProperty("ProviderNotes")
        public String providerNotes;
    }

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
        @JsonProperty("Country")
        public String country;
        @JsonProperty("County")
        public String county;
        @JsonProperty("Notes")
        public String notes;
    }

    public static class Person {
        @JsonProperty("Id")
        public int id;
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

    public static class GeoLocation {
        @JsonProperty("Latitude")
        public double latitude;
        @JsonProperty("Longitude")
        public double longitude;
    }

    public static class PlusCode {
        @JsonProperty("Region")
        public String region;
        @JsonProperty("GlobalCode")
        public String globalCode;
        @JsonProperty("LocalCode")
        public String localCode;
    }

    public static class Location {
        @JsonProperty("LocationId")
        public int locationId;
        @JsonProperty("Type")
        public String type;
        @JsonProperty("Address")
        public Address address;
        @JsonProperty("Persons")
        public List<Person> persons;
        @JsonProperty("GeoLocation")
        public GeoLocation geoLocation;
        @JsonProperty("PlusCode")
        public QuoteResponse.PlusCode plusCode;
    }

    public static class Package {
        @JsonProperty("Size")
        public String size;
    }

    public static class Item {
        @JsonProperty("Id")
        public int id;
        @JsonProperty("Ref")
        public String ref;
        @JsonProperty("ItemCount")
        public int itemCount;
        @JsonProperty("Package")
        public Package pckg;
        @JsonProperty("Notes")
        public String notes;
    }

    public static class Task {
        @JsonProperty("Id")
        public int id;
        @JsonProperty("Type")
        public String type;
        @JsonProperty("TransportType")
        public List<String> transportType;
        @JsonProperty("TaskStatus")
        public String taskStatus;
        @JsonProperty("ProcessedDates")
        public ProcessedDates processedDates;
        @JsonProperty("Flags")
        public List<String> flags;
        @JsonProperty("Tags")
        public List<String> tags;
        @JsonProperty("Labels")
        public List<Object> labels;
        @JsonProperty("Location")
        public Location location;
        @JsonProperty("Notes")
        public String notes;
        @JsonProperty("Items")
        public List<Item> items;
    }

    public static class Tracker {
        @JsonProperty("Pickup")
        public String pickup;
        @JsonProperty("Delivery")
        public String delivery;
        @JsonProperty("Complete")
        public String complete;
    }

    public static class Payload {
        @JsonProperty("Id")
        public int id;
        @JsonProperty("TransactionId")
        public String transactionId;
        @JsonProperty("ExpiresAt")
        public String expiresAt;
        @JsonProperty("ClientRef")
        public String clientRef;
        @JsonProperty("EntityId")
        public int entityId;
        @JsonProperty("TransportType")
        public List<String> transportType;
        @JsonProperty("SelectedQuote")
        public int selectedQuote;
        @JsonProperty("AssignedDriver")
        public int assignedDriver;
        @JsonProperty("Status")
        public String status;
        @JsonProperty("DeliveryStatus")
        public String deliveryStatus;
        @JsonProperty("Documents")
        public List<Object> documents;
        @JsonProperty("CallbackUrl")
        public String callbackUrl;
        @JsonProperty("ProcessedDates")
        public ProcessedDates processedDates;
        @JsonProperty("Quotes")
        public List<Quote> quotes;
        @JsonProperty("Tasks")
        public List<Task> tasks;
        @JsonProperty("Tracker")
        public Tracker tracker;
    }

    public static class Info {
        @JsonProperty("status")
        public int status;
        @JsonProperty("requestId")
        public String requestId;
        @JsonProperty("method")
        public String method;
        @JsonProperty("endpoint")
        public String endpoint;
        @JsonProperty("error")
        public boolean error;
        @JsonProperty("errorMessages")
        public List<Object> errorMessages;
        @JsonProperty("timestamp")
        public int timestamp;
        @JsonProperty("timezone")
        public String timezone;
        @JsonProperty("timing")
        public double timing;
        @JsonProperty("rows")
        public int rows;
    }

}
