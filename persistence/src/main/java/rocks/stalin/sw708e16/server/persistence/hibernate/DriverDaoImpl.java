package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Driver;
import rocks.stalin.sw708e16.server.persistence.DriverDao;

import javax.persistence.TypedQuery;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class DriverDaoImpl extends BaseDaoImpl<Driver> implements DriverDao {
    @Override
    public Collection<Driver> getAll() {
        TypedQuery<Driver> query = em.createQuery("SELECT d FROM Driver d", Driver.class);
        return query.getResultList();
    }

    @Override
    public Driver byId(long id) {
        TypedQuery<Driver> query = em.createQuery(
            "SELECT d " +
                "FROM Driver d " +
                "WHERE d.id = :id",
            Driver.class);
        query.setParameter("id", id);
        return getFirst(query);
    }
}
