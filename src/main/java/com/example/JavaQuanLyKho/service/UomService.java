package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Uom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UomService {

    Page<Uom> findAll(Pageable pageable);

    Uom save(String code, String name);

    Uom update(UUID id, String code, String name);

    void delete(UUID id);
}

