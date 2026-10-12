package com.wattwise.service;

import com.wattwise.dto.AnalyticsSummaryDto;
import com.wattwise.dto.AnomalyReportDto;
import com.wattwise.entity.EnergyDevice;
import com.wattwise.entity.EnergyReading;
import com.wattwise.repository.DeviceRepository;
import com.wattwise.repository.EnergyReadingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final DeviceRepository deviceRepository;
    private final EnergyReadingRepository readingRepository;
    private final double tariffRate;

    public AnalyticsService(
            DeviceRepository deviceRepository,
            EnergyReadingRepository readingRepository,
            @Value("${wattwise.analytics.default-tariff-rate:0.15}") double tariffRate) {
        this.deviceRepository = deviceRepository;
        this.readingRepository = readingRepository;
        this.tariffRate = tariffRate;
    }

    public AnalyticsSummaryDto getSummary() {
        List<EnergyDevice> devices = deviceRepository.findAll();
        long totalDevices = devices.size();
        long activeDevices = devices.stream().filter(d -> "ACTIVE".equalsIgnoreCase(d.getStatus())).count();

        // Calculate total consumption by summing latest reading for each device
        double totalConsumptionKwh = 0.0;
        for (EnergyDevice d : devices) {
            totalConsumptionKwh += readingRepository.findFirstByDeviceIdOrderByReadingTimestampDesc(d.getDeviceId())
                    .map(EnergyReading::getMeterReadingKwh)
                    .orElse(0.0);
        }

        // Peak power
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime thirtyDaysAgo = now.minusDays(30);
        Double peakWatts = readingRepository.findPeakPowerWattsBetween(thirtyDaysAgo, now);
        double peakPowerWatts = peakWatts != null ? peakWatts : 0.0;

        // Anomalies & Waste
        long totalAnomalies = readingRepository.countByIsAnomalyTrue();
        Double totalExcessKwhVal = readingRepository.calculateTotalExcessKwh();
        double totalExcessKwh = totalExcessKwhVal != null ? totalExcessKwhVal : 0.0;

        double estimatedCost = totalConsumptionKwh * tariffRate;
        double potentialSavings = totalExcessKwh * tariffRate;

        return new AnalyticsSummaryDto(
                totalDevices,
                activeDevices,
                round(totalConsumptionKwh),
                round(estimatedCost),
                tariffRate,
                round(peakPowerWatts),
                totalAnomalies,
                round(totalExcessKwh),
                round(potentialSavings)
        );
    }

    public List<AnomalyReportDto> getAnomalyReports() {
        List<EnergyReading> anomalyReadings = readingRepository.findByIsAnomalyTrueOrderByReadingTimestampDesc();
        Map<String, EnergyDevice> deviceMap = deviceRepository.findAll().stream()
                .collect(Collectors.toMap(EnergyDevice::getDeviceId, d -> d, (a, b) -> a));

        List<AnomalyReportDto> reports = new ArrayList<>();
        for (EnergyReading r : anomalyReadings) {
            EnergyDevice dev = deviceMap.get(r.getDeviceId());
            String devName = dev != null ? dev.getName() : "Unknown Device";
            String building = dev != null ? dev.getBuilding() : "Unknown Building";
            double excess = r.getExcessKwh() != null ? r.getExcessKwh() : 0.0;
            double wasteCost = excess * tariffRate;

            String reason = (r.getReadingTimestamp().getHour() >= 22 || r.getReadingTimestamp().getHour() < 6)
                    ? "Overnight consumption spike outside working hours"
                    : "High load anomaly exceeding baseline profile";

            String severity = (r.getActivePowerWatts() > 3000.0) ? "HIGH" : "MEDIUM";

            reports.add(new AnomalyReportDto(
                    r.getId(),
                    r.getDeviceId(),
                    devName,
                    building,
                    r.getReadingTimestamp(),
                    r.getActivePowerWatts(),
                    round(excess),
                    round(wasteCost),
                    reason,
                    severity
            ));
        }

        return reports;
    }

    private double round(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}
