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

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "total_price")
    private Double totalPrice;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

//    @JoinColumn(name = "vente_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private Vente vente;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private  Order order;

    public OrderedProduct(Product productId,Double quantity){
        this.product = productId;
        this.quantity = quantity;
    }


    public String getCreatedDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(createdDate != null) {
            return createdDate.toString(pattern);
        }
        return null;
    }
    public void setCreatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cd = null;
        if(createdDate!=null)
            cd = LocalDateTime.parse(createdDate, formatter);
        this.createdDate = cd;
    }

    public String getUpdatedDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(updatedDate != null) {
            return updatedDate.toString(pattern);
        }
        return null;
    }

    public void setUpdatedDate(String updatedDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime ud = null;
        if(updatedDate!=null)
            ud = LocalDateTime.parse(updatedDate, formatter);
        this.updatedDate = ud;
    }
//    public double getTotalPrice() {
//        return product.getPrice() * quantity;
//    }
}
