package Application.Trip.Collectors;

import Models.City.City;
import Models.Trip.Enums.TripType;
import Models.Trip.IntercityTrip;
import Models.Trip.Trip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class AveragePriceToCityCollector
        implements Collector<Trip, AveragePriceToCityCollector.Subtotal, BigDecimal> {

    private final City city;

    public AveragePriceToCityCollector(City city) {
        this.city = Objects.requireNonNull(city, "city must not be null");
    }

    public static final class Subtotal {
        BigDecimal sum = BigDecimal.ZERO;
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
                BigDecimal price = trip.getPrice();
                if (price == null) return;

                subtotal.sum = subtotal.sum.add(price);
                subtotal.count++;
            }
        };
    }

    @Override
    public BinaryOperator<Subtotal> combiner() {
        return (left, right) -> {
            left.sum = left.sum.add(right.sum);
            left.count += right.count;
            return left;
        };
    }

    @Override
    public Function<Subtotal, BigDecimal> finisher() {
        return subtotal -> subtotal.count == 0
                ? BigDecimal.ZERO
                : subtotal.sum.divide(BigDecimal.valueOf(subtotal.count), 2, RoundingMode.HALF_UP);
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.UNORDERED);
    }
}