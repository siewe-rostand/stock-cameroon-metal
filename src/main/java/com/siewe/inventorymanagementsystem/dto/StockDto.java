package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Stock;
import lombok.Data;

@Data
public class StockDto {

    private Long id;
    private String date;
    private Long productId;
    private Double stock;

    private Double cump;

    public StockDto createDTO(Stock productStock) {
        StockDto productStockDto = new StockDto();

        if(productStock != null){
            productStockDto.setId(productStock.getId());
            productStockDto.setDate(productStock.getCreatedDate());
            productStockDto.setStock(productStock.getQuantity());
            productStockDto.setCump(productStock.getProduct().getPrice());

            if(productStock.getProduct() != null)
                productStockDto.setProductId(productStock.getProduct().getId());
        }
        return productStockDto;
    }
}
