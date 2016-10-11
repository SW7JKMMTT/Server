package dk.aau.giraf.rest.given;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.persistence.UserDao;

public class GivenUser {
    private String name;
    private String password;
    private Department department;

    public GivenUser withName(String name) {
        this.name = name;
        return this;
    }

    public GivenUser withPassword(String passwod) {
        this.password = passwod;
        return this;
    }

    public GivenUser inDepartment(Department department) {
        this.department = department;
        return this;
    }

    public User in(UserDao userDao) {
        User user = new User(department, name, password);
        userDao.add(user);
        department.addMember(user);
        return user;
    }
}
