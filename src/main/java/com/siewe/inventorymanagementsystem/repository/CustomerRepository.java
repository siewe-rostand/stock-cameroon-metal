package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    default Customer findOne(Long id) {
        return (Customer) findById(id).orElse(null);
    }

    Customer findByCustomerId(Long id);

    @Query("select s from Customer s "
            + "where (s.name like ?1 or ?1 is null) ")
    Page<Customer> findAll(String name, Pageable pageable);

    @Query("SELECT s FROM Customer s WHERE s.name like ?1 ")
    List<Customer> findByMc(String mc);
}
