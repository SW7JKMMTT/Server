package dk.aau.giraf.rest.core;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Choices which can be used on week schedules and Sequences, they can contain Pictoframes.
 */
@Entity
@Table(name = "Choice")
@PrimaryKeyJoinColumn(name = "frame_id", referencedColumnName = "id")
public class Choice extends Frame implements Iterable<PictoFrame> {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "Choice__PictoFrame",
            joinColumns = {
                    @JoinColumn(name = "choice_id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "pictoFrame_id")})
    @OrderColumn(name = "placement")
    private List<PictoFrame> options;

    protected Choice() {
    }

    /**
     * Construct a choice with options.
     *
     * @param options the options to be added to the choice
     */
    public Choice(List<PictoFrame> options) {
        this.options = new ArrayList<PictoFrame>();
        this.options.addAll(options);
    }

    /**
     * Add option to the choice.
     * Appends to the existing choice options.
     *
     * @param option The option to be added to the choice
     */
    public void add(PictoFrame option) {
        this.options.add(option);
    }

    /**
     * Insert option to the choice at specific place.
     *
     * @param index where the option should be inserted in the list of options
     * @param option The option to be added to the choice
     */
    public void add(int index, PictoFrame option) {
        this.options.add(index, option);
    }

    /**
     * Add multiple options to the choice at once.
     * Appends to the existing options.
     *
     * @param options the options to be added to the choice
     */
    public void addAll(List<PictoFrame> options) {
        this.options.addAll(options);
    }

    /**
     * Insert multiple options to the choice starting at a specific place.
     *
     * @param index where the options should be inserted in the list of options
     * @param options the options to be added to the choice
     */
    public void addAll(int index, List<PictoFrame> options) {
        this.options.addAll(index, options);
    }

    /**
     * Remove an option from the choice.
     *
     * @param index the index of the choice option which is to be removed.
     */
    public void remove(int index) {
        this.options.remove(index);
    }

    /**
     * Clear the options from the choice.
     */
    public void clear() {
        this.options.clear();
    }

    /**
     * Get the iterator for the options.
     *
     * @return Iterator of all options
     */
    @JsonProperty("options")
    public Iterator<PictoFrame> iterator() {
        return this.options.iterator();
    }

}
