package rocks.stalin.sw708e16.server.services.features;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class CorsFeature implements Feature{
    @Override
    public boolean configure(FeatureContext context) {
        CorsFilter filter = new CorsFilter();
        filter.getAllowedOrigins().add("*");
        context.register(filter);
        return true;
    }
}
