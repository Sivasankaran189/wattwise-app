package com.wattwise;

import com.wattwise.entity.EnergyDevice;
import com.wattwise.entity.EnergyReading;
import com.wattwise.service.AnomalyDetectionService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class AnomalyDetectionServiceTest {

    private AnomalyDetectionService anomalyDetectionService;

    @BeforeEach
    void setUp() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        // 1500W off-hours threshold, 1.5x excess ratio threshold
        anomalyDetectionService = new AnomalyDetectionService(1500.0, 1.5, meterRegistry);
    }

    @Test
    void shouldNotFlagNormalConsumptionDuringWorkingHours() {
        EnergyDevice device = new EnergyDevice(
                "LAB-01", "Lab 40 PCs", "Science Block", "Room 101",
                "COMPUTING", 8000.0, 2.0, "ACTIVE"
        );

        // 2:00 PM on Wednesday (working hours)
        OffsetDateTime afternoon = OffsetDateTime.of(2026, 10, 14, 14, 0, 0, 0, ZoneOffset.UTC);
        EnergyReading reading = new EnergyReading("LAB-01", afternoon, 102.0, 2000.0, 230.0, 8.7);

        AnomalyDetectionService.DetectionResult result = anomalyDetectionService.evaluateReading(
                device, reading, 100.0, afternoon.minusHours(1)
        );

        assertFalse(result.isAnomaly());
        assertEquals(0.0, result.getExcessKwh());
        assertNull(result.getReason());
    }

    @Test
    void shouldFlagOvernightSpikeAsAnomaly() {
        EnergyDevice device = new EnergyDevice(
                "LAB-01", "Lab 40 PCs", "Science Block", "Room 101",
                "COMPUTING", 8000.0, 0.5, "ACTIVE"
        );

        // 2:00 AM on Wednesday (off-hours)
        OffsetDateTime overnight = OffsetDateTime.of(2026, 10, 14, 2, 0, 0, 0, ZoneOffset.UTC);
        // Active power is 3500W (well above 1500W threshold)
        EnergyReading reading = new EnergyReading("LAB-01", overnight, 110.0, 3500.0, 230.0, 15.2);

        AnomalyDetectionService.DetectionResult result = anomalyDetectionService.evaluateReading(
                device, reading, 105.0, overnight.minusHours(1)
        );

        assertTrue(result.isAnomaly());
        assertTrue(result.getExcessKwh() > 0.0);
        assertTrue(result.getReason().contains("Overnight"));
        assertEquals("HIGH", result.getSeverity());
    }

    @Test
    void shouldFlagWeekendLeakAsAnomaly() {
        EnergyDevice device = new EnergyDevice(
                "LAB-01", "Lab 40 PCs", "Science Block", "Room 101",
                "COMPUTING", 8000.0, 0.5, "ACTIVE"
        );

        // Sunday noon
        OffsetDateTime sunday = OffsetDateTime.of(2026, 10, 18, 12, 0, 0, 0, ZoneOffset.UTC);
        EnergyReading reading = new EnergyReading("LAB-01", sunday, 210.0, 2200.0, 230.0, 9.5);

        AnomalyDetectionService.DetectionResult result = anomalyDetectionService.evaluateReading(
                device, reading, 205.0, sunday.minusHours(1)
        );

        assertTrue(result.isAnomaly());
        assertTrue(result.getReason().contains("Weekend power leak"));
    }
}
