package dk.aau.giraf.rest;

import com.mongodb.MongoClient;
import org.junit.Before;

public abstract class DatabaseTest {
    @Before
    public void ClearDatabase() {
        MongoClient client = new MongoClient("localhost", 1338);
        client.getDatabase("test").drop();
    }
}
