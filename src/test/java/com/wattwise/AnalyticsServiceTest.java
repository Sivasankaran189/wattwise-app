package com.wattwise;

import com.wattwise.dto.AnalyticsSummaryDto;
import com.wattwise.dto.AnomalyReportDto;
import com.wattwise.entity.EnergyDevice;
import com.wattwise.entity.EnergyReading;
import com.wattwise.repository.DeviceRepository;
import com.wattwise.repository.EnergyReadingRepository;
import com.wattwise.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AnalyticsServiceTest {

    private DeviceRepository deviceRepository;
    private EnergyReadingRepository readingRepository;
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        deviceRepository = Mockito.mock(DeviceRepository.class);
        readingRepository = Mockito.mock(EnergyReadingRepository.class);
        analyticsService = new AnalyticsService(deviceRepository, readingRepository, 0.15);
    }

    @Test
    void shouldGenerateAccurateAnalyticsSummary() {
        EnergyDevice device = new EnergyDevice(
                "LAB-01", "Lab 40 PCs", "Science Block", "Room 101",
                "COMPUTING", 8000.0, 1.2, "ACTIVE"
        );
        when(deviceRepository.findAll()).thenReturn(Collections.singletonList(device));

        EnergyReading latestReading = new EnergyReading(
                "LAB-01", OffsetDateTime.now(), 200.0, 1800.0, 230.0, 7.8
        );
        when(readingRepository.findFirstByDeviceIdOrderByReadingTimestampDesc("LAB-01"))
                .thenReturn(Optional.of(latestReading));

        when(readingRepository.findPeakPowerWattsBetween(any(), any())).thenReturn(2500.0);
        when(readingRepository.countByIsAnomalyTrue()).thenReturn(2L);
        when(readingRepository.calculateTotalExcessKwh()).thenReturn(15.0);

        AnalyticsSummaryDto summary = analyticsService.getSummary();

        assertNotNull(summary);
        assertEquals(1, summary.getTotalDevices());
        assertEquals(1, summary.getActiveDevices());
        assertEquals(200.0, summary.getTotalConsumptionKwh());
        assertEquals(30.0, summary.getEstimatedCost()); // 200 * 0.15 = 30.0
        assertEquals(2500.0, summary.getPeakPowerWatts());
        assertEquals(2, summary.getTotalAnomalies());
        assertEquals(15.0, summary.getTotalExcessKwh());
        assertEquals(2.25, summary.getPotentialSavings()); // 15.0 * 0.15 = 2.25
    }

    @Test
    void shouldReturnAnomalyReports() {
        EnergyDevice device = new EnergyDevice(
                "LAB-01", "Lab 40 PCs", "Science Block", "Room 101",
                "COMPUTING", 8000.0, 1.2, "ACTIVE"
        );
        when(deviceRepository.findAll()).thenReturn(Collections.singletonList(device));

        EnergyReading anomalyReading = new EnergyReading(
                "LAB-01", OffsetDateTime.now(), 210.0, 3200.0, 230.0, 13.9
        );
        anomalyReading.setId(5L);
        anomalyReading.setIsAnomaly(true);
        anomalyReading.setExcessKwh(10.0);

        when(readingRepository.findByIsAnomalyTrueOrderByReadingTimestampDesc())
                .thenReturn(Collections.singletonList(anomalyReading));

        List<AnomalyReportDto> reports = analyticsService.getAnomalyReports();

        assertNotNull(reports);
        assertEquals(1, reports.size());
        assertEquals("LAB-01", reports.get(0).getDeviceId());
        assertEquals("Lab 40 PCs", reports.get(0).getDeviceName());
        assertEquals(1.5, reports.get(0).getEstimatedWasteCost()); // 10 * 0.15 = 1.5
    }
}
