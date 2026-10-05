package Application.City;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import Models.City.City;

public final class CityCatalog {

    private static volatile CityCatalog instance;

    private final List<City> allCities = new CopyOnWriteArrayList<>();
    private final Map<String, City> citiesByName = new ConcurrentHashMap<>();

    private CityCatalog() {
        if (instance == null) {
            register(new City("Moscow",         55.7558, 37.6173, 13_100_000));
            register(new City("St. Petersburg", 59.9343, 30.3351,  5_600_000));
            register(new City("Novosibirsk",    55.0084, 82.9357,  1_600_000));
        }
    }

    public static CityCatalog getInstance() {
        if (instance == null) {
            synchronized (CityCatalog.class) {
                if (instance == null) {
                    instance = new CityCatalog();
                }
            }
        }
        return instance;
    }

    private void register(City city) {
        Objects.requireNonNull(city, "city must not be null");

        String key = city.getName().toLowerCase();
        if (citiesByName.containsKey(key)) {
            return;
        }

        citiesByName.put(key, city);
        allCities.add(city);
    }

    public Optional<City> findByName(String name) {
        return Optional.ofNullable(citiesByName.get(name.toLowerCase()));
    }

    public List<City> getAllCity() {
        return List.copyOf(allCities);
    }
}