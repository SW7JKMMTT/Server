package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.bson.types.ObjectId;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;

import javax.persistence.TypedQuery;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class VehicleDaoImpl extends BaseDaoImpl<Vehicle> implements VehicleDao {
    @Override
    public Collection<Vehicle> getAll() {
        TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v", Vehicle.class);
        return query.getResultList();
    }

    @Override
    public Vehicle byId(ObjectId id) {
        TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v WHERE v.id = :id", Vehicle.class);
        query.setParameter("id", id);
        return getFirst(query);
    }
}
