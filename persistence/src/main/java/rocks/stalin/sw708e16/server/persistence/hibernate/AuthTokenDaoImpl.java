package rocks.stalin.sw708e16.server.persistence.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.persistence.AuthDao;

import javax.persistence.TypedQuery;
import java.util.Collection;

/**
 * Created by delusional on 3/2/16.
 */
@Transactional
@Repository
@Primary
public class AuthTokenDaoImpl extends BaseDaoImpl<AuthToken> implements AuthDao {
    @Override
    public AuthToken byTokenStr(String tkn) {
        TypedQuery<AuthToken> query =
            em.createQuery("SELECT at FROM AuthToken at WHERE at.token = :token", AuthToken.class);
        query.setParameter("token", tkn);
        return getFirst(query);
    }

    @Override
    public Collection<AuthToken> getAll() {
        TypedQuery<AuthToken> query = em.createQuery("SELECT at FROM AuthToken at", AuthToken.class);
        return query.getResultList();
    }
}
