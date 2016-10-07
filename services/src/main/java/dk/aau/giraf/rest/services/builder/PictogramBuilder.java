package dk.aau.giraf.rest.services.builder;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.aau.giraf.rest.core.AccessLevel;
import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.Pictogram;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.persistence.DepartmentDao;
import dk.aau.giraf.rest.persistence.UserDao;
import javassist.NotFoundException;

public class PictogramBuilder {
    private String title;
    private long ownerId;
    private long departmentId;
    private AccessLevel accessLevel;

    public PictogramBuilder() {
    }

    /**
     * Name of the pictogram.
     * @param title Name
     */
    @JsonProperty
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Id of the {@link User user} that owns the pictogram.
     * @param ownerId id of {@link User user}
     */
    @JsonProperty
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Id of the {@link Department department} that owns the pictogram.
     * @param departmentId id of the {@link Department department}
     */
    @JsonProperty
    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * Access level of the pictogram.
     * @param accessLevel Access level
     */
    @JsonProperty
    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    /**
     * Build the pictogram from the given properties.
     * @param userDao {@link UserDao dao} used to fetch owner
     * @param departmentDao {@link DepartmentDao dao} used to fetch department
     * @return A Pictogram object built from the propertied
     * @throws NotFoundException If the user (or department?) is not found
     */
    public Pictogram build(UserDao userDao, DepartmentDao departmentDao) throws NotFoundException {
        Department department = departmentDao.byId(departmentId);
        User user = userDao.byId(department, ownerId);
        if(user == null)
            throw new NotFoundException("user not found");

        return new Pictogram(title, accessLevel, user);
    }
}
