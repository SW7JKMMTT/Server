package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.Sequence;
import dk.aau.giraf.rest.core.User;

import java.util.Collection;

public interface SequenceDao extends BaseDao<Sequence> {
    Collection<Sequence> byDepartment(Department department);

    Sequence byId(long id);

    Collection<Sequence> searchByUserAndTitle(User user, String title);
}
