package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;
    @Column(name="cip")
    private String cip;

    @Column(name = "description")
    private String description;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "enable")
    private Boolean enabled;

    @Column(name = "available")
    private Boolean available;

    @Column(name = "deleted")
    private Boolean deleted;

    @Min(value = 0)
    @Basic(optional = false)
    @Column(name = "price")
    private double price;

    @Min(value = 0)
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
        String pattern = "yyyy-MM-dd";
        if(createdDate != null) {
            return createdDate.toString(pattern);
        }
        return null;
    }

    public void setCreatedDate(String createdDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate cd = null;
        if(createdDate!=null)
            cd = formatter.parseLocalDate(createdDate);
        this.createdDate = cd;
    }
    @Transient
    public String getPhotosImagePath() {
        if (imageUrl == null || id == null) return null;

        return "/user-photos/" + id + "/" + imageUrl;
    }
}
