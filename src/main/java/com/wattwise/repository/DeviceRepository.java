package com.wattwise.repository;

import com.wattwise.entity.EnergyDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<EnergyDevice, Long> {
    Optional<EnergyDevice> findByDeviceId(String deviceId);
    boolean existsByDeviceId(String deviceId);
    List<EnergyDevice> findByBuildingIgnoreCase(String building);
    List<EnergyDevice> findByStatus(String status);
}
