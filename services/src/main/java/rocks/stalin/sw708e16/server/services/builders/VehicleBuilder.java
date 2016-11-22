package rocks.stalin.sw708e16.server.services.builders;

import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.Vin;

public final class VehicleBuilder {
    private String make;
    private String model;
    private Integer vintage;
    private Vin vin;

    public VehicleBuilder() {
    }

    public VehicleBuilder(String make, String model, Integer vintage, Vin vin) {
        this.make = make;
        this.model = model;
        this.vintage = vintage;
        this.vin = vin;
    }

    public VehicleBuilder withMake(String make) {
        this.make = make;
        return this;
    }

    public VehicleBuilder withModel(String model) {
        this.model = model;
        return this;
    }

    public VehicleBuilder withVintage(Integer vintage) {
        this.vintage = vintage;
        return this;
    }

    public VehicleBuilder withVin(Vin vin) {
        this.vin = vin;
        return this;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public Integer getVintage() {
        return vintage;
    }

    public Vin getVin() {
        return vin;
    }

    /**
     * Merges elements in the {@link VehicleBuilder} into a {@link Vehicle}
     * @param vehicle A {@link Vehicle} to merge into.
     * @return The modified {@link Vehicle}.
     */
    public Vehicle merge(Vehicle vehicle) {
        if (vehicle == null)
            throw new IllegalArgumentException("Vehicle was null.");

        if (this.make != null)
            vehicle.setMake(this.make);

        if (this.model != null)
            vehicle.setModel(this.model);

        if (this.vintage != null)
            vehicle.setVintage(this.vintage);

        if (this.vin != null)
            vehicle.setVin(this.vin);

        return vehicle;
    }

    /**
     * Construct a {@link Vehicle} if and only if all arguments are given.
     *
     * @return The {@link Vehicle} constructed.
     */
    public Vehicle build() {
        if (make == null || model == null || vintage == null || vin == null) {
            throw new IllegalArgumentException("All arguments must be given in order to constuct a Vehicle.");
        }

        return new Vehicle(make, model, vintage, vin);
    }
}
