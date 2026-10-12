package com.wattwise.repository;

import com.wattwise.entity.EnergyReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnergyReadingRepository extends JpaRepository<EnergyReading, Long> {

    List<EnergyReading> findByDeviceIdOrderByReadingTimestampDesc(String deviceId);

    Optional<EnergyReading> findFirstByDeviceIdOrderByReadingTimestampDesc(String deviceId);

    List<EnergyReading> findByDeviceIdAndReadingTimestampBetweenOrderByReadingTimestampAsc(
            String deviceId, OffsetDateTime startTime, OffsetDateTime endTime);

    List<EnergyReading> findByReadingTimestampBetweenOrderByReadingTimestampAsc(
            OffsetDateTime startTime, OffsetDateTime endTime);

    List<EnergyReading> findByIsAnomalyTrueOrderByReadingTimestampDesc();

    long countByIsAnomalyTrue();

    @Query("SELECT COALESCE(SUM(r.excessKwh), 0.0) FROM EnergyReading r WHERE r.isAnomaly = true")
    Double calculateTotalExcessKwh();

    @Query("SELECT MAX(r.activePowerWatts) FROM EnergyReading r WHERE r.readingTimestamp BETWEEN :startTime AND :endTime")
    Double findPeakPowerWattsBetween(@Param("startTime") OffsetDateTime startTime, @Param("endTime") OffsetDateTime endTime);
}
