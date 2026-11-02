package Core.Main;

import Application.Trip.Collectors.*;
import Models.City.City;
import Models.People.People;
import Models.Trip.Enums.TripType;
import Models.Trip.IntercityTrip;
import Models.Trip.Trip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TripOperations {

    public static List<Trip> findTripsExpensivePriceIterative(List<Trip> trips, BigDecimal price) {
        List<Trip> result = new ArrayList<>();

        for (Trip trip : trips) {
            if (trip != null
                    && trip.getPrice() != null
                    && trip.getPrice().compareTo(price) > 0) {
                result.add(trip);
            }
        }
        return result;
    }

    public static List<Trip> findTripsExpensivePriceStreamAPI(List<Trip> trips, BigDecimal price) {

        if (trips == null || price == null) {
            return List.of();
        }

        return trips.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getPrice() != null)
                .filter(t -> t.getPrice().compareTo(price) > 0)
                .toList();
    }

    public static List<Trip> findTripsExpensivePriceCollector(List<Trip> trips, BigDecimal price) {

        if (trips == null || price == null) {
            return List.of();
        }

        return trips.stream()
                .collect(new ExpensiveTripsCollector(price));
    }

    public static List<Trip> findInterCityTripsToCurrentCityIterative(List<Trip> trips, City city) {
        List<Trip> result = new ArrayList<>();

        for (Trip trip : trips) {
            if (trip != null && trip.getType() == TripType.INTERCITY)
            {
                if(((IntercityTrip)trip).getEndCity() == city)
                    result.add(trip);
            }
        }
        return result;
    }

    public static List<Trip> findInterCityTripsToCurrentCityStreamAPI(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return List.of();
        }

        return trips.stream()
                .filter(t -> t instanceof IntercityTrip ic && ic.getEndCity() == city)
                .toList();
    }

    public static List<Trip> findInterCityTripsToCurrentCityCollector(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return List.of();
        }

        return trips.stream()
                .collect(new InterCityTripsToCurrentCityCollector(city));
    }

    public static BigDecimal findAveragePriceToCityIterative(List<Trip> trips, City city) {

        BigDecimal sum = BigDecimal.ZERO;
        var count = 0;
        for (Trip trip : trips) {
            if (trip != null && trip.getType() == TripType.INTERCITY)
            {
                if(trip instanceof IntercityTrip ic)
                {
                    BigDecimal price = ic.getPrice();
                    if (price == null) continue;
                    sum = sum.add(price);
                    count++;
                }
            }
        }
        if(count!=0)
            return sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        else
            return BigDecimal.ZERO;
    }

    public static BigDecimal findAveragePriceToCityStreamAPI(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return BigDecimal.ZERO;
        }

        record SumCount(BigDecimal sum, long count) {}

        SumCount result = trips.stream()
                .filter(t -> t instanceof IntercityTrip ic
                        && ic.getEndCity() == city)
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

    public static BigDecimal findAveragePriceToCityCollectorStreamAPI(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return BigDecimal.ZERO;
        }

        return trips.stream()
                .filter(t -> t instanceof IntercityTrip ic
                        && ic.getEndCity() == city)
                .map(Trip::getPrice).collect(new AveragePriceToCityCollectorWithSpecificStream());

    }

    public static BigDecimal findAveragePriceToCityCollector(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return BigDecimal.ZERO;
        }

        return trips.stream()
                .collect(new AveragePriceToCityCollector(city));
    }


    public static double findAverageAgeToCityIterative(List<Trip> trips, City city) {

        double sum = 0;
        var count = 0;
        for (Trip trip : trips) {
            if (trip instanceof IntercityTrip ic)
            {
                if(ic.getEndCity() == city)
                {
                    People price = ic.getPeople();
                    if (price == null) continue;

                    sum = sum+price.Age();
                    count++;
                }
            }
        }
        if(count!=0)
            return sum / count;
        else
            return 0;
    }

    public static double findAverageAgeToCityStreamAPI(List<Trip> trips, City city) {

        if (trips == null || city == null) {
            return 0;
        }

        record SumCount(int sum, long count) {}

        SumCount result = trips.stream()
                .filter(t -> t instanceof IntercityTrip ic
                        && ic.getEndCity() ==city)
                .map(Trip::getPeople)
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

    public static double findAverageAgeToCityStreamAPI(List<Trip> trips, City city, long delay) {

        if (trips == null || city == null) {
            return 0;
        }

        record SumCount(int sum, long count) {}

        SumCount result = trips.stream()
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

        return trips.stream()
                .collect(new AverageAgeToCityCollector(city));
    }
}
