package rocks.stalin.sw708e16.server.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.test.DatabaseTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
@Transactional
public class TestUserDao extends DatabaseTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void testTest() {
    }
    //TODO: THESE NEED TO BE WRITTEN YOU FAGGOT
    //TODO: THESE NEED TO BE WRITTEN YOU FAGGOT

}
