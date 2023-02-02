package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Category;
import com.siewe.inventorymanagementsystem.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    default Supplier findOne(Long id) {
        return (Supplier) findById(id).orElse(null);
    }

    @Query("select s from Supplier s "
            + "where (s.name like ?1 or ?1 is null) ")
    Page<Supplier> findAll(String name, Pageable pageable);

    @Query("SELECT s FROM Supplier s WHERE s.name like ?1 ")
    List<Supplier> findByMc(String mc);
}
