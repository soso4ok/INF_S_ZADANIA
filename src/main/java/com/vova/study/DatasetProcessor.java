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
    // data visualisation
    }


}