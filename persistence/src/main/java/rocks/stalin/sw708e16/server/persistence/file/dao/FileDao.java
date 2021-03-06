package rocks.stalin.sw708e16.server.persistence.file.dao;

import rocks.stalin.sw708e16.server.persistence.PersistFileHandle;
import rocks.stalin.sw708e16.server.persistence.PersistFileHandleFactory;
import rocks.stalin.sw708e16.server.persistence.file.Readable;

import java.io.IOException;

public interface FileDao<T extends PersistFileHandle> {

    T add(PersistFileHandleFactory<T> factory, Readable file) throws IOException;

    T add(Readable file) throws IOException;

    T update(T old, Readable newFile) throws IOException;

    T remove(T file);

    T newOrUpdate(PersistFileHandleFactory<T> factory, T file, Readable newFile) throws IOException;

    T newOrUpdate(T file, Readable newFile) throws IOException;

    Readable fromHandle(T handle);
}
