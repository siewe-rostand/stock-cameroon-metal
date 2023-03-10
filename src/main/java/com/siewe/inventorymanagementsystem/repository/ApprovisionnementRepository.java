package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Approvisionnement;
import com.siewe.inventorymanagementsystem.model.Category;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovisionnementRepository extends JpaRepository<Approvisionnement, Long> {
    default Approvisionnement findOne(Long id) {
        return (Approvisionnement) findById(id).orElse(null);
    }
    @Query("SELECT approvisionnement FROM  Approvisionnement approvisionnement "
            + "WHERE approvisionnement.createdDate between ?1 and ?2 ")
    Page<Approvisionnement> findByCreatedDateBetween(LocalDateTime cdf, LocalDateTime cdt, Pageable pageable);

    @Query("SELECT approvisionnement FROM  Approvisionnement approvisionnement "
            + "WHERE approvisionnement.createdDate between ?1 and ?2 "
            + "AND approvisionnement.product.id = ?3 ")
    Page<Approvisionnement> findOne(LocalDateTime cdf, LocalDateTime cdt, Long productId, Pageable pageable);


    public interface ProductApprovisionnements {
        Long getId();
        String getProductName();
        Double getTotalApprovisionnements();
    }

    @Query("SELECT approvisionnement.product.id as productId, approvisionnement.product.name as productName, "
            + "SUM(approvisionnement.quantity * approvisionnement.prixEntree) as totalApprovisionnements FROM Approvisionnement approvisionnement "
            + "WHERE approvisionnement.createdDate between ?1 and ?2 "
            + "GROUP BY approvisionnement.product.id")
    List<ProductApprovisionnements> findProductApprovisionnementsByDateRange(LocalDateTime cdf, LocalDateTime cdt);


    @Query("SELECT approvisionnement.product.id as productId, approvisionnement.product.name as productName, "
            + "SUM(approvisionnement.quantity * approvisionnement.prixEntree) as totalApprovisionnements FROM Approvisionnement approvisionnement "
            + "WHERE approvisionnement.createdDate between ?1 and ?2 "
            + "AND approvisionnement.product.id = ?3 "
            + "GROUP BY approvisionnement.product.id")
    List<ProductApprovisionnements> findProductApprovisionnementsByDateRange(LocalDateTime cdf, LocalDateTime cdt, Long productId);

}