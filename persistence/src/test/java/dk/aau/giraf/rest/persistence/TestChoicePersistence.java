package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.*;
import dk.aau.giraf.rest.core.Choice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestChoicePersistence {

    @Resource
    private UserDao userDao;
    private User user;

    private Pictogram  pictogramA;
    private Pictogram  pictogramB;
    private Pictogram  pictogramC;
    private Pictogram  pictogramD;
    private Pictogram  pictogramE;
    private Choice     choice;
    private List<PictoFrame> choicesToInsert = new ArrayList<PictoFrame>();

    @Resource
    private SequenceDao sequenceDao;

    private Department department;

    @Resource
    DepartmentDao departmentDao;

    @Before
    public void prepareData() {
        department = departmentDao.byName("monstertruck");
        user = userDao.byUsername(department, "Derp");
        pictogramA = new Pictogram("Dat", AccessLevel.PUBLIC, user);
        pictogramB = new Pictogram("Boi", AccessLevel.PUBLIC, user);
        pictogramC = new Pictogram("O", AccessLevel.PUBLIC, user);
        pictogramD = new Pictogram("shit", AccessLevel.PUBLIC, user);
        pictogramE = new Pictogram("waddup", AccessLevel.PUBLIC, user);

        choicesToInsert.addAll(Arrays.asList(pictogramA, pictogramB));
        choice = new Choice(choicesToInsert);
    }

    @Test
    public void testChoiceCreation() throws Exception {
        assertFalse("Creation of choice went wrong", compareListToIterator(choicesToInsert, choice.iterator()));
    }

    @Test
    public void testChoiceSingleOptionAdd() throws Exception {
        choicesToInsert.add(pictogramC);
        choice.add(pictogramC);
        assertFalse("Adding to choice went wrong", compareListToIterator(choicesToInsert, choice.iterator()));
    }

    @Test
    public void testChoiceMultipleOptionsAdd() throws Exception {
        List<PictoFrame> moar = Arrays.asList(pictogramD, pictogramE);
        choicesToInsert.addAll(moar);
        choice.addAll(moar);
        assertFalse("Adding list to choice went wrong", compareListToIterator(choicesToInsert, choice.iterator()));
    }

    private boolean compareListToIterator(List<PictoFrame> choicesA, Iterator<PictoFrame> choicesB) throws Exception {
        for (PictoFrame pictoFrame : choicesA) {
            if(!choicesB.hasNext() || !pictoFrame.getTitle().equals(choicesB.next().getTitle()));
                return false;
        }
        return true;
    }
}
