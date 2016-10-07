package dk.aau.giraf.rest.services.authentication;

import dk.aau.giraf.rest.core.authentication.AuthToken;
import dk.aau.giraf.rest.core.authentication.PermissionType;
import dk.aau.giraf.rest.persistence.PermissionDao;

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
