package com.wattwise.service;

import com.wattwise.entity.EnergyDevice;
import com.wattwise.entity.EnergyReading;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;

@Service
public class AnomalyDetectionService {

    private final double offHoursThresholdWatts;
    private final double excessRatioThreshold;
    private final Counter anomalyCounter;
    private final Counter estimatedWasteKwhCounter;

    public AnomalyDetectionService(
            @Value("${wattwise.anomaly.off-hours-threshold-watts:1500.0}") double offHoursThresholdWatts,
            @Value("${wattwise.anomaly.excess-ratio-threshold:1.5}") double excessRatioThreshold,
            MeterRegistry meterRegistry) {
        this.offHoursThresholdWatts = offHoursThresholdWatts;
        this.excessRatioThreshold = excessRatioThreshold;

        this.anomalyCounter = Counter.builder("wattwise_anomalies_detected_total")
                .description("Total number of energy consumption anomalies detected")
                .register(meterRegistry);

        this.estimatedWasteKwhCounter = Counter.builder("wattwise_estimated_waste_kwh_total")
                .description("Total estimated excess energy wasted in kWh")
                .baseUnit("kWh")
                .register(meterRegistry);
    }

    public static class DetectionResult {
        private final boolean anomaly;
        private final double excessKwh;
        private final String reason;
        private final String severity;

        public DetectionResult(boolean anomaly, double excessKwh, String reason, String severity) {
            this.anomaly = anomaly;
            this.excessKwh = excessKwh;
            this.reason = reason;
            this.severity = severity;
        }

        public boolean isAnomaly() {
            return anomaly;
        }

        public double getExcessKwh() {
            return excessKwh;
        }

        public String getReason() {
            return reason;
        }

        public String getSeverity() {
            return severity;
        }
    }

    public DetectionResult evaluateReading(EnergyDevice device, EnergyReading currentReading, Double previousMeterKwh, OffsetDateTime previousTimestamp) {
        OffsetDateTime timestamp = currentReading.getReadingTimestamp();
        int hour = timestamp.getHour();
        DayOfWeek day = timestamp.getDayOfWeek();
        boolean isWeekend = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY);
        boolean isOffHours = (hour >= 22 || hour < 6 || isWeekend);

        double baselineKwh = device.getBaselineHourlyKwh() != null ? device.getBaselineHourlyKwh() : 0.5;
        double activeWatts = currentReading.getActivePowerWatts() != null ? currentReading.getActivePowerWatts() : 0.0;

        double deltaKwh = 0.0;
        if (previousMeterKwh != null && currentReading.getMeterReadingKwh() >= previousMeterKwh) {
            deltaKwh = currentReading.getMeterReadingKwh() - previousMeterKwh;
        } else {
            // estimate hourly kWh from active power (watts / 1000)
            deltaKwh = activeWatts / 1000.0;
        }

        boolean isAnomaly = false;
        double excessKwh = 0.0;
        String reason = null;
        String severity = "LOW";

        // Check 1: Off-hours or weekend spike
        if (isOffHours && activeWatts > offHoursThresholdWatts) {
            isAnomaly = true;
            excessKwh = Math.max(0.0, deltaKwh - baselineKwh);
            reason = isWeekend ? "Weekend power leak detected during inactive period"
                               : "Overnight power surge detected outside operating hours";
            severity = (activeWatts > offHoursThresholdWatts * 2) ? "HIGH" : "MEDIUM";
        }
        // Check 2: Exceeding baseline ratio
        else if (deltaKwh > (baselineKwh * excessRatioThreshold)) {
            isAnomaly = true;
            excessKwh = Math.max(0.0, deltaKwh - baselineKwh);
            reason = "Consumption exceeded baseline threshold by " + String.format("%.1fx", (deltaKwh / baselineKwh));
            severity = (deltaKwh > (baselineKwh * 2.5)) ? "HIGH" : "MEDIUM";
        }

        if (isAnomaly) {
            anomalyCounter.increment();
            estimatedWasteKwhCounter.increment(excessKwh);
        }

        return new DetectionResult(isAnomaly, excessKwh, reason, severity);
    }
}
