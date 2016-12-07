package rocks.stalin.sw708e16.server.services;

import org.springframework.stereotype.Repository;
import org.springframework.web.context.ServletContextAware;
import rocks.stalin.sw708e16.server.core.spatial.Waypoint;

import javax.ws.rs.container.AsyncResponse;
import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class WaypointWaiterRegistry {
    private ConcurrentLinkedQueue<AsyncResponse> waiters = new ConcurrentLinkedQueue<>();


    public void addwaiter(AsyncResponse response) {
        waiters.add(response);
    }

    public void writeWaiters(Waypoint waypoint) {
        for(AsyncResponse response : waiters) {
            response.resume(waypoint);
        }
    }
}
