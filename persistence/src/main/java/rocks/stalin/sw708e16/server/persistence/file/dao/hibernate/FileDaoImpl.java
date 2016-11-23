package rocks.stalin.sw708e16.server.persistence.file.dao.hibernate;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.NotImplementedYetException;
import rocks.stalin.sw708e16.server.persistence.PersistFileHandle;
import rocks.stalin.sw708e16.server.persistence.PersistFileHandleFactory;
import rocks.stalin.sw708e16.server.persistence.file.FileBackedFile;
import rocks.stalin.sw708e16.server.persistence.file.Readable;
import rocks.stalin.sw708e16.server.persistence.file.Writable;
import rocks.stalin.sw708e16.server.persistence.file.dao.FileDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public abstract class FileDaoImpl<T extends PersistFileHandle> implements FileDao<T> {
    @PersistenceContext
    EntityManager em;
    Path storagePath;

    @Override
    public T add(PersistFileHandleFactory<T> factory, Readable file) throws IOException {
        if(Files.createDirectories(storagePath) == null)
            throw new IOException("Failed creating repo directory " + storagePath);

        UUID uuid = UUID.randomUUID();
        Path filePath = storagePath.resolve(uuid.toString());

        T fh = factory.create(uuid.toString());
        em.persist(fh);

        Writable fsFile = new FileBackedFile(filePath.toFile());
        IOUtils.copy(file.read(), fsFile.write());

        return fh;
    }

    @Override
    public T update(T old, Readable newFile) throws IOException {
        Path filePath = storagePath.resolve(old.getFilePath());

        Writable oldFile = new FileBackedFile(filePath.toFile());
        IOUtils.copy(newFile.read(), oldFile.write());
        return old;
    }

    @Override
    public T remove(T file) {
        //TODO: Make removal. It needs to remove the row from the database and delete the file on the filesystem
        throw new NotImplementedYetException("Removal of files in not implemented");
    }

    @Override
    public T newOrUpdate(PersistFileHandleFactory<T> factory, T file, Readable newFile) throws IOException {
        T fh = file;
        if(file == null) {
            fh = add(factory, newFile);
        } else {
            update(fh, newFile);
        }
        return fh;
    }

    @Override
    public Readable fromHandle(T handle) {
        Path filePath = storagePath.resolve(handle.getFilePath());

        return new FileBackedFile(filePath.toFile());
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = Paths.get(storagePath);
    }
}
