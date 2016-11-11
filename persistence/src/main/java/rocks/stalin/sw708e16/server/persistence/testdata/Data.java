package rocks.stalin.sw708e16.server.persistence.testdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.stalin.sw708e16.server.persistence.UserDao;
import rocks.stalin.sw708e16.server.persistence.given.GivenUser;
import rocks.stalin.sw708e16.server.spring.datainserter.DevelopmentData;
import rocks.stalin.sw708e16.server.spring.datainserter.DevelopmentDataInserter;

@Service
@DevelopmentData
public class Data extends DevelopmentDataInserter {
    @Autowired
    UserDao userDao;

    @Override
    public void insert() {
        new GivenUser().withName("Jeff").withPassword("fuck").in(userDao);
    }
}
