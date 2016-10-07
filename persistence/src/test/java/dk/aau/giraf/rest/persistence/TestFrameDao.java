package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestFrameDao {

    @Resource
    private FrameDao frameDao;

    @Test
    public void testFrameById() throws Exception {
        Frame frame = frameDao.byId(4);
        assertNotNull(frame);
        assertEquals(frame.getId(), 4L);
        assertEquals(((Sequence) frame).getTitle(), "dank");
    }
}
