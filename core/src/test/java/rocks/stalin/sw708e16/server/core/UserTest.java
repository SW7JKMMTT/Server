package rocks.stalin.sw708e16.server.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testMerge() {
        User jeff = new User("Jeff", "passw0rd");
        User alternativeJeff = new User();
        alternativeJeff.setPassword("1337");

        jeff.merge(alternativeJeff);

        assertEquals(jeff.getPassword(), alternativeJeff.getPassword());
        assertEquals(jeff.getUsername(), "Jeff");
    }

    @Test
    public void testHasIcon() {
        User jeff = new User("Jeff", "passw0rd");
        assertFalse(jeff.getHasIcon());
        jeff.setIcon(new UserIcon());

        assertTrue(jeff.getHasIcon());
    }
}
