package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Category;
import com.siewe.inventorymanagementsystem.model.Manquant;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ManquantRepository extends JpaRepository<Manquant,Long> {
    default Manquant findOne(Long id) {
        return (Manquant) findById(id).orElse(null);
    }
    @Query("SELECT manquant FROM  Manquant manquant "
            + "WHERE manquant.createdDate between ?1 and ?2 ")
    Page<Manquant> findByCreatedDateBetween(LocalDateTime cdf, LocalDateTime cdt, Pageable pageable);


    public interface ProductManquants {
        Long getId();
        String getProductName();
        Double getTotalManquants();
    }

    @Query("SELECT manquant.product.id as productId, manquant.product.name as productName, "
            + "SUM(manquant.quantity * manquant.cout) as totalManquants FROM Manquant manquant "
            + "WHERE manquant.createdDate between ?1 and ?2 "
            + "GROUP BY manquant.product.id")
    List<ProductManquants> findProductManquantsByDateRange(LocalDateTime cdf, LocalDateTime cdt);
}
