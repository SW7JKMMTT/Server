package dk.aau.giraf.rest.core.weekschedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.Pictogram;
import dk.aau.giraf.rest.core.User;

import javax.persistence.*;
import java.util.*;

/**
 * Week Schedules models the week schedule and contains meta-data such as its users and the frames related to it.
 */
@Entity
@Table(name = "WeekSchedule")
public class WeekSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "thumbnail_id", nullable = false)
    private Pictogram thumbnail;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdOn;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastEdit;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "User__WeekSchedule",
            joinColumns = {
                    @JoinColumn(name = "weekSchedule_id", nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", nullable = false, updatable = false)})
    private Collection<User> users;

    @OneToMany(mappedBy = "weekSchedule", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @MapKeyEnumerated(EnumType.ORDINAL)
    @MapKeyColumn(name = "day", insertable = false, updatable = false)
    private Map<Day, Weekday> weekdays;

    /**
     * Constructor for the weekschedule.
     *
     * @param name name of the week schedule to be given
     * @param thumbnail of the week schedule
     */
    public WeekSchedule(String name, Pictogram thumbnail, Department department) {
        this();
        this.name = name;
        this.thumbnail = thumbnail;
        this.department = department;
    }

    protected WeekSchedule() {
        this.createdOn = new Date();
        this.lastEdit = new Date();
        this.weekdays = new EnumMap<>(Day.class);
        this.users = new LinkedList<>();
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pictogram getThumbnail() {
        return this.thumbnail;
    }

    public void setThumbnail(Pictogram id) {
        this.thumbnail = id;
    }

    public Date getCreatedOn() {
        return this.createdOn;
    }

    @JsonIgnore
    public void setCreateDate() {
        this.createdOn = new Date();
    }

    public Date getLastEdit() {
        return this.lastEdit;
    }

    @PrePersist
    @PreUpdate
    private void updateLastEdit() {
        this.lastEdit = new Date();
    }

    public Department getDepartment() {
        return department;
    }

    /**
     *  Adds a user to the week schedule.
     *
     * @param user user to add to list.
     * @return boolean
     */
    public boolean addUser(User user) {
        if (this.users.contains(user)) {
            return false;
        }

        return this.users.add(user);
    }

    /**
     * Remove a user from the week schedule.
     *
     * @param user user to remove
     * @return boolean status of removal
     */
    public boolean removeUser(User user) {
        if (!this.users.contains(user)) {
            return false;
        }
        return this.users.remove(user);
    }

    /**
     * Returns true if the user uses the week schedule.
     *
     * @param user user to check
     * @return boolean true/false if they are on the week schedule
     */
    public boolean hasUser(User user) {
        return this.users.contains(user);
    }

    /**
     * Gets a specific weekday, if it doesn't exist then it will be created.
     *
     * @param day day to get
     * @return the weekday of the day
     */
    public Weekday getWeekday(Day day) {
        Weekday weekday = weekdays.get(day);
        if (weekday == null) {
            weekday = new Weekday(day, this);
            weekdays.put(day, weekday);
        }

        return weekday;
    }

    /**
     * Is true if the param date is after the last edit.
     *
     * @param date timestamp to compare with.
     * @return true if the date given is after the last edit.
     */
    public boolean editedAfter(Date date) {
        return date.after(this.lastEdit);
    }
}
