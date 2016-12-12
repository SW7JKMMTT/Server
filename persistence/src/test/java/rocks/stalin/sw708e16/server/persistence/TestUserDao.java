package rocks.stalin.sw708e16.server.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.test.DatabaseTest;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
@Transactional
public class TestUserDao extends DatabaseTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void testTest() {
    }

    @Test
    public void testById_UserExists_Found() throws Exception {
        // Arrange
        User user = new GivenUser().withName("Edd", "Eddsen").withUsername("Edd").withPassword("hunter2").in(userDao);

        // Act
        User found = userDao.byId(user.getId());

        // Assert
        assertThat(found, notNullValue());
        assertThat(user, is(found));
        assertThat(user.getId(), is(found.getId()));
    }
}
