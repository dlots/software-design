package org.example.calculator;

import java.util.Arrays;

public class AverageCalculator {
    public static double calculateAverage(int[] numbers) {
        int count = 0, sum = 0;
        for (int number: numbers) {
            count++;
            sum += number;
        }
        return sum / count;
    }
}
