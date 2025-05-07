package com.vova.study;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;


/**
 * Parallel sorting of a list of integers using a divide-and-conquer approach.
 * The list is partitioned into smaller sublists, each sorted concurrently using multiple threads,
 * and the sorted sublists are merged to produce the final sorted list
 */
public class ParallelSorter {
    public List<Integer> parallelSort(List<Integer> list, int numPartitions) {

        // Validation
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        if (numPartitions <= 0) {
            throw new IllegalArgumentException("Number of partitions must be positivee");
        }
        if (numPartitions == 1 || list.size() < numPartitions * 2) {
            return sequentialSort(list);
        }

        /**
         * Partition Setup
         *
         * n:                   The size of the input list.
         * actualPartitions:    The number of partitions is capped at the list size (n) to avoid creating more threads than elements.
         *                      example: if numPartitions is 10 but the list has 5 elements, use 5 partitions.
         *
         * executor:            Creates a thread pool with actualPartitions threads to handle concurrent sorting tasks.
         * futures:             A list to store Future objects, which will hold the results of each sorting task.
         */
        int n = list.size();
        int actualPartitions = Math.min(numPartitions, n);
        ExecutorService executor = Executors.newFixedThreadPool(actualPartitions);
        List<Future<List<Integer>>> futures = new ArrayList<>();

        // Partitioning
        int basePartitionSize = n / actualPartitions;
        int remainder = n % actualPartitions;
        int currentStart = 0;


        /**
         * For each partition i, calculate its size (basePartitionSize + 1 for the first remainder partitions, otherwise basePartitionSize).
         * Skip partitions with size 0 (though this is rare due to actualPartitions capping).
         * Compute the range (currentStart to currentEnd) for the sublist.
         */
        for (int i = 0; i < actualPartitions; i++) {
            int currentPartitionSize = basePartitionSize + (i < remainder ? 1 : 0);
            if (currentPartitionSize == 0) continue;

            int currentEnd = currentStart + currentPartitionSize;
            final List<Integer> subListCopy = new ArrayList<>(list.subList(currentStart, currentEnd));


            //Create a copy of the sublist (subListCopy) to avoid modifying the original list
            Callable<List<Integer>> sortTask = () -> {
                Collections.sort(subListCopy);
                return subListCopy;
            };
            futures.add(executor.submit(sortTask));
            currentStart = currentEnd;
        }

        // Collecting results
        List<List<Integer>> sortedSubLists = new ArrayList<>();
        try {
            for (Future<List<Integer>> future : futures) {
                sortedSubLists.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error during parallel sorting: " + e.getMessage());
            executor.shutdownNow();
            return sequentialSort(list);
        } finally {
            shutdownExecutor(executor);
        }

        /**
         * Merging - mergeTwoSortedLists()
         * *Combine the sorted sublists into a single sorted list.
         */

        if (sortedSubLists.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> finalMergedList = sortedSubLists.get(0);
        for (int i = 1; i < sortedSubLists.size(); i++) {
            finalMergedList = mergeTwoSortedLists(finalMergedList, sortedSubLists.get(i));
        }
        return finalMergedList;
    }

    /**
     * Sorts a list of integers
     */
    public List<Integer> sequentialSort(List<Integer> list) {
        List<Integer> copy = new ArrayList<>(list);
        Collections.sort(copy);
        return copy;
    }

    private List<Integer> mergeTwoSortedLists(List<Integer> list1, List<Integer> list2) {
        List<Integer> mergedList = new ArrayList<>(list1.size() + list2.size());
        int i = 0, j = 0;

        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i) <= list2.get(j)) {
                mergedList.add(list1.get(i++));
            } else {
                mergedList.add(list2.get(j++));
            }
        }
        while (i < list1.size()) {
            mergedList.add(list1.get(i++));
        }
        while (j < list2.size()) {
            mergedList.add(list2.get(j++));
        }
        return mergedList;
    }

    private void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Executor did not terminate");
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
