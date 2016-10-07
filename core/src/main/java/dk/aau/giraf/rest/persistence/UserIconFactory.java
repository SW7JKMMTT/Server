package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.UserIcon;

public class UserIconFactory extends PersistFileHandleFactory {
    @Override
    public PersistFileHandle create(String path) {
        return new UserIcon(path);
    }
}
