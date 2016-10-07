package dk.aau.giraf.rest.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class BaseDaoImpl<T> implements BaseDao<T> {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public void add(T obj) {
        em.persist(obj);
    }

    @Override
    public void update(T obj) {
        em.merge(obj);
    }

    @Override
    public void remove(T obj) {
        em.remove(obj);
    }

    protected T getFirst(TypedQuery<T> query) {
        List<T> res = query.getResultList();
        if(res.isEmpty())
            return null;
        return res.get(0);
    }
}
