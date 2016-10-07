package dk.aau.giraf.rest.core;

import dk.aau.giraf.rest.persistence.PersistFileHandle;

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
