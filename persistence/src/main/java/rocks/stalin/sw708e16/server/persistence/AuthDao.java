package rocks.stalin.sw708e16.server.persistence;

import rocks.stalin.sw708e16.server.core.authentication.AuthToken;

import java.util.Collection;
import java.util.List;

public interface AuthDao extends BaseDao<AuthToken> {
    AuthToken byTokenStr_ForAuthorization(String tkn);

    List<AuthToken> byUserId_ForDisplay(long userId);

    Collection<AuthToken> getAll();
}
