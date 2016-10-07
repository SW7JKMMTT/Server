package dk.aau.giraf.rest.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class for sequences, which contains elements and a title pictogram.
 */
@Entity
@Table(name = "Sequence")
public class Sequence extends PictoFrame implements Iterable<Frame> {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Sequence__Frame",
            joinColumns = {
                @JoinColumn(name = "sequence_id")},
            inverseJoinColumns = {
                @JoinColumn(name = "frame_id")})
    @OrderColumn(name = "placement")
    private List<Frame> elements = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "thumbnail_id", nullable = false)
    private Pictogram thumbnail;

    protected Sequence() {
    }

    public Sequence(String title, AccessLevel accessLevel, Department department, User owner, Pictogram thumbnail) {
        super(title, accessLevel, department, owner);
        this.thumbnail = thumbnail;
    }

    /**
     * Construct a sequence.
     *
     * @param title the title of the sequence
     * @param accessLevel the accessLevel of the sequence
     * @param department the department of the sequence
     * @param owner the owner of the sequence
     * @param elements the elements on the sequence
     * @param thumbnail the thumbnail of the sequence
     */
    public Sequence(String title,
                    AccessLevel accessLevel,
                    Department department,
                    User owner,
                    List<Frame> elements,
                    Pictogram thumbnail)
    {
        super(title, accessLevel, department, owner);
        this.elements.addAll(elements);
        this.thumbnail = thumbnail;
    }

    public Pictogram getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Pictogram thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * Get frame from the list of frames on the sequence at index.
     *
     * @param index The index of the frame
     * @return Frame at specified index
     */
    public Frame get(int index) {
        return elements.get(index);
    }

    /**
     * Add frame to the sequence.
     * Appends to the existing frames.
     *
     * @param frame The frame to be added to the sequence
     */
    public void add(Frame frame) {
        elements.add(frame);
    }

    /**
     * Insert frame into the sequence at specific index.
     *
     * @param index where the frame should be inserted in the list of frames
     * @param frame The frame to be added to the sequence
     */
    public void add(int index, Frame frame) {
        elements.add(index, frame);
    }

    /**
     * Insert multiple frames into the sequence.
     * Appends to the existing frames.
     *
     * @param frames the frames to be added to the sequence
     */
    public void addAll(List<Frame> frames) {
        elements.addAll(frames);
    }

    /**
     * Insert multiple frames into the sequence starting at a specific index.
     *
     * @param index where the frames should be inserted in the list of frames
     * @param frames the frames to be added to the choice
     */
    public void addAll(int index, List<Frame> frames) {
        elements.addAll(index, frames);
    }

    /**
     * Remove frame from the sequence.
     *
     * @param index the index of the frame which is to be removed.
     */
    public void remove(int index) {
        elements.remove(index);
    }

    /**
     * Clear all frames from the sequence.
     */
    public void clear() {
        elements.clear();
    }

    /**
     * Get the iterator for the frames on the sequence.
     *
     * @return Iterator of all frames
     */
    @JsonProperty("elements")
    public Iterator<Frame> iterator() {
        elements.removeIf(element -> element == null);
        return elements.iterator();
    }

    /**
     * Merge a sequence into this.
     *
     * @param sequence the sequence to merge into this
     */
    public void merge(Sequence sequence) {
        super.merge(sequence);
        if (sequence.getThumbnail() != null)
            this.setThumbnail(sequence.getThumbnail());
        if (sequence.iterator() != null) {
            this.clear();
            sequence.iterator().forEachRemaining(this::add);
        }
    }
}
