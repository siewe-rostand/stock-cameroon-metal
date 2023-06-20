package com.siewe.inventorymanagementsystem.dto;


import com.siewe.inventorymanagementsystem.model.Orders;
import com.siewe.inventorymanagementsystem.model.Produit;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Integer id;

    private String orderRef;
    private String createdUser;
    private Timestamp createdDate;
    private Boolean deleted;

    private List<ProduitDto> products;

    public OrderDto CreateDto(Orders order){
        OrderDto orderDto = new OrderDto();
        if (order != null){
            orderDto.setId(order.getOrderId());
            orderDto.setCreatedDate(order.getCreatedDate());
            orderDto.setDeleted(order.getDeleted());
            orderDto.setCreatedUser(order.getUser().getFullName());
            ArrayList<ProduitDto> produits = new ArrayList<>();

            if (order.getProduits() != null){
                for(Produit produit: order.getProduits()){
                    produits.add(new ProduitDto().createDTO(produit));
                }
            }
            orderDto.setProducts(produits);
            return orderDto;
        }
        return null;
    }
}
