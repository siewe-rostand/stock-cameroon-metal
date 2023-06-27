package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.OrderedProduit;
import lombok.Data;

@Data
public class OrderedProduitDto {

    private Integer id;
    private Double metrage;
    private Integer orderId;
    private String name;
    private Integer produitId;
    private String productRef;

   public OrderedProduitDto createDTO(OrderedProduit orderedProduit){
        OrderedProduitDto orderedProduitDto = new OrderedProduitDto();

        if (orderedProduit != null){
            orderedProduitDto.setId(orderedProduit.getId());
            orderedProduitDto.setMetrage(orderedProduit.getMetrage());
            if (orderedProduitDto.getProduitId() != null){
                orderedProduitDto.setProduitId(orderedProduit.getProduct().getProduitId());
                orderedProduitDto.setName(orderedProduit.getProduct().getName());
                orderedProduitDto.setProductRef(orderedProduit.getProduct().getRef());
            }
            if (orderedProduitDto.getOrderId() != null){
                orderedProduitDto.setOrderId(orderedProduit.getOrders().getOrderId());
            }
        }
        return orderedProduitDto;
    }
}
