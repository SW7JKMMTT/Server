package rocks.stalin.sw708e16.server.persistence;

public abstract class PersistFileHandleFactory<T> {
    public abstract T create(String path);
}
