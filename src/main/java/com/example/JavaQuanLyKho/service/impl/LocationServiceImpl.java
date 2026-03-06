package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.Location;
import com.example.JavaQuanLyKho.repository.LocationRepository;
import com.example.JavaQuanLyKho.service.LocationService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> findByWarehouse(UUID warehouseId) {
        return locationRepository.findByWarehouseId(warehouseId);
    }

    @Override
    @Transactional
    public Location create(Location location) {
        try {
            return locationRepository.save(location);
        } catch (Exception ex) {
            throw new DataIntegrityViolationException("CONFLICT_DUPLICATE_CODE");
        }
    }
}

