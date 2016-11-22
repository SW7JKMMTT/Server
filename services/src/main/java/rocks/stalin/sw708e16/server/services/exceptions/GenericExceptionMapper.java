package rocks.stalin.sw708e16.server.services.exceptions;

import org.springframework.stereotype.Component;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(Exception cee) {
        return Response
            .status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(new JsonError(cee.getMessage()))
            .build();
    }

    private class JsonError {
        private String message;

        private JsonError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
