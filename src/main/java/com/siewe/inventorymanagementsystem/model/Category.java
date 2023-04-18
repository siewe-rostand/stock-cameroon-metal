package com.siewe.inventorymanagementsystem.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "enabled",nullable = false)
    private Boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Product> products;

    public Category(String name) {
        this.name = name;
        this.enabled = true;
    }
}
