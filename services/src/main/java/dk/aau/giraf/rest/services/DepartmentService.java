package dk.aau.giraf.rest.services;

import dk.aau.giraf.rest.core.Choice;
import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.Pictogram;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.core.authentication.PermissionType;
import dk.aau.giraf.rest.persistence.DepartmentDao;
import dk.aau.giraf.rest.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NoContentException;
import java.util.Collection;
import java.util.Map;

@Transactional
@Path("/department")
public class DepartmentService {

    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    /**
     * List all the {@link Department departments} in the system. This is public
     *
     * @return A collection of all the {@link Department departments}
     */
    @Path("/")
    @GET
    @Produces("application/json")
    public Collection<Department> listDepartments() {
        return departmentDao.getAll();
    }

    /**
     * Get a specific {@link Department department}. Anyone can do this
     *
     * @param id The id of the department
     * @return The {@link Department department}
     */
    @Path("/{id}/")
    @GET
    @Produces("application/json")
    public Department getDepartment(@PathParam("id") long id) {
        return departmentDao.byId(id);
    }

    /**
     * Get the {@link UserService userservice} to serve the department specific user endpoint.
     *
     * @param id The id of the {@link Department department}.
     * @return The {@link UserService userservice} that serves the {@link Department department}.
     */
    @Path("/{id}/user")
    public UserService getUserService(@PathParam("id") long id) {
        Department department = departmentDao.byId(id);
        userService.setDepartment(department);
        return userService;
    }

    @Autowired
    private SequenceService sequenceService;

    /**
     * Get the {@link SequenceService sequenceservice} to serve the department specific sequence endpoint.
     *
     * @param id The id of the {@link Department department}.
     * @return The {@link SequenceService sequenceservice} that serves the {@link Department department}.
     */
    @Path("/{id}/sequence")
    public SequenceService getSequenceService(@PathParam("id") long id) {
        Department department = departmentDao.byId(id);
        sequenceService.setDepartment(department);
        return sequenceService;
    }

    @Autowired
    private ChoiceService choiceService;

    /**
     * Get the {@link ChoiceService choiceService} to serve the department specific sequence endpoint.
     *
     * @param id The id of the {@link Department department}.
     * @return The {@link ChoiceService choiceService} that serves the {@link Department department}.
     */
    @Path("/{id}/choice")
    public ChoiceService getChoiceService(@PathParam("id") long id) {
        Department department = departmentDao.byId(id);
        choiceService.setDepartment(department);
        return choiceService;
    }

    /**
     * Get the {@link AuthenticationService authservice} to serve the department specific auth endpoint.
     *
     * @param id The id of the {@link Department department}.
     * @return The {@link AuthenticationService authservice} that serves the {@link Department department}.
     */
    @Path("/{id}/auth")
    public AuthenticationService getAuthenticationService(@PathParam("id") long id) {
        Department department = departmentDao.byId(id);
        authenticationService.setDepartment(department);
        return authenticationService;
    }

    @Autowired
    private PictogramService pictogramService;

    /**
     * Get the {@link PictogramService pictogramservice} to serve the department specific pictogram endpoint.
     *
     * @param id The id of the {@link Department department}
     * @return The {@link PictogramService pictogramservice} that serves the {@link Department department}
     */
    @Path("/{id}/pictogram")
    public PictogramService getPictogramService(@PathParam("id") long id) {
        Department department = departmentDao.byId(id);
        pictogramService.setDepartment(department);
        return pictogramService;
    }

    @Autowired
    private UserDao userDao;

    @GET
    @Path("/test")
    @Produces("application/json")
    public Department createPictogram(@Context User user)
            throws javassist.NotFoundException
    {
        Department dep = new Department();
        dep.setName("Test");
        User user1 = new User(dep, "Wutface", "password");
        User user2 = new User(dep, "Wutface01", "sataiergay");

        departmentDao.add(dep);
        userDao.add(user1);
        userDao.add(user2);
        dep.addMember(user1);
        dep.addMember(user2);
        return dep;
    }
}
