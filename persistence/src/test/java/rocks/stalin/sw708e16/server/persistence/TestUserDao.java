package rocks.stalin.sw708e16.server.persistence;

import rocks.stalin.sw708e16.server.core.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
// Load the beans to configure, here the DAOs
@ContextConfiguration(locations = {"classpath:test-config.xml"})
// apply the transaction manager to the test class so every DAO methods are executed
// within a transaction
@Transactional
public class TestUserDao {
    private static final String CONTACT_PASSWORD_TEMP = "passw0rd";
    private static final String CONTACT_USERNAME_TEMP = "Judy";

    @Resource
    private UserDao userDao;

    @Test
    public void testIsInjected() {
        Assert.assertNotNull(userDao);
    }

    @Test
    public void testFindByUsername() {
        Assert.assertNotNull(userDao.byUsername("Jeff"));
    }

    @Test
    public void testFindByUsernameRightData() {
        User u = userDao.byUsername("Jeff");
        Assert.assertNotNull(u);
        Assert.assertEquals(u.getPassword(), "password123");
    }

    @Test
    public void testFindById() {
        User u = userDao.byId(1L);
        Assert.assertNotNull(u);
    }

    @Test
    public void testFindByIdRightData() {
        User u = userDao.byId(1L);
        Assert.assertNotNull(u);
        Assert.assertEquals(u.getPassword(), "password123");
    }

    @Test
    public void testFindAllContacts() {
        Assert.assertEquals(3, userDao.getAll().size());
    }

    @Test
    public void testAllHasResults() {
        Assert.assertFalse(userDao.getAll().isEmpty());
    }

    @Test
    public void testInsertContact() {
        User newUser = new User(CONTACT_USERNAME_TEMP, CONTACT_PASSWORD_TEMP);
        User user = userDao.byUsername(CONTACT_USERNAME_TEMP);
        Assert.assertNull(user);
        userDao.add(newUser);
        user = userDao.byUsername(CONTACT_USERNAME_TEMP);
        Assert.assertNotNull(user);
    }

    @Test
    public void testDeleteContact() {
        User newContact = new User(CONTACT_USERNAME_TEMP, CONTACT_PASSWORD_TEMP);
        User contact = userDao.byUsername(CONTACT_USERNAME_TEMP);
        Assert.assertNull(contact);
        userDao.add(newContact);
        contact = userDao.byUsername(CONTACT_USERNAME_TEMP);
        Assert.assertNotNull(contact);
        userDao.remove(contact);
        contact = userDao.byUsername(CONTACT_USERNAME_TEMP);
        Assert.assertNull(contact);
    }
}
