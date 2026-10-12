package com.wattwise.controller;

import com.wattwise.dto.ReadingRequestDto;
import com.wattwise.dto.ReadingResponseDto;
import com.wattwise.service.EnergyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/readings")
public class EnergyController {

    private final EnergyService energyService;

    public EnergyController(EnergyService energyService) {
        this.energyService = energyService;
    }

    @PostMapping
    public ResponseEntity<ReadingResponseDto> recordReading(@Valid @RequestBody ReadingRequestDto request) {
        ReadingResponseDto response = energyService.recordReading(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<ReadingResponseDto>> getReadingsByDevice(@PathVariable String deviceId) {
        return ResponseEntity.ok(energyService.getReadingsByDevice(deviceId));
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<ReadingResponseDto>> getAllAnomalies() {
        return ResponseEntity.ok(energyService.getAllAnomalies());
    }
}
