package dk.aau.giraf.rest.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import dk.aau.giraf.rest.core.authentication.AuthToken;
import dk.aau.giraf.rest.core.authentication.Permission;
import dk.aau.giraf.rest.core.authentication.PermissionType;
import dk.aau.giraf.rest.core.weekschedule.WeekSchedule;
import dk.aau.giraf.rest.core.weekschedule.WeekdayFrame;
import dk.aau.giraf.rest.core.weekschedule.WeekdayFrameProgress;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Entity
@Table(name = "User")
@JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ObjectId id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<AuthToken> authTokens;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Permission> permissions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Collection<PictoFrame> owned;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKeyJoinColumn(name = "weekdayframe_id", insertable = false, updatable = false)
    private Map<WeekdayFrame, WeekdayFrameProgress> userprogress;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "User__WeekSchedule",
            joinColumns = {
                    @JoinColumn(name = "user_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "weekSchedule_id", nullable = false, updatable = false)})
    private Collection<WeekSchedule> weekSchedule;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserIcon icon;

    protected User() {
    }

    public User(Department department, String username, String password) {
        this.department = department;
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
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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

    public Collection<WeekSchedule> getWeekSchedule() {
        return weekSchedule;
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
        if(newUser.getDepartment() != null)
            setDepartment(newUser.getDepartment());
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

    /**
     * Gets the WeekdayFrameProgress element for the user given a WeekdayFrame.
     *
     * @param weekdayFrame weekdayFrame to get userprogress for.
     * @return The WeekdayFrameProgress or NULL if not found.
     */
    public WeekdayFrameProgress getUserProgress(WeekdayFrame weekdayFrame) {
        return userprogress.get(weekdayFrame);
    }

    /**
     * Add or update the WeekdayFrameProgress for a given WeekdayFrame.
     *
     * @param weekdayFrameProgress WeekdayFrameProgress
     */
    public void addOrUpdateUserProgress(WeekdayFrameProgress weekdayFrameProgress) {
        userprogress.put(weekdayFrameProgress.getWeekdayFrame(), weekdayFrameProgress);
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
