package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.Frame;
import dk.aau.giraf.rest.core.Sequence;
import dk.aau.giraf.rest.core.User;

import java.util.Collection;
import java.util.List;

public interface FrameDao extends BaseDao<Frame> {
    Frame byId(long id);
}
