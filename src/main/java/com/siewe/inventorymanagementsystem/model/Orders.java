package com.siewe.inventorymanagementsystem.model;

import com.siewe.inventorymanagementsystem.model.enumeration.TypePaiement;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer orderId;

    private Boolean deleted;

    @Column(name = "order_ref")
    private  String orderRef;

    @CreationTimestamp
    private Timestamp createdDate;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @ToString.Exclude
    private List<Produit> produits;

}
