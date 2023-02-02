package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Manquant;
import lombok.Data;

@Data
public class ManquantDto {

    private Long id;
    private String createdDate;
    private Double quantity;
    private Double cout;
    private Long productId;
    private String productName;

    public ManquantDto createDTO(Manquant manquant) {
        ManquantDto manquantDto = new ManquantDto();
        if(manquant != null){
            manquantDto.setId(manquant.getId());
            manquantDto.setCreatedDate(manquant.getCreatedDate());
            manquantDto.setQuantity(manquant.getQuantity());
            manquantDto.setCout(manquant.getCout());

            if(manquant.getProduct() != null){
                manquantDto.setProductId(manquant.getProduct().getProductId());
                manquantDto.setProductName(manquant.getProduct().getName());
            }

            /*if(manquant.getUser() != null){
                manquantDto.setUserId(manquant.getUser().getId());
                manquantDto.setUserName(manquant.getUser().getName());
            }*/
            return manquantDto;
        }
        return null;
    }
}
