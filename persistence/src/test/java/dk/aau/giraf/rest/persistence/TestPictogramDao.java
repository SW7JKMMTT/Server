package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.Pictogram;
import dk.aau.giraf.rest.core.User;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestPictogramDao {

    private Department department;

    @Resource
    private PictogramDao pictogramDao;

    @Resource
    private UserDao userDao;

    @Resource
    private DepartmentDao departmentDao;

    @Before
    public void getDepartment() {
        department = departmentDao.byName("monstertruck");
    }

    @Test
    public void testPictogramGetAll() throws Exception {
        Collection<Pictogram> pictograms = pictogramDao.getAllPublicPictograms();
        Assert.assertFalse(pictograms.isEmpty());
    }

    @Test
    public void testPictogramGetAllWithUser() throws Exception {
        User u = userDao.byId(department, 1337L);
        Collection<Pictogram> pictograms = pictogramDao.getAll(u);
        Assert.assertFalse(pictograms.isEmpty());
        Assert.assertEquals(pictograms.size(), 4);
    }

    @Test
    public void testPictogramById() throws Exception {
        Pictogram p = pictogramDao.byId(1L);

        Assert.assertNotNull(p);
        Assert.assertTrue(p.getTitle().equals("test1"));

        Assert.assertNotNull(p.getPictogramImage());
        Assert.assertEquals(p.getPictogramImage().getFilePath(), "jeff");
        Assert.assertEquals(p.getPictogramImage().getId(), 1L);
    }

    @Test
    public void testPictogramByTitle() throws Exception {
        ArrayList<Pictogram> pictogramlist = new ArrayList<Pictogram>(pictogramDao.searchByTitle("test1"));

        Assert.assertFalse(pictogramlist.isEmpty());

        Assert.assertFalse(pictogramlist.isEmpty());
        Assert.assertTrue(pictogramlist.stream().anyMatch(p -> p.getId() == 1L));

        Assert.assertTrue(pictogramlist.stream().anyMatch(p -> p.getPictogramImage().getFilePath().equals("jeff")));
        Assert.assertTrue(pictogramlist.stream().anyMatch(p -> p.getPictogramImage().getId() == 1L));
    }

    @Test
    public void testPictogramByTitleSearch() throws Exception {
        ArrayList<Pictogram> pl1 = new ArrayList<Pictogram>(pictogramDao.searchByTitle("test"));
        Assert.assertFalse(pl1.isEmpty());

        Pictogram p = pl1.get(0);
        Assert.assertNotNull(p);

        ArrayList<Pictogram> pl2 = new ArrayList<Pictogram>(pictogramDao.searchByTitle("atest"));
        Assert.assertEquals(pl2.size(), 0);

        ArrayList<Pictogram> pl3 = new ArrayList<Pictogram>(pictogramDao.searchByTitle("est"));
        Assert.assertFalse(pl3.isEmpty());
        Assert.assertEquals(pl3.size(), 1);
    }

    @Test
    public void testPictogramByTitleSearchWithUser() throws Exception {
        User u = userDao.byId(department, 1337L);
        ArrayList<Pictogram> pl1 = new ArrayList<>(pictogramDao.searchByTitle(u, "test2"));
        Assert.assertFalse(pl1.isEmpty());
        Pictogram p = pl1.get(0);
        Assert.assertNotNull(p);
    }

    // TODO: Test with departments.
}
