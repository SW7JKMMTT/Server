package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.persistence.PermissionDao;

public class GivenPermission {
    private PermissionType type;
    private User user;

    public GivenPermission forUser(User user) {
        this.user = user;
        return this;
    }

    public GivenPermission ofType(PermissionType type) {
        this.type = type;
        return this;
    }

    /**
     * Insert permission into the DAO.
     * @param permissionDao the dao to insert the permission into
     * @return The create permission
     */
    public Permission in(PermissionDao permissionDao) {
        Permission perm = new Permission(user, type);
        user.addPermission(perm);
        permissionDao.add(perm);
        return perm;
    }
}
