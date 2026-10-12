package com.wattwise.dto;

import java.time.OffsetDateTime;

public class DeviceResponseDto {

    private Long id;
    private String deviceId;
    private String name;
    private String building;
    private String roomOrZone;
    private String deviceType;
    private Double powerRatingWatts;
    private Double baselineHourlyKwh;
    private String status;
    private OffsetDateTime createdAt;

    public DeviceResponseDto() {
    }

    public DeviceResponseDto(Long id, String deviceId, String name, String building, String roomOrZone,
                             String deviceType, Double powerRatingWatts, Double baselineHourlyKwh,
                             String status, OffsetDateTime createdAt) {
        this.id = id;
        this.deviceId = deviceId;
        this.name = name;
        this.building = building;
        this.roomOrZone = roomOrZone;
        this.deviceType = deviceType;
        this.powerRatingWatts = powerRatingWatts;
        this.baselineHourlyKwh = baselineHourlyKwh;
        this.status = status;
        this.createdAt = createdAt;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
