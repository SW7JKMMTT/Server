package dk.aau.giraf.rest.core;

import dk.aau.giraf.rest.persistence.PersistFileHandle;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PictogramImage")
public class PictogramImage extends PersistFileHandle {
    public PictogramImage() {
    }

    public PictogramImage(String filePath) {
        super(filePath);
    }
}
