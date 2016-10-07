package dk.aau.giraf.rest.services.authentication;

import dk.aau.giraf.rest.core.authentication.AuthToken;

import java.security.Principal;

public class GUser implements Principal {
    private AuthToken token;

    public GUser(AuthToken token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return token.getUser().getUsername();
    }

    public AuthToken getAuthToken() {
        return token;
    }
}
