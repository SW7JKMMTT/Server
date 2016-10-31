package rocks.stalin.sw708e16.server.core;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Vin {
    @Column(unique = true, nullable = false, length = 32)
    private String vin;
}
