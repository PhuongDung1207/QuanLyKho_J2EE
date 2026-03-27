package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.Stocktake;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface StocktakeRepository extends JpaRepository<Stocktake, UUID> {
    boolean existsByCode(String code);
}
