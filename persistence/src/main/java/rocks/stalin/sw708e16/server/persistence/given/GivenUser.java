package rocks.stalin.sw708e16.server.persistence.given;

import rocks.stalin.sw708e16.server.core.User;
import rocks.stalin.sw708e16.server.persistence.UserDao;

public class GivenUser {
    private String username;
    private String password;
    private String givenname;
    private String surname;

    public GivenUser() {
    }

    public GivenUser withUsername(String name) {
        this.username = name;
        return this;
    }

    public GivenUser withPassword(String passwod) {
        this.password = passwod;
        return this;
    }

    public GivenUser withName(String givenname, String surname) {
        this.givenname = givenname;
        this.surname = surname;
        return this;
    }

    /**
     * Create {@link User user} and insert into the DAO.
     * @param userDao The {@link UserDao UserDao} to insert the object into
     * @return The {@link User User} instance
     */
    public User in(UserDao userDao) {
        User user = new User(username, password, givenname, surname);
        userDao.add(user);
        return user;
    }
}
