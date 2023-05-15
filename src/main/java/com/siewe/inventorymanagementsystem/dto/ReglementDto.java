package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Reglement;
import lombok.Data;

@Data
public class ReglementDto {

    private Long id;
    private double amount;
    private String createdDate;
    private Long venteId;
    private Long staffId;
    private String staff;

    public ReglementDto createDTO(Reglement reglement) {
        ReglementDto reglementDto = new ReglementDto();
        if(reglement != null){
            reglementDto.setId(reglement.getId());
            reglementDto.setAmount(reglement.getAmount());
            reglementDto.setCreatedDate(reglement.getCreatedDate());

            if(reglement.getVente() != null){
                reglementDto.setVenteId(reglement.getVente().getVenteId());
            }

            if(reglement.getStaff() != null){
                reglementDto.setStaffId(reglement.getStaff().getUserId());
                reglementDto.setStaff(reglement.getStaff().getEmail());
            }
        }
        return reglementDto;
    }
}
