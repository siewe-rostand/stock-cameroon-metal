package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.ProductInvoice;
import lombok.Data;

@Data
public class ProductInvoiceDto {

    private Long refId;
    private Long productId;
    private Long invoiceId;

    public ProductInvoiceDto createDto(ProductInvoice productInvoice){
        ProductInvoiceDto productInvoiceDto = new ProductInvoiceDto();

        if (productInvoice != null){
            productInvoiceDto.setRefId(productInvoice.getRefId());
            productInvoiceDto.setProductId(productInvoice.getProduct().getProductId());
            productInvoiceDto.setInvoiceId(productInvoice.getInvoice().getInvoiceId());
        }

        return  productInvoiceDto;
    }
}
