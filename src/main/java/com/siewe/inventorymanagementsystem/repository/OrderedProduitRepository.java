package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.OrderedProduit;
import com.siewe.inventorymanagementsystem.model.Orders;
import com.siewe.inventorymanagementsystem.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderedProduitRepository extends JpaRepository<OrderedProduit, Integer> {

    default OrderedProduit findOne(Integer id) {
        return (OrderedProduit) findById(id).orElse(null);
    }
    OrderedProduit findByOrdersAndProduct(Orders orders, Produit produit);
    OrderedProduit findByOrdersOrderIdAndProductProduitId(Long orders_orderId, Integer product_produitId);

    @Query(  "SELECT op FROM OrderedProduit op WHERE op.orders.orderId = ?1 AND op.product.produitId = ?2")
    OrderedProduit findByOrdersIdAndProductId(Long orders_orderId, Integer product_produitId);

    @Query(value = "SELECT * FROM ordered_produit op WHERE op.id LIKE %?1%",nativeQuery = true)
    OrderedProduit findByThisId(Integer id);

}
