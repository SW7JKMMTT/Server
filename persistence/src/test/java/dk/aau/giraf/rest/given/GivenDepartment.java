package dk.aau.giraf.rest.given;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.persistence.DepartmentDao;

public class GivenDepartment {
    public String name;

    public GivenDepartment withName(String name) {
        this.name = name;
        return this;
    }

    public Department in(DepartmentDao departmentDao) {
        Department department = new Department(name);
        departmentDao.add(department);
        return department;
    }
}
