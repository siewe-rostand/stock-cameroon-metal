package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.ProductPricing;
import com.siewe.inventorymanagementsystem.model.UserRole;
import lombok.Data;

@Data
public class ProductPricingDto {

    private Long refId;
    private Long productId;
    private Long PricingId;

    public ProductPricingDto createDto(ProductPricing userRole){
        ProductPricingDto userRoleDto = new ProductPricingDto();

        if (userRole != null){
            userRoleDto.setRefId(userRole.getRefId());
            userRoleDto.setProductId(userRole.getProduct().getId());
            userRoleDto.setPricingId(userRole.getPricing().getPricingId());
        }
        return userRoleDto;
    }
}
