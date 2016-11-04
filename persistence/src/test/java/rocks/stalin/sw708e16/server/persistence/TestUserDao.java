package rocks.stalin.sw708e16.server.persistence;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.test.DatabaseTest;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-config.xml"})
@Transactional
public class TestUserDao extends DatabaseTest {
    @Resource
    private UserDao userDao;

    //TODO: THESE NEED TO BE WRITTEN YOU FAGGOT

    //TODO: THESE NEED TO BE WRITTEN YOU FAGGOT

}
