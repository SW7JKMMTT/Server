package rocks.stalin.sw708e16.server.core;

import rocks.stalin.sw708e16.server.persistence.PersistFileHandle;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class UserIcon extends PersistFileHandle {
    protected UserIcon() {
    }

    public UserIcon(String filePath) {
        super(filePath);
    }
}
