package rocks.stalin.sw708e16.server.persistence.hibernate;

import rocks.stalin.sw708e16.server.persistence.BaseDao;

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
    public T update(T obj) {
        return em.merge(obj);
    }

    @Override
    public void remove(T obj) {
        em.remove(em.merge(obj));
    }

    protected T getFirst(TypedQuery<T> query) {
        List<T> res = query.getResultList();
        if(res.isEmpty())
            return null;
        return res.get(0);
    }
}
