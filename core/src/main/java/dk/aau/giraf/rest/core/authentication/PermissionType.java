package dk.aau.giraf.rest.core.authentication;

/**
 * The different roles/accesslevels in giraf.
 */
public enum PermissionType {
    /**
     * Everyone should be a user. It simplifies checking if a user is logged in at all. Users can only do things that
     * impact themselves.
     */
    User(Constants.USER),
    /**
     * Guardians are the people assigned to take care of other people. They control the data of a department. Guardians
     * can do stuff that impacts their entire department.
     */
    Guardian(Constants.GUARDIAN),
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
         * Guardian role string.
         */
        public static final String GUARDIAN = "Guardian";
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
