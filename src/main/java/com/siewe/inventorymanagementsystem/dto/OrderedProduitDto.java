package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.OrderedProduit;
import lombok.Data;

@Data
public class OrderedProduitDto {

    private Long id;
    private Double metrage;
    private Integer orderId;
    private String name;
    private Integer produitId;

   public OrderedProduitDto createDTO(OrderedProduit orderedProduit){
        OrderedProduitDto orderedProduitDto = new OrderedProduitDto();

        if (orderedProduit != null){
            orderedProduitDto.setId(orderedProduit.getId());
            orderedProduitDto.setMetrage(orderedProduit.getMetrage());
            if (orderedProduitDto.getProduitId() != null){
                orderedProduitDto.setProduitId(orderedProduit.getProduct().getProduitId());
                orderedProduitDto.setName(orderedProduit.getProduct().getName());
            }
            if (orderedProduitDto.getOrderId() != null){
                orderedProduitDto.setOrderId(orderedProduit.getOrders().getOrderId());
            }
        }
        return orderedProduitDto;
    }
}
