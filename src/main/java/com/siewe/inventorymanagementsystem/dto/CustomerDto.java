package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Customer;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class CustomerDto {
    private Long id;
    @NotNull(message = "customer name must not be null")
    private String name;
    private String address;
    private String email;
    private String phone;
    private String phone2;
    private String city;
    private String quarter;
    private Timestamp createdDate;

    public CustomerDto createDTO(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        if(customer != null){
            customerDto.setId(customer.getId());
            customerDto.setName(customer.getName());
            customerDto.setAddress(customer.getAddress());
            customerDto.setPhone(customer.getPhone());
            customerDto.setEmail(customer.getEmail());
            customerDto.setCity(customer.getCity());
            customerDto.setQuarter(customer.getQuarter());
            customerDto.setCreatedDate(customer.getCreatedDate());
            return customerDto;
        }
        return null;
    }
}
