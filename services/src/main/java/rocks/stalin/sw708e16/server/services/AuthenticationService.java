package rocks.stalin.sw708e16.server.services;

import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.persistence.AuthDao;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.Map;

@Transactional
@Path("/auth")
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthDao authDao;

    /**
     * Authenticate as a user, retrieving an authentication token that can be used in future
     * requests. Authentication tokens should be provided in the "Authorization" HTTP header
     * in the format "Sleep token='token'". You can only authenticate in the department you
     * are associated with.
     *
     * @param obj The login credentials (username and password)
     * @return A new {@link AuthToken token} for the user
     */
    @Path("/")
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    //TODO: Simplify and improve, maybe don't use a map?
    public AuthToken authenticate(Map<String, String> obj) {
        User user = userDao.byUsernameAndPassword(obj.get("username"), obj.get("password"));
        if(user == null)
            throw new NotAuthorizedException("Wrong username or password");

        AuthToken token = new AuthToken(user);
        authDao.add(token);
        return token;
    }

    /**
     * Get the {@link AuthToken tokens} for the currently authenticated user.
     *
     * @param context The {@link SecurityContext securitycontext} of the current request.
     * @return A collection of the {@link AuthToken tokens} of the user.
     */
    @Path("/")
    @GET
    @Produces("application/json")
    @Consumes("application/json")
    @PermitAll()
    public Collection<AuthToken> listTokens(@Context SecurityContext context) {
        AuthToken token = authDao.byTokenStr(context.getAuthenticationScheme());
        if(token == null)
            throw new NotAuthorizedException("Unknown authentication token");

        return token.getUser().getAuthTokens();
    }
}
