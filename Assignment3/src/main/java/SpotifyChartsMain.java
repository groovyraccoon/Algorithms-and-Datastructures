import spotifycharts.ChartsCalculator;
import spotifycharts.Song;
import spotifycharts.SorterImpl;

import java.util.List;

public class SpotifyChartsMain {
    public static void main(String[] args) {
        System.out.println("Welcome to the HvA Spotify Charts Calculator\n");

        ChartsCalculator chartsCalculator = new ChartsCalculator(19670427L);
        chartsCalculator.registerStreamedSongs(257);
        chartsCalculator.showResults();

        final int QUANTITY = 10;

        long starting;
        double durationLength;
        SorterImpl<Song> sorter = new SorterImpl<>();

        for (int i = 0; i < QUANTITY; i++) {
            ChartsCalculator chartsCalc;
            System.out.println("Iteration " + (i + 1) + "\n");

            //2*2^2
            int size = 50;
            double totDuration = 0;
            chartsCalc = new ChartsCalculator((long) (Math.random() * 10000));


            while (size < 5_000_000 && totDuration < 20_000) {
                size = size * 2;
                List<Song> songs = chartsCalc.registerStreamedSongs(size);
                System.gc();
                starting = System.nanoTime();

                //quickSort
                sorter.quickSort(songs, Song::compareByHighestStreamsCountTotal);
                //selectionSort
//                sorter.selInsBubSort(songs, Song::compareByHighestStreamsCountTotal);
                //topHeapSort
//                sorter.topsHeapSort(songs.size(), songs, Song::compareByHighestStreamsCountTotal);

                durationLength = 1E-6 * (System.nanoTime() - starting);
                totDuration += durationLength;
                System.out.printf("Time with size %d : %.4f msec.  Total amount duration: %.4f msec\n", size, durationLength, totDuration);
            }

        }
    }
}