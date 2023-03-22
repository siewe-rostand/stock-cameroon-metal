package com.siewe.inventorymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private Boolean deleted;

    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private  User customer;

    @OneToMany(mappedBy = "order")
    private List<OrderedProduct> orderedProducts;


}
