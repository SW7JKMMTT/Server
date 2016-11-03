package rocks.stalin.sw708e16.server.persistence;

import org.bson.types.ObjectId;
import rocks.stalin.sw708e16.server.core.User;

import java.util.Collection;

public interface UserDao extends BaseDao<User> {
    User byUsername(String contactName);

    User byId(ObjectId id);

    Collection<User> getAll();

    //For the login
    User byUsernameAndPassword(String username, String password);
}
