package com.wattwise.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class DeviceRequestDto {

    @NotBlank(message = "Device ID cannot be blank")
    private String deviceId;

    @NotBlank(message = "Device name cannot be blank")
    private String name;

    @NotBlank(message = "Building name cannot be blank")
    private String building;

    private String roomOrZone;

    @NotBlank(message = "Device type cannot be blank (e.g. COMPUTING, HVAC, INFRASTRUCTURE)")
    private String deviceType;

    @PositiveOrZero(message = "Power rating must be non-negative")
    private Double powerRatingWatts;

    @PositiveOrZero(message = "Baseline hourly kWh must be non-negative")
    private Double baselineHourlyKwh;

    public DeviceRequestDto() {
    }

    public DeviceRequestDto(String deviceId, String name, String building, String roomOrZone,
                            String deviceType, Double powerRatingWatts, Double baselineHourlyKwh) {
        this.deviceId = deviceId;
        this.name = name;
        this.building = building;
        this.roomOrZone = roomOrZone;
        this.deviceType = deviceType;
        this.powerRatingWatts = powerRatingWatts;
        this.baselineHourlyKwh = baselineHourlyKwh;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoomOrZone() {
        return roomOrZone;
    }

    public void setRoomOrZone(String roomOrZone) {
        this.roomOrZone = roomOrZone;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Double getPowerRatingWatts() {
        return powerRatingWatts;
    }

    public void setPowerRatingWatts(Double powerRatingWatts) {
        this.powerRatingWatts = powerRatingWatts;
    }

    public Double getBaselineHourlyKwh() {
        return baselineHourlyKwh;
    }

    public void setBaselineHourlyKwh(Double baselineHourlyKwh) {
        this.baselineHourlyKwh = baselineHourlyKwh;
    }
}
