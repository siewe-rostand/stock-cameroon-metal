package com.siewe.inventorymanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long categoryId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(mappedBy="category")
    private List<Product> products;

    //bidirectional many-to-one association to Stock
    @OneToMany(mappedBy="category")
    private List<Stock> stocks;

    public Category(String name) {
        this.name = name;
    }
}
