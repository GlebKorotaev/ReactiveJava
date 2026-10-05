package Models.Trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import Models.Trip.Enums.*;
import Models.People.People;
import Models.Transport.Transport;
import Models.City.City;

public abstract class Trip {

    private final BigDecimal price;
    private final TripType type;
    private final City startCity;
    private final People people;
    private final Transport transport;
    private final LocalDateTime startTime;

    protected Trip(
            BigDecimal price,
            TripType type,
            City startCity,
            People people,
            Transport transport,
            LocalDateTime startTime) {

        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("price must be >= 0");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        if (startCity == null) {
            throw new IllegalArgumentException("startCity must not be blank");
        }
        if (people == null) {
            throw new IllegalArgumentException("people must not be empty");
        }
        if (transport == null) {
            throw new IllegalArgumentException("transport must not be null");
        }
        if (startTime == null) {
            throw new IllegalArgumentException("startTime must not be null");
        }

        this.price = price;
        this.type = type;
        this.startCity = startCity;
        this.people = people;   // защитная копия, иммутабельный список
        this.transport = transport;
        this.startTime = startTime;
    }

    public BigDecimal getPrice() { return price; }
    public TripType getType() { return type; }
    public City getStartCity() { return startCity; }
    public People getPeople() { return people; }
    public Transport getTransport() { return transport; }
    public LocalDateTime getStartTime() { return startTime; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "price=" + price +
                ", type=" + type +
                ", startCity='" + startCity + '\'' +
                ", people=" + people +
                ", transport=" + transport +
                ", startTime=" + startTime +
                '}';
    }
}
