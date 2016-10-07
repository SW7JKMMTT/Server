package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.core.authentication.AuthToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestAuthDao {

    @Resource
    DepartmentDao departmentDao;
    @Resource
    private AuthDao authDao;
    @Resource
    private UserDao userDao;
    private Department department;

    @Before
    public void getDepartmentId() {
        department = departmentDao.byName("LEGACY");
    }

    @Test
    public void testIsInjected() {
        assertNotNull(authDao);
    }

    @Test
    public void testAdd() throws Exception {
        User u = userDao.byUsername(department, "Jeff");
        AuthToken token = new AuthToken("AABBCC", u);

        assertNull(authDao.byTokenStr("AABBCC"));
        authDao.add(token);
        assertNotNull(authDao.byTokenStr("AABBCC"));
    }

    @Test
    public void testRemove() throws Exception {
        User u = userDao.byUsername(department, "Jeff");
        AuthToken token = new AuthToken("AABBCC", u);
        authDao.add(token);

        assertNotNull(authDao.byTokenStr("AABBCC"));
        authDao.remove(token);
        assertNull(authDao.byTokenStr("AABBCC"));
    }

    @Test
    public void testByTokenStr() throws Exception {
        AuthToken token = authDao.byTokenStr("FAKETOKEN");
        assertNotNull(token);
    }

    @Test
    public void testByTokenStrRightData() throws Exception {
        AuthToken token = authDao.byTokenStr("FAKETOKEN");
        assertNotNull(token);
        assertEquals(token.getUser().getUsername(), "Jeff");
    }

    @Test
    public void testByUser() throws Exception {
        User u = userDao.byUsername(department, "Jeff");

        AuthToken token1 = authDao.byTokenStr("FAKETOKEN");
        AuthToken token2 = authDao.byTokenStr("MOBILETOKEN");

        Collection<AuthToken> at = u.getAuthTokens();
        assertEquals(at.size(), 2);
        assertTrue(at.contains(token1));
        assertTrue(at.contains(token2));
    }
}
