//package com.siewe.inventorymanagementsystem.repository;
//
//import com.siewe.inventorymanagementsystem.model.Order;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface OrdersRepository extends JpaRepository<Order,Long> {
//    default Order findOne(Long id) {
//        return (Order) findById(id).orElse(null);
//    }
//
//    @Override
//    Page<Order> findAll(Pageable pageable);
//}
