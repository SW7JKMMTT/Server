package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.*;
import dk.aau.giraf.rest.core.Sequence;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestSequenceDao {

    @Resource
    private UserDao userDao;

    private User user;

    @Resource
    private SequenceDao sequenceDao;

    private Department department;
    private Sequence seq;

    @Resource
    private DepartmentDao departmentDao;

    @Before
    public void prepareData() {
        department = departmentDao.byName("monstertruck");
        user = userDao.byUsername(department, "Derp");
        seq = sequenceDao.byId(4);
    }

    @Test
    public void testSequencebyDepartment() throws Exception {
        Collection<Sequence> sequences = sequenceDao.byDepartment(department);
        assertFalse(sequences.isEmpty());
        assertTrue(sequences.stream().anyMatch(
                sequence -> sequence.getOwner().getUsername().equals(user.getUsername())));
    }

    @Test
    public void testSequencebSearchByTitle() throws Exception {
        Collection<Sequence> sequences = sequenceDao.searchByUserAndTitle(user, "dan");
        assertFalse(sequences.isEmpty());
        assertTrue(sequences.stream().anyMatch(s -> s.getTitle().equals("dank")));
    }

    @Test
    public void testSequenceById() throws Exception {
        Sequence sequence = sequenceDao.byId(4);
        assertNotNull(sequence);
        assertEquals(sequence.getTitle(), "dank");
    }

    /**
     * Check if a sequence is as created in the SQL data.
     */
    @Test
    public void testSequenceCreation() throws Exception {
        assertEquals(seq.getThumbnail().getClass(), Pictogram.class);
        assertEquals(seq.getThumbnail().getId(), 2);
        assertEquals(((PictoFrame)seq.get(2)).getTitle(),"pepe");
    }

    /**
     * Add elements, check if it is added and that the old has changed index.
     */
    @Test
    public void testSequenceAddElement() throws Exception {
        Pictogram pic = new Pictogram("addedPictogram", AccessLevel.PUBLIC, user);
        Pictogram pic2 = new Pictogram("addedPictogram2", AccessLevel.PUBLIC, user);
        seq.add(2, pic);
        seq.add(2, pic2);
        seq.add(2, pic);
        seq.add(2, pic2);
        assertEquals(((PictoFrame)seq.get(2)).getTitle(),"addedPictogram2");
        assertEquals(((PictoFrame)seq.get(6)).getTitle(),"pepe");
    }

    /**
     * Remove element and check that element list is as before.
     */
    @Test
    public void testSequenceRemoveElement() throws Exception {
        assertEquals(((PictoFrame)seq.get(1)).getTitle(),"meme");
        seq.remove(1);
        assertEquals(((PictoFrame)seq.get(1)).getTitle(),"pepe");
    }
}
