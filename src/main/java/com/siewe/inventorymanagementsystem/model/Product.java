package com.siewe.inventorymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @NotBlank(message = "product name need to be provided")
    @Column(name="name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;
    @Column(name="cip")
    private String cip;

    @Column(name = "description")
    private String description;

    // @Getter(AccessLevel.NONE)
    // @Setter(AccessLevel.NONE)
    // @Column(name = "created_date")
    // private LocalDate createdDate;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime createdDate;

    @Column(name = "enable")
    private Boolean enabled;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "deleted")
    private Boolean deleted;

    @Min(1)
    @Basic(optional = false)
    @Column(name = "price")
    private double price;


    @Column(name = "quantity")
    private double quantity;
    @Min(0)
    @Column(name = "stock")
    private double stock;

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

    public Product(String name) {
        this.name = name;
    }

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

}
