/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.netnet.moviedex;

/**
 *
 * @author quasar
 */
public class QuickSort {

    // Quick Sort entry point
    public static Movie[] sort(Movie[] movies, boolean sortByRating, boolean ascending, boolean arrangeByIndex) {
        quickSort(movies, 0, movies.length - 1, sortByRating, ascending, arrangeByIndex);
        return movies;
    }

    // Actual recursive Quick Sort
    private static void quickSort(Movie[] movies, int low, int high,
                                  boolean sortByRating, boolean ascending, boolean arrangeByIndex) {
        if (low < high) {
            int pi = partition(movies, low, high, sortByRating, ascending, arrangeByIndex);

            quickSort(movies, low, pi - 1, sortByRating, ascending, arrangeByIndex);
            quickSort(movies, pi + 1, high, sortByRating, ascending, arrangeByIndex);
        }
    }

    // Partition function
    private static int partition(Movie[] movies, int low, int high,
                                 boolean sortByRating, boolean ascending, boolean arrangeByIndex) {
        double pivotValue = getValue(movies[high], sortByRating, arrangeByIndex);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            double currentValue = getValue(movies[j], sortByRating, arrangeByIndex);

            if ((ascending && currentValue <= pivotValue) ||
                (!ascending && currentValue >= pivotValue)) {
                i++;
                swap(movies, i, j);
            }
        }

        swap(movies, i + 1, high);
        return i + 1;
    }

    
    private static double getValue(Movie movie, boolean sortByRating, boolean arrangeByIndex) {
        if (arrangeByIndex) {
            return movie.getIndex(); 
        } else if (sortByRating) {
            return movie.getScore();
        } else {
            return movie.getTimesRated();
        }
    }

    private static void swap(Movie[] movies, int i, int j) {
        Movie temp = movies[i];
        movies[i] = movies[j];
        movies[j] = temp;
    }


}
