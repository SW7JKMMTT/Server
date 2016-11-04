package rocks.stalin.sw708e16.server.persistence.file;

import rocks.stalin.sw708e16.server.persistence.file.dao.FileDao;

import java.io.IOException;
import java.io.OutputStream;

/**
 * For anything writable by a {@link FileDao}.
 */
public interface Writable {
    OutputStream write() throws IOException;

    void delete() throws IOException;
}
