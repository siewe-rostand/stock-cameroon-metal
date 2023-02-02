package com.siewe.inventorymanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Invoice  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long invoiceId;

    @Column(name = "line_total")
    private double lineTotal;

    private Long productId;

    private String productName;

    private double quantity;

    private double total;

    private BigDecimal version;

    @OneToMany(mappedBy="invoice")
    private List<ProductInvoice> productInvoices;
}
