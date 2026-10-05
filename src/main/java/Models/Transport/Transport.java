package Models.Transport;
import Models.Transport.Enums.TransportType;

public record Transport(
        TransportType TransportType,
        String Model,
        String PlateNumber,
        int Speed
) {
    public Transport {
        if (TransportType == null)
            throw new IllegalArgumentException("type must not be null");
        if (Model == null || Model.isBlank())
            throw new IllegalArgumentException("model must not be blank");
        if (PlateNumber == null || PlateNumber.isBlank())
            throw new IllegalArgumentException("plateNumber must not be blank");
        if(Speed<=0)
            throw new IllegalArgumentException("Speed must be more 0");
    }
}