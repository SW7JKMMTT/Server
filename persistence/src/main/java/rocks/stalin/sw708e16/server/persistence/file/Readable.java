package rocks.stalin.sw708e16.server.persistence.file;

import rocks.stalin.sw708e16.server.persistence.file.dao.FileDao;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for anything readable by a {@link FileDao}.
 */
public interface Readable {
    InputStream read() throws IOException;
}
