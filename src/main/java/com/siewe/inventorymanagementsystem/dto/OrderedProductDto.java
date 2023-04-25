package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.OrderedProduct;
import lombok.Data;

@Data
public class OrderedProductDto {

    private Long id;
    private Double quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String name;
    private Long saleId;
    private Long orderId;
    private Long productId;

    public OrderedProductDto createDTO(OrderedProduct orderedProduct) {
        OrderedProductDto orderedProductDto = new OrderedProductDto();
        if(orderedProduct != null){
            orderedProductDto.setId(orderedProduct.getId());
            orderedProductDto.setQuantity(orderedProduct.getQuantity());
//            orderedProductDto.setUnitPrice(orderedProduct.getUnitPrice());
//            orderedProductDto.setTotalPrice(orderedProduct.getUnitPrice()*orderedProduct.getQuantity());


            if(orderedProduct.getProduct() != null){
                orderedProductDto.setProductId(orderedProduct.getProduct().getId());
                orderedProductDto.setName(orderedProduct.getProduct().getName());
                orderedProductDto.setUnitPrice(orderedProduct.getProduct().getPrice());
                orderedProductDto.setTotalPrice(orderedProduct.getTotalPrice());
                System.out.println("------------"+orderedProduct.getProduct().getPrice());
            }
//            if(saleId!= null)
//                orderedProductDto.setSaleId(orderedProduct.getVente().getVenteId());

            if (orderedProduct.getOrder() != null){
                orderedProductDto.setOrderId(orderedProduct.getOrder().getId());
            }
        }
        return orderedProductDto;
    }
}
