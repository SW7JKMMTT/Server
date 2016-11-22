package rocks.stalin.sw708e16.server.persistence;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.Vin;

import java.util.Collection;

public interface VehicleDao extends BaseDao<Vehicle>  {
    Collection<Vehicle> getAll();

    Vehicle byId(ObjectId id);

    Vehicle byVin(Vin vin);

    Vehicle byVin(String vin);

    // TODO: By Make
    // TODO: By Model
    // TODO: By Year
    // TODO: By Vin
}
