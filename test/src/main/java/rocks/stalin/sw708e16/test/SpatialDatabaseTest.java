package rocks.stalin.sw708e16.test;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class SpatialDatabaseTest extends DatabaseTest {
    @PersistenceContext
    private EntityManager em;
    private FullTextEntityManager ftem;

    protected void flushIndex() {
        ftem = Search.getFullTextEntityManager(em);
        ftem.flushToIndexes();
    }
}
