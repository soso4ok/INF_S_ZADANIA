package com.vova.study;

import java.util.List;
import java.util.stream.Collectors;

public class DataPrinter {
    public void printHeader(String header) {
        System.out.println("\n--- " + header + " ---");
    }

    // Prints the number of available processors (e.g., CPU cores) to the console.
    public void printProcessors(int processors) {
        System.out.println("Available processors: " + processors);
    }


    /**
     * Prints information about the start of a processing task, including a description
     * and the input data. For small datasets (less than 20 elements), the full data is printed;
     * for larger datasets, only the size is shown.
     */
    public void printProcessingStart(String description, List<Integer> data) {
        System.out.println("\nProcessing: " + description);
        if (data.size() < 20) {
            System.out.println("Original data: " + data);
        } else {
            System.out.println("Original data size: " + data.size());
        }
    }

    /**
     * Prints the results of a processing task, including the sorted data and the time taken.
     * For small datasets (less than 20 elements), the full sorted data is printed; for larger
     * datasets, the size and the first five elements are shown.
     * @param sortedData
     * @param durationMs
     */
    public void printProcessingResult(List<Integer> sortedData, long durationMs) {
        if (sortedData.size() < 20) {
            System.out.println("Sorted data -   " + sortedData);
        } else {
            System.out.println("Sorted data size: " + sortedData.size() +
                    ". First 5: " + sortedData.subList(0, Math.min(5, sortedData.size())));
        }
        System.out.println("Time taken - " + durationMs + " ms");
    }
}