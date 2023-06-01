package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Supplier;
import lombok.Data;

@Data
public class SupplierDto {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String createdDate;

    public SupplierDto createDTO(Supplier supplier) {
        SupplierDto supplierDto = new SupplierDto();
        if(supplier != null){
            supplierDto.setId(supplier.getId());
            supplierDto.setName(supplier.getName());
            supplierDto.setAddress(supplier.getAddress());
            supplierDto.setPhone(supplier.getPhone());
            supplierDto.setEmail(supplier.getEmail());
            supplierDto.setCreatedDate(supplier.getCreatedDate());
            return supplierDto;
        }
        return null;
    }
}
