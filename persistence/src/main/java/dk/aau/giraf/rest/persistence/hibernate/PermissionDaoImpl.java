package dk.aau.giraf.rest.persistence.hibernate;

import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.core.authentication.Permission;
import dk.aau.giraf.rest.core.authentication.PermissionType;
import dk.aau.giraf.rest.persistence.PermissionDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by delusional on 3/4/16.
 */
@Transactional
@Repository
@Primary
public class PermissionDaoImpl implements PermissionDao {

    @PersistenceContext
    EntityManager em;

    @Override
    public boolean userHasPermission(User user, PermissionType permission) {
        TypedQuery<Permission> query =
            em.createQuery("SELECT p FROM Permission p WHERE p.permission = :ptype AND p.user = :user",
                Permission.class);
        query.setParameter("ptype", permission);
        query.setParameter("user", user);
        return !query.getResultList().isEmpty();
    }
}
