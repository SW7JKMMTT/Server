package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.bson.types.ObjectId;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.persistence.UserDao;

import javax.persistence.TypedQuery;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
    public User byUsername(final String username) {
        TypedQuery<User> query =
            em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        return getFirst(query);
    }

    @Override
    public User byId(ObjectId id) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u " +
                "FROM User u " +
                "WHERE u.id = :id",
            User.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public Collection<User> getAll() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    @Override
    public User byUsernameAndPassword(String username, String password) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u " +
            "WHERE u.username = :name AND u.password = :password", User.class);
        query.setParameter("name", username);
        query.setParameter("password", password);
        return getFirst(query);
    }
}
