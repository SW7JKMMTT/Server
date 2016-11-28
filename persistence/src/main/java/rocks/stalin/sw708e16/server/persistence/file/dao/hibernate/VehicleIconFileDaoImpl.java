package rocks.stalin.sw708e16.server.persistence.file.dao.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.VehicleIcon;
import rocks.stalin.sw708e16.server.persistence.VehicleIconFactory;
import rocks.stalin.sw708e16.server.persistence.file.Readable;
import rocks.stalin.sw708e16.server.persistence.file.dao.VehicleIconFileDao;

import java.io.IOException;

@Transactional
@Repository
@Primary
public class VehicleIconFileDaoImpl extends FileDaoImpl<VehicleIcon> implements VehicleIconFileDao {
    private VehicleIconFactory factory;

    public VehicleIconFileDaoImpl() {
        factory = new VehicleIconFactory();
    }

    @Override
    public VehicleIcon add(Readable file) throws IOException {
        return super.add(factory, file);
    }

    @Override
    public VehicleIcon newOrUpdate(VehicleIcon file, Readable newFile) throws IOException {
        return super.newOrUpdate(factory, file, newFile);
    }
}
