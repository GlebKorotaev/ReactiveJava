package Core.Main;

import Application.Trip.Collectors.*;
import Application.Trip.TripSpliterator;
import Models.City.City;
import Models.People.People;
import Models.Trip.IntercityTrip;
import Models.Trip.Trip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TripOperationsParallel {

    public static ConcurrentLinkedQueue<Trip> findTripsExpensivePriceStreamAPI(List<Trip> trips, BigDecimal price) {

        if (trips == null || price == null) {
            return new ConcurrentLinkedQueue<Trip>();
        }

        return trips.parallelStream()
                .filter(Objects::nonNull)
                .filter(t -> t.getPrice() != null)
                .filter(t -> t.getPrice().compareTo(price) > 0)
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    public static ConcurrentLinkedQueue<Trip> findTripsExpensivePriceCollector(List<Trip> trips, BigDecimal price) {

        if (trips == null || price == null) {
            return new ConcurrentLinkedQueue<Trip>();
        }

        return new ConcurrentLinkedQueue<>(
                trips.parallelStream()
                        .collect(new ExpensiveTripsCollector(price))
        );
    }

    public static ConcurrentLinkedQueue<Trip> findInterCityTripsToCurrentCityStreamAPI(List<Trip> trips, City city, long delay) {

        if (trips == null || city == null) {
            return new ConcurrentLinkedQueue<Trip>();
        }

        return trips.parallelStream()
                .filter(t -> t instanceof IntercityTrip ic && ic.getEndCity(delay) == city)
                .collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
    }

    public static ConcurrentLinkedQueue<Trip> findInterCityTripsToCurrentCityCollector(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return new ConcurrentLinkedQueue<Trip>();
        }

        return new ConcurrentLinkedQueue<>(trips.parallelStream()
                .collect(new InterCityTripsToCurrentCityCollector(city)));
    }

    public static BigDecimal findAveragePriceToCityStreamAPI(List<Trip> trips, City city, long delay) {

        if (trips == null || city == null) {
            return BigDecimal.ZERO;
        }

        record SumCount(BigDecimal sum, long count) {}

        SumCount result = trips.parallelStream()
                .filter(t -> t instanceof IntercityTrip ic
                        && ic.getEndCity(delay) == city)
                .map(Trip::getPrice)
                .collect(Collectors.teeing(
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        ),
                        Collectors.counting(),
                        SumCount::new
                ));

        if (result.count() == 0) {
            return BigDecimal.ZERO;
        }

        return result.sum()
                .divide(
                        BigDecimal.valueOf(result.count()),
                        2,
                        RoundingMode.HALF_UP
                );

    }

    public static BigDecimal findAveragePriceToCityCollectorStreamAPI(List<Trip> trips, City city, long delay) {

        if (trips == null || city == null) {
            return BigDecimal.ZERO;
        }

        return trips.parallelStream()
                .filter(t -> t instanceof IntercityTrip ic
                        && ic.getEndCity(delay) == city)
                .map(Trip::getPrice).collect(new AveragePriceToCityCollectorWithSpecificStream());

    }

    public static BigDecimal findAveragePriceToCityCollector(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return BigDecimal.ZERO;
        }

        return trips.parallelStream()
                .collect(new AveragePriceToCityCollector(city));
    }

    public static double findAverageAgeToCityStreamAPI(List<Trip> trips, City city, long delay) {

        if (trips == null || city == null) {
            return 0;
        }
        record SumCount(int sum, long count) {}

        SumCount result = trips.parallelStream()
                .filter(t -> t instanceof IntercityTrip ic
                        && ic.getEndCity() ==city)
                .map(t -> t.getPeople(delay))
                .filter(Objects::nonNull)
                .collect(Collectors.teeing(
                        Collectors.summingInt(People::Age),     // сумма
                        Collectors.counting(),                   // количество
                        SumCount::new
                ));

        return result.count() == 0
                ? 0.0
                : (double) result.sum() / result.count();

    }

    public static double findAverageAgeToCityStreamAPIWithMySpliterator(List<Trip> trips, City city, long delay) {

        if (trips == null || city == null) {
            return 0;
        }
        record SumCount(int sum, long count) {}

        SumCount result = StreamSupport.stream(new TripSpliterator(trips), true)
                .filter(t -> t instanceof IntercityTrip ic
                        && ic.getEndCity() ==city)
                .map(t -> t.getPeople(delay))
                .filter(Objects::nonNull)
                .collect(Collectors.teeing(
                        Collectors.summingInt(People::Age),     // сумма
                        Collectors.counting(),                   // количество
                        SumCount::new
                ));

        return result.count() == 0
                ? 0.0
                : (double) result.sum() / result.count();

    }

    public static double findAverageAgeToCityCollector(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return 0;
        }

        return trips.parallelStream()
                .collect(new AverageAgeToCityCollector(city));
    }

    public static double findAverageAgeToCityCollectorWithMySpliterator(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return 0;
        }

        return StreamSupport.stream(new TripSpliterator(trips), true)
                .collect(new AverageAgeToCityCollector(city));
    }
}
