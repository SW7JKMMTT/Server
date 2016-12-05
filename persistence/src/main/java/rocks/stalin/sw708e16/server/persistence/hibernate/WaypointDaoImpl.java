package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;
import org.hibernate.search.spatial.Coordinates;
import org.hibernate.search.spatial.impl.GeometricConstants;
import org.hibernate.search.spatial.impl.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;
import rocks.stalin.sw708e16.server.persistence.Coordinate;
import rocks.stalin.sw708e16.server.persistence.WaypointDao;

import java.util.List;

@Transactional
@Repository
@Primary
public class WaypointDaoImpl extends BaseDaoImpl<Waypoint> implements WaypointDao {
    private static Logger logger = LoggerFactory.getLogger(WaypointDaoImpl.class);

    @Override
    public List<Waypoint> withinRadius(Coordinate coordinate, double kilometers) {
        logger.info("Querying for within radius");

        if(coordinate.getLatitude() > GeometricConstants.LATITUDE_DEGREE_MAX || coordinate.getLatitude() < GeometricConstants.LATITUDE_DEGREE_MIN)
            throw new IllegalArgumentException("Illegal latitude");
        if(coordinate.getLongitude() > GeometricConstants.LONGITUDE_DEGREE_MAX || coordinate.getLongitude() < GeometricConstants.LONGITUDE_DEGREE_MIN)
            throw new IllegalArgumentException("Illegal longitude");

        FullTextEntityManager ftem = Search.getFullTextEntityManager(em);

        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Waypoint.class).get();
        Coordinates cord = Point.fromDegrees(coordinate.getLatitude(), coordinate.getLongitude());

        Query query = qb.spatial().within(kilometers, Unit.KM).ofCoordinates(cord).createQuery();

        return (List<Waypoint>) ftem.createFullTextQuery(query, Waypoint.class).getResultList();
    }
}
