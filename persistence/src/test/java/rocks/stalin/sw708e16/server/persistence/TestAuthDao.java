package rocks.stalin.sw708e16.server.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.core.authentication.AuthToken;
import rocks.stalin.sw708e16.test.DatabaseTest;
import rocks.stalin.sw708e16.server.persistence.given.GivenAuthToken;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;

import javax.annotation.Resource;
import java.util.Collection;

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
        userDao.add(new User("Jeff", "password"));
        User jeff = userDao.byUsername("Jeff");
        AuthToken token = new AuthToken("AABBCC", jeff);

        assertNull(authDao.byTokenStr("AABBCC"));
        authDao.add(token);
        assertNotNull(authDao.byTokenStr("AABBCC"));
    }

    @Test
    public void testRemove() throws Exception {
        User jeff = new GivenUser().withName("Jeff").withPassword("1000").in(userDao);
        AuthToken token = new GivenAuthToken().forUser(jeff).withToken("AABBCC").in(authDao);

        assertNotNull(authDao.byTokenStr("AABBCC"));
        jeff.revokeToken(token);
        authDao.remove(token);
        assertNull(authDao.byTokenStr("AABBCC"));
    }

    @Test
    public void testByTokenStr() throws Exception {
        User jeff = new GivenUser().withName("Jeff").withPassword("1000").in(userDao);
        new GivenAuthToken().forUser(jeff).withToken("AABBCC").in(authDao);

        AuthToken token = authDao.byTokenStr("AABBCC");
        assertNotNull(token);
    }

    @Test
    public void testByTokenStrRightData() throws Exception {
        User jeff = new GivenUser().withName("Jeff").withPassword("1000").in(userDao);
        new GivenAuthToken().forUser(jeff).withToken("AABBCC").in(authDao);

        AuthToken token = authDao.byTokenStr("AABBCC");
        assertNotNull(token);
        assertEquals(token.getUser().getUsername(), jeff.getUsername());
    }

    @Test
    public void testByUser() throws Exception {
        User jeff = new GivenUser().withName("Jeff").withPassword("1000").in(userDao);
        new GivenAuthToken().forUser(jeff).withToken("FAKETOKEN").in(authDao);
        new GivenAuthToken().forUser(jeff).withToken("MOBILETOKEN").in(authDao);

        User databaseJeff = userDao.byUsername("Jeff");

        AuthToken token1 = authDao.byTokenStr("FAKETOKEN");
        AuthToken token2 = authDao.byTokenStr("MOBILETOKEN");

        Collection<AuthToken> at = databaseJeff.getAuthTokens();
        assertEquals(at.size(), 2);
        assertTrue(at.contains(token1));
        assertTrue(at.contains(token2));
    }
}
