package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDto {

    private Long id;
    private String name;
    private String cip;
    private String description;
    private Double price;
    private String createdDate;

    private Long categoryId;
    private String categoryName;
    private Boolean enabled;
    private Boolean deleted;

    private Double stock;
    private Double stockAlerte;
    //cout unitaire moyen pondéré or weighted average cost(anglais)
    private Double cump;
    private Double valeurStock;

    private Boolean stockBas;

    public ProductDto createDTO(Product product) {
        ProductDto productDto = new ProductDto();

        if(product != null){
            productDto.setId(product.getId());
            productDto.setCreatedDate(product.getCreatedDate());
            productDto.setName(product.getName());
            productDto.setCip(product.getCip());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());
            productDto.setEnabled(product.getEnabled());
            productDto.setDeleted(product.getDeleted());

            productDto.setStock(product.getStock());
            //if(product.getStock() != null)
            productDto.setStock(Math.round(product.getStock() * 100.00) / 100.00);


            productDto.setStockAlerte((double) 0);
            if(product.getStockAlerte() != null)
                productDto.setStockAlerte(product.getStockAlerte());

            productDto.setCump(product.getCump());
            //productDto.setValeurStock(product.getValeurStock());
            //if(product.getValeurStock() != null)
            productDto.setValeurStock(Math.round(product.getValeurStock() * 100.0) / 100.0);

            if(product.getStockAlerte() != null){
                if(product.getStock() <= product.getStockAlerte())
                    productDto.setStockBas(true);
            }

            if(product.getCategory() != null){
                productDto.setCategoryId(product.getCategory().getId());
                productDto.setCategoryName(product.getCategory().getName());
            }

        }
        return productDto;
    }
}
