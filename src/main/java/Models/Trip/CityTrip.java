package Models.Trip;

import Models.City.City;
import Models.People.People;
import Models.Transport.Transport;
import Models.Trip.Enums.TripType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CityTrip extends Trip {


    public CityTrip(
            BigDecimal price,
            City startCity,
            People people,
            Transport transport,
            LocalDateTime startTime) {
        super(price, TripType.INTRACITY, startCity, people, transport, startTime);
    }

    @Override
    public String toString() {
        return "CityTrip{" +
                "from=" + getStartCity().getName() +
                ", price=" + getPrice() +
                ", transport=" + getTransport().Model() +
                ", startTime=" + getStartTime() +
                '}';
    }
}