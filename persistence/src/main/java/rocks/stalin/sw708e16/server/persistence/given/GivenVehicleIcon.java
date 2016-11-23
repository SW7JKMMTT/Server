package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.VehicleIcon;
import rocks.stalin.sw708e16.server.persistence.VehicleIconFactory;
import rocks.stalin.sw708e16.server.persistence.file.MemoryBackedRoFile;
import rocks.stalin.sw708e16.server.persistence.file.Readable;
import rocks.stalin.sw708e16.server.persistence.file.dao.VehicleIconFileDao;

import java.io.IOException;
import java.io.InputStream;

public class GivenVehicleIcon {
    private String filepath;

    public GivenVehicleIcon withFilepath(String filepath) {
        this.filepath = filepath;
        return this;
    }

    public VehicleIcon in(VehicleIconFileDao vehicleIconFileDao) throws IOException
    {
        VehicleIcon vehicleIcon = new VehicleIcon(this.filepath);
        vehicleIconFileDao.add(new VehicleIconFactory(), () -> new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        });

        return vehicleIcon;
    }
}
