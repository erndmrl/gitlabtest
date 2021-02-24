package apicalls.responses.authentication;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {

    private String token_type;
    private int expires_in;
    private String access_token;
}
