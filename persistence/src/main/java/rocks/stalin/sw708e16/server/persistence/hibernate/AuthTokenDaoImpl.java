package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.persistence.AuthDao;

import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

@Transactional
@Repository
@Primary
public class AuthTokenDaoImpl extends BaseDaoImpl<AuthToken> implements AuthDao {
    @Override
    public AuthToken byTokenStr_ForAuthorization(String tkn) {
        TypedQuery<AuthToken> query =
            em.createQuery(
                "SELECT at FROM AuthToken at " +
                    "INNER JOIN FETCH at.user as u " +
                    "LEFT JOIN FETCH u.driver " +
                    "LEFT JOIN FETCH u.permissions " +
                    "WHERE at.token = :token",
                AuthToken.class);
        query.setParameter("token", tkn);
        return getFirst(query);
    }

    @Override
    public List<AuthToken> byUserId_ForDisplay(long userId) {
        TypedQuery<AuthToken> query =
            em.createQuery(
                "SELECT at FROM AuthToken at " +
                    "INNER JOIN FETCH at.user as u " +
                    "LEFT JOIN FETCH u.driver " +
                    "WHERE u.id = :userId",
                AuthToken.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Collection<AuthToken> getAll() {
        TypedQuery<AuthToken> query = em.createQuery("SELECT at FROM AuthToken at", AuthToken.class);
        return query.getResultList();
    }
}
