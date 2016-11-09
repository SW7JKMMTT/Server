package rocks.stalin.sw708e16.server.persistence;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.Vehicle;

import java.util.Collection;

public interface VehicleDao extends BaseDao<Vehicle>  {
    Collection<Vehicle> getAll();

    Vehicle byId(ObjectId id);

    // TODO: By Make
    // TODO: By Model
    // TODO: By Year
    // TODO: By Vin
}
