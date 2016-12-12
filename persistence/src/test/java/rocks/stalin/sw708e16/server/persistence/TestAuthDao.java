package rocks.stalin.sw708e16.server.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.server.persistence.given.GivenAuthToken;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.test.DatabaseTest;

import javax.annotation.Resource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:dao-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestAuthDao extends DatabaseTest {

    @Resource
    private AuthDao authDao;
    @Resource
    private UserDao userDao;

    @Test
    public void testIsInjected() {
        assertNotNull(authDao);
    }

    @Test
    public void testAdd() throws Exception {
        User jeff = new GivenUser().withName("Jeff", "Lam").withUsername("Jeff").withPassword("password").in(userDao);
        AuthToken token = new AuthToken("AABBCC", jeff);

        assertNull(authDao.byTokenStr_ForAuthorization("AABBCC"));
        authDao.add(token);
        assertNotNull(authDao.byTokenStr_ForAuthorization("AABBCC"));
    }

    @Test
    public void testRemove() throws Exception {
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("1000").in(userDao);
        AuthToken token = new GivenAuthToken().forUser(jeff).withToken("AABBCC").in(authDao);

        assertNotNull(authDao.byTokenStr_ForAuthorization("AABBCC"));
        jeff.revokeToken(token);
        authDao.remove(token);
        assertNull(authDao.byTokenStr_ForAuthorization("AABBCC"));
    }

    @Test
    public void testByTokenStr() throws Exception {
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("1000").in(userDao);
        new GivenAuthToken().forUser(jeff).withToken("AABBCC").in(authDao);

        AuthToken token = authDao.byTokenStr_ForAuthorization("AABBCC");
        assertNotNull(token);
    }

    @Test
    public void testByTokenStrRightData() throws Exception {
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("1000").in(userDao);
        new GivenAuthToken().forUser(jeff).withToken("AABBCC").in(authDao);

        AuthToken token = authDao.byTokenStr_ForAuthorization("AABBCC");
        assertNotNull(token);
        assertEquals(token.getUser().getUsername(), jeff.getUsername());
    }

    @Test
    public void testByUser() throws Exception {
        User jeff = new GivenUser().withName("Jeff", "Jeffsen").withUsername("Jeff").withPassword("1000").in(userDao);

        boolean wasFound = userDao.usernameIsTaken("Jeff");

        assertThat(wasFound, is(true));
    }
}
