package org.example.calculator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {
    double average(Integer... values) {
        return GradeCalculator.calculateAverage(Arrays.asList(values));
    }

    @Test
    void testAverageResult() {
        assertEquals(2.5 , average(1, 2, 3, 4));
    }

    @Test
    void testEmptyList() {
        final List<Integer> emptyList = new ArrayList<>();
        assertEquals(0, GradeCalculator.calculateAverage(emptyList));
    }

    @Test
    void testNullList() {
        assertEquals(0, GradeCalculator.calculateAverage(null));
    }

    @Test
    void testListWithNull() {
        assertThrows(IllegalArgumentException.class, () -> average(1, null, 2));
    }

    @Test
    void testListWithNegative() {
        assertThrows(IllegalArgumentException.class, () -> average(1, 2, -3, 4));
    }
}