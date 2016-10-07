package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.PictogramImage;

public class PictogramImageFactory extends PersistFileHandleFactory {
    @Override
    public PersistFileHandle create(String path) {
        return new PictogramImage(path);
    }
}
