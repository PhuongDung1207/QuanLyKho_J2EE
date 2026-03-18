package com.example.JavaQuanLyKho.service.impl;

import com.example.JavaQuanLyKho.model.entity.Uom;
import com.example.JavaQuanLyKho.repository.UomRepository;
import com.example.JavaQuanLyKho.service.UomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public Uom save(String code, String name) {
        Uom uom = new Uom();
        uom.setCode(code.toUpperCase().trim());
        uom.setName(name.trim());
        return uomRepository.save(uom);
    }

    @Override
    public Uom update(UUID id, String code, String name) {
        Uom uom = uomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("UOM not found: " + id));
        uom.setCode(code.toUpperCase().trim());
        uom.setName(name.trim());
        return uomRepository.save(uom);
    }

    @Override
    public void delete(UUID id) {
        if (!uomRepository.existsById(id)) {
            throw new IllegalArgumentException("UOM not found: " + id);
        }
        uomRepository.deleteById(id);
    }
}


