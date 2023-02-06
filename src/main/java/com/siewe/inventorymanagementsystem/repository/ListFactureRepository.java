package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.ListFacture;
import com.siewe.inventorymanagementsystem.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListFactureRepository extends JpaRepository<ListFacture,Long> {
    default ListFacture findOne(Long id) {
        return (ListFacture) findById(id).orElse(null);
    }
}
