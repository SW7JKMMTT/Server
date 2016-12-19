package rocks.stalin.sw708e16.server.persistence;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Transactional
public class SpringHibernateSearchReIndexer {
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void reIndex() throws InterruptedException {
        FullTextEntityManager ftem = Search.getFullTextEntityManager(em);
        ftem.createIndexer().startAndWait();
    }
}
