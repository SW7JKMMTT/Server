package dk.aau.giraf.rest.core.weekschedule;

import dk.aau.giraf.rest.core.User;

import javax.persistence.*;

/**
 * WeekdayFrameProgress is the relation between a WeekdayFrame and a User, it has additional information (progress).
 */
@Entity
@Table(name = "WeekdayFrameProgress")
public class WeekdayFrameProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "weekdayFrame_id", nullable = false)
    private WeekdayFrame weekdayFrame;

    @Column(name = "progress")
    private Progress progress;

    protected WeekdayFrameProgress() {
    }

    /**
     * Constructor for a weekdayprogress.
     *
     * @param user user to give the progress
     * @param weekdayFrame the frame in the weekday to add the progress to
     * @param progress progress to give
     */
    public WeekdayFrameProgress(User user, WeekdayFrame weekdayFrame, Progress progress) {
        this.user = user;
        this.weekdayFrame = weekdayFrame;
        this.progress = progress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WeekdayFrame getWeekdayFrame() {
        return weekdayFrame;
    }

    public void setWeekdayFrame(WeekdayFrame weekdayFrame) {
        this.weekdayFrame = weekdayFrame;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
