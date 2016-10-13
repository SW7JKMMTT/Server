package rocks.stalin.sw708e16.server.persistence;

import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;

public interface PermissionDao {
    boolean userHasPermission(User user, PermissionType permission);
}
