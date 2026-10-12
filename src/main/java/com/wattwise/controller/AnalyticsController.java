package com.wattwise.controller;

import com.wattwise.dto.AnalyticsSummaryDto;
import com.wattwise.dto.AnomalyReportDto;
import com.wattwise.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public ResponseEntity<AnalyticsSummaryDto> getSummary() {
        return ResponseEntity.ok(analyticsService.getSummary());
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<AnomalyReportDto>> getAnomalyReports() {
        return ResponseEntity.ok(analyticsService.getAnomalyReports());
    }
}
