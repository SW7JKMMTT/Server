package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.WaypointDao;

@Transactional
@Repository
@Primary
public class WaypointDaoImpl extends BaseDaoImpl<Waypoint> implements WaypointDao {
}
