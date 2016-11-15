package rocks.stalin.sw708e16.server.persistence;

import org.bson.types.ObjectId;
import org.hibernate.search.bridge.builtin.StringBridge;

public class ObjectIdBridge extends StringBridge {
    @Override
    public Object stringToObject(String stringValue) {
        return new ObjectId(stringValue);
    }

    @Override
    public String objectToString(Object object) {
        ObjectId id = (ObjectId)object;
        return id.toHexString();
    }
}
