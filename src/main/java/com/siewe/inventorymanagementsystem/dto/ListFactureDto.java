package com.siewe.inventorymanagementsystem.dto;



import com.siewe.inventorymanagementsystem.model.Approvisionnement;
import com.siewe.inventorymanagementsystem.model.ListFacture;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListFactureDto {
    private Long id;
    private String createdDate;
    private String numFacture;
    private String releve;
    private List<ApprovisionnementDto> approvisionnements;

    private Long userId;
    private String userName;

    private Long supplierId;
    private SupplierDto supplier;

    public ListFactureDto createDTO(ListFacture bl) {
        ListFactureDto blDto = new ListFactureDto();
        if(bl != null){
            blDto.setId(bl.getId());
            blDto.setCreatedDate(bl.getCreatedDate());
            blDto.setNumFacture(bl.getNumFacture());
            blDto.setReleve(bl.getReleve());

            ArrayList<ApprovisionnementDto> approvisionnementsDto = new ArrayList<>();
            if (bl.getApprovisionnements() != null) {
                for (Approvisionnement approvisionnement : bl.getApprovisionnements()){
                    approvisionnementsDto.add(new ApprovisionnementDto().createDTO(approvisionnement));
                }
            }
            blDto.setApprovisionnements(approvisionnementsDto);

            if(bl.getSupplier() != null){
                blDto.setSupplierId(bl.getSupplier().getId());
                blDto.setSupplier(new SupplierDto()
                        .createDTO(bl.getSupplier()));
            }

            if(bl.getUser() != null){
                blDto.setUserId(bl.getUser().getUserId());
                blDto.setUserName(bl.getUser().getName());
            }

            return blDto;
        }
        return null;
    }

}

