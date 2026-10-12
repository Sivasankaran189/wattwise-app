package com.wattwise;

import com.wattwise.dto.ReadingRequestDto;
import com.wattwise.dto.ReadingResponseDto;
import com.wattwise.entity.EnergyDevice;
import com.wattwise.entity.EnergyReading;
import com.wattwise.repository.EnergyReadingRepository;
import com.wattwise.service.AnomalyDetectionService;
import com.wattwise.service.DeviceService;
import com.wattwise.service.EnergyService;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EnergyServiceTest {

    private EnergyReadingRepository readingRepository;
    private DeviceService deviceService;
    private AnomalyDetectionService anomalyDetectionService;
    private EnergyService energyService;

    @BeforeEach
    void setUp() {
        readingRepository = Mockito.mock(EnergyReadingRepository.class);
        deviceService = Mockito.mock(DeviceService.class);
        anomalyDetectionService = Mockito.mock(AnomalyDetectionService.class);
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();

        energyService = new EnergyService(readingRepository, deviceService, anomalyDetectionService, meterRegistry);
    }

    @Test
    void shouldSuccessfullyRecordReading() {
        EnergyDevice mockDevice = new EnergyDevice(
                "LAB-COMP-01", "Lab 40 PCs", "Block A", "Room 204",
                "COMPUTING", 8000.0, 1.2, "ACTIVE"
        );
        when(deviceService.getDeviceEntity("LAB-COMP-01")).thenReturn(mockDevice);
        when(readingRepository.findFirstByDeviceIdOrderByReadingTimestampDesc("LAB-COMP-01"))
                .thenReturn(Optional.empty());

        when(anomalyDetectionService.evaluateReading(any(), any(), any(), any()))
                .thenReturn(new AnomalyDetectionService.DetectionResult(false, 0.0, null, "LOW"));

        when(readingRepository.save(any(EnergyReading.class))).thenAnswer(invocation -> {
            EnergyReading r = invocation.getArgument(0);
            r.setId(101L);
            return r;
        });

        ReadingRequestDto req = new ReadingRequestDto(
                "LAB-COMP-01", OffsetDateTime.now(), 45.5, 2100.0, 230.0, 9.1
        );

        ReadingResponseDto resp = energyService.recordReading(req);

        assertNotNull(resp);
        assertEquals("LAB-COMP-01", resp.getDeviceId());
        assertEquals(45.5, resp.getMeterReadingKwh());
        assertFalse(resp.getIsAnomaly());
    }
}
