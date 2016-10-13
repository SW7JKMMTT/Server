package rocks.stalin.sw708e16.server.persistence;

import rocks.stalin.sw708e16.server.core.authentication.AuthToken;

import java.util.Collection;

public interface AuthDao extends BaseDao<AuthToken> {
    AuthToken byTokenStr(String tkn);

    Collection<AuthToken> getAll();
}
