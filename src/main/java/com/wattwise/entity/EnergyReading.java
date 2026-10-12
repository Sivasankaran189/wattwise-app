package com.wattwise.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "energy_readings")
public class EnergyReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false, length = 64)
    private String deviceId;

    @Column(name = "reading_timestamp", nullable = false)
    private OffsetDateTime readingTimestamp;

    @Column(name = "meter_reading_kwh", nullable = false)
    private Double meterReadingKwh;

    @Column(name = "active_power_watts", nullable = false)
    private Double activePowerWatts;

    @Column(name = "voltage")
    private Double voltage;

    @Column(name = "current_amps")
    private Double currentAmps;

    @Column(name = "is_anomaly")
    private Boolean isAnomaly = false;

    @Column(name = "excess_kwh")
    private Double excessKwh = 0.0;

    @Column(name = "recorded_at", updatable = false)
    private OffsetDateTime recordedAt;

    public EnergyReading() {
    }

    public EnergyReading(String deviceId, OffsetDateTime readingTimestamp, Double meterReadingKwh,
                         Double activePowerWatts, Double voltage, Double currentAmps) {
        this.deviceId = deviceId;
        this.readingTimestamp = readingTimestamp;
        this.meterReadingKwh = meterReadingKwh;
        this.activePowerWatts = activePowerWatts;
        this.voltage = voltage;
        this.currentAmps = currentAmps;
        this.isAnomaly = false;
        this.excessKwh = 0.0;
    }

    @PrePersist
    public void prePersist() {
        if (this.recordedAt == null) {
            this.recordedAt = OffsetDateTime.now();
        }
        if (this.isAnomaly == null) {
            this.isAnomaly = false;
        }
        if (this.excessKwh == null) {
            this.excessKwh = 0.0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public OffsetDateTime getReadingTimestamp() {
        return readingTimestamp;
    }

    public void setReadingTimestamp(OffsetDateTime readingTimestamp) {
        this.readingTimestamp = readingTimestamp;
    }

    public Double getMeterReadingKwh() {
        return meterReadingKwh;
    }

    public void setMeterReadingKwh(Double meterReadingKwh) {
        this.meterReadingKwh = meterReadingKwh;
    }

    public Double getActivePowerWatts() {
        return activePowerWatts;
    }

    public void setActivePowerWatts(Double activePowerWatts) {
        this.activePowerWatts = activePowerWatts;
    }

    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
    }

    public Double getCurrentAmps() {
        return currentAmps;
    }

    public void setCurrentAmps(Double currentAmps) {
        this.currentAmps = currentAmps;
    }

    public Boolean getIsAnomaly() {
        return isAnomaly;
    }

    public void setIsAnomaly(Boolean anomaly) {
        isAnomaly = anomaly;
    }

    public Double getExcessKwh() {
        return excessKwh;
    }

    public void setExcessKwh(Double excessKwh) {
        this.excessKwh = excessKwh;
    }

    public OffsetDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(OffsetDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
