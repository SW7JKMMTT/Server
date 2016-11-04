package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.persistence.AuthDao;

public class GivenAuthToken {
    private User user;
    private String token;

    public GivenAuthToken forUser(User user) {
        this.user = user;
        return this;
    }

    public GivenAuthToken withToken(String token) {
        this.token = token;
        return this;
    }

    /**
     * Create {@link AuthToken AuthToken} and insert into the DAO.
     * @param authTokenDao The {@link AuthDao AuthDao} to insert the object into
     * @return The {@link AuthToken AuthToken} instance
     */
    public AuthToken in(AuthDao authTokenDao) {
        AuthToken token = new AuthToken(this.token, user);
        authTokenDao.add(token);
        user.addToken(token);
        return token;
    }
}
