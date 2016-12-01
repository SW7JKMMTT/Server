package rocks.stalin.sw708e16.server.services.exceptions;

public class OffThePlanetException extends Exception {
    public OffThePlanetException(Component component, double arg) {
        super(String.format("%s (%s) was set, but not within the valid range: [%d,%d]",
            component,
            arg,
            component == Component.LATITUDE ? -90 : -180,
            component == Component.LATITUDE ?  90 :  180));
    }

    public enum Component {
        LATITUDE("Latitude"),
        LONGITUDE("Longitude");

        private String string;

        Component(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }
    }
}
