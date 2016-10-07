package dk.aau.giraf.rest.persistence;

import dk.aau.giraf.rest.core.Pictogram;
import dk.aau.giraf.rest.core.User;

import java.util.Collection;

public interface PictogramDao extends BaseDao<Pictogram> {
    Collection<Pictogram> getAllPublicPictograms();

    Collection<Pictogram> getAll(User user);

    Collection<Pictogram> searchByTitle(String title);

    Collection<Pictogram> searchByTitle(User user, String title);

    Pictogram byId(long id);
}
