package rocks.stalin.sw708e16.server.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testMerge() {
        User u = new User("Jeff", "passw0rd");
        User u2 = new User();
        u2.setPassword("1337");

        u.merge(u2);

        assertEquals(u.getPassword(), u2.getPassword());
        assertEquals(u.getUsername(), "Jeff");
    }

    @Test
    public void testHasIcon() {
        User u = new User("Jeff", "passw0rd");
        assertFalse(u.getHasIcon());
        u.setIcon(new UserIcon());

        assertTrue(u.getHasIcon());
    }
}
