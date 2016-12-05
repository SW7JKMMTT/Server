package rocks.stalin.sw708e16.server.services.exceptions.mappers;

import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/*
TODO: This is a really bad hack
According to https://issues.jboss.org/browse/RESTEASY-1342 resteasy broke mapping exceptions with entities.
Unfortunately for us, this means we can't map it to JSON. To get around this we try to filter it out here
And insert some hardcoded JSON in its place.

There are better ways to do it, but lets wait and see if they fix it.
*/

@Provider
@Component
@Priority(Priorities.ENTITY_CODER)
public class ForbiddenContainerResponseFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
        throws IOException
    {
        if(responseContext.getStatus() == Response.Status.FORBIDDEN.getStatusCode()) {
            //We only want to map that specific exception. Sadly at this points resteasy has already thrown away the
            //exception. The best we can do is therefore to see if the string matches.
            if(responseContext.getEntity() instanceof String &&
                responseContext.getEntity().equals("Access forbidden: role not allowed"))
                responseContext.setEntity("{\"message\":\"Access forbidden: Role not allowed\"}");
        }
    }

}
