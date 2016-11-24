package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.VehicleIcon;
import rocks.stalin.sw708e16.server.persistence.VehicleIconFactory;
import rocks.stalin.sw708e16.server.persistence.file.Mock.MockReadable;
import rocks.stalin.sw708e16.server.persistence.file.dao.VehicleIconFileDao;

import java.io.IOException;

public class GivenVehicleIcon {
    private String filecontent = "";

    public GivenVehicleIcon withContent(String filecontent) {
        this.filecontent = filecontent;
        return this;
    }

    public VehicleIcon in(VehicleIconFileDao vehicleIconFileDao) throws IOException {
        VehicleIconFactory vehicleIconFactory = new VehicleIconFactory();
        return vehicleIconFileDao.add(vehicleIconFactory, new MockReadable(filecontent));
    }
}
