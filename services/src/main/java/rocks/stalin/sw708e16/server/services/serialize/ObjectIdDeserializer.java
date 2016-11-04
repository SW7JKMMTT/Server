package rocks.stalin.sw708e16.server.services.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {

    @Override
    public ObjectId deserialize(JsonParser parser, DeserializationContext context)
        throws IOException, JsonProcessingException
    {
        String value = parser.readValueAs(String.class);
        return new ObjectId(value);
    }
}
