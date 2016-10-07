package dk.aau.giraf.rest.persistence;

public interface BaseDao<T> {
    void add(T obj);

    void update(T obj);

    void remove(T obj);
}
