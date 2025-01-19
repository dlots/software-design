package org.example.calculator;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AverageCalculatorTest {
    @Test
    void testCalculateAverage() {
        int[] input = {1, 3};
        assertEquals(
                0,
                AverageCalculator.calculateAverage(input)
        );
    }
    // Example of insufficient test coverage: happy path is covered.
    // But empty array case and non-integer result case are not covered,
    // and division by zero bug and integer division bug are not captured.
}