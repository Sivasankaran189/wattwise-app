package com.wattwise.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "devices")
public class EnergyDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false, unique = true, length = 64)
    private String deviceId;

    @Column(name = "name", nullable = false, length = 128)
    private String name;

    @Column(name = "building", nullable = false, length = 128)
    private String building;

    @Column(name = "room_or_zone", length = 128)
    private String roomOrZone;

    @Column(name = "device_type", nullable = false, length = 64)
    private String deviceType;

    @Column(name = "power_rating_watts")
    private Double powerRatingWatts;

    @Column(name = "baseline_hourly_kwh")
    private Double baselineHourlyKwh = 0.5;

    @Column(name = "status", length = 32)
    private String status = "ACTIVE";

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    public EnergyDevice() {
    }

    public EnergyDevice(String deviceId, String name, String building, String roomOrZone,
                        String deviceType, Double powerRatingWatts, Double baselineHourlyKwh, String status) {
        this.deviceId = deviceId;
        this.name = name;
        this.building = building;
        this.roomOrZone = roomOrZone;
        this.deviceType = deviceType;
        this.powerRatingWatts = powerRatingWatts;
        this.baselineHourlyKwh = baselineHourlyKwh;
        this.status = status != null ? status : "ACTIVE";
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = OffsetDateTime.now();
        }
        if (this.status == null) {
            this.status = "ACTIVE";
        }
        if (this.baselineHourlyKwh == null) {
            this.baselineHourlyKwh = 0.5;
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
