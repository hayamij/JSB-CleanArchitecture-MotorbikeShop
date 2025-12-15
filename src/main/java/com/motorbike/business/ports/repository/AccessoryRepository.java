package com.motorbike.business.ports.repository;

import java.util.List;
import java.util.Optional;

import com.motorbike.domain.entities.PhuKienXeMay;

public interface AccessoryRepository {
    PhuKienXeMay save(PhuKienXeMay accessory);
    Optional<PhuKienXeMay> findById(Long id);
    List<PhuKienXeMay> findAllAccessories();
    void deleteById(Long id);
}
