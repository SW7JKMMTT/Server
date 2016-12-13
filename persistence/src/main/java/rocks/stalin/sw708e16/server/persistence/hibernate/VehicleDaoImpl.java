package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.Vin;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;

import javax.persistence.TypedQuery;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class VehicleDaoImpl extends BaseDaoImpl<Vehicle> implements VehicleDao {
    @Override
    public Collection<Vehicle> getAll() {
        TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v LEFT JOIN FETCH v.routes", Vehicle.class);
        return query.getResultList();
    }

    @Override
    public Vehicle byId(long id) {
        TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v LEFT JOIN FETCH v.routes WHERE v.id = :id", Vehicle.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public Vehicle byVin(Vin vin) {
        // Using .vin.vin here to get the string vin on the Vin class.
        TypedQuery<Vehicle> query = em.createQuery("SELECT v FROM Vehicle v WHERE v.vin.vin = :vin", Vehicle.class);
        // vin.getVin() returns the string stores in Vin
        query.setParameter("vin", vin.getVin());
        return getFirst(query);
    }

    @Override
    public Vehicle byVin(String vin) {
        return byVin(new Vin(vin));
    }
}
