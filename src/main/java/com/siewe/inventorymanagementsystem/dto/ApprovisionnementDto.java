package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Approvisionnement;
import lombok.Data;

@Data
public class ApprovisionnementDto {
    private Long id;
    private String createdDate;
    private String expiryDate;

    private Double quantity;
    private Double prixEntree;
    private Double coutTotal;
    private Long productId;
    private String productName;

    public ApprovisionnementDto createDTO(Approvisionnement approvisionnement){
        ApprovisionnementDto approvisionnementDto = new ApprovisionnementDto();

        if(approvisionnement != null){
            approvisionnementDto.setId(approvisionnement.getId());
            approvisionnementDto.setCreatedDate(approvisionnement.getCreatedDate());
            approvisionnementDto.setExpiryDate(approvisionnement.getExpiryDate());
            approvisionnementDto.setQuantity(approvisionnement.getQuantity());
            if(approvisionnement.getPrixEntree() != null){
                approvisionnementDto.setPrixEntree((double) Math.round(approvisionnement.getPrixEntree() * 100.0) / 100.0);
                approvisionnementDto.setCoutTotal((double) Math.round( (approvisionnement.getPrixEntree() * approvisionnement.getQuantity()) * 100.0)/100.0);
            }

            if(approvisionnement.getProduct() != null){
                approvisionnementDto.setProductId(approvisionnement.getProduct().getProductId());
                approvisionnementDto.setProductName(approvisionnement.getProduct().getName());
            }
            return approvisionnementDto;
        }

        return null;
    }
}
