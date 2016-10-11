package dk.aau.giraf.rest.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.aau.giraf.rest.core.authentication.PermissionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * PictoFrame are frames that should have a service, which is why Sequence and Pictogram inherits from it
 * They contain all the information which Sequence and Pictogram share, like title, owner and department etc.
 * A choice will have a list of PictoFrames rather than frames to avoid nested Choices.
 */
@Entity
@Table(name = "PictoFrame")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@PrimaryKeyJoinColumn(name = "frame_id", referencedColumnName = "id")
public abstract class PictoFrame extends Frame {
    @Column(nullable = false, unique = false)
    protected String title;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "owner_id", nullable = true)
    protected User owner;

    @Column(nullable = false)
    protected AccessLevel accessLevel;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "department_id", nullable = true)
    protected Department department;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "options")
    private Collection<Choice> optionOn;

    protected PictoFrame() {
    }

    /**
     * Constructer for PictoFrame object.
     *
     * @param title Descriptive title
     * @param accessLevel Indicates which access level is needed to retrieve
     */
    protected PictoFrame(String title, AccessLevel accessLevel) {
        this.title = title;
        this.accessLevel = accessLevel;
    }

    /**
     * Constructer for PictoFrame object.
     *
     * @param title Descriptive title
     * @param accessLevel Indicates which access level is needed to retrieve
     * @param department The only department who can use it if it is protected
     */
    public PictoFrame(String title, AccessLevel accessLevel, Department department) {
        this.title = title;
        this.accessLevel = accessLevel;
        this.department = department;
    }

    /**
     * Constructer for PictoFrame object.
     *
     * @param title Descriptive title
     * @param accessLevel Indicates which access level is needed to retrieve
     * @param department The only department who can use it if it is protected
     * @param owner The owner i.e. the only one who can use it if it is private
     */
    public PictoFrame(String title, AccessLevel accessLevel, Department department, User owner) {
        this.title = title;
        this.accessLevel = accessLevel;
        this.department = department;
        this.owner = owner;
    }

    /**
     * Constructer for PictoFrame object.
     *
     * @param title Descriptive title
     * @param accessLevel Indicates which access level is needed to retrieve
     * @param owner The owner i.e. the only one who can use it if it is private
     */
    public PictoFrame(String title, AccessLevel accessLevel, User owner) {
        this(title, accessLevel, owner.getDepartment(), owner);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonIgnore
    public void setDepartment(Department department) {
        this.department = department;
    }

    @JsonIgnore
    public void setOwner(User user) {
        this.owner = user;
    }

    @Enumerated(EnumType.ORDINAL)
    public AccessLevel getAccessLevel() {
        return this.accessLevel;
    }

    @JsonIgnore
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getTitle() {
        return this.title;
    }

    @JsonIgnore
    public Department getDepartment() {
        return this.department;
    }

    @JsonIgnore
    public User getOwner() {
        return this.owner;
    }

    /**
     * Check if a given user has permission to access the pictoFrame.
     *
     * @param user is the user
     * @return true if the user has permission
     */
    @JsonIgnore
    public boolean hasPermission(User user) {
        if (this.getAccessLevel() == AccessLevel.PUBLIC || user.hasPermission(PermissionType.SuperUser))
            return true;

        if (this.getAccessLevel() == AccessLevel.PROTECTED)
            return user.getDepartment().equals(this.getDepartment());

        if (this.getAccessLevel() == AccessLevel.PRIVATE)
            if (user.hasPermission(PermissionType.Guardian) &&
                user.getDepartment().equals(this.getOwner().getDepartment()) ||
                    user.equals(this.getOwner()))
            {
                return true;
            }
        return false;
    }

    /**
     * Removes the PictoFrame from all choices it exists on before removal.
     */
    @PreRemove
    public void removeFromChoice() {
        for (Choice choice : optionOn) {
            List<Integer> toDelete = new ArrayList<>();
            int index = 0;
            Iterator<PictoFrame> frames = choice.iterator();
            while (frames.hasNext()) {
                Frame frame = frames.next();
                if (frame.equals(this)) {
                    toDelete.add(index);
                }
                index++;
            }
            for (int reverseIndex = toDelete.size() - 1; reverseIndex >= 0; reverseIndex--) {
                choice.remove(toDelete.get(reverseIndex));
            }
        }
    }

    /**
     * Changes the pictoframe's Owner and Title to the input if they are not null.
     *
     * @param pictoFrame The new pictoframe to get values from
     */
    public void merge(PictoFrame pictoFrame) {
        if (pictoFrame.getOwner() != null)
            this.setOwner(pictoFrame.getOwner());
        if (pictoFrame.getTitle() != null)
            this.setTitle(pictoFrame.getTitle());
        if (pictoFrame.getAccessLevel() != null)
            this.setAccessLevel(pictoFrame.getAccessLevel());
    }
}
