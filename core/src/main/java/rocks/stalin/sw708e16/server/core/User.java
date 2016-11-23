package rocks.stalin.sw708e16.server.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String givenname;

    @Column(nullable = false)
    private String surname;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<AuthToken> authTokens = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnoreProperties({"user"})
    private Collection<Permission> permissions = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserIcon icon;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Driver driver;


    protected User() {
    }

    /**
     * A user in the system.
     * @param username The username of the user
     * @param password The plaintext password of the user
     * @param givenname The given name of the user
     * @param surname The sur/family name of the user
     */
    public User(String username, String password, String givenname, String surname) {
        this.username = username;
        this.password = password;
        this.givenname = givenname;
        this.surname = surname;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonSetter
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public Collection<AuthToken> getAuthTokens() {
        return authTokens;
    }

    @JsonIgnore
    public UserIcon getIcon() {
        return icon;
    }

    @JsonIgnore
    public void setIcon(UserIcon icon) {
        this.icon = icon;
    }

    @JsonGetter
    public boolean getHasIcon() {
        return icon != null;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Add a permission to the user.
     *
     * @param permission to add
     */
    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    /**
     * Remove a permission from the user.
     *
     * @param permission to remove
     */
    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    /**
     * Get a read-only list of user permissions.
     *
     * @return A read only collection of user permissions
     */
    public Collection<Permission> getPermissions() {
        return Collections.unmodifiableCollection(this.permissions);
    }

    public boolean hasPermission(PermissionType permission) {
        return getPermissions().stream().anyMatch((tperm) -> tperm.getPermission() == permission);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;

        return getId() != null ? getId().equals(user.getId()) : user.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public void addToken(AuthToken token) {
        authTokens.add(token);
    }

    public void revokeToken(AuthToken token) {
        authTokens.remove(token);
    }
}
