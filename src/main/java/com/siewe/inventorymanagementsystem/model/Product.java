package com.siewe.inventorymanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

//    @NotBlank(message = "product name need to be provided")
    @NotBlank
    @Column(name="name",unique = true)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name="cip")
    private String cip;

    @Column(name = "description")
    private String description;

    @Column(name = "enable")
    private Boolean enabled;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "deleted")
    private Boolean deleted;

//    @NotNull
    @DecimalMin(value = "1.0")
    @Column(name = "price")
    private Double price;


//    @NotNull
//    @Min(value = 0)
@DecimalMin(value = "1.0")
    @Column(name = "quantity")
    private Double quantity;
    @Min(0)
    @Column(name = "stock")
    private double stock;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "stockAlerte")
    private Double stockAlerte;

    //cout unitaire moyen pondéré or weighted average cost(anglais)
    @Min(value = 0)
    @Column(name = "cump")
    private double cump;

    @Min(value = 0)
    @Column(name = "valeurStock")
    private double valeurStock;

    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private List<ProductStock> productProductStocks;

    /*@OneToMany(mappedBy = "product")
    private List<Vente> ventes;*/

    @OneToMany(mappedBy = "product")
    private List<Manquant> manquants;

    @OneToMany(mappedBy = "product")
    private List<Approvisionnement> approvisionnements;

    @OneToMany(mappedBy = "order")
    private List<OrderedProduct> orderedProducts;

    public Product(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        if(createdDate != null) {
            return createdDate.toString(pattern);
        }
        return null;
    }

    public void setCreatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(createdDate!=null && !createdDate.isEmpty())
            cd = formatter.parseLocalDateTime(createdDate);
        this.createdDate = cd;
    }
    public String getUpdatedDate() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        if(updatedDate != null) {
            return updatedDate.toString(pattern);
        }
        return null;
    }

    public void setUpdatedDate(String updatedDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime cd = null;
        if(updatedDate !=null && !updatedDate.isEmpty())
            cd = formatter.parseLocalDateTime(updatedDate);
        this.updatedDate = cd;
    }

}
