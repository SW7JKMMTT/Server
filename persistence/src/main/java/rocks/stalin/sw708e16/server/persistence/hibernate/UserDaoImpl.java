package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import rocks.stalin.sw708e16.server.persistence.hibernate.magic.HibernateMagic;

import javax.persistence.TypedQuery;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
    /**
     * Does another user with the given username exist.
     * @param username The username to search by
     * @return The first {@link User} in the database with the username
     */
    public boolean usernameIsTaken(final String username) {
        TypedQuery<Long> query =
            em.createQuery(
                "SELECT count(u) " +
                    "FROM User u " +
                    "WHERE u.username = :username",
                Long.class);
        query.setParameter("username", username);
        Long result = query.getSingleResult();
        return result != 0;
    }

    @Override
    public User byId(long id) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u " +
                "FROM User u " +
                "WHERE u.id = :id",
            User.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public User byId_ForIcon(long id) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u " +
                "FROM User u " +
                "LEFT JOIN FETCH u.driver " +
                "LEFT JOIN FETCH u.icon " +
                "WHERE u.id = :id",
            User.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public User byId_ForDisplay(long id) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u " +
                "FROM User u " +
                "LEFT JOIN FETCH u.permissions " +
                "LEFT JOIN FETCH u.driver " +
                "WHERE u.id = :id",
            User.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public Collection<User> getAll_ForDisplay() {
        TypedQuery<User> query = em.createQuery(
            "SELECT DISTINCT u " +
                "FROM User u " +
                "LEFT JOIN FETCH u.driver " +
                "LEFT JOIN FETCH u.permissions",
            User.class);
        return query.getResultList();
    }

    @Override
    public User byUsernameAndPassword_ForLogin(String username, String password) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u " +
                "FROM User u " +
                "LEFT JOIN FETCH u.driver " +
                "WHERE u.username = :name " +
                "AND u.password = :password",
            User.class);
        query.setParameter("name", username);
        query.setParameter("password", password);
        return getFirst(query);
    }
}
