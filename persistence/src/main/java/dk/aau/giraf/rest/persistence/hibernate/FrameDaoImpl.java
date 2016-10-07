package dk.aau.giraf.rest.persistence.hibernate;

import dk.aau.giraf.rest.core.*;
import dk.aau.giraf.rest.persistence.BaseDaoImpl;
import dk.aau.giraf.rest.persistence.FrameDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.LongStream;

@Transactional
@Repository
@Primary
public class FrameDaoImpl extends BaseDaoImpl<Frame> implements FrameDao {

    @Override
    public Frame byId(long id) {
        TypedQuery<Frame> query = em.createQuery("SELECT f FROM Frame f WHERE f.id = :id", Frame.class);
        query.setParameter("id", id);
        return getFirst(query);
    }
}
