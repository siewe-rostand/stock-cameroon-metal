package com.siewe.inventorymanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "product")
public class Product extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long productId;

    @Column(name="name")
    private String name;

//    @Min(value = 0)0
    @Basic(optional = false)
    @Column(name = "price")
    private double price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "enabled")
    private Boolean enabled;

//    @Min(value = 0)
    @Column(name = "cump")
    private double cump;

    @Column(name="cip")
    private String cip;

    @Column(name = "stock")
    private double stock;

    @Column(name = "stockAlerte")
    private Double stockAlerte;

//    @Min(value = 0)
    @Column(name = "valeurStock")
    private double valeurStock;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "product_selling_price")
    private double productSellingPrice;

    @ManyToOne
    @JoinColumn(name="category_id",referencedColumnName = "id")
    private Category category;

    //bidirectional many-to-one association to ProductInvoice
    @OneToMany(mappedBy="product")
    private List<ProductInvoice> productInvoices;

    //bidirectional many-to-one association to ProductPricing
    @OneToMany(mappedBy="product")
    private List<ProductPricing> productPricings;

    //bidirectional many-to-one association to Stock
    @OneToMany(mappedBy="product")
    private List<Stock> stocks;

    @OneToMany(mappedBy = "product")
    private List<Manquant> manquants;

    @OneToMany(mappedBy = "product")
    private List<Approvisionnement> approvisionnements;

    public Product(String name) {
        this.name = name;
    }

    public List<Stock> getStocks() {
        return this.stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public Stock addStock(Stock stock) {
        getStocks().add(stock);
        stock.setProduct(this);

        return stock;
    }

    public Stock removeStock(Stock stock) {
        getStocks().remove(stock);
        stock.setProduct(null);

        return stock;
    }
}
