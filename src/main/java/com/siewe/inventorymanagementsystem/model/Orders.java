package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity(name = "orders")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Orders{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer orderId;

    private Boolean deleted;

    @Column(name = "order_ref",unique = true)
    private  String orderRef;

    @CreationTimestamp
    private Timestamp createdDate;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orders")
    @ToString.Exclude
    private List<OrderedProduit> orderedProduits;

}