package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.Department;

import java.util.Collection;

public interface DepartmentDao extends BaseDao<Department> {
    Department byId(long id);

    Department byName(String name);

    Collection<Department> getAll();
}
