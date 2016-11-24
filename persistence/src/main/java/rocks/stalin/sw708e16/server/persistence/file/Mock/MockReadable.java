package rocks.stalin.sw708e16.server.persistence.file.Mock;

import org.apache.commons.io.IOUtils;
import rocks.stalin.sw708e16.server.persistence.file.Readable;

import java.io.IOException;
import java.io.InputStream;

public class MockReadable implements Readable {
    private String contents;

    public MockReadable(String contents) {
        this.contents = contents;
    }

    @Override
    public InputStream read() throws IOException {
        InputStream is = IOUtils.toInputStream(contents);
        is.close();
        return is;
    }
}
