package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Produit;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProduitDto {
    private Integer id;
    private String ref;
    private String color;
    private String name;
    private Boolean available;
    private Double epaiseur;
    private Double metrage;
    private Timestamp createdDate;

    public ProduitDto createDTO(Produit produit){
        ProduitDto produitDto = new ProduitDto();
        if (produit != null){
            produitDto.setId(produit.getProduitId());
            produitDto.setRef(produit.getRef());
            produitDto.setName(produit.getName());
            produitDto.setAvailable(produit.getAvailable());
            produitDto.setColor(produit.getColor());
            produitDto.setEpaiseur(produit.getEpaiseur());
            produitDto.setCreatedDate(produit.getCreatedDate());
            produitDto.setMetrage(produit.getMetrage());
        }
        return produitDto;
    }
}
