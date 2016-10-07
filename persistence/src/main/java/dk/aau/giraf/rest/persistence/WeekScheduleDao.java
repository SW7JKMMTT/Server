package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.weekschedule.WeekSchedule;

import java.util.Collection;

/**
 * WeekScheduleDao gets WeekSchedules from the database.
 */
public interface WeekScheduleDao extends BaseDao<WeekSchedule> {
    WeekSchedule byId(long id);

    Collection<WeekSchedule> getAll();
}
