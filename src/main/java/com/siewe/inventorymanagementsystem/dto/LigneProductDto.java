package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.LigneProduct;
import lombok.Data;

@Data
public class LigneProductDto {

    private Long id;
    private double stockTheorique;
    private double stockPhysique;
    private Long productId;
    private String product;
    private Long inventaireId;

    public LigneProductDto createDTO(LigneProduct ligneProduct) {
        LigneProductDto ligneProductDto = new LigneProductDto();
        if(ligneProduct != null){
            ligneProductDto.setId(ligneProduct.getId());
            ligneProductDto.setStockTheorique(ligneProduct.getStockTheorique());
            ligneProductDto.setStockPhysique(ligneProduct.getStockPhysique());

            if(ligneProduct.getProduct() != null){
                ligneProductDto.setProductId(ligneProduct.getProduct().getProductId());
                ligneProductDto.setProduct(ligneProduct.getProduct().getName());
            }
            if(ligneProduct.getInventaire() != null){
                ligneProductDto.setInventaireId(ligneProduct.getInventaire().getId());
            }

            return ligneProductDto;
        }
        return null;
    }

}
