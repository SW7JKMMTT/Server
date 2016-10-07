package dk.aau.giraf.rest.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.aau.giraf.rest.core.weekschedule.WeekSchedule;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "Pictogram")
public class Pictogram extends PictoFrame {
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    private PictogramImage pictogramImage;

    public Pictogram(String title, AccessLevel accessLevel, PictogramImage pictogramImage, User owner) {
        super(title, accessLevel, owner);
        this.pictogramImage = pictogramImage;
    }

    public Pictogram(String title, AccessLevel accessLevel, PictogramImage pictogramImage) {
        super(title, accessLevel);
        this.pictogramImage = pictogramImage;
    }

    public Pictogram(String title, AccessLevel accessLevel, Department department) {
        super(title, accessLevel, department);
    }

    public Pictogram(String title, AccessLevel accessLevel, Department department, User owner) {
        super(title, accessLevel, department, owner);
    }

    public Pictogram(String title, AccessLevel accessLevel, User owner) {
        super(title, accessLevel, owner);
    }

    protected Pictogram() {
    }

    @JsonIgnore
    public void setPictogramImage(PictogramImage pictogramImage) {
        this.pictogramImage = pictogramImage;
    }

    @JsonIgnore
    public PictogramImage getPictogramImage() {
        return pictogramImage;
    }
}
