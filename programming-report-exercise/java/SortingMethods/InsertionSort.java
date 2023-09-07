package com.company.SortingMethods;

public class InsertionSort {
    // sort the array (of ints) using insertion sort, returning the sorted array and the number of array operations
    // we count every array comparison and every array assignment
    // arrays are used to ensure easy access to index
    private int numberOfComps;
    private int numberOfAssigns;

    public InsertionSort(int numberOfComps, int numberOfAssigns) {
        this.numberOfComps = numberOfComps;
        this.numberOfAssigns = numberOfAssigns;
    }

    public int[] sort(int[] array) {
        int[] sortedArray = array.clone();
        numberOfComps = 0;
        numberOfAssigns = 0;


        // insertion sort implementation
        for (int i = 0; i < array.length; i++) {
            int x = sortedArray[i];
            int j = i - 1;

            while ((j >= 0) && (sortedArray[j] > x)) {
                sortedArray[j + 1] = sortedArray[j]; // compares sortedArray[j+1] to x
                numberOfComps++;                     // sortedArray[j+1] becomes sortedArray[j]
                numberOfAssigns++;
                j = j - 1;
            }
            sortedArray[j + 1] = x;
            numberOfAssigns++;      // additional assignment of the i'th element
        }
        return sortedArray;
    }

    public int getNumberOfAssigns() {
        return numberOfAssigns;
    }

    public int getNumberOfComps() {
        return numberOfComps;
    }
}

