package dk.aau.giraf.rest.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PictogramTest {

    @Test
    public void testMerge() {
        Department d1 = new Department();
        Pictogram p1 = new Pictogram("derp", AccessLevel.PUBLIC, new User(d1, "Jeff", "hunter2"));
        Pictogram p2 = new Pictogram();
        p2.setTitle("herp");

        p1.merge(p2);

        assertEquals(p1.getTitle(), p2.getTitle());
        assertEquals(p1.getTitle(), "herp");

        p2.setOwner(new User(d1, "jeffy", "hunter2"));
        p1.merge(p2);

        assertEquals(p1.getOwner(), p2.getOwner());
        assertEquals(p1.getOwner().getUsername(), "jeffy");
    }
}
