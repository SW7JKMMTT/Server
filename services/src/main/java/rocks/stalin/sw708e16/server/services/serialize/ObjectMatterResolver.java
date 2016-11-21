package rocks.stalin.sw708e16.server.services.serialize;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import rocks.stalin.sw708e16.server.services.serialize.ObjectIdSerializer;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

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
        module.addSerializer(ObjectId.class, new ObjectIdSerializer());
        module.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
        mapper.registerModule(module);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
