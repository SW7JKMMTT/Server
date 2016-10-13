package rocks.stalin.sw708e16.server.services.authentication;

import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.persistence.PermissionDao;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class GSecurityContext implements SecurityContext {
    private AuthToken token;
    private PermissionDao permissionDao;

    public GSecurityContext(AuthToken token, PermissionDao permissionDao) {
        this.token = token;
        this.permissionDao = permissionDao;
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

        return permissionDao.userHasPermission(token.getUser(), type);
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
