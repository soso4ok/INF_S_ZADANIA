package com.vova.study;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    /**
     * Method generate random numbers
     *
     * @param size  size of list
     * @param bound maximum absolute value of numbers
     */

    public List<Integer> generateRandomList(int size, int bound) {
        List<Integer> list = new ArrayList<>(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt(bound * 2) - bound);
        }
        return list;
    }
}