package Application.Trip;

import Application.City.CityCatalog;
import Models.City.City;
import Models.People.People;
import Models.Transport.Transport;
import Models.Trip.Enums.TripType;
import Models.Trip.IntercityTrip;
import Models.Trip.CityTrip;
import Models.Trip.Trip;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TripCreate {

    private final CityCatalog cityCatalog;

    public TripCreate(CityCatalog cityCatalog) {
        this.cityCatalog = Objects.requireNonNull(cityCatalog, "cityCatalog must not be null");
    }

    public TripCreate() {
        this(CityCatalog.getInstance());
    }

    public IntercityTrip createIntercityTrip(
            City startCity,
            City endCity,
            People people,
            Transport transport,
            BigDecimal price,
            LocalDateTime startTime) {

        Objects.requireNonNull(startCity, "startCity must not be null");
        Objects.requireNonNull(endCity, "endCity must not be null");
        Objects.requireNonNull(people, "people must not be null");
        Objects.requireNonNull(transport, "transport must not be null");
        Objects.requireNonNull(price, "price must not be null");
        Objects.requireNonNull(startTime, "startTime must not be null");

        requireRegistered(startCity);
        requireRegistered(endCity);

        if (startCity.equals(endCity)) {
            throw new IllegalArgumentException(
                    "startCity and endCity must differ");
        }

        if (!TripType.INTERCITY.allows(transport.TransportType())) {
            throw new IllegalArgumentException(
                    "Transport " + transport.TransportType().getDisplayName()
                            + " is not allowed for INTERCITY trip");
        }

        IntercityTrip trip = new IntercityTrip(
                price, startCity, endCity, people, transport, startTime);

        startCity.addTrip(trip);
        return trip;
    }


    public CityTrip createIntracityTrip(
            City city,
            People people,
            Transport transport,
            BigDecimal price,
            LocalDateTime startTime) {

        Objects.requireNonNull(city, "city must not be null");
        Objects.requireNonNull(people, "people must not be null");
        Objects.requireNonNull(transport, "transport must not be null");
        Objects.requireNonNull(price, "price must not be null");
        Objects.requireNonNull(startTime, "startTime must not be null");

        requireRegistered(city);

        if (!TripType.INTRACITY.allows(transport.TransportType())) {
            throw new IllegalArgumentException(
                    "Transport " + transport.TransportType().getDisplayName()
                            + " is not allowed for INTRACITY trip");
        }

        CityTrip trip = new CityTrip(
                price, city, people, transport, startTime);

        city.addTrip(trip);
        return trip;
    }

    private void requireRegistered(City city) {
        boolean registered = cityCatalog.findByName(city.getName())
                .filter(c -> c.equals(city))
                .isPresent();
        if (!registered) {
            throw new IllegalArgumentException(
                    "City " + city.getName() + " is not registered in CityCatalog");
        }
    }

    public List<City> getCities() {
        return cityCatalog.getAllCity();
    }

    public List<Trip> getAllTrips() {
        return cityCatalog.getAllCity().stream()
                .flatMap(city -> city.getTrips().stream())
                .toList();
    }

    public City getCity(String name) {
        return cityCatalog.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException(
                        "City not found: " + name));
    }


}