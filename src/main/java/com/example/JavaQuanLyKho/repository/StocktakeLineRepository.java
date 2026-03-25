package com.example.JavaQuanLyKho.repository;

import com.example.JavaQuanLyKho.model.entity.StocktakeLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StocktakeLineRepository extends JpaRepository<StocktakeLine, UUID> {
    List<StocktakeLine> findByStocktakeId(UUID stocktakeId);
    Optional<StocktakeLine> findByStocktakeIdAndProductId(UUID stocktakeId, UUID productId);
}
