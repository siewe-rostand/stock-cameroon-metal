package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Invoice;
import com.siewe.inventorymanagementsystem.model.Stock;
import lombok.Data;

@Data
public class InvoiceDto {

    private Long id;
    private Long productId;
    private String productName;
    private double lineTotal;
    private Double quantity;
    private Double total;

    public InvoiceDto createDTO(Invoice invoice) {
        InvoiceDto invoiceDto = new InvoiceDto();

        if(invoice != null){
            invoiceDto.setId(invoice.getInvoiceId());
            invoiceDto.setProductId(invoice.getProductId());
            invoiceDto.setProductName(invoice.getProductName());
            invoiceDto.setQuantity(invoice.getQuantity());
            invoiceDto.setTotal(invoice.getTotal());
            invoiceDto.setLineTotal(invoice.getLineTotal());

        }
        return invoiceDto;
    }
}
