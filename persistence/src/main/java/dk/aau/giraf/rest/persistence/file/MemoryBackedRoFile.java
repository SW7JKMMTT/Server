package dk.aau.giraf.rest.persistence.file;

import java.io.InputStream;

public class MemoryBackedRoFile implements Readable {
    private InputStream is;

    public MemoryBackedRoFile(InputStream is) {
        this.is = is;
    }

    @Override
    public InputStream read() {
        return is;
    }
}
