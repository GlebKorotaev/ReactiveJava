package Application.Trip;

import Models.City.City;
import Models.People.People;
import Models.Transport.Enums.TransportType;
import Models.Transport.Transport;
import Models.Trip.Enums.TripType;
import Models.Trip.IntercityTrip;
import Models.Trip.CityTrip;
import Models.Trip.Trip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class TripGenerator {

    private static final String[] FIRST_NAMES = {
            "Иван", "Мария", "Пётр", "Анна", "Сергей",
            "Ольга", "Дмитрий", "Екатерина", "Алексей", "Наталья"
    };

    private static final String[] LAST_NAMES = {
            "Иванов", "Петров", "Сидоров", "Кузнецов", "Смирнов",
            "Попов", "Волков", "Морозов", "Новиков", "Фёдоров"
    };

    private final TripCreate dispatcher;
    private final Random random;

    public TripGenerator(TripCreate dispatcher, long seed) {
        this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher must not be null");
        this.random = new Random(seed);
    }

    public TripGenerator(TripCreate dispatcher) {
        this(dispatcher, System.nanoTime());
    }

    public Stream<Trip> stream(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        }
        return Stream.generate(this::generateOne)
                .limit(count);
    }

    public List<Trip> generate(int count) {
        return stream(count).toList();
    }

    public Stream<IntercityTrip> streamIntercity(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        }
        return Stream.generate(this::generateIntercity)
                .limit(count);
    }

    public Stream<CityTrip> streamCityTrip(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be >= 0");
        }
        return Stream.generate(this::generateCityTrip)
                .limit(count);
    }

    private List<IntercityTrip> generateIntercity(int count) {
        return streamIntercity(count).toList();
    }

    private List<CityTrip> generateCityTrip(int count) {
        return streamCityTrip(count).toList();
    }

    private Trip generateOne() {
        TripType type = randomType();
        return switch (type) {
            case INTERCITY -> generateIntercity();
            case INTRACITY -> generateCityTrip();
        };
    }

    private IntercityTrip generateIntercity() {
        List<City> cities = dispatcher.getCities();
        if (cities.size() < 2) {
            throw new IllegalStateException(
                    "Need at least 2 cities for intercity trips");
        }

        City startCity = randomCity(cities);
        City endCity = randomOtherCity(cities, startCity);
        People people = randomPeople();
        Transport transport = randomTransport(TripType.INTERCITY);
        BigDecimal price = randomPrice(500, 50000);
        LocalDateTime startTime = randomStartTime();

        return dispatcher.createIntercityTrip(
                startCity, endCity, people, transport, price, startTime);
    }

    private CityTrip generateCityTrip() {
        List<City> cities = dispatcher.getCities();
        if (cities.isEmpty()) {
            throw new IllegalStateException("No cities registered");
        }

        City city = randomCity(cities);
        People people = randomPeople();
        Transport transport = randomTransport(TripType.INTRACITY);
        BigDecimal price = randomPrice(50, 500);
        LocalDateTime startTime = randomStartTime();

        return dispatcher.createIntracityTrip(
                city, people, transport, price, startTime);
    }

    private TripType randomType() {
        TripType[] types = TripType.values();
        return types[random.nextInt(types.length)];
    }

    private City randomCity(List<City> cities) {
        return cities.get(random.nextInt(cities.size()));
    }

    private City randomOtherCity(List<City> cities, City exclude) {
        City candidate;
        do {
            candidate = randomCity(cities);
        } while (candidate.equals(exclude));
        return candidate;
    }

    private People randomPeople() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        int age = random.nextInt(63);
        String document = String.format("%04d %06d",
                random.nextInt(10_000), random.nextInt(1_000_000));
        return new People(firstName, lastName, age, document);
    }

    private Transport randomTransport(TripType type) {
        List<TransportType> allowed = new ArrayList<>(type.getAllowedTransports());
        TransportType transportType = allowed.get(random.nextInt(allowed.size()));

        String model = transportModel(transportType);
        String plateNumber = randomPlateNumber();
        int speed = transportSpeed(transportType);
        return new Transport(transportType, model, plateNumber, speed);
    }

    private BigDecimal randomPrice(int min, int max) {
        int value = min + random.nextInt(max - min + 1);
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    private LocalDateTime randomStartTime() {
        return LocalDateTime.now()
                .minusDays(random.nextInt(30))
                .plusHours(random.nextInt(24))
                .plusMinutes(random.nextInt(60));
    }


    private String transportModel(TransportType type) {
        return switch (type) {
            case PLANE          -> "Boeing 737";
            case BUS            -> "PAZ-3205";
            case TRAM           -> "71-405";
            case TROLLEYBUS     -> "TROLZA-5265";
            case TAXI           -> "Kia Rio";
            case TRAIN          -> "Sapsan";
            case ELECTRIC_TRAIN -> "Lastochka";
        };
    }

    private int transportSpeed(TransportType type) {
        return switch (type) {
            case PLANE          -> 800;
            case TRAIN          -> 150;
            case ELECTRIC_TRAIN -> 90;
            case BUS            -> 60;
            case TROLLEYBUS     -> 40;
            case TRAM           -> 30;
            case TAXI           -> 50;
        };
    }


    private String randomPlateNumber() {
        char letter1 = (char) ('А' + random.nextInt(32));
        char letter2 = (char) ('А' + random.nextInt(32));
        int digits = 100 + random.nextInt(900);
        int region = 1 + random.nextInt(199);
        return String.format("%c%03d%c%c%02d",
                letter1, digits, letter2, letter1, region);
    }
}