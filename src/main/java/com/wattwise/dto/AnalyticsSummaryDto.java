package com.wattwise.dto;

public class AnalyticsSummaryDto {

    private long totalDevices;
    private long activeDevices;
    private double totalConsumptionKwh;
    private double estimatedCost;
    private double tariffRatePerKwh;
    private double peakPowerWatts;
    private long totalAnomalies;
    private double totalExcessKwh;
    private double potentialSavings;

    public AnalyticsSummaryDto() {
    }

    public AnalyticsSummaryDto(long totalDevices, long activeDevices, double totalConsumptionKwh,
                               double estimatedCost, double tariffRatePerKwh, double peakPowerWatts,
                               long totalAnomalies, double totalExcessKwh, double potentialSavings) {
        this.totalDevices = totalDevices;
        this.activeDevices = activeDevices;
        this.totalConsumptionKwh = totalConsumptionKwh;
        this.estimatedCost = estimatedCost;
        this.tariffRatePerKwh = tariffRatePerKwh;
        this.peakPowerWatts = peakPowerWatts;
        this.totalAnomalies = totalAnomalies;
        this.totalExcessKwh = totalExcessKwh;
        this.potentialSavings = potentialSavings;
    }

    public long getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(long totalDevices) {
        this.totalDevices = totalDevices;
    }

    public long getActiveDevices() {
        return activeDevices;
    }

    public void setActiveDevices(long activeDevices) {
        this.activeDevices = activeDevices;
    }

    public double getTotalConsumptionKwh() {
        return totalConsumptionKwh;
    }

    public void setTotalConsumptionKwh(double totalConsumptionKwh) {
        this.totalConsumptionKwh = totalConsumptionKwh;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public double getTariffRatePerKwh() {
        return tariffRatePerKwh;
    }

    public void setTariffRatePerKwh(double tariffRatePerKwh) {
        this.tariffRatePerKwh = tariffRatePerKwh;
    }

    public double getPeakPowerWatts() {
        return peakPowerWatts;
    }

    public void setPeakPowerWatts(double peakPowerWatts) {
        this.peakPowerWatts = peakPowerWatts;
    }

    public long getTotalAnomalies() {
        return totalAnomalies;
    }

    public void setTotalAnomalies(long totalAnomalies) {
        this.totalAnomalies = totalAnomalies;
    }

    public double getTotalExcessKwh() {
        return totalExcessKwh;
    }

    public void setTotalExcessKwh(double totalExcessKwh) {
        this.totalExcessKwh = totalExcessKwh;
    }

    public double getPotentialSavings() {
        return potentialSavings;
    }

    public void setPotentialSavings(double potentialSavings) {
        this.potentialSavings = potentialSavings;
    }
}
