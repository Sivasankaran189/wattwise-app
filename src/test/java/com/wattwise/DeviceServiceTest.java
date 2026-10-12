package com.wattwise;

import com.wattwise.dto.DeviceRequestDto;
import com.wattwise.dto.DeviceResponseDto;
import com.wattwise.entity.EnergyDevice;
import com.wattwise.exception.DuplicateResourceException;
import com.wattwise.exception.ResourceNotFoundException;
import com.wattwise.repository.DeviceRepository;
import com.wattwise.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DeviceServiceTest {

    private DeviceRepository deviceRepository;
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceRepository = Mockito.mock(DeviceRepository.class);
        deviceService = new DeviceService(deviceRepository);
    }

    @Test
    void shouldRegisterDeviceSuccessfully() {
        when(deviceRepository.existsByDeviceId("TEST-01")).thenReturn(false);
        when(deviceRepository.save(any(EnergyDevice.class))).thenAnswer(invocation -> {
            EnergyDevice dev = invocation.getArgument(0);
            dev.setId(1L);
            return dev;
        });

        DeviceRequestDto req = new DeviceRequestDto(
                "TEST-01", "Test Device", "Block B", "Room 10", "COMPUTING", 1200.0, 0.4
        );

        DeviceResponseDto resp = deviceService.registerDevice(req);

        assertNotNull(resp);
        assertEquals("TEST-01", resp.getDeviceId());
        assertEquals("Test Device", resp.getName());
    }

    @Test
    void shouldThrowDuplicateExceptionWhenDeviceIdExists() {
        when(deviceRepository.existsByDeviceId("EXISTING-01")).thenReturn(true);

        DeviceRequestDto req = new DeviceRequestDto(
                "EXISTING-01", "Duplicate Device", "Block B", "Room 10", "COMPUTING", 1200.0, 0.4
        );

        assertThrows(DuplicateResourceException.class, () -> deviceService.registerDevice(req));
    }

    @Test
    void shouldThrowNotFoundWhenDeviceDoesNotExist() {
        when(deviceRepository.findByDeviceId("MISSING")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> deviceService.getDeviceById("MISSING"));
    }
}
