package rocks.stalin.sw708e16.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.persistence.AuthDao;
import rocks.stalin.sw708e16.server.persistence.UserDao;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.util.Collection;
import java.util.List;

@Component
@Path("/auth")
@Transactional
public class AuthenticationService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthDao authDao;

    /**
     * Authenticate as a user, retrieving an authentication token that can be used in future
     * requests. Authentication tokens should be provided in the "Authorization" HTTP header
     * in the format "Sleepy token='token'".
     *
     * @param credentials The login credentials (username and password)
     * @return A new {@link AuthToken token} for the user
     *
     * @HTTP 401 Wrong username or password
     */
    @Path("/")
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public AuthToken authenticate(UserCredentials credentials) {
        if(credentials.getUsername() == null || credentials.getPassword() == null)
            throw new BadRequestException("Missing username or password");

        User user = userDao.byUsernameAndPassword_ForLogin(credentials.getUsername(), credentials.getPassword());
        if(user == null)
            throw new NotAuthorizedException("Wrong username or password", "Sleepy");

        AuthToken token = new AuthToken(user);
        authDao.add(token);
        return token;
    }

    /**
     * Get the {@link AuthToken tokens} for the currently authenticated user.
     *
     * @param user The {@link User user} of the current request.
     * @return A collection of the {@link AuthToken tokens} of the user.
     *
     * @HTTP 401 User isn't authorized
     */
    @Path("/")
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @PermitAll()
    public Collection<AuthToken> listTokens(@Context User user) {
        if(user == null)
            throw new NotAuthorizedException("Unknown user");

        List<AuthToken> tokens = authDao.byUserId_ForDisplay(user.getId());

        return tokens;
    }

    /**
     * Credentials of a system user.
     */
    public static class UserCredentials {
        private String username;
        private String password;

        public UserCredentials() {
        }

        public UserCredentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
