package rocks.stalin.sw708e16.server.persistence.file.dao.hibernate;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.UserIcon;
import rocks.stalin.sw708e16.server.persistence.UserIconFactory;
import rocks.stalin.sw708e16.server.persistence.file.Readable;
import rocks.stalin.sw708e16.server.persistence.file.dao.UserIconFileDao;

import java.io.IOException;

@Transactional
@Repository
@Primary
@Qualifier("userIcon")
public class UserIconFileDaoImpl extends FileDaoImpl<UserIcon> implements UserIconFileDao {

    private UserIconFactory factory;

    public UserIconFileDaoImpl() {
        factory = new UserIconFactory();
    }

    @Override
    public UserIcon add(Readable file) throws IOException {
        return super.add(factory, file);
    }

    @Override
    public UserIcon newOrUpdate(UserIcon file, Readable newFile) throws IOException {
        return super.newOrUpdate(factory, file, newFile);
    }
}
