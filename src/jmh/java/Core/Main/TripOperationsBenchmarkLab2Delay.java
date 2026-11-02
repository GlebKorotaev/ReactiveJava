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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import java.math.BigDecimal;
import java.util.List;


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class TripOperationsBenchmarkLab2Delay {

    private List<Trip> trips;
    private City moscow;
    @Param({"1", "2"})
    public long delay;
    @Param({"1000", "10000", "50000", "250000","1250000"})
    public int tripCount;
    @Setup(Level.Trial)
    public void setup() {

        CityCatalog catalog = CityCatalog.getInstance();

        TripCreate dispatcher = new TripCreate(catalog);
        TripGenerator generator = new TripGenerator(dispatcher);

        trips = generator.generate(tripCount);

        moscow = catalog.findByName("Moscow")
                .orElseThrow(() ->
                        new IllegalStateException("Moscow not found"));
    }

    // =========================================================
    // 1. EXPENSIVE TRIPS
    // =========================================================



    // =========================================================
    // 2. TRIPS TO MOSCOW
    // =========================================================

    //@Benchmark
  //  public ConcurrentLinkedQueue<Trip> toMoscow_parallel_stream() {
//
    //    return TripOperationsParallel.findInterCityTripsToCurrentCityStreamAPI(trips, moscow,delay);
    //}


    // =========================================================
    // 3. AVERAGE PRICE
    // =========================================================


    // =========================================================
    // 4. AVERAGE AGE
    // =========================================================

    @Benchmark
    public double averageAge_parallel_stream()
    {
        return TripOperationsParallel.findAverageAgeToCityStreamAPI(trips, moscow,delay );
    }

    @Benchmark
    public double averageAge_parallel_stream_spliterator()
    {
        return TripOperationsParallel.findAverageAgeToCityStreamAPIWithMySpliterator(trips, moscow,delay );
    }


    public double averageAge_stream()
    {
        return TripOperations.findAverageAgeToCityStreamAPI(trips, moscow,delay );
    }

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(TripOperationsBenchmarkLab2Delay.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}