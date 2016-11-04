package rocks.stalin.sw708e16.test.given;

import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.persistence.UserDao;

public class GivenUser {
    private String name;
    private String password;

    public GivenUser withName(String name) {
        this.name = name;
        return this;
    }

    public GivenUser withPassword(String passwod) {
        this.password = passwod;
        return this;
    }

    /**
     * Create {@link User user} and insert into the DAO.
     * @param userDao The {@link UserDao UserDao} to insert the object into
     * @return The {@link User User} instance
     */
    public User in(UserDao userDao) {
        User user = new User(name, password);
        userDao.add(user);
        return user;
    }
}
