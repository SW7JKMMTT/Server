package dk.aau.giraf.rest.persistence.file;

import java.io.IOException;
import java.io.OutputStream;

/**
 * For anything writable by a {@link dk.aau.giraf.rest.persistence.file.dao.FileDao filedao}
 */
public interface Writable {
    OutputStream write() throws IOException;

    void delete() throws IOException;
}
