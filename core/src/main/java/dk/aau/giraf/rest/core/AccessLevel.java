package dk.aau.giraf.rest.core;

/**
 * The access level of a resource.
 */
public enum AccessLevel {
    /**
     * Accessible by everyone, even non-users.
     */
    PUBLIC,
    /**
     * Accessible to everyone associated with the owning department.
     */
    PROTECTED,
    /**
     * Accessible only to the owning user.
     */
    PRIVATE
}
