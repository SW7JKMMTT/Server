package rocks.stalin.sw708e16.server.services.interceptor;

import com.sun.mail.util.BASE64EncoderStream;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.io.OutputStream;

@Provider
@Component
@Priority(Priorities.ENTITY_CODER)
public class Base64EncodingInterceptor implements WriterInterceptor {
    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        Object encoding = context.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);

        if (encoding != null && encoding.toString().equalsIgnoreCase("base64")) {
            OutputStream old = context.getOutputStream();
            // GZIPOutputStream constructor writes to underlying OS causing headers to be written.
            BASE64EncoderStream base64Stream = new BASE64EncoderStream(old);

            // Any content length set will be obsolete
            context.getHeaders().remove("Content-Length");

            context.setOutputStream(base64Stream);
            try {
                context.proceed();
            } finally {
                context.setOutputStream(old);
            }
            return;
        } else {
            context.proceed();
        }
    }
}
