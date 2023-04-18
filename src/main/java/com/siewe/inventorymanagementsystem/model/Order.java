package com.siewe.inventorymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "order_ref")
    private UUID order_ref;

    private Boolean deleted;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "shipping_fee")
    private double shippingFee;

    @Column(name = "order_status")
    private String orderStatus;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private  User customer;

    @OneToMany(mappedBy = "order")
    private List<OrderedProduct> orderedProducts;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    public String getCreatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(createdDate != null) {
            return createdDate.format(formatter);
        }
        return null;
    }
    public void setCreatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(createdDate!=null)
            cd = LocalDateTime.parse(createdDate,formatter);
        this.createdDate = cd;
    }

    public String getUpdatedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(updatedDate != null) {
            return updatedDate.format(formatter);
        }
        return null;
    }

    public void setUpdatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(createdDate!=null)
            cd = LocalDateTime.parse(createdDate,formatter);
        this.updatedDate = cd;
    }
    public String getDeletedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(deletedDate != null) {
            return deletedDate.format(formatter);
        }
        return null;
    }

    public void setDeletedDate(String deletedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(deletedDate!=null)
            cd = LocalDateTime.parse(deletedDate,formatter);
        this.deletedDate = cd;
    }


}
