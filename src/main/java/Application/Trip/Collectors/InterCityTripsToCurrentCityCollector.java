package Application.Trip.Collectors;

import Models.City.City;
import Models.Trip.Enums.TripType;
import Models.Trip.IntercityTrip;
import Models.Trip.Trip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class InterCityTripsToCurrentCityCollector implements Collector<Trip, List<Trip>, List<Trip>> {

    private final City city;

    public InterCityTripsToCurrentCityCollector(City city) {
        if (city == null) {
            throw new IllegalArgumentException("threshold must not be null");
        }
        this.city = city;
    }

    @Override
    public Supplier<List<Trip>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<Trip>, Trip> accumulator() {
        return (list, trip) -> {
            if (trip == null) return;
            var tripType = trip.getType();
            if (tripType == TripType.INTRACITY) return;

            if (((IntercityTrip)trip).getEndCity() == city) list.add(trip);
        };
    }

    @Override
    public BinaryOperator<List<Trip>> combiner() {
        return (left, right) -> {
            left.addAll(right);
            return left;
        };
    }

    @Override
    public Function<List<Trip>, List<Trip>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
    }
}
