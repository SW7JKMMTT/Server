package dk.aau.giraf.rest.persistence.hibernate;

import dk.aau.giraf.rest.core.AccessLevel;
import dk.aau.giraf.rest.core.Pictogram;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.persistence.BaseDaoImpl;
import dk.aau.giraf.rest.persistence.PictogramDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class PictogramDaoImpl extends BaseDaoImpl<Pictogram> implements PictogramDao {
    /**
     * @return all public pictograms.
     */
    @Override
    public Collection<Pictogram> getAllPublicPictograms() {
        TypedQuery<Pictogram> query =
            em.createQuery("SELECT p FROM Pictogram p WHERE p.accessLevel = :accesslevel", Pictogram.class);
        query.setParameter("accesslevel", AccessLevel.PUBLIC);
        return query.getResultList();
    }

    /**
     * Gets all this user can access.
     *
     * @param user for which pictograms are wanted
     * @return all the pictograms this user can access
     */
    @Override
    public Collection<Pictogram> getAll(User user) {
        // TODO: Implement institutions in this.
        TypedQuery<Pictogram> query = em.createQuery("SELECT p " +
                "FROM Pictogram p " +
                "WHERE (p.accessLevel = :public) " +
                "OR (p.accessLevel = :protected)" +
                "OR (p.accessLevel = :private AND p.owner = :user)",
            Pictogram.class);
        query.setParameter("public", AccessLevel.PUBLIC);
        // TODO: Implement protected in other places
        query.setParameter("protected", AccessLevel.PROTECTED);
        query.setParameter("private", AccessLevel.PRIVATE);
        query.setParameter("user", user);

        return query.getResultList();
    }

    /**
     * Search for public pictograms by title.
     *
     * @param title to search for (substring of real title)
     * @return pictograms whose title include the query
     */
    @Override
    public Collection<Pictogram> searchByTitle(String title) {
        TypedQuery<Pictogram> query = em.createQuery("SELECT p FROM Pictogram p " +
            "WHERE (p.title LIKE :title) " +
            "AND p.accessLevel = :public", Pictogram.class);
        query.setParameter("title", "%" + title + "%");
        query.setParameter("public", AccessLevel.PUBLIC);
        return query.getResultList();
    }

    /**
     * Search for all the pictograms a user can access by title
     *
     * @param user  whose pictograms are serached though
     * @param title to search for (substring of real title)
     * @return pictograms whose title include the query that the user can access.
     */
    @Override
    public Collection<Pictogram> searchByTitle(User user, String title) {
        TypedQuery<Pictogram> query = em.createQuery("SELECT p FROM Pictogram p " +
            "WHERE (p.title LIKE :title) " +
            "AND (p.accessLevel = :public " +
            "OR (p.accessLevel = :protected AND  p.department = :usersdepartment) " +
            "OR (p.accessLevel = :private AND p.owner = :user))", Pictogram.class);
        query.setParameter("title", "%" + title + "%");
        query.setParameter("public", AccessLevel.PUBLIC);
        query.setParameter("protected", AccessLevel.PROTECTED);
        query.setParameter("private", AccessLevel.PRIVATE);
        query.setParameter("user", user);
        query.setParameter("usersdepartment", user.getDepartment());
        return query.getResultList();
    }

    /**
     * Gets a pictogram by id
     *
     * @param id of the pictogram to get
     * @return the pictogram or null if not found.
     */
    @Override
    public Pictogram byId(long id) {
        TypedQuery<Pictogram> query = em.createQuery("SELECT p FROM Pictogram p WHERE p.id = :id", Pictogram.class);
        query.setParameter("id", id);
        return getFirst(query);
    }
}
