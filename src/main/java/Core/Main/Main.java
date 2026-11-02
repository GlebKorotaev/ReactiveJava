package Core.Main;

import Application.City.CityCatalog;

import Application.Trip.TripCreate;
import Application.Trip.TripGenerator;

import Models.Trip.Trip;

import java.math.BigDecimal;

import java.util.List;


import static Core.Main.TripOperations.*;

public class Main {

    static void main(String[] args) {
        CityCatalog catalog = CityCatalog.getInstance();
        TripCreate dispatcher = new TripCreate(catalog);
        TripGenerator generator = new TripGenerator(dispatcher);
        var size = 250000 * 4;
        System.out.println("Collection size: "+ size);
        List<Trip> allTrips = generator.generate(size);
        var tp = dispatcher.getAllTrips();
        var price = new BigDecimal(500);
    
        var startTime = System.currentTimeMillis();
        var tripExpensive500 = findTripsExpensivePriceIterative(allTrips, price);
        var endTime = System.currentTimeMillis();
        System.out.println("Time iterative price more: " +tripExpensive500.size()+"  " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        tripExpensive500 = findTripsExpensivePriceStreamAPI(allTrips, price);
        endTime = System.currentTimeMillis();
        System.out.println("Time StreamAPI price more: " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        tripExpensive500 = findTripsExpensivePriceCollector(allTrips, price);
        endTime = System.currentTimeMillis();
        System.out.println("Time Collector price more: " + (endTime - startTime) + " ms");
        System.out.println();
        var optMoscow = catalog.findByName("Moscow");
        if(optMoscow.isPresent())
        {
            var moscow = optMoscow.get();
            startTime = System.currentTimeMillis();
            var tripToMoscow = findInterCityTripsToCurrentCityIterative(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time Iterative to Moscow: " + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            tripToMoscow = findInterCityTripsToCurrentCityStreamAPI(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time StreamAPI to Moscow: " + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            tripToMoscow = findInterCityTripsToCurrentCityCollector(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time Collector to Moscow: " + (endTime - startTime) + " ms");
            System.out.println();
            startTime = System.currentTimeMillis();
            var averagePriceToCityIterative = findAveragePriceToCityIterative(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time Iterative to average="+ averagePriceToCityIterative+ " time=" + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            averagePriceToCityIterative = findAveragePriceToCityStreamAPI(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time StreamAPI to average="+ averagePriceToCityIterative+ " time=" + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            averagePriceToCityIterative = findAveragePriceToCityCollector(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time Collector to average="+ averagePriceToCityIterative+ " time=" + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            averagePriceToCityIterative = findAveragePriceToCityCollectorStreamAPI(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time Collector + StreamAPI to average="+ averagePriceToCityIterative+ " time=" + (endTime - startTime) + " ms");
            System.out.println();
            startTime = System.currentTimeMillis();
            var averageAgeToCityIterative = findAverageAgeToCityIterative(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time Iterative to average Age="+ averageAgeToCityIterative+ " time=" + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            averageAgeToCityIterative = findAverageAgeToCityStreamAPI(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time StreamAPI to average Age="+ averageAgeToCityIterative+ " time=" + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            averageAgeToCityIterative = findAverageAgeToCityCollector(allTrips, moscow);
            endTime = System.currentTimeMillis();
            System.out.println("Time Collector to average Age="+ averageAgeToCityIterative+ " time=" + (endTime - startTime) + " ms");

            startTime = System.currentTimeMillis();
            averageAgeToCityIterative = TripOperationsParallel.findAverageAgeToCityStreamAPIWithMySpliterator(allTrips, moscow,0 );
            endTime = System.currentTimeMillis();
            System.out.println("Time findAverageAgeToCityStreamAPIWithMySpliterator Age="+ averageAgeToCityIterative+ " time=" + (endTime - startTime) + " ms");
        }
    }

}