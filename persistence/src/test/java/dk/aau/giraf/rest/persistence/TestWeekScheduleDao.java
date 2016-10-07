package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.core.weekschedule.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
@Transactional
public class TestWeekScheduleDao {
    @Resource
    private WeekScheduleDao weekScheduleDao;

    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private UserDao userDao;

    private Department department;
    private User user1;
    private User user2;

    @Before
    public void setup() {
        department = departmentDao.byId(1L);
        user1 = userDao.byId(department, 1L);
        user2 = userDao.byId(department, 2L);
    }

    @Test
    public void testById() throws Exception {
        WeekSchedule w = weekScheduleDao.byId(1L);

        assertNotNull(w);
        assertEquals("marks uge", w.getName());
    }

    @Test
    public void testByIdHasThumbnail() throws Exception {
        WeekSchedule w = weekScheduleDao.byId(1L);

        assertNotNull(w.getThumbnail());
        assertEquals("test1", w.getThumbnail().getTitle());
    }

    @Test
    public void testByIdCreatedFor() throws Exception {
        WeekSchedule w = weekScheduleDao.byId(1L);

        assertNotNull(w.hasUser(user1));
    }

    @Test
    public void testByIdHasWeekday() throws Exception {
        WeekSchedule w = weekScheduleDao.byId(1L);

        assertNotNull(w.getWeekday(Day.Monday));
    }

    @Test
    public void testWeekScheduleDepartment() throws Exception {
        WeekSchedule w = weekScheduleDao.byId(1L);

        // Should this be here or in department
        Department dep = w.getDepartment();
        assertEquals(departmentDao.byId(1L), dep);
        assertNotNull(dep.getWeekSchedules());
        // Department should have a weekSchedule called "marks uge"
        assertTrue(dep.getWeekSchedules().stream().anyMatch(i -> "marks uge".equals(i.getName())));
    }

    @Test
    public void testWeekScheduleUser() throws Exception {
        User u = userDao.byId(department, 1L);
        assertNotNull(u.getWeekSchedule());
        // Users should have a weekSchedule called "marks uge"
        assertTrue(u.getWeekSchedule().stream().anyMatch(i -> "marks uge".equals(i.getName())));
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<WeekSchedule> w = weekScheduleDao.getAll();

        assertNotNull(w);
        assertTrue(!w.isEmpty());
        // "marks uge" should be present in the list
        assertTrue(w.stream().anyMatch(i -> "marks uge".equals(i.getName())));
    }

    @Test
    public void testIndividualProgress() throws Exception {
        WeekSchedule ws = weekScheduleDao.byId(2L);
        Weekday monday = ws.getWeekday(Day.Monday);
        Weekday tuesday = ws.getWeekday(Day.Tuesday);

        Iterator<WeekdayFrame> mondayIterator = monday.getFrameIterator();
        Iterator<WeekdayFrame> tuesdayIterator = tuesday.getFrameIterator();

        WeekdayFrame wdf1 = mondayIterator.next();
        WeekdayFrame wdf2 = mondayIterator.next();

        WeekdayFrame wdf3 = tuesdayIterator.next();
        WeekdayFrame wdf4 = tuesdayIterator.next();

        wdf3.setPictoFrameProgress(Progress.Active, user1);
        wdf4.setPictoFrameProgress(Progress.Active, user2);

        assertNull(wdf1.getPictoFrameProgress(user1));
        assertNull(wdf1.getPictoFrameProgress(user2));
        assertNull(wdf2.getPictoFrameProgress(user1));
        assertNull(wdf2.getPictoFrameProgress(user2));
        assertEquals(Progress.Active, wdf3.getPictoFrameProgress(user1));
        assertNull(wdf3.getPictoFrameProgress(user2));
        assertNull(wdf4.getPictoFrameProgress(user1));
        assertEquals(Progress.Active, wdf4.getPictoFrameProgress(user2));
    }

    @Test
    public void testUserProgress() {
        WeekSchedule ws = weekScheduleDao.byId(2L);
        Weekday monday = ws.getWeekday(Day.Monday);
        Iterator<WeekdayFrame> mondayIterator = monday.getFrameIterator();
        WeekdayFrame wdf1 = mondayIterator.next();

        wdf1.setPictoFrameProgress(Progress.Active, user1);
        assertNotNull(user1.getUserProgress(wdf1));
    }
}
