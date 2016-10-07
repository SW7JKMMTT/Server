package dk.aau.giraf.rest.core.weekschedule;

/**
 * Days of the week, Monday to Sunday.
 */
public enum Day {
    /**
     * Monday.
     */
    Monday(0),
    /**
     * Tuesday.
     */
    Tuesday(1),
    /**
     * Wednesday.
     */
    Wednesday(2),
    /**
     * Thursday.
     */
    Thursday(3),
    /**
     * Friday.
     */
    Friday(4),
    /**
     * Saturday.
     */
    Saturday(5),
    /**
     * Sunday.
     */
    Sunday(6);

    private final int value;

    Day(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
