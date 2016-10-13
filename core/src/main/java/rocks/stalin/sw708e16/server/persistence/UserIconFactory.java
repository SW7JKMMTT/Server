package rocks.stalin.sw708e16.server.persistence;

import rocks.stalin.sw708e16.server.core.UserIcon;

public class UserIconFactory extends PersistFileHandleFactory {
    @Override
    public PersistFileHandle create(String path) {
        return new UserIcon(path);
    }
}
