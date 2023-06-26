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

    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Orders orders;

    @CreationTimestamp
    private Timestamp createdDate;
    @UpdateTimestamp
    private Timestamp updatedDate;

    @OneToMany(mappedBy = "product")
    private List<OrderedProduit> orderedProduitList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Produit produit = (Produit) o;
        return produitId != null && Objects.equals(produitId, produit.produitId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
