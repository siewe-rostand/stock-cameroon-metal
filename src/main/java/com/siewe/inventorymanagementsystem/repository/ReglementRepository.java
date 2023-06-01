package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Manquant;
import com.siewe.inventorymanagementsystem.model.Reglement;
import com.siewe.inventorymanagementsystem.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReglementRepository extends JpaRepository<Reglement, Long> {
    default Reglement findOne(Long id) {
        return (Reglement) findById(id).orElse(null);
    }

    List<Reglement> findAllByVente(Vente vente);
}
