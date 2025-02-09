package org.example.concurrency;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class EventsTest {
    @Test
    void testLatch() {
        assertTimeout(Duration.ofSeconds(3), () -> assertDoesNotThrow(RocketLaunch::launch));
    }

    @Test
    void testObserver() {
        ObserverExample.launch();
    }

    @Test
    void testFlow1() {
        assertDoesNotThrow(FlowExample::launch);
    }

    @Test
    void testFLow2() {
        assertDoesNotThrow(FlowStockMarket::launch);
    }

    @Test
    void testEventBus() {
        GuavaEventBusExample.launch();
    }
}