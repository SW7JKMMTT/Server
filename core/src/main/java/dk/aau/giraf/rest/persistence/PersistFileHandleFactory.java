package dk.aau.giraf.rest.persistence;

public abstract class PersistFileHandleFactory<T> {
    public abstract T create(String path);
}
