package com.siewe.inventorymanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long refId;

    private double quantity;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "product_buying_price")
    private double productBuyingPrice;

    @Min(value = 0)
    @Column(name = "cump")
    private Double cump;


    @Column(name = "date_stock")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="categoryId",referencedColumnName = "id")
    private Category category;

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
}
