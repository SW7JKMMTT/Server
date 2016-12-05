package rocks.stalin.sw708e16.server.services.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ConflictException extends WebApplicationException {
    public ConflictException(String string) {
        super(string, Response.Status.CONFLICT);
    }
}
