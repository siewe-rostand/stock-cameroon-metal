package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "approvisionnement")
public class Approvisionnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "prixEntree")
    private Double prixEntree;

    /*@JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;*/

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "expiryDate")
    private LocalDate expiryDate;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product product;

 //    @JoinColumn(name = "bl_id", referencedColumnName = "id")
//    @ManyToOne
//    private Bl bl;

    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    @ManyToOne
    private Supplier supplier;

    /*
    @OneToOne(mappedBy = "approvisionnement", cascade = CascadeType.REMOVE)
    private Mouvement mouvement;
    */

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

    public String getExpiryDate() {
        String pattern = "yyyy-MM-dd";
        if(expiryDate != null) {
            return expiryDate.toString(pattern);
        }
        return null;
    }

    public void setExpiryDate(String expiryDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate cd = null;
        if(expiryDate!=null && !expiryDate.isEmpty())
            cd = formatter.parseLocalDate(expiryDate);
        this.expiryDate = cd;
    }
}
