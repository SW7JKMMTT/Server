package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleData;
import rocks.stalin.sw708e16.server.persistence.VehicleDataDao;

import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Repository
@Primary
public class VehicleDataDaoImpl extends BaseDaoImpl<VehicleData> implements VehicleDataDao {
    @Override
    public List<VehicleData> byRoute(Route route) {
        TypedQuery<VehicleData> query = em.createQuery(
            "SELECT vehicledata " +
                "FROM VehicleData vehicledata " +
                "WHERE vehicledata.route = :route", VehicleData.class);
        query.setParameter("route", route);
        return query.getResultList();
    }
}
