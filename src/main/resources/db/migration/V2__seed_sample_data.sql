-- Flyway Migration V2: Seed Sample Facilities and Energy Devices
INSERT INTO devices (device_id, name, building, room_or_zone, device_type, power_rating_watts, baseline_hourly_kwh, status)
VALUES 
('LAB-COMP-01', 'Computer Lab 40 PCs', 'Science Block A', 'Room 204', 'COMPUTING', 8000.0, 1.2, 'ACTIVE'),
('SRV-ROOM-01', 'Data Center Core Rack', 'Science Block A', 'Basement 01', 'INFRASTRUCTURE', 3500.0, 3.5, 'ACTIVE'),
('HVAC-FLOOR-2', 'Central Air Conditioning Floor 2', 'Science Block A', 'Floor 2', 'HVAC', 12000.0, 4.0, 'ACTIVE')
ON CONFLICT (device_id) DO NOTHING;
