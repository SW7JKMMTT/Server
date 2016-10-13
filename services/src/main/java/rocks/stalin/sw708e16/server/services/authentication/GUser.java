package rocks.stalin.sw708e16.server.services.authentication;

import rocks.stalin.sw708e16.server.core.authentication.AuthToken;

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
