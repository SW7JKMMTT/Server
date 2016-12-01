package rocks.stalin.sw708e16.server.services.exceptions.mappers;

import org.springframework.stereotype.Component;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(ForbiddenException except) {
        if (headers.getMediaType().equals(MediaType.APPLICATION_JSON_TYPE)) {
            return Response
                .status(except.getResponse().getStatus())
                .entity(new JsonError(except.getMessage()))
                .type(headers.getMediaType())
                .build();
        }

        throw except;
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
