package Models.City;

import Models.Trip.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class City {

    private final String name;
    private final double latitude;
    private final double longitude;
    private final long population;
    private final List<Trip> trips = new ArrayList<>();

    public City(String name, double latitude, double longitude, long population) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("latitude must be in [-90, 90]");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("longitude must be in [-180, 180]");
        }
        if (population < 0) {
            throw new IllegalArgumentException("population must be >= 0");
        }

        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population = population;
    }

    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public long getPopulation() { return population; }

    public List<Trip> getTrips() {
        return List.copyOf(trips);
    }

    public void addTrip(Trip trip) {
        if (trip == null) {
            throw new IllegalArgumentException("trip must not be null");
        }
        trips.add(trip);
    }

    public int getTripCount() {
        return trips.size();
    }
}
