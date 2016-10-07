package dk.aau.giraf.rest.persistence.file.dao;

import dk.aau.giraf.rest.core.PictogramImage;
import dk.aau.giraf.rest.persistence.PictogramImageFactory;
import dk.aau.giraf.rest.persistence.file.Readable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Transactional
@Repository
@Primary
@Qualifier("pictogramImage")
public class PictogramImageFileDaoImpl extends FileDaoImpl<PictogramImage> {
    private PictogramImageFactory factory;

    public PictogramImageFileDaoImpl() {
        factory = new PictogramImageFactory();
    }

    @Override
    public PictogramImage add(Readable file) throws IOException {
        return add(factory, file);
    }

    @Override
    public PictogramImage newOrUpdate(PictogramImage file, Readable newFile) throws IOException {
        return newOrUpdate(factory, file, newFile);
    }
}
