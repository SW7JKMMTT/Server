package dk.aau.giraf.rest.core;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Soren on 29-04-2016.
 */

public class PictoFrameTest {

    private Pictogram pictogram1;
    private Pictogram pictogram2;
    private Pictogram pictogram3;

    private User user1;
    private User user2;

    private Department department1;
    private Department department2;

    @Before
    public void prepareData(){
        department1 = new Department("meme");
        department2 = new Department("dank");
        user1 = new User(department1, "test1", "hunter2");
        user2 = new User(department2, "test2", "hunter2");
    }

    /**
     * Tests that user1 and user2 has access to a public pictogram
     */
    @Test
    public void testHasAccessToPublicPictogram(){
        pictogram1 = new Pictogram("rare", AccessLevel.PUBLIC, user1);
        Assert.assertTrue(pictogram1.hasPermission(user1));
        Assert.assertTrue(pictogram1.hasPermission(user2));
    }

    /**
     * Tests that user1 has access and user2 from another department does not to a "protected" pictogram
     */
    @Test
    public void testHasAccessToProtectedPictogram(){
        pictogram2 = new Pictogram("dank", AccessLevel.PROTECTED, user1);
        Assert.assertTrue(pictogram2.hasPermission(user1));
        Assert.assertFalse(pictogram2.hasPermission(user2));
    }

    /**
     * Tests that user1 has access to its private pictogram and user2 does not
     */
    @Test
    public void testHasAccessToPrivatePictogram(){
        pictogram3 = new Pictogram("pepe", AccessLevel.PRIVATE, user1.getDepartment(), user1);
        Assert.assertTrue(pictogram3.hasPermission(user1));
        Assert.assertFalse(pictogram3.hasPermission(user2));
    }
}
