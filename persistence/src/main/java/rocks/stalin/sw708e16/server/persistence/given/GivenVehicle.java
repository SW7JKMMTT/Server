package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.VehicleIcon;
import rocks.stalin.sw708e16.server.core.Vin;
import rocks.stalin.sw708e16.server.persistence.VehicleDao;

public class GivenVehicle {
    private String make;
    private String model;
    private int vintage;
    private Vin vin;
    private VehicleIcon vehicleIcon;

    public GivenVehicle withMake(String make) {
        this.make = make;
        return this;
    }

    public GivenVehicle withModel(String model) {
        this.model = model;
        return this;
    }

    public GivenVehicle withVintage(int vintage) {
        this.vintage = vintage;
        return this;
    }

    public GivenVehicle withVin(Vin vin) {
        this.vin = vin;
        return this;
    }

    public GivenVehicle withVehicleIcon(VehicleIcon vehicleIcon) {
        this.vehicleIcon = vehicleIcon;
        return this;
    }

    public Vehicle in(VehicleDao vehicleDao) {
        Vehicle vehicle = new Vehicle(make, model,vintage, vin);
        vehicle.setVehicleIcon(vehicleIcon);
        vehicleDao.add(vehicle);
        return vehicle;
    }
}
