package dk.aau.giraf.rest.core.weekschedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import dk.aau.giraf.rest.core.Frame;

import javax.persistence.*;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A Weekday is used to contain Frames for the week schedule.
 */
@Entity
@Table(name = "Weekday")
public class Weekday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, insertable = false, updatable = false)
    private Day day;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastEdit;

    @ManyToOne
    @JoinColumn(name = "weekSchedule_id", nullable = false)
    private WeekSchedule weekSchedule;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "weekday_id", nullable = false)
    @OrderColumn(name = "weekdayframe_index", updatable = false, insertable = false)
    private List<WeekdayFrame> frames;

    protected Weekday() {
        this.frames = new LinkedList<>();
    }

    /**
     * Constructor for Weekday.
     *
     * @param day Day enum this Weekday is.
     * @param weekSchedule WeekSchedule to be part of.
     */
    public Weekday(Day day, WeekSchedule weekSchedule) {
        this();
        this.day = day;
        this.weekSchedule = weekSchedule;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Day getDay() {
        return this.day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public void addFrame(WeekdayFrame weekdayFrame) {
        weekdayFrame.setWeekday(this);
        frames.add(weekdayFrame);
    }

    public WeekSchedule getWeekSchedule() {
        return weekSchedule;
    }

    public void setWeekSchedule(WeekSchedule weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public Iterator<WeekdayFrame> getFrameIterator() {
        return frames.iterator();
    }

    public void setFrames(List<WeekdayFrame> frames) {
        this.frames = frames;
    }

    public Date getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(Date lastEdit) {
        this.lastEdit = lastEdit;
    }

    /**
     * Adds a WeekdayFrame to the weekday with an index.
     *
     * @param index index to add in.
     * @param frame frame to add.
     */
    public void addToWeekdayFrame(int index, Frame frame) {
        WeekdayFrame tempWeekdayFrame = new WeekdayFrame(this, frame);
        this.frames.add(index, tempWeekdayFrame);
    }
}
