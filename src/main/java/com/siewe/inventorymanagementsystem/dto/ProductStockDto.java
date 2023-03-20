package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.ProductStock;
import lombok.Data;

@Data
public class ProductStockDto {

    private Long id;
    private String date;
    private Long productId;
    private Double stock;
    //cout unitaire moyen pondéré or weighted average cost(anglais)
    private Double cump;

    public ProductStockDto createDTO(ProductStock productStock) {
        ProductStockDto productStockDto = new ProductStockDto();

        if(productStock != null){
            productStockDto.setId(productStock.getId());
            productStockDto.setDate(productStock.getDate());
            productStockDto.setStock(productStock.getStock());
            productStockDto.setCump(productStock.getCump());

            if(productStock.getProduct() != null)
                productStockDto.setProductId(productStock.getProduct().getId());
        }
        return productStockDto;
    }
}
