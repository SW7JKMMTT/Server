package rocks.stalin.sw708e16.server.spring.datainserter;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public abstract class DevelopmentDataInserter {
    public abstract void insert();
}
