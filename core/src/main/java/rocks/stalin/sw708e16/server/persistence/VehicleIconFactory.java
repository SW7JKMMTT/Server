package rocks.stalin.sw708e16.server.persistence;


import rocks.stalin.sw708e16.server.core.VehicleIcon;

public class VehicleIconFactory extends PersistFileHandleFactory {
    @Override
    public PersistFileHandle create(String path) {
        return new VehicleIcon(path);
    }
}
