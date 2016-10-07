package dk.aau.giraf.rest.persistence.hibernate;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.persistence.BaseDaoImpl;
import dk.aau.giraf.rest.persistence.DepartmentDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.Collection;

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
            em.createQuery("SELECT d FROM Department d WHERE d.name = :name", Department.class);
        query.setParameter("name", name);
        return getFirst(query);
    }

    @Override
    public Collection<Department> getAll() {
        return em.createQuery("SELECT d FROM Department d").getResultList();
    }
}
