package Core.Main;

import Application.City.CityCatalog;
import Application.Trip.TripCreate;
import Application.Trip.TripGenerator;
import Models.City.City;
import Models.Trip.Trip;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static Core.Main.TripOperations.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Threads(4)
@Fork(1)
public class TripOperationsBenchmark {

    private List<Trip> trips;
    private BigDecimal price;
    private City moscow;

    @Setup(Level.Trial)
    public void setup() {

        CityCatalog catalog = CityCatalog.getInstance();

        TripCreate dispatcher = new TripCreate(catalog);
        TripGenerator generator = new TripGenerator(dispatcher);

        trips = generator.generate(250_000);

        price = new BigDecimal("500");

        moscow = catalog.findByName("Moscow")
                .orElseThrow(() ->
                        new IllegalStateException("Moscow not found"));
    }

    // =========================================================
    // 1. EXPENSIVE TRIPS
    // =========================================================

    @Benchmark
    public List<Trip> expensive_iterative() {
        return findTripsExpensivePriceIterative(trips, price);
    }

    @Benchmark
    public List<Trip> expensive_stream() {
        return findTripsExpensivePriceStreamAPI(trips, price);
    }

    @Benchmark
    public List<Trip> expensive_collector() {
        return findTripsExpensivePriceCollector(trips, price);
    }


    // =========================================================
    // 2. TRIPS TO MOSCOW
    // =========================================================

    @Benchmark
    public List<Trip> toMoscow_iterative() {
        return findInterCityTripsToCurrentCityIterative(trips, moscow);
    }

    @Benchmark
    public List<Trip> toMoscow_stream() {
        return findInterCityTripsToCurrentCityStreamAPI(trips, moscow);
    }

    @Benchmark
    public List<Trip> toMoscow_collector() {
        return findInterCityTripsToCurrentCityCollector(trips, moscow);
    }


    // =========================================================
    // 3. AVERAGE PRICE
    // =========================================================

    @Benchmark
    public BigDecimal averagePrice_iterative() {
        return findAveragePriceToCityIterative(trips, moscow);
    }

    @Benchmark
    public BigDecimal averagePrice_stream() {
        return findAveragePriceToCityStreamAPI(trips, moscow);
    }

    @Benchmark
    public BigDecimal averagePrice_collector() {
        return findAveragePriceToCityCollector(trips, moscow);
    }


    // =========================================================
    // 4. AVERAGE AGE
    // =========================================================

    @Benchmark
    public double averageAge_iterative() {
        return findAverageAgeToCityIterative(trips, moscow);
    }

    @Benchmark
    public double averageAge_stream() {
        return findAverageAgeToCityStreamAPI(trips, moscow);
    }

    @Benchmark
    public double averageAge_collector() {
        return findAverageAgeToCityCollector(trips, moscow);
    }

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(TripOperationsBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}