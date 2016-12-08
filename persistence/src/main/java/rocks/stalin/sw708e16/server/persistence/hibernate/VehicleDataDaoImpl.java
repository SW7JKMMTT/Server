package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.vehicledata.VehicleData;
import rocks.stalin.sw708e16.server.persistence.VehicleDataDao;

@Transactional
@Repository
@Primary
public class VehicleDataDaoImpl extends BaseDaoImpl<VehicleData> implements VehicleDataDao {
}
