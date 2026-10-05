package Models.Trip.Enums;

import java.util.Set;

import Models.Transport.Enums.TransportType;
public enum TripType {

    INTERCITY("INTERCITY", Set.of(
            TransportType.ELECTRIC_TRAIN,
            TransportType.TRAIN,
            TransportType.PLANE,
            TransportType.BUS
    )),
    INTRACITY("INTRACITY", Set.of(
            TransportType.BUS,
            TransportType.TRAM,
            TransportType.TROLLEYBUS,
            TransportType.TAXI
    ));

    private final String displayName;
    private final Set<TransportType> allowedTransports;

    TripType(String displayName, Set<TransportType> allowedTransports) {
        this.displayName = displayName;
        this.allowedTransports = Set.copyOf(allowedTransports);
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<TransportType> getAllowedTransports() {
        return allowedTransports;
    }

    public boolean allows(TransportType transportType) {
        return allowedTransports.contains(transportType);
    }
}
