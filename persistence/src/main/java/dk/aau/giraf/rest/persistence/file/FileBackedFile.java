package dk.aau.giraf.rest.persistence.file;

import java.io.*;

/**
 * A wrapper for read and write operations of a file.
 */
public class FileBackedFile implements Readable, Writable {
    private File filePath;

    /**
     * Create a filepath.
     *
     * @param filePath Path to the file
     */
    public FileBackedFile(File filePath) {
        this.filePath = filePath;
    }

    /**
     * Open an inputstream to the file.
     *
     * @return The {@link InputStream inputstream} of the file
     * @throws FileNotFoundException If the file doesn't exist
     */
    public InputStream read() throws FileNotFoundException {
        return new FileInputStream(filePath);
    }

    /**
     * Open an outpustream to the file.
     *
     * @return The {@link OutputStream outputstream} of the file
     * @throws IOException If the file is unwritable
     */
    public OutputStream write() throws IOException {
        if(!filePath.exists())
            filePath.createNewFile();
        if(!filePath.canWrite())
            throw new IOException("File not writable");
        return new FileOutputStream(filePath);
    }

    /**
     * Delete the file.
     */
    public void delete() {
        filePath.delete();
    }

    /**
     * Get the path to the file.
     *
     * @return The {@File file} object containing the path
     */
    public File getFilePath() {
        return filePath;
    }
}
