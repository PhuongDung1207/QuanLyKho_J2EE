package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    List<Location> findByWarehouseId(UUID warehouseId);
}

