package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventaire")
public class Inventaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "validated")
    private Boolean validated;

    @JoinColumn(name = "user_validated_id", referencedColumnName = "id")
    @ManyToOne
    private User validatedBy;

    @Column(name = "validatedDate")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private LocalDateTime validatedDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventaire")
    @ToString.Exclude
    private List<LigneProduct> lignesProduct;

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

    public String getValidatedDate() {
        String pattern = "yyyy-MM-dd HH:mm";
        if(validatedDate != null) {
            return validatedDate.toString(pattern);
        }
        return null;
    }

    public void setValidatedDate(String validatedDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        LocalDateTime cd = null;
        if(validatedDate!=null)
            cd = LocalDateTime.parse(validatedDate, formatter);
        this.validatedDate = cd;
    }

    @Override
    public String toString() {
        if(createdDate != null)
            return "Inventaire du " + this.createdDate.getDayOfMonth() + "-" +  this.createdDate.getMonthOfYear() + "-" + this.createdDate.getYear();
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Inventaire that = (Inventaire) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
