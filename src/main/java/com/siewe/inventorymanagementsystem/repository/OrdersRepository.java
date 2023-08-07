package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long> {
    default Orders findOne(Long id) {
        return (Orders) findById(id).orElse(null);
    }

    Orders findByOrderId(Long orderId);

    List<Orders> findByOrderRef(String ref);

    @Override
    Page<Orders> findAll(Pageable pageable);

    Boolean existsByOrderId(Long id);
}
