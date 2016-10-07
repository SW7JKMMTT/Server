package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.authentication.AuthToken;

import java.util.Collection;

public interface AuthDao extends BaseDao<AuthToken> {
    AuthToken byTokenStr(String tkn);

    Collection<AuthToken> getAll();
}
