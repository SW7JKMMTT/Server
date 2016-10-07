package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.core.authentication.PermissionType;

public interface PermissionDao {
    boolean userHasPermission(User user, PermissionType permission);
}
