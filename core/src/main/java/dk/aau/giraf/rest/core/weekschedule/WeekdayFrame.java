package dk.aau.giraf.rest.core.weekschedule;

import dk.aau.giraf.rest.core.Frame;
import dk.aau.giraf.rest.core.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A relation between Weekdays and Frames such that additional properties can be added (Index and Progress).
 */
@Entity
@Table(name = "Weekday__Frame")
public class WeekdayFrame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "weekday_id", insertable = false, updatable = false)
    private Weekday weekday;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "frame_id")
    private Frame frame;

    @Column(name = "weekdayframe_index", insertable = false, updatable = false)
    private int index;

    @OneToMany(mappedBy = "weekdayFrame")
    @MapKeyJoinColumn(name = "user_id", insertable = false, updatable = false)
    private Map<User, WeekdayFrameProgress> pictoFrameProgress;

    protected WeekdayFrame() {
        this.pictoFrameProgress = new HashMap<>();
    }

    /**
     * Constructor for a WeekdayFrame.
     *
     * @param weekday weekday to be part of.
     * @param frame frame to be part of the weekday.
     */
    public WeekdayFrame(Weekday weekday, Frame frame) {
        this();
        this.setWeekday(weekday);
        this.setFrame(frame);
    }

    /**
     * Gets the progress of a weekdayFrame for a user, defaults to NotStarted if it doesn't exist.
     *
     * @param user user for whose progress to get.
     * @return the progress for the user.
     */
    public Progress getPictoFrameProgress(User user) {
        WeekdayFrameProgress weekdayFrameProgress = this.pictoFrameProgress.get(user);
        if (weekdayFrameProgress == null)
            return null;

        return weekdayFrameProgress.getProgress();
    }

    /**
     * Sets the progress on a WeekdayFrame for a user.
     *
     * @param progress progress to set
     * @param user     user to add the progress to
     */
    public void setPictoFrameProgress(Progress progress, User user) {
        WeekdayFrameProgress weekdayFrameProgress = this.pictoFrameProgress.get(user);
        if (weekdayFrameProgress == null) {
            weekdayFrameProgress = new WeekdayFrameProgress(user, this, progress);
            this.pictoFrameProgress.put(user, weekdayFrameProgress);
            user.addOrUpdateUserProgress(weekdayFrameProgress);
        } else {
            weekdayFrameProgress.setProgress(progress);
        }
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        WeekdayFrame that = (WeekdayFrame) obj;

        if (id != that.id) return false;
        if (index != that.index) return false;
        if (weekday != null ? !weekday.equals(that.weekday) : that.weekday != null) return false;
        return frame != null ? frame.equals(that.frame) : that.frame == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (weekday != null ? weekday.hashCode() : 0);
        result = 31 * result + (frame != null ? frame.hashCode() : 0);
        result = 31 * result + index;
        return result;
    }
}
