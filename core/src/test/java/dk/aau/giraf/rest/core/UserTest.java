package dk.aau.giraf.rest.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testMerge() {
        Department department = new Department();
        Department department2 = new Department();
        User u = new User(department, "Jeff", "passw0rd");
        User u2 = new User();
        u2.setPassword("1337");
        u2.setDepartment(department2);

        u.merge(u2);

        assertEquals(u.getPassword(), u2.getPassword());
        assertEquals(u.getUsername(), "Jeff");
        assertEquals(u.getDepartment(), u2.getDepartment());
    }

    @Test
    public void testHasIcon() {
        Department department = new Department();
        User u = new User(department, "Jeff", "passw0rd");
        assertFalse(u.getHasIcon());
        u.setIcon(new UserIcon());

        assertTrue(u.getHasIcon());
    }
}
