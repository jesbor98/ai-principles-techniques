package com.company;
import com.company.SortingMethods.InsertionSort;
import com.company.SortingMethods.SelectionSort;

import java.io.*;
import java.util.*;

public class SortingSimulator {

    public static void main(String[] args) {
        // Constant values used
        final String INPUTFILENAME = "example.csv";
        final String OUTPUTFILENAME = "output.txt";


        InsertionSort insertionSort = new InsertionSort(0, 0);
        SelectionSort selectionSort = new SelectionSort(0, 0);

        // Use string builder to ensure we can reuse helpMessage easily
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("i, input \t csv file with arrays to sort, uses input.csv\n");
        helpMessage.append("o, output \t output text file with results\n");
        helpMessage.append("I, insertion \t run insertion sort\n");
        helpMessage.append("S, selection \t run selection sort\n");
        helpMessage.append("x, example \t run on example array\n");
        helpMessage.append("h, help \t display this help and exit\n");
        helpMessage.append("v, version \t output version information and exit\n");

        System.out.println(helpMessage);

        Scanner inputScanner = new Scanner(System.in);
        System.out.print(">> "); // specifically want a List as result to use .contians in if-statement
        List<String> userInput = Arrays.asList(inputScanner.nextLine().split(" "));
        if (userInput.contains("h")) {
            System.out.println(helpMessage);
            System.exit(1);
        }
        if (userInput.contains("v")) {
            System.out.println("Simple sorting simulation version 1.0");
            System.out.println("Implements insertion sort and selection sort and");
            System.out.println("keeps track of array comparisons and assignments");
            System.out.println("The programme and its source code are governed by a BSD-style license");
            System.out.println("that can be found in the LICENSE file.");
            System.exit(1);
        }
        // determining which operations must be performed based on user-input
        boolean example = userInput.contains("x");
        boolean selection = userInput.contains("S");
        boolean insertion = userInput.contains("I");
        boolean requiresOutputFile = userInput.contains("i");
        boolean requiresInputFile = userInput.contains("o");

        if (example) {
            sortExamples(insertionSort, selectionSort);
            System.exit(1);
        }
        if (requiresInputFile) {
            try {
                Scanner csvScanner = new Scanner(new File(INPUTFILENAME));
                StringBuilder results = new StringBuilder();
                // Reading csv file line by line
                // append to string builder, easier to read than string concatenation
                while (csvScanner.hasNext()) {
                    String input = csvScanner.next();
                    int[] parsedInput = parseLine(input);
                    results.append("\n[Original array]: ").append(Arrays.toString(parsedInput)).append("\n");
                    if (insertion) {
                        results.append("\n[Insertion]: ").append(Arrays.toString(insertionSort.sort(parsedInput))).append(" with (")
                                .append(insertionSort.getNumberOfComps()).append(" Comparisons, ").append(insertionSort.getNumberOfAssigns())
                                .append(" assignments)\n");
                    }
                    if (selection) {
                        results.append("\n[Selection]: ").append(Arrays.toString(selectionSort.sort(parsedInput))).append(" with (")
                                .append(selectionSort.getNumberOfComps()).append(" Comparisons, ").append(selectionSort.getNumberOfAssigns())
                                .append(" assignments)\n");
                    }
                    if (requiresOutputFile) {
                        Writer output = new FileWriter(OUTPUTFILENAME);
                        output.write(results.toString());
                        output.close();
                    } else {
                        System.out.println(results);
                    }
                }
                System.exit(1);
            } catch (FileNotFoundException e) {
                System.err.println("Something went wrong with opening the file");
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Something went wrong with reading/writing from a file");
                e.printStackTrace();
            }
        } else {
            System.err.println("Something went wrong with parsing input, likely invalid command");
        }

    }

    /**
     * Converts a comma separated line of int values into an int array
     *
     * @param input comma separated string of int
     * @return in[] representation of input line
     */
    public static int[] parseLine(String input) {
        return Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
    }

    public static void sortExamples(InsertionSort insertionSort, SelectionSort selectionSort) {
        int[] SORTED_ARRAY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        int[] UNSORTED_ARRAY = {15, 3, 12, 7, 10, 14, 16, 13, 18, 9, 2, 17, 1, 8, 4, 11, 5, 6, 20, 19};
        int[] NEGATED_SORTED_ARRAY = {20, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] sorted;

        System.out.println("sorting an already sorted array using insertion sort:");
        sorted = insertionSort.sort(SORTED_ARRAY);
        System.out.println("Sorted array: " + Arrays.toString(sorted));
        System.out.println("sorting took " + insertionSort.getNumberOfComps() + " array comparisons and " + insertionSort.getNumberOfAssigns() + " array assignments\n");

        System.out.println("sorting an already sorted array using selection sort:");
        sorted = selectionSort.sort(SORTED_ARRAY);
        System.out.println("Sorted array: " + Arrays.toString(sorted));
        System.out.println("sorting took " + selectionSort.getNumberOfComps() + " array comparisons and " + selectionSort.getNumberOfAssigns() + " array assignments\n");

        System.out.println("sorting a non-sorted array using insertion sort:");
        sorted = insertionSort.sort(UNSORTED_ARRAY);
        System.out.println("Sorted array: " + Arrays.toString(sorted));
        System.out.println("sorting took " + insertionSort.getNumberOfComps() + " array comparisons and " + insertionSort.getNumberOfAssigns() + " array assignments\n");

        System.out.println("sorting a non-sorted array using selection sort:");
        sorted = selectionSort.sort(UNSORTED_ARRAY);
        System.out.println("Sorted array: " + Arrays.toString(sorted));
        System.out.println("sorting took " + selectionSort.getNumberOfComps() + " array comparisons and " + selectionSort.getNumberOfAssigns() + " array assignments\n");

        System.out.println("sorting an inverse-sorted array using insertion sort:");
        sorted = insertionSort.sort(NEGATED_SORTED_ARRAY);
        System.out.println("Sorted array: " + Arrays.toString(sorted));
        System.out.println("sorting took " + insertionSort.getNumberOfComps() + " array comparisons and " + insertionSort.getNumberOfAssigns() + " array assignments\n");

        System.out.println("sorting an inverse-sorted array using selection sort:");
        sorted = selectionSort.sort(NEGATED_SORTED_ARRAY);
        System.out.println("Sorted array: " + Arrays.toString(sorted));
        System.out.println("sorting took " + selectionSort.getNumberOfComps() + " array comparisons and " + selectionSort.getNumberOfAssigns() + " array assignments\n");
    }
}
