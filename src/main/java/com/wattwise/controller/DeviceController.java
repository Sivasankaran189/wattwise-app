package com.wattwise.controller;

import com.wattwise.dto.DeviceRequestDto;
import com.wattwise.dto.DeviceResponseDto;
import com.wattwise.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceResponseDto> registerDevice(@Valid @RequestBody DeviceRequestDto request) {
        DeviceResponseDto response = deviceService.registerDevice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponseDto>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResponseDto> getDeviceById(@PathVariable String deviceId) {
        return ResponseEntity.ok(deviceService.getDeviceById(deviceId));
    }

    @PatchMapping("/{deviceId}/status")
    public ResponseEntity<DeviceResponseDto> updateStatus(@PathVariable String deviceId, @RequestParam String status) {
        return ResponseEntity.ok(deviceService.updateStatus(deviceId, status));
    }
}
