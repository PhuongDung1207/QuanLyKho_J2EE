package com.example.JavaQuanLyKho.service;

import com.example.JavaQuanLyKho.model.entity.Uom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UomService {

    Page<Uom> findAll(Pageable pageable);
}

