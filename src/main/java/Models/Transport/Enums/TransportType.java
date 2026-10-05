package Models.Transport.Enums;

public enum TransportType {

    PLANE("PLANE"),
    BUS("BUS"),
    TRAM("TRAM"),
    TROLLEYBUS("TROLLEYBUS"),
    TAXI("TAXI"),
    TRAIN("TRAIN"),
    ELECTRIC_TRAIN("ELECTRIC_TRAIN");

    private final String displayName;

    TransportType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}