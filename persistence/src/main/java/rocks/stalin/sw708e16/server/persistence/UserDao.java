package rocks.stalin.sw708e16.server.persistence;

import rocks.stalin.sw708e16.server.core.User;

import java.util.Collection;

public interface UserDao extends BaseDao<User> {
    boolean usernameIsTaken(String contactName);

    User byId(long id);

    User byId_ForIcon(long id);

    User byId_ForDisplay(long id);

    Collection<User> getAll_ForDisplay();

    //For the login
    User byUsernameAndPassword_ForLogin(String username, String password);
}
