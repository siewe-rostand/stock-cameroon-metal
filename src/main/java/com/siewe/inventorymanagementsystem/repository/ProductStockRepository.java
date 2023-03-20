package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.ProductStock;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock,Long> {

    ProductStock findByProductIdAndDate(Long productId, LocalDate date);

    ProductStock findFirstByProductIdOrderByDateDesc(Long productId);

    List<ProductStock> findByProductIdAndDateAfter(Long id, LocalDate localDate);

    @Query("SELECT ps FROM  ProductStock ps "
            + "WHERE ps.product.id = ?1 AND ps.date between ?2 and ?3 ")
    List<ProductStock> findByProductIdAndDateRange(Long productId, LocalDate df, LocalDate dt);
}
