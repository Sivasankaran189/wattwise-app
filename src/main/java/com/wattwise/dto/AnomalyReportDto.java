package com.wattwise.dto;

import java.time.OffsetDateTime;

public class AnomalyReportDto {

    private Long readingId;
    private String deviceId;
    private String deviceName;
    private String building;
    private OffsetDateTime readingTimestamp;
    private Double activePowerWatts;
    private Double excessKwh;
    private Double estimatedWasteCost;
    private String reason;
    private String severity;

    public AnomalyReportDto() {
    }

    public AnomalyReportDto(Long readingId, String deviceId, String deviceName, String building,
                            OffsetDateTime readingTimestamp, Double activePowerWatts,
                            Double excessKwh, Double estimatedWasteCost, String reason, String severity) {
        this.readingId = readingId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.building = building;
        this.readingTimestamp = readingTimestamp;
        this.activePowerWatts = activePowerWatts;
        this.excessKwh = excessKwh;
        this.estimatedWasteCost = estimatedWasteCost;
        this.reason = reason;
        this.severity = severity;
    }

    public Long getReadingId() {
        return readingId;
    }

    public void setReadingId(Long readingId) {
        this.readingId = readingId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public OffsetDateTime getReadingTimestamp() {
        return readingTimestamp;
    }

    public void setReadingTimestamp(OffsetDateTime readingTimestamp) {
        this.readingTimestamp = readingTimestamp;
    }

    public Double getActivePowerWatts() {
        return activePowerWatts;
    }

    public void setActivePowerWatts(Double activePowerWatts) {
        this.activePowerWatts = activePowerWatts;
    }

    public Double getExcessKwh() {
        return excessKwh;
    }

    public void setExcessKwh(Double excessKwh) {
        this.excessKwh = excessKwh;
    }

    public Double getEstimatedWasteCost() {
        return estimatedWasteCost;
    }

    public void setEstimatedWasteCost(Double estimatedWasteCost) {
        this.estimatedWasteCost = estimatedWasteCost;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}
