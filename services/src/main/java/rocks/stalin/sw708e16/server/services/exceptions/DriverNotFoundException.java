package rocks.stalin.sw708e16.server.services.exceptions;

public class DriverNotFoundException extends Exception {
    public DriverNotFoundException() {
        super("Driver not found.");
    }
}
