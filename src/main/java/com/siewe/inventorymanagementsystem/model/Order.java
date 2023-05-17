package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
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


    @Column(name = "shipping_fee")
    private double shippingFee;

    @Column(name = "order_status")
    private String orderStatus;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private  Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderedProduct> orderedProducts;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm")
    @Column(name = "deleted_date")
    private LocalDateTime deletedDate;

    public Order(List<OrderedProduct> orderedProducts){
        this.orderedProducts = orderedProducts;
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

    public void setUpdatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cd = null;
        if(createdDate!=null)
            cd = LocalDateTime.parse(createdDate, formatter);
        this.updatedDate = cd;
    }
    public String getDeletedDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(deletedDate != null) {
            return deletedDate.toString(pattern);
        }
        return null;
    }

    public void setDeletedDate(String deletedDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cd = null;
        if(deletedDate!=null)
            cd = LocalDateTime.parse(deletedDate, formatter);
        this.deletedDate = cd;
    }


}
