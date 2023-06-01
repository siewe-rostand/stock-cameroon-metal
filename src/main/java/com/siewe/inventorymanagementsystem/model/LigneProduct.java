package com.siewe.inventorymanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LigneProduct")
public class LigneProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "stockTheorique")
    private double stockTheorique;

    @Column(name = "stockPhysique")
    private double stockPhysique;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product product;

    @JoinColumn(name = "inventaire_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Inventaire inventaire;
}
