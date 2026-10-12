-- Flyway Migration V1: Initial Schema for Devices and Energy Readings
CREATE TABLE IF NOT EXISTS devices (
    id BIGSERIAL PRIMARY KEY,
    device_id VARCHAR(64) NOT NULL UNIQUE,
    name VARCHAR(128) NOT NULL,
    building VARCHAR(128) NOT NULL,
    room_or_zone VARCHAR(128),
    device_type VARCHAR(64) NOT NULL,
    power_rating_watts DOUBLE PRECISION,
    baseline_hourly_kwh DOUBLE PRECISION DEFAULT 0.5,
    status VARCHAR(32) DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS energy_readings (
    id BIGSERIAL PRIMARY KEY,
    device_id VARCHAR(64) NOT NULL REFERENCES devices(device_id) ON DELETE CASCADE,
    reading_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    meter_reading_kwh DOUBLE PRECISION NOT NULL,
    active_power_watts DOUBLE PRECISION NOT NULL,
    voltage DOUBLE PRECISION,
    current_amps DOUBLE PRECISION,
    is_anomaly BOOLEAN DEFAULT FALSE,
    excess_kwh DOUBLE PRECISION DEFAULT 0.0,
    recorded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_readings_device_time ON energy_readings(device_id, reading_timestamp DESC);
CREATE INDEX idx_readings_anomaly ON energy_readings(is_anomaly);
