package Application.Trip;

import Models.Trip.Trip;

import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class TripSpliterator implements Spliterator<Trip> {
    private final List<Trip> trips;
    private int index;
    private final int fence;

    public TripSpliterator(List<Trip> trips) {
        this(trips, 0, trips.size());
    }

    private TripSpliterator(List<Trip> trips, int index, int fence) {
        this.trips = trips;
        this.index = index;
        this.fence = fence;
    }

    @Override
    public boolean tryAdvance(Consumer<? super Trip> action) {
        if (index < fence) {
            action.accept(trips.get(index++));
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<Trip> trySplit() {
        int mid = (index + fence) / 2;

        if (index >= mid) {
            return null;
        }

        int oldIndex = index;
        index = mid;

        return new TripSpliterator(trips, oldIndex, mid);
    }

    @Override
    public long estimateSize() {
        return fence - index;
    }

    @Override
    public int characteristics() {
        return ORDERED | SIZED | SUBSIZED;
    }
}