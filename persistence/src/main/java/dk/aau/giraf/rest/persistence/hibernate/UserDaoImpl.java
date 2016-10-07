package dk.aau.giraf.rest.persistence.hibernate;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.persistence.BaseDaoImpl;
import dk.aau.giraf.rest.persistence.UserDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
    public User byUsername(Department department, final String username) {
        TypedQuery<User> query =
            em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.department = :dep", User.class);
        query.setParameter("username", username);
        query.setParameter("dep", department);
        return getFirst(query);
    }

    @Override
    public User byId(Department department, Long id) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u " +
                "FROM User u " +
                "WHERE u.id = :id " +
                    "AND u.department = :dep",
            User.class);
        query.setParameter("id", id);
        query.setParameter("dep", department);
        return getFirst(query);
    }

    @Override
    public Collection<User> getAll(Department department) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.department = :dep", User.class);
        query.setParameter("dep", department);
        return query.getResultList();
    }

    @Override
    public User byUsernameAndPassword(Department department, String username, String password) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u " +
            "WHERE u.username = :name AND u.password = :password AND u.department = :dep", User.class);
        query.setParameter("name", username);
        query.setParameter("password", password);
        query.setParameter("dep", department);
        return getFirst(query);
    }
}
