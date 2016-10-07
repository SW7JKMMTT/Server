package dk.aau.giraf.rest.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.util.*;

/**
 * Main attributes of any element to be used on e.g. weekscedules or sequences.
 */
@Entity
@Table(name = "Frame")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
public abstract class Frame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date lastEditAt;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "elements")
    private Collection<Sequence> partOfSequences;

    protected Frame() {
    }

    public long getId() {
        return id;
    }

    @PrePersist
    @PreUpdate
    private void setLastEdit() {
        this.lastEditAt = new Date();
    }

    public Date getLastEditAt() {
        return lastEditAt;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Frame frame = (Frame) other;

        if (id != frame.id) return false;
        if (lastEditAt != null ? !lastEditAt.equals(frame.lastEditAt) : frame.lastEditAt != null) return false;
        return partOfSequences != null ? partOfSequences.equals(frame.partOfSequences) : frame.partOfSequences == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (lastEditAt != null ? lastEditAt.hashCode() : 0);
        result = 31 * result + (partOfSequences != null ? partOfSequences.hashCode() : 0);
        return result;
    }

    /**
     * Removes the Frame from Sequences it might be on before deleting the Frame.
     */
    @PreRemove
    public void removeFromSequence() {
        for (Sequence seq : partOfSequences) {
            List<Integer> toDelete = new ArrayList<>();
            int index = 0;
            Iterator<Frame> frames = seq.iterator();
            while (frames.hasNext()) {
                Frame frame = frames.next();
                if (frame.equals(this)) {
                    toDelete.add(index);
                }
                index++;
            }
            for (int reverseIndex = toDelete.size() - 1 ; reverseIndex >= 0; reverseIndex--) {
                seq.remove(toDelete.get(reverseIndex));
            }
        }
    }
}
