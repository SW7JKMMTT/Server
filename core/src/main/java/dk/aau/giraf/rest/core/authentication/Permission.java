package dk.aau.giraf.rest.core.authentication;

import dk.aau.giraf.rest.core.User;

import javax.persistence.*;

@Entity
@Table(name = "Permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PermissionType permission;

    public long getId() {
        return id;
    }

    public PermissionType getPermission() {
        return permission;
    }

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
