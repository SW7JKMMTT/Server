package rocks.stalin.sw708e16.server.core.authentication;

/**
 * The different roles/accesslevels.
 */
public enum PermissionType {
    /**
     * Everyone should be a user. It simplifies checking if a user is logged in at all. Users can only do things that
     * impact themselves.
     */
    User(Constants.USER),
    /**
     * A SuperUser is a permission used to access management features.
     */
    SuperUser(Constants.SUPERUSER);

    private final String permissionStr;

    PermissionType(String permissionStr) {
        this.permissionStr = permissionStr;
    }

    public static PermissionType fromString(String str) {
        return valueOf(str);
    }

    //Keep this updated with the enum values
    public static class Constants {
        /**
         * User role string.
         */
        public static final String USER = "User";
        /**
         * SuperUser role string.
         */
        public static final String SUPERUSER = "SuperUser";
    }
}
