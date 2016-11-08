package rocks.stalin.sw708e16.test;

import com.mongodb.MongoClient;
import com.mongodb.connection.Connection;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import rocks.stalin.sw708e16.server.persistence.spring.connection.ConnectionInformationProvider;

public abstract class DatabaseTest {
    @Autowired
    ConnectionInformationProvider informationProvider;

    @Before
    public void clearDatabase() {
        MongoClient client = new MongoClient(informationProvider.getUrl(), informationProvider.getPort());
        client.getDatabase("test").drop();
    }
}
