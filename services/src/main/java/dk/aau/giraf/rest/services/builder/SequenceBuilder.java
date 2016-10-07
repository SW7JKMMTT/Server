package dk.aau.giraf.rest.services.builder;

import dk.aau.giraf.rest.core.*;
import dk.aau.giraf.rest.persistence.DepartmentDao;
import dk.aau.giraf.rest.persistence.FrameDao;
import dk.aau.giraf.rest.persistence.UserDao;

import javax.ws.rs.NotAllowedException;
import java.util.ArrayList;
import java.util.List;

public class SequenceBuilder {
    private String title = null;
    private Long ownerId = null;
    private Long departmentId = null;
    private AccessLevel accessLevel = null;
    private Long[] elementIds = null;
    private Long thumbnailId = null;
    private boolean elementsSet = false;


    public SequenceBuilder() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setThumbnailId(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public void setElementIds(Long[] elementIds) {
        this.elementIds = elementIds;
        elementsSet = true;
    }

    /**
     * Builds the sequence object.
     *
     * @param userDao used to retrieve the user
     * @param departmentDao used to retrieve the department
     * @param frameDao used to retrieve the frames
     * @return the new sequence object
     */
    public Sequence build(UserDao userDao, DepartmentDao departmentDao, FrameDao frameDao) {
        Department department = null;
        if (departmentId != null)
            department = departmentDao.byId(departmentId);

        User owner = null;
        if (ownerId != null)
            owner = userDao.byId(department, ownerId);

        Pictogram thumbnail = null;
        if (thumbnailId != null)
            thumbnail = (Pictogram) frameDao.byId(thumbnailId);

        List<Frame> elements;
        if (elementsSet) {
            elements = new ArrayList<>();
            findByIds(elements, frameDao);
            return new Sequence(title, accessLevel, department, owner, elements, thumbnail);
        }

        return new Sequence(title, accessLevel, department, owner, thumbnail);
    }

    /**
     * Merges data sent with existing sequence already in database
     * @param userDao Used to retrieve the user
     * @param departmentDao Used to retrieve the department
     * @param frameDao Used to retrieve the frames
     * @param sequence Sequence to be changed
     */
    public void merge(UserDao userDao, DepartmentDao departmentDao, FrameDao frameDao, Sequence sequence) {
        Department department;
        if(title != null)
            sequence.setTitle(title);

        if(departmentId != null) {
            department = departmentDao.byId(departmentId);
            sequence.setDepartment(department);
        }

        if(ownerId != null)
            sequence.setOwner(userDao.byId(sequence.getDepartment(),ownerId));

        if(thumbnailId != null)
            sequence.setThumbnail((Pictogram) frameDao.byId(thumbnailId));

        List<Frame> elements;
        if (elementsSet) {
            elements = new ArrayList<>();
            findByIds(elements, frameDao);
            sequence.clear();
            sequence.addAll(elements);
        }
    }

    /**
     * Adds all Frames according to the ids, and returns them in correct order
     * @param elements list for frames to be added to
     * @param frameDao used to retrieve the frames
     */
    private void findByIds(List<Frame> elements, FrameDao frameDao) {
        for (int index = 0; index < elementIds.length; index++) {
            Frame frame = frameDao.byId(elementIds[index]);
            if (frame == null)
                throw new NotAllowedException("You cannot use non existent frames");

            elements.add(frame);
        }
    }
}
