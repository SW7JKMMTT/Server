package rocks.stalin.sw708e16.server.services.builders;

import org.junit.Assert;
import org.junit.Test;
import rocks.stalin.sw708e16.server.core.Vehicle;
import rocks.stalin.sw708e16.server.core.Vin;

public class TestVehicleBuilder {
    @Test
    public void testMerge_NoVehicleBuilderFields() throws Exception {
        // Arrange
        VehicleBuilder vehicleBuilder = new VehicleBuilder();
        Vehicle vehicle = new Vehicle("Ford", "SuperFast", 1994, new Vin("ABC123"));

        // Act
        Vehicle merged = vehicleBuilder.merge(vehicle);

        // Assert
        Assert.assertNotNull(merged);
        Assert.assertEquals(vehicle, merged);
        Assert.assertEquals(vehicle.getId(), merged.getId());
        Assert.assertEquals(vehicle.getMake(), merged.getMake());
        Assert.assertEquals(vehicle.getModel(), merged.getModel());
        Assert.assertEquals(vehicle.getVin(), merged.getVin());
        Assert.assertEquals(vehicle.getVintage(), merged.getVintage());
    }

    @Test
    public void testMerge_SomeFieldsSet() throws Exception {
        // Arrange
        VehicleBuilder vehicleBuilder = new VehicleBuilder().withMake("Mazda").withVintage(2000);
        Vehicle vehicle = new Vehicle("Ford", "SuperFast", -1994, new Vin("ABC123"));

        // Act
        Vehicle merged = vehicleBuilder.merge(vehicle);

        // Assert
        Assert.assertNotNull(merged);
        Assert.assertEquals(vehicle, merged);
        Assert.assertEquals(vehicle.getId(), merged.getId());
        Assert.assertEquals(vehicle.getModel(), merged.getModel());
        Assert.assertEquals(vehicle.getVin(), merged.getVin());
        Assert.assertEquals(vehicleBuilder.getMake(), merged.getMake());
        Assert.assertEquals(vehicleBuilder.getVintage().intValue(), merged.getVintage());
    }

    @Test
    public void testMerge_AllFieldsSet() throws Exception {
        // Arrange
        VehicleBuilder vehicleBuilder = new VehicleBuilder().withMake("Mazda").withVintage(2000).withModel("SuperSlow").withVin(new Vin("321CBA"));
        Vehicle vehicle = new Vehicle("Ford", "SuperFast", 1994, new Vin("ABC123"));

        // Act
        Vehicle merged = vehicleBuilder.merge(vehicle);

        // Assert
        Assert.assertNotNull(merged);
        Assert.assertEquals(vehicle, merged);
        Assert.assertEquals(vehicle.getId(), merged.getId());
        Assert.assertEquals(vehicleBuilder.getMake(), merged.getMake());
        Assert.assertEquals(vehicleBuilder.getModel(), merged.getModel());
        Assert.assertEquals(vehicleBuilder.getVin().getVin(), merged.getVin().getVin());
        Assert.assertEquals(vehicleBuilder.getVintage().intValue(), merged.getVintage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMerge_Null() throws Exception {
        // Arrange
        VehicleBuilder vehicleBuilder = new VehicleBuilder().withMake("Mazda").withVintage(2000).withModel("SuperSlow").withVin(new Vin("321CBA"));

        // Act
        vehicleBuilder.merge(null);

        // Assert
    }
}
