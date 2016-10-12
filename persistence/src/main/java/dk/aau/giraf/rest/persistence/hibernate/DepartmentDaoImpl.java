package dk.aau.giraf.rest.persistence.hibernate;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.persistence.BaseDaoImpl;
import dk.aau.giraf.rest.persistence.DepartmentDao;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Repository
@Primary
public class DepartmentDaoImpl extends BaseDaoImpl<Department> implements DepartmentDao {
    @Override
    public Department byId(long id) {
        TypedQuery<Department> query = em.createQuery("SELECT d FROM Department d WHERE d.id = :id", Department.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    @Override
    public Department byName(String name) {
        TypedQuery<Department> query =
            em.createQuery("SELECT d FROM Department d WHERE d.name = :name JOIN FETCH d.members u", Department.class);
        Department res = getFirst(query);
        return res;
    }

    @Override
    public Collection<Department> getAll() {
        TypedQuery<Department> query = em.createQuery("SELECT d FROM Department d JOIN FETCH d.members m", Department.class);
        Collection<Department> departments = query.getResultList();
        departments.forEach(Department::getMemberCount);
        return departments;
    }
}
