package com.wattwise.service;

import com.wattwise.dto.ReadingRequestDto;
import com.wattwise.dto.ReadingResponseDto;
import com.wattwise.entity.EnergyDevice;
import com.wattwise.entity.EnergyReading;
import com.wattwise.repository.EnergyReadingRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnergyService {

    private final EnergyReadingRepository readingRepository;
    private final DeviceService deviceService;
    private final AnomalyDetectionService anomalyDetectionService;
    private final Counter totalKwhCounter;

    public EnergyService(EnergyReadingRepository readingRepository,
                         DeviceService deviceService,
                         AnomalyDetectionService anomalyDetectionService,
                         MeterRegistry meterRegistry) {
        this.readingRepository = readingRepository;
        this.deviceService = deviceService;
        this.anomalyDetectionService = anomalyDetectionService;
        this.totalKwhCounter = Counter.builder("wattwise_energy_recorded_kwh_total")
                .description("Total energy recorded across all devices in kWh")
                .baseUnit("kWh")
                .register(meterRegistry);
    }

    public ReadingResponseDto recordReading(ReadingRequestDto request) {
        EnergyDevice device = deviceService.getDeviceEntity(request.getDeviceId());

        // Find the most recent previous reading to calculate delta
        Optional<EnergyReading> lastReadingOpt = readingRepository
                .findFirstByDeviceIdOrderByReadingTimestampDesc(request.getDeviceId());

        Double prevKwh = lastReadingOpt.map(EnergyReading::getMeterReadingKwh).orElse(null);
        var prevTimestamp = lastReadingOpt.map(EnergyReading::getReadingTimestamp).orElse(null);

        EnergyReading reading = new EnergyReading(
                device.getDeviceId(),
                request.getReadingTimestamp(),
                request.getMeterReadingKwh(),
                request.getActivePowerWatts(),
                request.getVoltage(),
                request.getCurrentAmps()
        );

        // Run Anomaly and Waste Detection
        AnomalyDetectionService.DetectionResult detection = anomalyDetectionService
                .evaluateReading(device, reading, prevKwh, prevTimestamp);

        reading.setIsAnomaly(detection.isAnomaly());
        reading.setExcessKwh(detection.getExcessKwh());

        EnergyReading saved = readingRepository.save(reading);

        // Update Prometheus metric for total energy recorded
        if (prevKwh != null && saved.getMeterReadingKwh() >= prevKwh) {
            double deltaKwh = saved.getMeterReadingKwh() - prevKwh;
            totalKwhCounter.increment(deltaKwh);
        } else {
            totalKwhCounter.increment(saved.getMeterReadingKwh());
        }

        return mapToDto(saved);
    }

    @Transactional(readOnly = true)
    public List<ReadingResponseDto> getReadingsByDevice(String deviceId) {
        // Validate device exists
        deviceService.getDeviceById(deviceId);
        return readingRepository.findByDeviceIdOrderByReadingTimestampDesc(deviceId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReadingResponseDto> getAllAnomalies() {
        return readingRepository.findByIsAnomalyTrueOrderByReadingTimestampDesc()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ReadingResponseDto mapToDto(EnergyReading reading) {
        return new ReadingResponseDto(
                reading.getId(),
                reading.getDeviceId(),
                reading.getReadingTimestamp(),
                reading.getMeterReadingKwh(),
                reading.getActivePowerWatts(),
                reading.getVoltage(),
                reading.getCurrentAmps(),
                reading.getIsAnomaly(),
                reading.getExcessKwh(),
                reading.getRecordedAt()
        );
    }
}
