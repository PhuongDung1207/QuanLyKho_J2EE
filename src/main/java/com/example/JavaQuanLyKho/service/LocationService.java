package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Location;

import java.util.List;
import java.util.UUID;

public interface LocationService {

    List<Location> findByWarehouse(UUID warehouseId);

    Location create(Location location);
}

