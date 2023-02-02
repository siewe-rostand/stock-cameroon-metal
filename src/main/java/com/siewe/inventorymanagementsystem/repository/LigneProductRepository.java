package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Approvisionnement;
import com.siewe.inventorymanagementsystem.model.LigneProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneProductRepository extends JpaRepository<LigneProduct,Long> {
    default LigneProduct findOne(Long id) {
        return (LigneProduct) findById(id).orElse(null);
    }
}
