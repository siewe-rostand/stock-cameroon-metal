package com.siewe.inventorymanagementsystem.repository;

import com.siewe.inventorymanagementsystem.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice,Long> {

    Invoice findInvoiceByInvoiceId(Long id);
}
