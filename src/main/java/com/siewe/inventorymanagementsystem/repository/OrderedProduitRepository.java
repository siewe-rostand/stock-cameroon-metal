package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.OrderedProduit;
import com.siewe.inventorymanagementsystem.model.Orders;
import com.siewe.inventorymanagementsystem.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProduitRepository extends JpaRepository<OrderedProduit, Integer> {

    default OrderedProduit findOne(Integer id) {
        return (OrderedProduit) findById(id).orElse(null);
    }
    OrderedProduit findByOrdersAndProduct(Orders orders, Produit produit);
}
