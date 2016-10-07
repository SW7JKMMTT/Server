package dk.aau.giraf.rest.persistence.hibernate;

import dk.aau.giraf.rest.core.weekschedule.WeekSchedule;
import dk.aau.giraf.rest.persistence.BaseDaoImpl;
import dk.aau.giraf.rest.persistence.WeekScheduleDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.Collection;

/**
 * WeekScheduleDaoImpl implements WeekScheduleDao to access the database and get weekschedules.
 */
@Transactional
@Repository
@Primary
public class WeekScheduleDaoImpl extends BaseDaoImpl<WeekSchedule> implements WeekScheduleDao {
    @Override
    public Collection<WeekSchedule> getAll() {
        TypedQuery<WeekSchedule> query = em.createQuery("SELECT w FROM WeekSchedule w", WeekSchedule.class);
        return query.getResultList();
    }

    @Override
    public WeekSchedule byId(long id) {
        TypedQuery<WeekSchedule> query = em.createQuery(
                "SELECT ws FROM WeekSchedule ws WHERE ws.id = :id",
                WeekSchedule.class);
        query.setParameter("id", id);
        return getFirst(query);
    }
}
