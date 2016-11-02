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
     * A superuser is part of the giraf team. They are allowed to do things that impact multiple departments.
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
         * SuperUser role string.
         */
        public static final String SUPERUSER = "SuperUser";
        /**
         * User role string.
         */
        public static final String USER = "User";
    }
}
