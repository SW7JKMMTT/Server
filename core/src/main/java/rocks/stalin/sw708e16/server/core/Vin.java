package rocks.stalin.sw708e16.server.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Vin {
    @Column(unique = true, nullable = false, length = 32)
    private String vin;

    protected Vin() {
    }

    @JsonCreator
    public Vin(String vin) {
        this.vin = vin;
    }

    @JsonValue
    public String getVin() {
        return vin;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Vin vin1 = (Vin) obj;

        return vin.equals(vin1.vin);
    }

    @Override
    public int hashCode() {
        return vin.hashCode();
    }
}
