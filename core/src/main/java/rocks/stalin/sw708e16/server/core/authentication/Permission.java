package rocks.stalin.sw708e16.server.core.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import rocks.stalin.sw708e16.server.core.User;

import javax.persistence.*;

@Entity
@Table(name = "Permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PermissionType permission;

    protected Permission() {
    }

    public Permission(User user, PermissionType permission) {
        this.user = user;
        this.permission = permission;
    }

    public long getId() {
        return id;
    }

    public PermissionType getPermission() {
        return permission;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        return permission.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;

        Permission that = (Permission)obj;

        return permission == that.permission;
    }
}
