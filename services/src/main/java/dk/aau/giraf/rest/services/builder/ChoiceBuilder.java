package dk.aau.giraf.rest.services.builder;

import dk.aau.giraf.rest.core.*;
import dk.aau.giraf.rest.persistence.DepartmentDao;
import dk.aau.giraf.rest.persistence.FrameDao;
import dk.aau.giraf.rest.persistence.UserDao;

import javax.ws.rs.NotAllowedException;
import java.util.ArrayList;
import java.util.List;

public class ChoiceBuilder {
    private Long[] optionIds = null;

    public ChoiceBuilder() {
    }

    public void setOptionIds(Long[] optionIds) {
        this.optionIds = optionIds;
    }

    /**
     * Builds the sequence object.
     *
     * @param frameDao used to retrieve the frames
     * @return the new sequence object
     */
    public Choice build(FrameDao frameDao) {
        List<PictoFrame> options;
        options = findByIds(frameDao);
        return new Choice(options);
    }

    /**
     * Merges data sent with existing sequence already in database.
     *
     * @param frameDao Used to retrieve the frames
     */
    public void merge(FrameDao frameDao, Choice choice) {
        List<PictoFrame> options;
        options = findByIds(frameDao);
        choice.clear();
        choice.addAll(options);
    }

    /**
     * Adds all Frames according to the ids, and returns them in correct order.
     *
     * @param frameDao used to retrieve the frames
     */
    private List<PictoFrame> findByIds(FrameDao frameDao) {
        List<PictoFrame> options = new ArrayList<>();
        for (int index = 0; index < optionIds.length; index++) {
            Frame frame = frameDao.byId(optionIds[index]);
            if (frame == null)
                throw new NotAllowedException("You cannot use non existent frames");

            options.add((PictoFrame) frame);
        }

        return options;
    }
}
