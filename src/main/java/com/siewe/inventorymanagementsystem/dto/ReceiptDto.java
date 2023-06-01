package com.siewe.inventorymanagementsystem.dto;

import com.siewe.inventorymanagementsystem.model.Receipt;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReceiptDto {

    private Long id;

    private Long orderId;

    private double amount;
    private double total_amount;

    private String payment_mode;

    private String payment_status;

    private String type;

    private Boolean deleted;

    private String createdDate;

    private String updatedDate;

    private String deletedDate;

    public ReceiptDto createDto(Receipt receipt){
        ReceiptDto receiptDto = new ReceiptDto();

        if (receipt != null){
            receiptDto.setId(receipt.getId());


            receiptDto.setAmount(receipt.getAmount());
            receiptDto.setTotal_amount(receipt.getTotal_amount());
            receiptDto.setPayment_mode(receipt.getPayment_mode());
            receiptDto.setPayment_status(receipt.getPayment_status());
            receiptDto.setType(receipt.getType());
            receiptDto.setDeleted(receipt.getDeleted());
            receiptDto.setCreatedDate(receipt.getCreatedDate());
            receiptDto.setUpdatedDate(receipt.getUpdatedDate());
            receiptDto.setDeletedDate(receipt.getDeletedDate());
        }


        return receiptDto;
    }
}
