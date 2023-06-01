package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Approvisionnement;
import com.siewe.inventorymanagementsystem.model.Inventaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventaireRepository extends JpaRepository<Inventaire,Long> {
    default Inventaire findOne(Long id) {
        return (Inventaire) findById(id).orElse(null);
    }
}
