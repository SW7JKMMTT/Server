package dk.aau.giraf.rest.persistence.file;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for anything readable by a {@link dk.aau.giraf.rest.persistence.file.dao.FileDao filedao}
 */
public interface Readable {
    InputStream read() throws IOException;
}
