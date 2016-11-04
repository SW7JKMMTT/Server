package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.Permission;
import rocks.stalin.sw708e16.server.core.authentication.PermissionType;
import rocks.stalin.sw708e16.server.persistence.PermissionDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Transactional
@Repository
@Primary
public class PermissionDaoImpl implements PermissionDao {

    @PersistenceContext
    private EntityManager em;

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
