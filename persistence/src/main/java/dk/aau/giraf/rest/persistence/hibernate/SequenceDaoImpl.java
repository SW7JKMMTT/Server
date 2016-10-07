package dk.aau.giraf.rest.persistence.hibernate;

import dk.aau.giraf.rest.core.AccessLevel;
import dk.aau.giraf.rest.core.Department;
import dk.aau.giraf.rest.core.Sequence;
import dk.aau.giraf.rest.core.User;
import dk.aau.giraf.rest.persistence.BaseDaoImpl;
import dk.aau.giraf.rest.persistence.SequenceDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collection;

@Transactional
@Repository
@Primary
public class SequenceDaoImpl extends BaseDaoImpl<Sequence> implements SequenceDao {
    /**
     * Will search the database for any sequences belonging to the department sent as input.
     *
     * @param department the department being searched for
     * @return A collection of Sequences which are all belonging to the department
     */
    @Override
    public Collection<Sequence> byDepartment(Department department) {
        TypedQuery<Sequence> query = em.createQuery(
                "SELECT s " +
                    "FROM Sequence s " +
                        "WHERE (s.department = :department " +
                            "AND s.accessLevel = :depaccessLevel) " +
                            "OR (s.accessLevel = :accessLevel) ",
                Sequence.class);
        query.setParameter("department", department);
        query.setParameter("accessLevel", AccessLevel.PUBLIC);
        query.setParameter("depaccessLevel", AccessLevel.PROTECTED);
        return query.getResultList();
    }

    /**
     * Searched the database for a Sequence with the id that is sent as a parameter.
     *
     * @param id the id being searched for
     * @return returns a Sequence which has the id that was sent as input
     */
    @Override
    public Sequence byId(long id) {
        TypedQuery<Sequence> query = em.createQuery("SELECT s FROM Sequence s WHERE s.id = :id", Sequence.class);
        query.setParameter("id", id);
        return getFirst(query);
    }

    /**
     * Searches the database for Sequences containing the string being searched for.
     * While filtering for sequences which the user has access to
     *
     * @param user  The user searching
     * @param title The string being searched for
     * @return A filtered collection of sequences which Titles contains the searchstring title, and is filtered such that
     *         the user do not get any Sequences which it does not have access to
     */
    @Override
    public Collection<Sequence> searchByUserAndTitle(User user, String title) {
        TypedQuery<Sequence> query = em.createQuery(
                "SELECT s " +
                "FROM Sequence s " +
                "WHERE (LOWER(s.title) LIKE :title) " +
                    "AND (s.accessLevel = :public " +
                        "OR (s.accessLevel = :protected " +
                            "AND  s.department = :usersdepartment) " +
                        "OR (s.accessLevel = :private " +
                            "AND s.owner = :user))",
                Sequence.class);
        query.setParameter("title", "%" + title.toLowerCase() + "%");
        query.setParameter("public", AccessLevel.PUBLIC);
        query.setParameter("protected", AccessLevel.PROTECTED);
        query.setParameter("private", AccessLevel.PRIVATE);
        query.setParameter("user", user);
        query.setParameter("usersdepartment", user.getDepartment());
        return query.getResultList();
    }
}
