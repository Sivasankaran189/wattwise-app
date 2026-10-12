package com.wattwise.dto;

import java.time.OffsetDateTime;

public class ReadingResponseDto {

    private Long id;
    private String deviceId;
    private OffsetDateTime readingTimestamp;
    private Double meterReadingKwh;
    private Double activePowerWatts;
    private Double voltage;
    private Double currentAmps;
    private Boolean isAnomaly;
    private Double excessKwh;
    private OffsetDateTime recordedAt;

    public ReadingResponseDto() {
    }

    public ReadingResponseDto(Long id, String deviceId, OffsetDateTime readingTimestamp,
                              Double meterReadingKwh, Double activePowerWatts,
                              Double voltage, Double currentAmps, Boolean isAnomaly,
                              Double excessKwh, OffsetDateTime recordedAt) {
        this.id = id;
        this.deviceId = deviceId;
        this.readingTimestamp = readingTimestamp;
        this.meterReadingKwh = meterReadingKwh;
        this.activePowerWatts = activePowerWatts;
        this.voltage = voltage;
        this.currentAmps = currentAmps;
        this.isAnomaly = isAnomaly;
        this.excessKwh = excessKwh;
        this.recordedAt = recordedAt;
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
