package rocks.stalin.sw708e16.server.services.serialize;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public abstract class ObjectIdMixIn {
    @JsonCreator
    public ObjectIdMixIn(String hexString) {
    }

    @JsonValue
    public abstract String toHexString();
}
