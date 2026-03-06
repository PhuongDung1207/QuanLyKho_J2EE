package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.Uom;
import com.example.JavaQuanLyKho.repository.UomRepository;
import com.example.JavaQuanLyKho.service.UomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UomServiceImpl implements UomService {

    private final UomRepository uomRepository;

    public UomServiceImpl(UomRepository uomRepository) {
        this.uomRepository = uomRepository;
    }

    @Override
    public Page<Uom> findAll(Pageable pageable) {
        return uomRepository.findAll(pageable);
    }
}

