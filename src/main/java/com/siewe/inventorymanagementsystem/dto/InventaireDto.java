package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Inventaire;
import com.siewe.inventorymanagementsystem.model.LigneProduct;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InventaireDto {

    private Long id;
    private String label;
    private String createdDate;
    private double ecartTotal;
    private Boolean validated;
    private String validatedBy;
    private String validatedDate;
    private List<LigneProductDto> lignesProduct;

    public InventaireDto createDTO(Inventaire inventaire) {
        InventaireDto inventaireDto = new InventaireDto();
        if(inventaire != null){
            inventaireDto.setId(inventaire.getId());
            inventaireDto.setLabel(inventaire.toString());
            inventaireDto.setCreatedDate(inventaire.getCreatedDate());

            inventaireDto.setValidated(inventaire.getValidated());
            inventaireDto.setValidatedDate(inventaire.getValidatedDate());
            if(inventaire.getValidatedBy() != null){
                inventaireDto.setValidatedBy(inventaire.getValidatedBy().getFirstname());
            }

            ArrayList<LigneProductDto> lignesProductDto = new ArrayList<>();
            double ecart = 0.0;
            if (inventaire.getLignesProduct() != null) {
                for (LigneProduct ligneProduct : inventaire.getLignesProduct()){
                    lignesProductDto.add(new LigneProductDto().createDTO(ligneProduct));
                    ecart += (ligneProduct.getStockTheorique() - ligneProduct.getStockPhysique()) * ligneProduct.getProduct().getCump();
                }
            }
            inventaireDto.setLignesProduct(lignesProductDto);
            inventaireDto.setEcartTotal(ecart);

            return inventaireDto;
        }
        return null;
    }
}
