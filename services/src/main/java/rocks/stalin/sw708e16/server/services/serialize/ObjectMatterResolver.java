package rocks.stalin.sw708e16.server.services.serialize;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ObjectMatterResolver implements ContextResolver<ObjectMapper> {
    private ObjectMapper mapper;

    /**
     * Injects the {@link ObjectId} (de)serializer into the {@link ObjectMapper} used by jackson.
     */
    public ObjectMatterResolver() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("bsonSerializer", new Version(1, 0, 0, "final", null, null));
        module.setMixInAnnotation(ObjectId.class, ObjectIdMixIn.class);
        mapper.registerModule(module);
        mapper.registerModule(new Hibernate5Module());
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
