package rocks.stalin.sw708e16.server.services.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Provider
@Priority(Priorities.HEADER_DECORATOR)
@Component
public class AcceptedEncodingHackInterceptor implements ContainerRequestFilter {
    Logger logger = LoggerFactory.getLogger(AcceptedEncodingHackInterceptor.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if(requestContext.getHeaders() != null) {
            List<String> xAccept = requestContext.getHeaders().get("X-Accept-Encoding");
            if (xAccept == null)
                return;

            if(xAccept.size() == 0)
                return;
            String xAcceptHeader = xAccept.get(0);

            List<String> accept = requestContext.getHeaders().get("Accept-Encoding");
            if(accept == null)
                accept = new ArrayList<>(1);

            logger.info("Dumping the Accept-Encoding header, byRoute_after the X-Accept-Encoding is specified");
            if(accept.size() >= 1) {
                accept.set(0, xAcceptHeader);
            } else {
                accept.add(0, xAcceptHeader);
            }
        }
    }
}
