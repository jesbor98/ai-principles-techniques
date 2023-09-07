package com.company.SortingMethods;

public class SelectionSort {
    // sort the array (of ints) using selection sort, returning the sorted array and the number of array operations
    // we count every array comparison and every array assignment
    // arrays are used to ensure easy access to index

    // make a copy of the array on which we apply the sorting operation
    private int numberOfComps;
    private int numberOfAssigns;

    public SelectionSort(int numberOfComps, int numberOfAssigns) {
        this.numberOfComps = numberOfComps;
        this.numberOfAssigns = numberOfAssigns;
    }

    public int[] sort(int[] array) {
        int[] sortedArray = array.clone();
        numberOfComps = 0;
        numberOfAssigns = 0;

        // selection sort implementation
        for (int i = 0; i < array.length; i++) {
            int start = i; // keeps track of index at which unsorted part of the list starts

            for (int j = start + 1; j < array.length; j++) { // loop to find smallest element in the unsorted part of the list
                if (sortedArray[j] < sortedArray[start]) {
                    start = j;
                }
                numberOfComps++;
            }
            int x = sortedArray[i];
            sortedArray[i] = sortedArray[start];
            sortedArray[start] = x; // moves smallest element in unsorted part list to the back of the sorted part of the list
            numberOfAssigns += 2;
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