package rocks.stalin.sw708e16.server.persistence;

public interface BaseDao<T> {
    void add(T obj);

    void update(T obj);

    void remove(T obj);
}
