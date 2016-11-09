package rocks.stalin.sw708e16.server.persistence;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.Driver;

import java.util.Collection;

public interface DriverDao extends BaseDao<Driver> {
    Collection<Driver> getAll();

    Driver byId(ObjectId id);
}
