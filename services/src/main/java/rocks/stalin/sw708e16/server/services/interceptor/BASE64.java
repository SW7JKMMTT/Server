package rocks.stalin.sw708e16.server.services.interceptor;

import org.jboss.resteasy.annotations.ContentEncoding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ContentEncoding("base64")
public @interface BASE64 {
}
