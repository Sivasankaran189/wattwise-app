package com.wattwise;

import com.wattwise.controller.EnergyController;
import com.wattwise.dto.ReadingRequestDto;
import com.wattwise.dto.ReadingResponseDto;
import com.wattwise.service.EnergyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EnergyControllerTest {

    private EnergyService energyService;
    private EnergyController energyController;

    @BeforeEach
    void setUp() {
        energyService = Mockito.mock(EnergyService.class);
        energyController = new EnergyController(energyService);
    }

    @Test
    void shouldReturnReadingsForDevice() {
        ReadingResponseDto mockDto = new ReadingResponseDto(
                1L, "LAB-COMP-01", OffsetDateTime.now(), 120.0, 1500.0, 230.0, 6.5, false, 0.0, OffsetDateTime.now()
        );

        when(energyService.getReadingsByDevice("LAB-COMP-01"))
                .thenReturn(Collections.singletonList(mockDto));

        ResponseEntity<List<ReadingResponseDto>> response = energyController.getReadingsByDevice("LAB-COMP-01");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("LAB-COMP-01", response.getBody().get(0).getDeviceId());
    }

    @Test
    void shouldCreateReadingViaPost() {
        ReadingRequestDto req = new ReadingRequestDto(
                "LAB-COMP-01", OffsetDateTime.now(), 125.0, 2000.0, 230.0, 8.7
        );

        ReadingResponseDto res = new ReadingResponseDto(
                2L, "LAB-COMP-01", req.getReadingTimestamp(), 125.0, 2000.0, 230.0, 8.7, false, 0.0, OffsetDateTime.now()
        );

        when(energyService.recordReading(any(ReadingRequestDto.class))).thenReturn(res);

        ResponseEntity<ReadingResponseDto> response = energyController.recordReading(req);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().getId());
        assertEquals("LAB-COMP-01", response.getBody().getDeviceId());
    }
}
