package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer produitId;
    @Column(unique = true,name = "ref")
    private String ref;
    private String name;

    private String color;

    private Double epaiseur;

    @DecimalMin(value = "1.0")
    private Double metrage;

    private Boolean available;

    @CreationTimestamp
    private Timestamp createdDate;
    @UpdateTimestamp
    private Timestamp updatedDate;

    @OneToMany(mappedBy = "product")
    private List<OrderedProduit> orderedProduitList;
}
