package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.hibernate.search.spatial.Coordinates;
import org.hibernate.search.spatial.impl.Point;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.WaypointDao;

import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
@Repository
@Primary
public class WaypointDaoImpl extends BaseDaoImpl<Waypoint> implements WaypointDao {

    @Override
    public List<Waypoint> withinRadius(double latitude, double longitude, double kilometers) throws InterruptedException {
        FullTextEntityManager ftem = Search.getFullTextEntityManager(em);
        ftem.createIndexer().startAndWait();
        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Waypoint.class).get();
        Coordinates coordinate = Point.fromDegrees(latitude, longitude);
        Query query = qb.spatial().within(kilometers, Unit.KM).ofCoordinates(coordinate).createQuery();
        return (List<Waypoint>) ftem.createFullTextQuery(query, Waypoint.class).getResultList();
    }
}
