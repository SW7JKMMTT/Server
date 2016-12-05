package rocks.stalin.sw708e16.server.core;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {
    @Test
    public void testHasIcon() {
        User jeff = new User("Jeff", "passw0rd", "Jeff", "Lam");
        assertFalse(jeff.getHasIcon());
        jeff.setIcon(new UserIcon());

        assertTrue(jeff.getHasIcon());
    }
}
