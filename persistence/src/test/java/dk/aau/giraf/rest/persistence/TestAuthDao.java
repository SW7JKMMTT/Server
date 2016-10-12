package dk.aau.giraf.rest.persistence;

import com.mongodb.MongoClient;
import dk.aau.giraf.rest.DatabaseTest;
import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.core.authentication.AuthToken;
import dk.aau.giraf.rest.given.GivenAuthToken;
import dk.aau.giraf.rest.given.GivenDepartment;
import dk.aau.giraf.rest.given.GivenUser;
import org.hibernate.Session;
import org.hibernate.ogm.OgmSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestAuthDao extends DatabaseTest{

    @Resource
    DepartmentDao departmentDao;
    @Resource
    private AuthDao authDao;
    @Resource
    private UserDao userDao;
    private Department department;

    @Test
    public void testIsInjected() {
        assertNotNull(authDao);
    }

    @Test
    public void testAdd() throws Exception {
        Department dep = new Department();
        dep.setName("monstertruck2");
        departmentDao.add(dep);
        userDao.add(new User(dep, "Jeff", "password"));
        User u = userDao.byUsername(dep, "Jeff");
        AuthToken token = new AuthToken("AABBCC", u);

        assertNull(authDao.byTokenStr("AABBCC"));
        authDao.add(token);
        assertNotNull(authDao.byTokenStr("AABBCC"));
    }

    @Test
    public void testRemove() throws Exception {
        Department monster = new GivenDepartment().withName("mmonstertruck").in(departmentDao);
        User jeff = new GivenUser().withName("Jeff").withPassword("1000").inDepartment(monster).in(userDao);
        AuthToken token = new GivenAuthToken().forUser(jeff).withToken("AABBCC").in(authDao);

        assertNotNull(authDao.byTokenStr("AABBCC"));
        jeff.revokeToken(token);
        authDao.remove(token);
        assertNull(authDao.byTokenStr("AABBCC"));
    }

    @Test
    public void testByTokenStr() throws Exception {
        Department monster = new GivenDepartment().withName("mmonstertruck").in(departmentDao);
        User jeff = new GivenUser().withName("Jeff").withPassword("1000").inDepartment(monster).in(userDao);
        new GivenAuthToken().forUser(jeff).withToken("AABBCC").in(authDao);

        AuthToken token = authDao.byTokenStr("AABBCC");
        assertNotNull(token);
    }

    @Test
    public void testByTokenStrRightData() throws Exception {
        Department monster = new GivenDepartment().withName("mmonstertruck").in(departmentDao);
        User jeff = new GivenUser().withName("Jeff").withPassword("1000").inDepartment(monster).in(userDao);
        new GivenAuthToken().forUser(jeff).withToken("AABBCC").in(authDao);

        AuthToken token = authDao.byTokenStr("AABBCC");
        assertNotNull(token);
        assertEquals(token.getUser().getUsername(), jeff.getUsername());
    }

    @Test
    public void testByUser() throws Exception {
        Department monster = new GivenDepartment().withName("mmonstertruck").in(departmentDao);
        User jeff = new GivenUser().withName("Jeff").withPassword("1000").inDepartment(monster).in(userDao);
        new GivenAuthToken().forUser(jeff).withToken("FAKETOKEN").in(authDao);
        new GivenAuthToken().forUser(jeff).withToken("MOBILETOKEN").in(authDao);

        User u = userDao.byUsername(monster, "Jeff");

        AuthToken token1 = authDao.byTokenStr("FAKETOKEN");
        AuthToken token2 = authDao.byTokenStr("MOBILETOKEN");

        Collection<AuthToken> at = u.getAuthTokens();
        assertEquals(at.size(), 2);
        assertTrue(at.contains(token1));
        assertTrue(at.contains(token2));
    }
}
