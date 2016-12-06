package rocks.stalin.sw708e16.server.services.exceptions.mappers;

import org.springframework.stereotype.Component;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class JsonExceptionMapper implements ExceptionMapper<WebApplicationException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(WebApplicationException except) {
        return Response
            .status(except.getResponse().getStatus())
            .entity(new JsonError(except.getMessage()))
            .type(MediaType.APPLICATION_JSON_TYPE)
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
