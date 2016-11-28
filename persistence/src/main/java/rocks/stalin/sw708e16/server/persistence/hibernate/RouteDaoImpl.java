package rocks.stalin.sw708e16.server.persistence.hibernate;


import org.bson.types.ObjectId;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.RouteState;
import rocks.stalin.sw708e16.server.core.spatial.Route;
import rocks.stalin.sw708e16.server.persistence.RouteDao;

import javax.persistence.TypedQuery;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class RouteDaoImpl extends BaseDaoImpl<Route> implements RouteDao {
    @Override
    public Collection<Route> getAll() {
        TypedQuery<Route> query = em.createQuery("SELECT p FROM Route p", Route.class);
        return query.getResultList();
    }

    @Override
    public Route byId(ObjectId id) {
        TypedQuery<Route> query = em.createQuery(
                "SELECT r " +
                        "FROM Route r " +
                        "WHERE r.id = :id",
                Route.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public Collection<Route> getByState(RouteState routeState) {
        TypedQuery<Route> query = em.createQuery(
            "SELECT p " +
                    "FROM Route p " +
                    "WHERE p.routeState = :state",
                Route.class);
        query.setParameter("state", routeState);
        return query.getResultList();
    }
}
