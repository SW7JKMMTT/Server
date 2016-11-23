package rocks.stalin.sw708e16.server.core;

import rocks.stalin.sw708e16.server.persistence.PersistFileHandle;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class VehicleIcon extends PersistFileHandle {
    protected VehicleIcon() {
    }

    public VehicleIcon(String filePath) {
        super(filePath);
    }
}
