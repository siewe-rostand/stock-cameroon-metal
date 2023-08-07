package com.siewe.inventorymanagementsystem.dto;


import com.siewe.inventorymanagementsystem.model.OrderedProduit;
import com.siewe.inventorymanagementsystem.model.Orders;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;

    private String orderRef;
    private String createdUser;
    private Timestamp createdDate;
    private Boolean deleted;
    private Long customerId;
    private CustomerDto customer;
    private  Long userId;
    private  String username;

    private List<OrderedProduitDto> products;

    public OrderDto CreateDto(Orders order){
        OrderDto orderDto = new OrderDto();
        if (order != null){
            orderDto.setId(order.getOrderId());
            orderDto.setCreatedDate(order.getCreatedDate());
            orderDto.setDeleted(order.getDeleted());
            orderDto.setOrderRef(order.getOrderRef());
            ArrayList<OrderedProduitDto> produits = new ArrayList<>();

            if (order.getOrderedProduits() != null){
                for(OrderedProduit produit: order.getOrderedProduits()){
                    produits.add(new OrderedProduitDto().createDTO(produit));
                }
            }
            orderDto.setProducts(produits);
            if (order.getCustomer() != null){
                orderDto.setCustomerId(order.getCustomer().getCustomerId());
                orderDto.setCustomer(new CustomerDto().createDTO(order.getCustomer()));
            }

            if (order.getUser() != null){
                orderDto.setUserId(order.getUser().getUserId());
                orderDto.setUsername(order.getUser().getFullName());
            }
            return orderDto;
        }
        return null;
    }
}
