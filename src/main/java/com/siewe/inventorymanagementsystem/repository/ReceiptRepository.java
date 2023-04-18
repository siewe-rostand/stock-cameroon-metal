package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt,Long> {
}
