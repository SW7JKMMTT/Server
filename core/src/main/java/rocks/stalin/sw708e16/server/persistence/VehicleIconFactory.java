package rocks.stalin.sw708e16.server.persistence;


import rocks.stalin.sw708e16.server.core.VehicleIcon;

public class VehicleIconFactory extends PersistFileHandleFactory<VehicleIcon> {
    @Override
    public VehicleIcon create(String path) {
        return new VehicleIcon(path);
    }
}
