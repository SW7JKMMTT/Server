package rocks.stalin.sw708e16.server.services.authentication;

import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.persistence.PermissionDao;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

@Transactional
public class GSecurityContext implements SecurityContext {
    private AuthToken token;
    private User user;

    public GSecurityContext(AuthToken token, PermissionDao permissionDao) {
        this.token = token;
        this.user = token.getUser();
    }

    @Override
    public Principal getUserPrincipal() {
        return new GUser(token);
    }

    @Override
    public boolean isUserInRole(String str) {
        PermissionType type = PermissionType.fromString(str);
        if(type == null)
            return false;

        return user.hasPermission(type);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return token.getToken();
    }
}
