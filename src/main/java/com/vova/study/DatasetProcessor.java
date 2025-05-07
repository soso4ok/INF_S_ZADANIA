package com.vova.study;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatasetProcessor {

    // Static variables and data
    private static final List<Integer> WHITEBOARD_DATA = Arrays.asList(3, -2, 7, 1, 14, -5, 8, 11, -1);
    private final DataPrinter printer;
    private final ParallelSorter sorter;
    private final DataGenerator generator;

    public DatasetProcessor() {
        this.printer = new DataPrinter();
        this.sorter = new ParallelSorter();
        this.generator = new DataGenerator();
    }

    public void run() {
        printer.printHeader("Processing Whiteboard Data");
        processDataset(new ArrayList<>(WHITEBOARD_DATA), 1, "Whiteboard Data (1 Process - Sequential)");
        processDataset(new ArrayList<>(WHITEBOARD_DATA), 2, "Whiteboard Data (2 Processes)");
        processDataset(new ArrayList<>(WHITEBOARD_DATA), 3, "Whiteboard Data (3 Processes)");

        printer.printHeader("Processing Larger Random Data (100,000 elements)");
        List<Integer> largeData = generator.generateRandomList(100_000, 1000);

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        printer.printProcessors(availableProcessors);

        processDataset(new ArrayList<>(largeData), 1, "Large Data (1 Process - Sequential)");
        processDataset(new ArrayList<>(largeData), 2, "Large Data (2 Processes)");
        processDataset(new ArrayList<>(largeData), availableProcessors, "Large Data (" + availableProcessors + " Processes)");
        if (availableProcessors * 2 <= 16) {
            processDataset(new ArrayList<>(largeData), availableProcessors * 2,
                    "Large Data (" + (availableProcessors * 2) + " Processes)");
        }
    }

    /**
     * The method measures the time taken for sorting and prints the processing details and results.
     *
     * @param data        the list of integers to be sorted
     * @param numProcesses the number of processes to use for sorting; if <= 1, sequential sorting is used;
     *                    otherwise, parallel sorting is performed with the specified number of partitions
     * @param description a string describing the dataset and processing configuration for output purposes
     */
    public void processDataset(List<Integer> data, int numProcesses, String description) {
        printer.printProcessingStart(description, data);

        long startTime = System.nanoTime();
        List<Integer> sortedData = (numProcesses <= 1)
                ? sorter.sequentialSort(data)
                : sorter.parallelSort(data, numProcesses);
        long durationMs = (System.nanoTime() - startTime) / 1_000_000;

        printer.printProcessingResult(sortedData, durationMs);
    }


}