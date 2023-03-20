package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Stock;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock,Long> {

    Stock findByProductAndDate(Long productId, LocalDate date);

    Stock findFirstByProductOrderByDateDesc(Long productId);

    List<Stock> findByProductAndDateAfter(Long id, LocalDate localDate);

    @Query("SELECT ps FROM  Stock ps "
            + "WHERE ps.product.productId = ?1 AND ps.date between ?2 and ?3 ")
    List<Stock> findByProductIdAndDateRange(Long productId, LocalDate df, LocalDate dt);
}