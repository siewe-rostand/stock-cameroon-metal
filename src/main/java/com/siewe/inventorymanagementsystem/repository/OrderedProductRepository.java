package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import com.siewe.inventorymanagementsystem.model.Product;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Long> {

    default OrderedProduct findOne(Long id) {
        return (OrderedProduct) findById(id).orElse(null);
    }
//    OrderedProduct findByVenteAndProduct(Long venteId, Long productId);

    @Query("select p from Product p inner join p.orderedProducts op where p.id = op.product.id")
    OrderedProduct findByProduct( Long productId);

//    List<OrderedProduct> findByVente(Long id);

//    @Query("SELECT op FROM  OrderedProduct op "
//            + "WHERE op.product.id = ?1 "
//            + "AND op.vente.createdDate between ?2 and ?3 ")
//    List<OrderedProduct> findOneAndCreatedDateBetween(Long productId, LocalDateTime df, LocalDateTime dt);
//
//    @Query("SELECT op FROM  OrderedProduct op "
//            + "WHERE op.product.id = ?1 "
//            + "AND op.vente.user.userId = ?2 "
//            + "AND op.vente.createdDate between ?3 and ?4 ")
//    List<OrderedProduct> findOneAndSellerIdCreatedDateBetween(Long productId, Long sellerId, LocalDateTime d, LocalDateTime localDateTime);
}
