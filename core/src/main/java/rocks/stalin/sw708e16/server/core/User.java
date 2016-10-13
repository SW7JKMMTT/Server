package rocks.stalin.sw708e16.server.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "User")
@JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<AuthToken> authTokens;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Permission> permissions;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserIcon icon;

    protected User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.permissions = new ArrayList<>();
        this.authTokens = new ArrayList<>();
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

    /**
     * Merge two user objects.
     *
     * @param newUser the user to merge into this one
     */
    public void merge(User newUser) {
        if(newUser.getUsername() != null)
            setUsername((newUser.getUsername()));
        if(newUser.getPassword() != null)
            setPassword((newUser.getPassword()));
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
    public Collection<Permission> permissions() {
        return Collections.unmodifiableCollection(this.permissions);
    }

    public boolean hasPermission(PermissionType permission) {
        return permissions().stream().anyMatch((tperm) -> tperm.getPermission() == permission);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

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
