package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ordered_product")
public class OrderedProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "prix_vente")
    private Double prixVente;

    @JoinColumn(name = "vente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Vente vente;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product product;
}
