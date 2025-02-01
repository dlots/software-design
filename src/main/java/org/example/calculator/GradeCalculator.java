package org.example.calculator;

import java.util.Collections;
import java.util.List;

public class GradeCalculator {
    public static double calculateAverage(List<Integer> grades) throws IllegalArgumentException {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        double sum = grades.stream().mapToDouble((grade) -> {
            if (grade == null || grade < 0) {
                throw new IllegalArgumentException("Negative or null grades are not allowed");
            }
            return grade.doubleValue();
        }).sum();
        return sum / grades.size();
    }
}
