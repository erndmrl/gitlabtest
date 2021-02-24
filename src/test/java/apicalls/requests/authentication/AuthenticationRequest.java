package apicalls.requests.authentication;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {

    private String client_id;

    private String client_secret;

    private String grant_type;

    private String scope;

}

