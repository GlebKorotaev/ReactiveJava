package Models.Trip;

import Models.City.City;
import Models.People.People;
import Models.Transport.Transport;
import Models.Trip.Enums.TripType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class IntercityTrip extends Trip {

    private final City endCity;

    public IntercityTrip(
            BigDecimal price,
            City startCity,
            City endCity,
            People people,
            Transport transport,
            LocalDateTime startTime) {
        super(price, TripType.INTERCITY, startCity, people, transport, startTime);

        if (endCity == null) {
            throw new IllegalArgumentException("endCity must not be null");
        }
        if (endCity.equals(startCity)) {
            throw new IllegalArgumentException("endCity must differ from startCity");
        }

        this.endCity = endCity;
    }

    public City getEndCity() {
        return endCity;
    }

    public City getEndCity(long delay) {
        if(delay>0) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return endCity;
    }

    @Override
    public String toString() {
        return "IntercityTrip{" +
                "from=" + getStartCity().getName() +
                ", to=" + endCity.getName() +
                ", price=" + getPrice() +
                ", transport=" + getTransport().Model() +
                ", startTime=" + getStartTime() +
                '}';
    }
}