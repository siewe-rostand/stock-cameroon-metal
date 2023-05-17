package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Order;
import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import com.siewe.inventorymanagementsystem.model.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;

    private Long customerId;
    private CustomerDto customer;
    private String createdDate;
    private Boolean deleted;
    private double totalAmount;

    private List<OrderedProductDto> orderedProducts;

    public OrderDto CreateDto(Order order){
        OrderDto orderDto = new OrderDto();
        if (order != null){
            orderDto.setId(order.getId());
            orderDto.setCreatedDate(order.getCreatedDate());
            orderDto.setDeleted(order.getDeleted());

            if (order.getCustomer() != null){
               orderDto.setCustomerId(order.getCustomer().getId());

               orderDto.setCustomer(new CustomerDto().createDTO(order.getCustomer()));
            }
            ArrayList<OrderedProductDto> orderedProductDtos = new ArrayList<>();
            if (order.getOrderedProducts() != null){
                for(OrderedProduct orderedProduct: order.getOrderedProducts()){
                    orderedProductDtos.add(new OrderedProductDto().createDTO(orderedProduct));
                }
            }
            orderDto.setOrderedProducts(orderedProductDtos);
            return orderDto;
        }
        return null;
    }
}
