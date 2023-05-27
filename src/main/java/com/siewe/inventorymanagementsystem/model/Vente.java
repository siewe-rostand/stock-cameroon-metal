package com.siewe.inventorymanagementsystem.model;

import com.siewe.inventorymanagementsystem.model.enumeration.TypePaiement;
import lombok.*;
import org.hibernate.Hibernate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vente")
public class Vente{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    /*
    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "prixVente")
    private Double prixVente;
    */

    @Column(name = "acompte")
    private Double acompte;

    @Column(name = "prixTotal")
    @DecimalMin(value = "1.0",message = "prix total de la ventesvp!!")
    private Double prixTotal;

    @Column(name = "reglement")
    private Double reglement;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;

    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vente")
    @ToString.Exclude
    private List<OrderedProduct> orderedProducts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vente")
    @ToString.Exclude
    private List<Reglement> reglements;

    @Size(max = 20)
    @Column(name = "typePaiement")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String typePaiement;

    @Column(name = "deleted")
    private Boolean deleted;

    @Transient
    public TypePaiement getTypePaiement() {
        return TypePaiement.fromValue(typePaiement);
    }

    public void setTypePaiement(TypePaiement typePaiement) {
        this.typePaiement = typePaiement.toValue();
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

    public String getDate(){
        String patternDate = "dd-MM-yyyy";
        String patternTime = "HH:mm";

        return createdDate.toString(patternDate) + " à " + createdDate.toString(patternTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Vente vente = (Vente) o;
        return id != null && Objects.equals(id, vente.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
