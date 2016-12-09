package rocks.stalin.sw708e16.server.core;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TestUser {
    @Test
    public void testHasIcon_WithNoIcon_IsFalse() {
        // Arrange
        User jeff = new User("Jeff", "passw0rd", "Jeff", "Lam");

        // Act
        // ...

        // Assert
        assertThat(jeff.getHasIcon(), is(false));
    }

    @Test
    public void testHasIcon_WithIcon_IsTrue() throws Exception {
        // Arrange
        User jeff = new User("Jeff", "passw0rd", "Jeff", "Lam");

        // Act
        jeff.setIcon(new UserIcon());

        // Assert
        assertThat(jeff.getHasIcon(), is(true));
    }
}
