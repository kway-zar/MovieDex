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
    public static Movie[] sort(Movie[] movies, boolean sortByRating, boolean ascending) {
        quickSort(movies, 0, movies.length - 1, sortByRating, ascending);
        return movies;
    }

    // Actual recursive Quick Sort
    private static void quickSort(Movie[] movies, int low, int high, boolean sortByRating, boolean ascending) {
        if (low < high) {
            int pi = partition(movies, low, high, sortByRating, ascending);

            quickSort(movies, low, pi - 1, sortByRating, ascending);
            quickSort(movies, pi + 1, high, sortByRating, ascending);
        }
    }

    // Partition function
    private static int partition(Movie[] movies, int low, int high, boolean sortByRating, boolean ascending) {
        double pivotValue = sortByRating ? movies[high].getScore() : movies[high].getTimesRated();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            double currentValue = sortByRating ? movies[j].getScore() : movies[j].getTimesRated();

            if ((ascending && currentValue <= pivotValue) ||
                (!ascending && currentValue >= pivotValue)) {
                i++;

                Movie temp = movies[i];
                movies[i] = movies[j];
                movies[j] = temp;
            }
        }

        Movie temp = movies[i + 1];
        movies[i + 1] = movies[high];
        movies[high] = temp;

        return i + 1;
    }


}
