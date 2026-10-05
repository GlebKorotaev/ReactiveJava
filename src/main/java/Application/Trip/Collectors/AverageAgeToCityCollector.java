package Application.Trip.Collectors;

import Models.City.City;
import Models.People.People;
import Models.Trip.Enums.TripType;
import Models.Trip.IntercityTrip;
import Models.Trip.Trip;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class AverageAgeToCityCollector
        implements Collector<Trip, AverageAgeToCityCollector.Subtotal, Double> {

    private final City city;

    public AverageAgeToCityCollector(City city) {
        this.city = Objects.requireNonNull(city, "city must not be null");
    }

    public static final class Subtotal {
        long sum = 0;
        long count = 0;
    }

    @Override
    public Supplier<Subtotal> supplier() {
        return Subtotal::new;
    }

    @Override
    public BiConsumer<Subtotal, Trip> accumulator() {
        return (subtotal, trip) -> {
            if (trip.getType() == TripType.INTRACITY) return;
            if (((IntercityTrip)trip).getEndCity() == city) {
                People people = trip.getPeople();
                if (people == null) return;

                subtotal.sum += people.Age();
                subtotal.count++;
            }
        };
    }

    @Override
    public BinaryOperator<Subtotal> combiner() {
        return (left, right) -> {
            left.sum = left.sum + right.sum;
            left.count += right.count;
            return left;
        };
    }

    @Override
    public Function<AverageAgeToCityCollector.Subtotal, Double> finisher() {

        return subtotal -> {
            double sum = subtotal.sum;
            double count = subtotal.count;
            return subtotal.count == 0
                ? 0
                : sum / count;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}