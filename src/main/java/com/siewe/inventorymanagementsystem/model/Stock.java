package com.siewe.inventorymanagementsystem.model;

import lombok.*;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stock extends BaseEntity {

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
    private Double quantity;

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
                ", stock=" + quantity +
                ", cump=" + cump +
                '}';
    }
}
