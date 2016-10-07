package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.User;

import java.util.Collection;

public interface UserDao extends BaseDao<User> {
    User byUsername(Department department, String contactName);

    User byId(Department department, Long id);

    Collection<User> getAll(Department department);

    //For the login
    User byUsernameAndPassword(Department department, String username, String password);
}
