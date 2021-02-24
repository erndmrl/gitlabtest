package apicalls.restassured.tests.authentication;

public enum UseBearerToken {

    INSTANCE;

    public String getBearerToken(){
        Authenticate authenticate = new Authenticate();
        return authenticate.getBearerToken();
    }
}
