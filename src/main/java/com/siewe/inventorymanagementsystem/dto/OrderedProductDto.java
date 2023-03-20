package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import lombok.Data;

@Data
public class OrderedProductDto {

    private Long id;
    private Double quantity;
    private Double prixVente;
    private String name;
    private Long venteId;
    private Long productId;

    public OrderedProductDto createDTO(OrderedProduct orderedProduct) {
        OrderedProductDto orderedProductDto = new OrderedProductDto();
        if(orderedProduct != null){
            orderedProductDto.setId(orderedProduct.getId());
            orderedProductDto.setQuantity(orderedProduct.getQuantity());
            orderedProductDto.setPrixVente(orderedProduct.getPrixVente());
            if(orderedProduct.getProduct() != null){
                orderedProductDto.setProductId(orderedProduct.getProduct().getId());
                orderedProductDto.setName(orderedProduct.getProduct().getName());
            }
            if(orderedProduct.getVente() != null)
                orderedProductDto.setVenteId(orderedProduct.getVente().getVenteId());
        }
        return orderedProductDto;
    }
}
