package com.wattwise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.OffsetDateTime;

public class ReadingRequestDto {

    @NotBlank(message = "Device ID cannot be blank")
    private String deviceId;

    @NotNull(message = "Reading timestamp is required")
    private OffsetDateTime readingTimestamp;

    @NotNull(message = "Meter reading in kWh is required")
    @PositiveOrZero(message = "Meter reading must be positive or zero")
    private Double meterReadingKwh;

    @NotNull(message = "Active power in Watts is required")
    @PositiveOrZero(message = "Active power must be positive or zero")
    private Double activePowerWatts;

    private Double voltage;
    private Double currentAmps;

    public ReadingRequestDto() {
    }

    public ReadingRequestDto(String deviceId, OffsetDateTime readingTimestamp,
                             Double meterReadingKwh, Double activePowerWatts,
                             Double voltage, Double currentAmps) {
        this.deviceId = deviceId;
        this.readingTimestamp = readingTimestamp;
        this.meterReadingKwh = meterReadingKwh;
        this.activePowerWatts = activePowerWatts;
        this.voltage = voltage;
        this.currentAmps = currentAmps;
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
}
