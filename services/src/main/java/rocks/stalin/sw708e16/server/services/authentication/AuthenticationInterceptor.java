package rocks.stalin.sw708e16.server.services.authentication;

import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.springframework.stereotype.Service;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.persistence.AuthDao;
import rocks.stalin.sw708e16.server.persistence.PermissionDao;

import javax.annotation.Priority;
import javax.annotation.Resource;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Provider
@Service
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationInterceptor implements ContainerRequestFilter {
    private static Pattern authPattern = Pattern.compile("Sleepy token=(?<token>.*)");

    @Context
    private ResourceInfo resInfo;

    @Resource
    private AuthDao authDao;

    @Resource
    private PermissionDao permissionDao;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authHeader = containerRequestContext.getHeaderString("Authorization");
        if(authHeader == null) {
            return;
        }
        Matcher match = authPattern.matcher(authHeader);
        if(!match.matches()) {
            return;
        }
        String tokenstr = match.group("token");
        if(tokenstr == null) {
            return;
        }

        AuthToken atoken = authDao.byTokenStr(tokenstr);
        if(atoken == null) {
            return;
        }
        //TODO: Extract the construction
        containerRequestContext.setSecurityContext(new GSecurityContext(atoken, permissionDao));
        //Push the context, this allow the use of @Context in the method params for Users
        ResteasyProviderFactory.pushContext(User.class, atoken.getUser());
    }
}
