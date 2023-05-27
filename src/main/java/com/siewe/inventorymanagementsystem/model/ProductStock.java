package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "product_stock")
public class ProductStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    @Basic(optional = false)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private LocalDate date;

    @Min(value = 0)
    @Column(name = "stock")
    private Double stock;

    @Min(value = 0)
    @Column(name = "cump")
    private Double cump;

    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Product product;


    public String getDate() {
        String pattern = "yyyy-MM-dd";
        if(date != null) {
            return date.toString(pattern);
        }
        return null;
    }

    public void setDate(String date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate ld = null;
        if(date!=null)
            ld = formatter.parseLocalDate(date);
        this.date = ld;
    }

    @Override
    public String toString() {
        return "ProductStock{" +
                "id=" + id +
                ", date=" + date +
                ", stock=" + stock +
                ", cump=" + cump +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductStock that = (ProductStock) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
