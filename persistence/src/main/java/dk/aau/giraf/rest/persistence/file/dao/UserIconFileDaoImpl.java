package dk.aau.giraf.rest.persistence.file.dao;

import dk.aau.giraf.rest.core.UserIcon;
import dk.aau.giraf.rest.persistence.UserIconFactory;
import dk.aau.giraf.rest.persistence.file.Readable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Transactional
@Repository
@Primary
@Qualifier("userIcon")
public class UserIconFileDaoImpl extends FileDaoImpl<UserIcon> {

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
