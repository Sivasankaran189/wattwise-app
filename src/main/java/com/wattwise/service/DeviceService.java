package com.wattwise.service;

import com.wattwise.dto.DeviceRequestDto;
import com.wattwise.dto.DeviceResponseDto;
import com.wattwise.entity.EnergyDevice;
import com.wattwise.exception.DuplicateResourceException;
import com.wattwise.exception.ResourceNotFoundException;
import com.wattwise.repository.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public DeviceResponseDto registerDevice(DeviceRequestDto dto) {
        if (deviceRepository.existsByDeviceId(dto.getDeviceId())) {
            throw new DuplicateResourceException("Device with ID '" + dto.getDeviceId() + "' already exists.");
        }

        EnergyDevice device = new EnergyDevice(
                dto.getDeviceId(),
                dto.getName(),
                dto.getBuilding(),
                dto.getRoomOrZone(),
                dto.getDeviceType(),
                dto.getPowerRatingWatts(),
                dto.getBaselineHourlyKwh(),
                "ACTIVE"
        );

        EnergyDevice saved = deviceRepository.save(device);
        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<DeviceResponseDto> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeviceResponseDto getDeviceById(String deviceId) {
        EnergyDevice device = deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with ID: " + deviceId));
        return mapToDto(device);
    }

    @Transactional(readOnly = true)
    public EnergyDevice getDeviceEntity(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with ID: " + deviceId));
    }

    public DeviceResponseDto updateStatus(String deviceId, String status) {
        EnergyDevice device = getDeviceEntity(deviceId);
        device.setStatus(status.toUpperCase());
        return mapToDto(deviceRepository.save(device));
    }

    public DeviceResponseDto mapToDto(EnergyDevice device) {
        return new DeviceResponseDto(
                device.getId(),
                device.getDeviceId(),
                device.getName(),
                device.getBuilding(),
                device.getRoomOrZone(),
                device.getDeviceType(),
                device.getPowerRatingWatts(),
                device.getBaselineHourlyKwh(),
                device.getStatus(),
                device.getCreatedAt()
        );
    }
}
