package com.siewe.inventorymanagementsystem.exceptions;

public class OutOfStockException  extends RuntimeException{

    private Long productId;
    private Integer availableQuantity;

    public OutOfStockException(Long productId, Integer availableQuantity){
        super("product with ID "+ productId + " is out of stock.\n Available quantity: "+ availableQuantity);

        this.productId = productId;
        this.availableQuantity = availableQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }
}
