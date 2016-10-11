package dk.aau.giraf.rest.given;

import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.core.authentication.AuthToken;
import dk.aau.giraf.rest.persistence.AuthDao;
import dk.aau.giraf.rest.persistence.hibernate.AuthTokenDaoImpl;

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

    public AuthToken in(AuthDao authTokenDao) {
        AuthToken token = new AuthToken(this.token, user);
        authTokenDao.add(token);
        user.addToken(token);
        return token;
    }
}
