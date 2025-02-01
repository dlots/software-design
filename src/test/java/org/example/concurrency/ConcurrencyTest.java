package org.example.concurrency;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyTest {

@Test
void testNoRace() {
    assertEquals(
        Concurrency.RaceConditionExample.numberOfIncrements * Concurrency.RaceConditionExample.numberOfThreads,
        Concurrency.RaceConditionExample.run()
    );
}

@Test
void testNoDeadlock() {
    assertTimeout(Duration.ofMillis(200), Concurrency.DeadlockExample::run);
}

}